package com.hrst.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hrst.common.ui.R;
import com.hrst.common.ui.database.dao.NumberManageDao;
import com.hrst.common.ui.utils.Utils;
import com.hrst.common.util.ToastUtils;

/**
 * @author 赵耿忠
 * @describe 添加分组Dialog
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class AddGroupDialog extends Dialog implements
        View.OnClickListener, TextWatcher {
    private final Context context;
    // 数据库操作Dao
    private NumberManageDao numberManageDao;
    // 输入框
    private EditText etGroup;
    private TextView tvGroupname;
    private Button btnOk;
    private Handler handler;
    private String editStr = "分组名称";

    /**
     * 添加分组Dialog构造方法
     * 
     * @param context
     *            上下文
     * @param numberManageDao
     *            号码管理数据操作Dao
     * @param handler
     *            消息机制
     */
    public AddGroupDialog(final Context context,
            final NumberManageDao numberManageDao, final Handler handler) {
        super(context);
        this.context = context;
        this.numberManageDao = numberManageDao;
        this.setCanceledOnTouchOutside(false);
        this.handler = handler;
        init();
    }

    /**
     * 初始化数据
     * 
     */
    private void init() {
        LayoutInflater inflat = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflat.inflate(R.layout.dialog_addgroup, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(layout);

        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
        btnOk = (Button) layout.findViewById(R.id.btn_ok);
        etGroup = (EditText) layout.findViewById(R.id.et_group);
        tvGroupname = (TextView) layout.findViewById(R.id.tv_groupname);
        etGroup.addTextChangedListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    /**
     * 监听按钮点击事件
     * 
     * @param v
     */
    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.btn_cancel) {
            this.cancel();

        } else if (i == R.id.btn_ok) {
            addGroup();

        }
    }

    /**
     * 添加分组信息
     * 
     */
    private void addGroup() {
        String groupName = etGroup.getText().toString();// 获取组名称
        if (groupName.trim().isEmpty()) {
        	Toast.makeText(context, "分组名称不能为空", Toast.LENGTH_SHORT).show();
        	return;
        }
        if (numberManageDao.getGroupsNum(groupName) == 0) {
            boolean isadd = numberManageDao.saveGroup(groupName);
            if (isadd) {
            	ToastUtils.showToast( "添加成功");
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
                this.cancel();
            } else {
            	ToastUtils.showToast("添加失败");
            }
        } else {
        	ToastUtils.showToast("分组信息已存在");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {


    }

    /**
     * 监听文本框改变
     * 
     * @param s
     * @param start
     *            开始改变的地方
     * @param before
     *            结束的地方
     * @param count
     *            改变的个数
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String groupName = etGroup.getText().toString();
        if (Utils.verifySymbol(groupName)) {
            btnOk.setEnabled(false);
            tvGroupname.setText(editStr + "(" + "不能输入特殊符号！" + ")");
        } else if (!groupName.equals("")) {
            tvGroupname.setText(editStr);
            btnOk.setEnabled(true);
        } else {
            tvGroupname.setText(editStr);
            btnOk.setEnabled(false);
        }
    }

}
