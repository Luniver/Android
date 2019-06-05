package com.hrst.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hrst.common.ui.R;
import com.hrst.common.ui.database.DatabaseGlobal;
import com.hrst.common.ui.database.dao.NumberManageDao;
import com.hrst.common.ui.utils.Utils;
import com.hrst.common.util.ToastUtils;

import java.util.Map;

/**
 * @author 赵耿忠
 * @describe 修改分组Dialog
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class UpdateGroupDialog extends Dialog implements
        View.OnClickListener, TextWatcher {
    private final Context context;
    private final Handler handler;
    // 数据库操作Dao
    private NumberManageDao numberManageDao;
    // 数据
    private Map<String, Object> map;
    // 标题
    private TextView tvTitle;
    // 输入框
    private EditText etGroup;
    private Button btnOk;
    private TextView tvGroupname;
    private String editStr = "分组名称";

    /**
     * 构造方法
     * 
     * @param context
     *            上下文
     * @param numberManageDao
     *            数据操作dao
     * @param map
     *            数据
     * @param handler
     *            更新操作包柄
     */
    public UpdateGroupDialog(final Context context,
                             final NumberManageDao numberManageDao, final Map map,
                             Handler handler) {
        super(context);
        this.context = context;
        this.numberManageDao = numberManageDao;
        this.map = map;
        this.setCanceledOnTouchOutside(false);
        this.handler = handler;

        init();
    }

    /**
     * 初始化
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
        tvTitle = (TextView) layout.findViewById(R.id.tv_title);
        tvGroupname = (TextView) layout.findViewById(R.id.tv_groupname);
        tvTitle.setText("修改分组");
        etGroup.setText(map.get(DatabaseGlobal.groupsField[1]).toString());
        etGroup.addTextChangedListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    /**
     * 确定，取消按钮点击事件
     * 
     * @param v
     */
    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.btn_cancel) {
            this.cancel();

        } else if (i == R.id.btn_ok) {
            updateGroup();

        }
    }

    /**
     * 添加分组信息
     * 
     */
    private void updateGroup() {
        String groupName = etGroup.getText().toString();// 获取组名称
        if (numberManageDao.getGroupsNum(groupName) == 0) {
            String groupID = map.get(DatabaseGlobal.groupsField[0]).toString();
            int isadd = numberManageDao.updateGroup(groupID, groupName);
            if (isadd != 0) {
            	ToastUtils.showToast("修改成功");
            	handler.sendEmptyMessage(8);
                map.put(DatabaseGlobal.groupsField[1], groupName);
                this.cancel();
            } else {
            	ToastUtils.showToast("修改失败");
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
        } else if (!groupName.equals("")
                && !groupName.equals(map.get(DatabaseGlobal.groupsField[1])
                        .toString())) {
            tvGroupname.setText(editStr);
            btnOk.setEnabled(true);
        } else {
            tvGroupname.setText(editStr);
            btnOk.setEnabled(false);
        }
    }

}
