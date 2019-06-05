package hrst.sczd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.hrst.common.ui.interfaces.Constants;
import com.hrst.common.util.SharedPreferencesUtils;

import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.ui.activity.dialog.DtModeConfDialog;
import hrst.sczd.ui.activity.dialog.SmsTestDialog;
import hrst.sczd.ui.base.NavigationActivity;
import hrst.sczd.ui.manager.SkinSettingManager;

public class DebugActivity extends NavigationActivity {

	private DtModeConfDialog dtModeConfDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dtModeConfDialog = new DtModeConfDialog(this);
//		EventBus.getDefault().register(this);
	}

	@Override
	public void setContentView() {
		ExitApplication.getInstance().addActivity(this);
		SkinSettingManager skinManager = new SkinSettingManager(this);
		if (skinManager.getCurrentSkinRes() == 0) {
			this.setTheme(android.R.style.Theme_Black_NoTitleBar);
		} else {
			this.setTheme(android.R.style.Theme_Light_NoTitleBar);
		}
		setContentView(R.layout.activity_debug_manual_s);
		setTitle("调试工具集");
        CheckBox cbDebug = findViewById(R.id.cb_debugging);
        cbDebug.setChecked(SharedPreferencesUtils.getBoolean(Constants.Common.KEY_DEBUG));
        cbDebug.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferencesUtils.put(Constants.Common.KEY_DEBUG, isChecked);
            InteractiveManager.getInstance().sendDebugging(isChecked);
        });
	}
	
	/**
	 * 调试数据获取
	 * @param view
	 */
	public void ToDebugDataAcquisition_sczd (View view) {
		Intent intentAboutUs = new Intent(this, DebugDataAcquisitionActivity.class);
		intentAboutUs.putExtra(Constants.Common.KEY_BACK_STACK, "调试工具集");
		startActivity(intentAboutUs);
	}
	
	
	/**
	 * 数传模式配置
	 * @param view
	 */
	public void TO_DT_MODE_CONF_SCZD(View view) {
	    if (dtModeConfDialog == null){
			dtModeConfDialog = new DtModeConfDialog(this);
		}
		dtModeConfDialog.show();
	}

	/**
	 * 灵敏度测试
	 */
	public void sensibilityTest(View view){
		startActivity(new Intent(this, SensibilityActivity.class));
	}
	
	/**
	 * 短信功能测试
	 * @param view
	 */
	public void ToSMS_sczd (View view) {
		new SmsTestDialog(this).initDialog();
	}
	
	/**
	 * 关闭设备
	 * @param view
	 */
	public void ToCloseDevice_sczd (View view) {
		
	}
	
	@Override
	public void handleMessage(Message msg) {
		
	}

	@Override
	protected void onDestroy() {
//		EventBus.getDefault().unregister(this);
		if (dtModeConfDialog != null){
			dtModeConfDialog.destory();
		}
		super.onDestroy();
	}

    /**
     * 跳转到433无线模块控制
     */
    public void To433_module(View view) {
        startActivity(new Intent(this, Module433ManagerActivity.class));
    }
}
