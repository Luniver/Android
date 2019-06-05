package com.hrst.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
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
import com.hrst.common.ui.database.DatabaseGlobal;
import com.hrst.common.ui.database.dao.NumberManageDao;
import com.hrst.common.ui.utils.Utils;
import com.hrst.common.util.ToastUtils;

import java.util.Map;

/**
 * @author 赵耿忠
 * @describe 修改号码Dialog
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class UpdateNumberDialog extends Dialog implements
        View.OnClickListener, TextWatcher {
    private final Context context;
    // 要改变的数据View
    private View convertView;
    // 输入框
    private EditText etPhoneNumber;
    private EditText etName;
    // 提示信息
    private TextView tvTargetNumber;
    private TextView tvDialogName;
    // 数据库操作Dao
    private NumberManageDao numberManageDao;
    // 改变的数据
    private Map<String, Object> map;
    private Button btnOk;

    /**
     * 构造方法
     * 
     * @param context
     *            上下文
     * @param convertView
     *            选中的数据列表View
     * @param numberManageDao
     *            号码管理数据操作Dao
     * @param map
     *            选中的数据
     */
    public UpdateNumberDialog(final Context context, final View convertView,
            final NumberManageDao numberManageDao, Map map) {
        super(context);
        this.context = context;
        this.convertView = convertView;
        this.numberManageDao = numberManageDao;
        this.map = map;
        this.setCanceledOnTouchOutside(false);

        init();
    }

    /**
     * 初始化
     * 
     */
    private void init() {
        LayoutInflater inflat = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflat.inflate(R.layout.dialog_updatenumber, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(layout);

        etPhoneNumber = (EditText) layout.findViewById(R.id.et_phonenumber);
        etName = (EditText) layout.findViewById(R.id.et_name);
        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
        btnOk = (Button) layout.findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTargetNumber = (TextView) findViewById(R.id.tv_target_number);
        tvDialogName = (TextView) findViewById(R.id.tv_dialog_name);
        etPhoneNumber.setText(map.get(DatabaseGlobal.phoneNumberField[2])
                .toString());
        etName.setText(map.get(DatabaseGlobal.phoneNumberField[3]).toString());
        tvTitle.setText("修改号码");
        etPhoneNumber.addTextChangedListener(this);
        etName.addTextChangedListener(this);

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
            updateNumber();

        }
    }

    /**
     * 修改号码信息
     * 
     */
    private void updateNumber() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();// 获取号码
        String name = etName.getText().toString().trim();// 获取姓名
        if (phoneNumber.length() <= 0) {
        	Toast.makeText(context, "电话号码不能为空", Toast.LENGTH_SHORT).show();
        	return;
        }
        if (name.length() <= 0) {
        	Toast.makeText(context, "姓名不能为空", Toast.LENGTH_SHORT).show();
        	return;
        }
        
        if (phoneNumber.length() < 4)
        {
        	Toast.makeText(context, "电话号码长度不能小于4", Toast.LENGTH_SHORT).show();
        	return;
        }
        
        if (phoneNumber.length() > 11)
        {
        	Toast.makeText(context, "电话号码长度不能大于11", Toast.LENGTH_SHORT).show();
        	return;
        }
        
        String[] format = new String[] { "移动", "联通", "电信", "未知" };
        if (phoneNumber.equals(map.get(DatabaseGlobal.phoneNumberField[2]))
                || numberManageDao.getPhoneNumberNum(phoneNumber) == 0) {
            String dealer = format[Utils.getPhoneFormat(etPhoneNumber.getText()
                    .toString())];
            int isupdate = numberManageDao.updatePhoneNumber(
                    map.get(DatabaseGlobal.phoneNumberField[0]).toString(),
                    phoneNumber, name, dealer);
            if (isupdate > 0) {
            	ToastUtils.showToast("修改成功");
                final TextView tvTargetname = (TextView) convertView
                        .findViewById(R.id.tv_targetname);
                final TextView tvDealer = (TextView) convertView
                        .findViewById(R.id.tv_dealer);
                final TextView tvNumber = (TextView) convertView
                        .findViewById(R.id.tv_number);
                tvTargetname.setText(name);
                tvDealer.setText(dealer);
                tvNumber.setText(phoneNumber);
                map.put(DatabaseGlobal.phoneNumberField[2], phoneNumber);
                map.put(DatabaseGlobal.phoneNumberField[4], dealer);
                map.put(DatabaseGlobal.phoneNumberField[3], name);
                this.cancel();
            } else {
            	ToastUtils.showToast("修改失败");
            }
        } else {
        	ToastUtils.showToast("号码信息已存在");
            return;
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
     * 监听输入框改变
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

        String phoneNumber = etPhoneNumber.getText().toString();// 获取号码
        String name = etName.getText().toString();// 获取姓名
        if (phoneNumber.equals(map.get(DatabaseGlobal.phoneNumberField[2])
                .toString())
                && name.equals(map.get(DatabaseGlobal.phoneNumberField[3])
                        .toString())) {
            btnOk.setEnabled(false);
            tvTargetNumber.setText("目标号码");
        } else if (Utils.checkNumberAvailable(phoneNumber, "目标号码",
                tvTargetNumber, etPhoneNumber)
                && Utils.checkTextAvailable(name, "备注姓名", tvDialogName, 5)) {
            btnOk.setEnabled(true);
        } else {
            btnOk.setEnabled(false);
        }
    }

}
