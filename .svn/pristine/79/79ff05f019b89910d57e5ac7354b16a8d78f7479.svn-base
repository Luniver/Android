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
 * @describe 输入电流窗口
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class HostDeficiencyDialogActivity extends Activity implements
		TextWatcher, OnClickListener {

	private Button btnCancel, btnOK;
	private EditText etHostDeficiencyValue;
	private TextView tvHostDeficiencyValue;
	private String hostValue, strValue;
	SaveData_withPreferences preferences;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_input_host_deficiency);
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
		tvHostDeficiencyValue = (TextView) findViewById(R.id.tv_host_deficiency_value);
		etHostDeficiencyValue = (EditText) this.findViewById(R.id.et_host_deficiency_value);
		btnCancel = (Button) this.findViewById(R.id.btn_cancel);
		btnOK = (Button) this.findViewById(R.id.btn_ok);

		btnCancel.setOnClickListener(this);
		btnOK.setOnClickListener(this);
		etHostDeficiencyValue.addTextChangedListener(this);

		// 获取配置，如果没有默认20%
		hostValue = preferences.getData_String(SettingUtils.HostDeficiencyValue, "20");
		etHostDeficiencyValue.setText(hostValue);
	}

	/**
	 * 确定，取消按钮点击事件
	 * 
	 */
	@Override
	public void onClick(View v) {
		if (v == btnCancel) {
			HostDeficiencyDialogActivity.this.finish();
		} else if (v == btnOK) {
			preferences.saveDatas_String(SettingUtils.HostDeficiencyValue, strValue);
			finish();
		}
	}

	 /**
     * 监听文本框改变
     */
	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// 检查过低电量值
		strValue = etHostDeficiencyValue.getText().toString().trim();
		if (strValue.equals(hostValue)) {
			btnOK.setEnabled(false);
		} else if (strValue.length() <= 0) {
			tvHostDeficiencyValue.setText("电量值(11-99)");
			btnOK.setEnabled(false);
		} else if (Integer.parseInt(strValue) > 10
				&& Integer.parseInt(strValue) < 100) {
			tvHostDeficiencyValue.setText("电量值(11-99)");
			btnOK.setEnabled(true);
		} else {
			tvHostDeficiencyValue.setText("请输入正确的数值，11-99");
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
