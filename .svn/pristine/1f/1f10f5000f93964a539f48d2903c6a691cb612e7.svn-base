package hrst.sczd.ui.activity.dialog;

import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.utils.SaveData_withPreferences;
import hrst.sczd.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hrst.common.ui.InduceConfigure;

/**
 * @author 沈月美
 * @version 1.1.1.3
 *          修改者，修改日期，修改内容
 * @describe 输入号码Activity
 * @date 2014.05.23
 */
public class NumberDialogActivity extends Activity implements TextWatcher,
        OnClickListener {

    private Button btnCancel, btnOK;
    private AutoCompleteTextView etPhoneNumber;
    private EditText etName;
    private TextView tvTargetNumber;
    private TextView tvTargetName;
    private String num, name;
    private String strNumber, strName;

    private TextView tv_title;

    private SaveData_withPreferences spreferences;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 取消标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_number);
        this.setFinishOnTouchOutside(false);
        ExitApplication.getInstance().addActivity(this);
        init();
    }

    /**
     * 初始化控件的方法
     *
     * @author 沈月美
     */
    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        etPhoneNumber = (AutoCompleteTextView) findViewById(R.id.et_inputphonenumber);
        etName = (EditText) findViewById(R.id.et_inputname);
        btnCancel = (Button) this.findViewById(R.id.btn_cancel);
        btnOK = (Button) this.findViewById(R.id.btn_inputok);
        tvTargetNumber = (TextView) this.findViewById(R.id.tv_target_number);
        tvTargetName = (TextView) this.findViewById(R.id.tv_target_name);
        btnCancel.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        etPhoneNumber.addTextChangedListener(this);
        etName.addTextChangedListener(this);
        spreferences = SaveData_withPreferences.getInstance(this);
        Intent intent = getIntent();
        strNumber = intent.getStringExtra("number");
        strName = intent.getStringExtra("name");
        if (!strNumber.equals("点击输入号码")) {
            etPhoneNumber.setText(strNumber);
            etName.setText(strName);
        }
        //国家码为中国时限制号码长度
        if (InduceConfigure.countryNum.equals("+86") || InduceConfigure.countryName.contains("中国") || InduceConfigure.countryName.contains("China")) {
            etPhoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        }
        tv_title.setText("输入号码(" + InduceConfigure.countryName + ")");
        String str = spreferences.getData_String("history_number");
        etPhoneNumber.setThreshold(0);


        if (str.length() > 0) {
            String[] phoneNumbers = str.split(",");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dialog_number_text, phoneNumbers);
            etPhoneNumber.setAdapter(adapter);
        }
    }

    /**
     * 判断“确定”按钮的可用性
     *
     * @author 沈月美
     */
    private void setBtnOKEnable(boolean enable) {
        btnOK.setEnabled(enable);
    }

    @Override
    public void afterTextChanged(Editable s) {
        num = etPhoneNumber.getText().toString();// 获取号码
        name = etName.getText().toString();// 获取姓名
        if (tv_title.getText().toString().contains("中国") || tv_title.getText().toString().contains("China")) {
            if (!Utils.checkNumberAvailable(num, "目标号码", tvTargetNumber)) {
                setBtnOKEnable(false);
            }
        }
        if (!Utils.verifySymbol(name)) {
            if (!name.equals(strName) || !num.equals(strNumber)) {// 判断与上次的值是否相同，如果不同，确定按钮才有点击作用
                setBtnOKEnable(true);
            } else {
                setBtnOKEnable(false);
            }
        } else {
            tvTargetName.setText("备注姓名(不能输入特殊符号!)");
            setBtnOKEnable(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    /**
     * 按钮的监听方法
     *
     * @author 沈月美
     */
    @Override
    public void onClick(View v) {
        if (v == btnCancel) {// 取得“取消”按钮监听
            NumberDialogActivity.this.finish();
        } else if (v == btnOK) {// 取得“确定”按钮监听
            // 获取填入的号码
            String strPhoneNumber = etPhoneNumber.getText().toString().trim();
            // 获取填入的姓名
            String strName = etName.getText().toString().trim();
            if (strPhoneNumber == null || strPhoneNumber.length() < 4) {
                Toast.makeText(NumberDialogActivity.this, "电话号码不能为空并且长度不能小于4位!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (strPhoneNumber.length() > 11) {
                Toast.makeText(NumberDialogActivity.this, "电话号码长度不能大于11位!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (strName == null || strName.length() <= 0) {
                Toast.makeText(NumberDialogActivity.this, "姓名不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }

            String string = spreferences.getData_String("history_number");
            if (!string.contains(strPhoneNumber)) {
                spreferences.saveDatas_String("history_number", strPhoneNumber + "," + string);
            }

            // 初始化发送者
            Intent data = new Intent();
            data.putExtra("phoneNumber", strPhoneNumber);
            data.putExtra("name", strName);
            // 请求码可以自己设置
            setResult(RESULT_OK, data);
            // 关闭Activity
            finish();
        }
    }

}
