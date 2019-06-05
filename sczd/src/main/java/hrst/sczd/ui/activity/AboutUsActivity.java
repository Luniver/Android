package hrst.sczd.ui.activity;

import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.agreement.manager.vo.UDiskFirmwareNameInfo;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE_ACK;
import hrst.sczd.ui.activity.dialog.CheckVersionActivity;
import hrst.sczd.ui.base.NavigationActivity;
import hrst.sczd.ui.manager.AboutUsManager;
import hrst.sczd.ui.manager.SkinSettingManager;
import hrst.sczd.utils.SaveData_withPreferences;
import hrst.sczd.utils.ToastUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hrst.BluetoothCommHelper;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 关于我们
 * 
 * @author 沈月美
 * @date 2014-04-03
 * 
 */
public class AboutUsActivity extends NavigationActivity {
	private TextView tvVersion;
	private Button firmwareUpdateBtn;
	private boolean isGSM, isCDMA, isWCDMA, isTDSCDMA;
	private SaveData_withPreferences sPreferences;
	private AboutUsManager manager;
	private String queryTag, updateTag;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		sPreferences = SaveData_withPreferences.getInstance(this);
		initRegData();
		
	}
	public void initRegData(){
		queryTag = getString(R.string.query);
		updateTag = getString(R.string.update);
		isGSM = sPreferences.getData_Boolean("gsmFormat");
		isCDMA = sPreferences.getData_Boolean("cdmaFormat");
		isWCDMA = sPreferences.getData_Boolean("wcdmaFormat");
		isTDSCDMA = sPreferences.getData_Boolean("tdscdmaFormat");
		StringBuffer sBuffer = new StringBuffer();
		//初始化只是
		if (isGSM)
			sBuffer.append("GSM|");
		if (isCDMA)
			sBuffer.append("CDMA|");
		if (isWCDMA)
			sBuffer.append("WCDMA|");
		if (isTDSCDMA)
			sBuffer.append("TDSCDMA");
		String modules = sBuffer.toString();
		if (modules.length() > 1) {
			if (modules.substring(modules.length() - 1, modules.length())
					.equals("|")) {
				modules = modules.substring(0, modules.length() - 1);
			}
		}
	}
	@Override
	public void setContentView() {
		SkinSettingManager skinManager = new SkinSettingManager(this);
		if (skinManager.getCurrentSkinRes() == 0) {
			this.setTheme(android.R.style.Theme_Black_NoTitleBar);
		} else {
			this.setTheme(android.R.style.Theme_Light_NoTitleBar);
		}
		setContentView(R.layout.activity_aboutu_sczd);
		setTitle("关于我们");
		initView();
		initData();
		initListener();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		tvVersion = (TextView) findViewById(R.id.version_s);
		firmwareUpdateBtn = (Button) findViewById(R.id.btn_firmware_update);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
//		EventBus.getDefault().register(this);
		manager = new AboutUsManager(this);
		manager.setVersionText(tvVersion);
	}
	
	/**
	 * 初始化监听
	 */
	private void initListener() {
		firmwareUpdateBtn.setOnClickListener(this);
	}
	
	/**
	 * 获取固件升级应答
	 * @param obj
	 */
	@Subscribe (threadMode = ThreadMode.MainThread)
	public void getFirmwareUpdateInfo(PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE_ACK obj){
		manager.showFirmwareUpdateInfo(obj);
	}

	/**
	 * U盘固件名称应答
	 * @param obj
	 */
	@Subscribe (threadMode = ThreadMode.MainThread)
	public void getUDiskFirmwareName(UDiskFirmwareNameInfo obj){
		if(obj.getResult() == 1){
			ToastUtil.showToastLong(context, "固件名称:" + obj.getDescribeStr());
			firmwareUpdateBtn.setText(updateTag);
		}else{
			ToastUtil.showToastLong(context, "固件不存在");
		}
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_firmware_update:
			if (false || !BluetoothCommHelper.get().isConnected()) {
//		    if (!BluetoothCommHelper.get().isConnected()){
				ToastUtil.showToastLong(context, "蓝牙未连接");
//				Intent intentCheckVersion = new Intent(this, BluetoothPairActivity.class);
//				startActivity(intentCheckVersion);
//				finish();
                jumpToBluetoothActivity();
				return;
			}
			if(queryTag.equals(firmwareUpdateBtn.getText())){
				// 查询
				manager.sendQueryFirmwareVersion();
			}else{
				//升级
				manager.sendFirmwareUpdate();
			}
			break;

		default:
			break;
		}
		super.onClick(v);
	}
	
	/**
	 * 跳转到检查版本界面的控件监听方法
	 * 
	 * @param v
	 */
	public void checkVersion(View v) {
		Intent intentCheckVersion = new Intent(this, CheckVersionActivity.class);
		startActivity(intentCheckVersion);
	}

	@Override
	public void handleMessage(Message msg) {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
