package hrst.sczd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.hrst.common.ui.interfaces.Constants;
import com.hrst.common.util.SharedPreferencesUtils;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_DT_MODE_SETTING_ACK;
import hrst.sczd.ui.activity.dialog.DebugPasswdDialog;
import hrst.sczd.ui.activity.dialog.DtModeConfDialog;
import hrst.sczd.ui.base.NavigationActivity;
import hrst.sczd.ui.manager.SkinSettingManager;

/**
 * @author 刘兰
 * @describe 更多
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class SettingActivity extends NavigationActivity {

	private long lastClickTime;
	private int click;
	private DtModeConfDialog dtModeConfDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		EventBus.getDefault().register(this);
	}

	/**
	 * 设置布局，标题与视觉模式
	 * 
	 */
	@Override
	public void setContentView() {
		ExitApplication.getInstance().addActivity(this);
		SkinSettingManager skinManager = new SkinSettingManager(this);
		if (skinManager.getCurrentSkinRes() == 0) {
			this.setTheme(android.R.style.Theme_Black_NoTitleBar);
		} else {
			this.setTheme(android.R.style.Theme_Light_NoTitleBar);
		}
		setContentView(R.layout.user_manual_s);
		setTitle("更多");
	}
	
	//	跳转系统设置
	public void ToSystemSettingForSczd(View view) {
		Intent intentSystemSettings = new Intent(this,
				SystemSettingActivity.class);
		intentSystemSettings.putExtra(Constants.Common.KEY_BACK_STACK, "更多");
		startActivity(intentSystemSettings);
	}

	// 跳转到关于我们界面
	public void ToAboutUsSczd(View view) {
		Intent intentAboutUs = new Intent(this, AboutUsActivity.class);
		intentAboutUs.putExtra(Constants.Common.KEY_BACK_STACK, "更多");
		startActivity(intentAboutUs);
	}
	
	//使用记录
	public void ToHistoryRecordSczd(View view) {
		Intent intentUsedRecord = new Intent(this, HistoryRecordActivity.class);
		intentUsedRecord.putExtra("source", 1);
		intentUsedRecord.putExtra(Constants.Common.KEY_BACK_STACK, "更多");
		startActivity(intentUsedRecord);
	}
	
	//授权管理
	public void ToAuthorizationManagementSczd(View view) {
		Intent intentAuthorizationManagement = new Intent(this, AuthorizationManagementActivity.class);
		intentAuthorizationManagement.putExtra(Constants.Common.KEY_BACK_STACK, "更多");
		startActivity(intentAuthorizationManagement);
	}
	/**
	 * 调测工具集
	 * @param view
	 */
	public void ToDebugModeSczd (View view) {
		if (SharedPreferencesUtils.getBoolean(Constants.Common.KEY_PASSWORD_VERIFY)) {
			Intent intent = new Intent(context, DebugActivity.class);
			intent.putExtra(Constants.Common.KEY_BACK_STACK, "更多");
			context.startActivity(intent);
		} else {
			new DebugPasswdDialog(this).initDialog();
		}
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

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void onDataModeAck(PDA_TO_MCU_DT_MODE_SETTING_ACK obj){
		Toast.makeText(this, getString(R.string.mode_setting_success), Toast.LENGTH_LONG).show();
	}
	@Override
	public void handleMessage(Message msg) {

	}

	@Override
	protected void onDestroy() {
	    if (dtModeConfDialog != null){
			dtModeConfDialog.destory();
		}

		super.onDestroy();
//		EventBus.getDefault().unregister(this);
	}

	// 测试入口
	public void testEntry(View view) {
		long curTime = System.currentTimeMillis();
	    if (lastClickTime != 0 && curTime - lastClickTime < 300){
	        click++;
		}else {
	    	click = 0;
		}
		lastClickTime = System.currentTimeMillis();
		if (click == 5){
	    	startActivity(new Intent(this, HideTestActivity.class));
		}
	}
}
