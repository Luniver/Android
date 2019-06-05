package com.hrst.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hrst.common.ui.R;
import com.hrst.common.ui.database.DatabaseGlobal;
import com.hrst.common.ui.database.dao.NumberManageDao;
import com.hrst.common.ui.utils.Utils;
import com.hrst.common.util.ToastUtils;

import java.util.List;
import java.util.Map;

/**
 * @author 赵耿忠
 * @describe 添加号码dialog
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class AddNumberDialog extends Dialog implements
        View.OnClickListener, TextWatcher {
    private final Context context;
    // 输入框
    private EditText etPhoneNumber;
    private EditText etName;
    // 提示信息
    private TextView tvTargetNumber;
    private TextView tvDialogName;
    // 确定按钮
    private Button btnOk;
    // 数据库操作Dao
    private NumberManageDao numberManageDao;
    // 消息机制
    private Handler handler;
    // 分组列表
    private List<Map<String, Object>> groupLst;
    // 选择分组TextView
    TextView tvGroup;
    TextView tvTitle;
    private PopupWindow pw;
    private int lastGroup = 0;
    private String selectGroupId = "1";

    /**
     * 添加号码Dialog构造方法
     *
     * @param context
     *            上下文
     * @param numberManageDao
     *            号码管理数据操作dao
     * @param groupLst
     *            分组列表
     * @param handler
     *            消息机制
     */
    public AddNumberDialog(final Context context,
            final NumberManageDao numberManageDao, final List groupLst,
            final Handler handler) {
        super(context);
        this.context = context;
        this.numberManageDao = numberManageDao;
        this.handler = handler;
        this.groupLst = groupLst;
        this.setCanceledOnTouchOutside(false);

        init();
    }

    /**
     * 初始化方法
     *
     */
    private void init() {
        LayoutInflater inflat = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflat.inflate(R.layout.dialog_addnumber, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(layout);

        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
        btnOk = (Button) layout.findViewById(R.id.btn_ok);
        etPhoneNumber = (EditText) layout.findViewById(R.id.et_phonenumber);
        etName = (EditText) layout.findViewById(R.id.et_name);
        tvTargetNumber = (TextView) findViewById(R.id.tv_target_number);
        tvGroup = (TextView) findViewById(R.id.tv_group);
        tvDialogName = (TextView) findViewById(R.id.tv_dialog_name);
        tvGroup.setText(groupLst.get(0).get(DatabaseGlobal.groupsField[1])
                .toString());
        tvGroup.setOnClickListener(this);
        etPhoneNumber.addTextChangedListener(this);
        etName.addTextChangedListener(this);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("添加号码(中国+86)");
        etPhoneNumber.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});
        moveGroupDialog();
    }

    /**
     * 弹出移至分组数据Dialog
     *
     */
    private void moveGroupDialog() {
        LayoutInflater inflat = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup menuView = (ViewGroup) inflat.inflate(
                R.layout.dialog_grouplst, null, true);

        // 弹出分组列表
        pw = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(false);
        pw.setFocusable(true);

        // 分组列表数据
        SimpleAdapter apater = new SimpleAdapter(context, groupLst,
                R.layout.list_group,
                new String[] { DatabaseGlobal.groupsField[1] },
                new int[] { R.id.name });
        final ListView groupListView = (ListView) menuView.findViewById(R.id.groupLst);
        groupListView.setAdapter(apater);
        // 分组列表点击
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                            int arg2, long arg3) {

                        lastGroup = arg2;
                        selectGroupId = groupLst.get(arg2)
                                .get(DatabaseGlobal.groupsField[0]).toString();
                        tvGroup.setText(groupLst.get(arg2)
                                .get(DatabaseGlobal.groupsField[1]).toString());
                        pw.dismiss();
                    }
                });

    }

    /**
     * 显示dialog
     * 
     */
    private void showDialog() {
        pw.showAsDropDown(tvGroup);
    }

    /**
     * 监听点击分组列表，确定，取消按钮点击事件
     * 
     * @param v
     */
    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.btn_cancel) {
            this.cancel();

        } else if (i == R.id.btn_ok) {
            addNumber();

        } else if (i == R.id.tv_group) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etPhoneNumber.getWindowToken(), 0);
            showDialog();

        }
    }

    /**
     * 添加号码信息
     * 
     */
    private void addNumber() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();// 获取号码
        String name = etName.getText().toString().trim();// 获取姓名
        if (phoneNumber.length() <= 0) {
        	ToastUtils.showToast("电话号码不能为空");
        	return;
        }
        
        if (phoneNumber.length() < 4)
        {
        	ToastUtils.showToast("电话号码长度不能小于4");
        	return;
        }
        
        if (phoneNumber.length() > 11)
        {
        	ToastUtils.showToast( "电话号码长度不能大于11");
        	return;
        }
        
        if (name.length() <= 0) {
        	ToastUtils.showToast( "姓名不能为空");
        	return;
        }
        String[] format = new String[] { "移动", "联通", "电信", "未知" };
        if (numberManageDao.getPhoneNumberNum(phoneNumber) == 0) {
            boolean isadd = numberManageDao.savePhoneNumber(selectGroupId,
                    etPhoneNumber.getText().toString(), etName.getText()
                            .toString(),
                    format[Utils.getPhoneFormat(etPhoneNumber.getText()
                            .toString().trim())]);
            if (isadd) {
            	ToastUtils.showToast( "添加成功");
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("groupPosition", lastGroup);
                msg.setData(bundle);
                msg.what = 2;
                handler.sendMessage(msg);
                this.cancel();
            } else {
            	ToastUtils.showToast( "添加失败");
            }
        } else {
        	ToastUtils.showToast( "号码信息已存在");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String phoneNumber = etPhoneNumber.getText().toString();// 获取号码
        String name = etName.getText().toString();// 获取姓名
        // 目前只针对中国
        String countryNum = "+86";
        if (countryNum.contains("86")) {
            if (Utils.checkNumberAvailable(phoneNumber, "目标号码", tvTargetNumber,etPhoneNumber)
                    && Utils.checkTextAvailable(name, "备注姓名", tvDialogName, 5)) {
                btnOk.setEnabled(true);
            } else {
                btnOk.setEnabled(false);
            }
        } else {
            if (Utils.checkTextAvailable(name, "备注姓名", tvDialogName, 5)) {
                btnOk.setEnabled(true);
            } else {
                btnOk.setEnabled(false);
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) { }

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
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

}
