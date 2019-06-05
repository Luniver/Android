package hrst.sczd.ui.activity.dialog;

import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.systemsettings.SettingUtils;
import hrst.sczd.utils.SaveData_withPreferences;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author 李家威
 * @describe 输入温度值
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class TempHighDialogActivity extends Activity implements TextWatcher,
		OnClickListener {

	private Button btnCancel, btnOK;
	private EditText etTempHighValue;
	private TextView tvTempHighValue;
	private String tempValue, strValue;

	SaveData_withPreferences preferences;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_input_temp_high);
		this.setFinishOnTouchOutside(false);
		ExitApplication.getInstance().addActivity(this);
		preferences = SaveData_withPreferences.getInstance(this);
		init();
	
	}

	/**
	 * 初始化
	 * 
	 */
	private void init() {
		tvTempHighValue = (TextView) findViewById(R.id.tv_temp_high_value);
		etTempHighValue = (EditText) this.findViewById(R.id.et_temp_high_value);
		btnCancel = (Button) this.findViewById(R.id.btn_cancel);
		btnOK = (Button) this.findViewById(R.id.btn_ok);

		btnCancel.setOnClickListener(this);
		btnOK.setOnClickListener(this);
		etTempHighValue.addTextChangedListener(this);

		// 获取配置，如果没有默认65°
		tempValue = preferences.getData_String(SettingUtils.TempHighValue, "65");
		etTempHighValue.setText(tempValue);
	}

	/**
	 * 确定，取消按钮点击事件
	 * 
	 */
	@Override
	public void onClick(View v) {
		if (v == btnCancel) {
			TempHighDialogActivity.this.finish();
		} else if (v == btnOK) {
			preferences.saveDatas_String(SettingUtils.TempHighValue, strValue);
			finish();
		}
	}

	 /**
     * 监听文本框改变
     * 
     */
	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// 检查过高温度值
		strValue = etTempHighValue.getText().toString().trim();
		if (strValue.equals(tempValue)) {
			btnOK.setEnabled(false);
		} else if (strValue.length() <= 0) {
			tvTempHighValue.setText("温度值(1-89)");
			btnOK.setEnabled(false);
		} else if (Integer.parseInt(strValue) > 0
				&& Integer.parseInt(strValue) < 90) {
			tvTempHighValue.setText("温度值(1-89)");
			btnOK.setEnabled(true);
		} else {
			tvTempHighValue.setText("请输入正确的数值，1-89");
			btnOK.setEnabled(false);
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}
}
