package hrst.sczd.ui.activity.dialog;

import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.systemsettings.SettingUtils;
import hrst.sczd.utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author 李家威
 * @describe 更改数据效果的窗口
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class VisualModeDialogActivity extends Activity implements
		OnClickListener, OnCheckedChangeListener {
	private Button btnCancel, btnOK;
	private boolean visualMode;
	private RadioGroup rgVidualMode;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_set_visual_mode);
		this.setFinishOnTouchOutside(false);
		ExitApplication.getInstance().addActivity(this);
		init();
	}

	/**
	 * 初始化
	 * 
	 */
	private void init() {
		rgVidualMode = (RadioGroup) findViewById(R.id.rg_visual_mode);
		btnCancel = (Button) this.findViewById(R.id.btn_cancel);
		btnOK = (Button) this.findViewById(R.id.btn_ok);

		btnCancel.setOnClickListener(this);
		btnOK.setOnClickListener(this);

		rgVidualMode.setOnCheckedChangeListener(this);

		// 获取配置 true 夜间模式 false 白天模式
		visualMode = Utils.getSetting(this, SettingUtils.VisionMode, true);
		if (visualMode) {
			rgVidualMode.check(R.id.rb_night_mode);
		} else {
			rgVidualMode.check(R.id.rb_day_mode);
		}
	}
	
	/**
     * 确定，取消按钮点击事件
     * 
     */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel: {
			VisualModeDialogActivity.this.finish();
		}
		case R.id.btn_ok: {
			Intent data = new Intent();
			data.putExtra("visualMode", visualMode);
			setResult(102, data);
			finish();
			break;
		}
		default:
			break;
		}
	}

	 /**
     * 监听文本框改变
     * 
     */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedid) {
		visualMode = checkedid == R.id.rb_night_mode;
	}
}
