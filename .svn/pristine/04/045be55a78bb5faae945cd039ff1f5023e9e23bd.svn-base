package hrst.sczd.ui.activity.dialog;

import java.io.UnsupportedEncodingException;

import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.manager.vo.VersionInfo;
import hrst.sczd.agreement.sczd.vo.JumpToBluetoothSelectCmd;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_VERSION_ACK;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.hrst.BluetoothCommHelper;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 检查版本更新
 * 
 * @author 刘兰
 * @date 2014-04-03
 * 
 */
public class CheckVersionActivity extends Activity {
	private final static String TAG = "goo-CheckVersion";
	private static EditText versionlist;
	private VersionInfo versionInfo = new VersionInfo();
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// 取消标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_check_version);
		ExitApplication.getInstance().addActivity(this);
		this.setFinishOnTouchOutside(false);
		EventBus.getDefault().register(this);
		versionlist = (EditText) findViewById(R.id.editText1);
	}
	
	/**
	 * 取消检查监听
	 * 
	 * @param v
	 */
	public void cancel(View v) {
		this.finish();
	}
	
//	/**
//	 * 设备上报固件启动异常
//	 * @param obj
//	 */
//	@Subscribe (threadMode = ThreadMode.MainThread)
//	public void getVersionInfo (VersionInfo info){
//		String versionInfo = 
//				"Linux系统固件版本\n"+ info.getLinuxSystemFirmwareVersion() + "\n\n"+
//				"FPGA固件版本\n"+ info.getFpgaFirmwareVersion() + "\n\n"+
//				"Linux APP版本\n" + info.getLinuxAppVersion() + "\n\n"+
//				"硬件版本\n" + info.getHardwareVersion() + "\n\n";
//		versionlist.setText(versionInfo);
//		
//	}
	/**
	 * 设备上报固件启动异常
	 * @param obj
	 */
	@Subscribe (threadMode = ThreadMode.MainThread)
	public void getVersionInfo (PDA_TO_MCU_REQUEST_VERSION_ACK obj){
		Log.d(TAG, "getVersionInfo: " + obj.toString());
		String realVersionStr = null;
		try {
			realVersionStr = new String(obj.getVersionMessage_b(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		switch (obj.getcVersionType_a()){
			case 0: // Linux 系统固件版本
				versionInfo.setLinuxSystemFirmwareVersion(realVersionStr);
				break;
			case 1: // FPGA 固件版本
				versionInfo.setFpgaFirmwareVersion(realVersionStr);
				break;
			case 2: // Linux APP 版本
				versionInfo.setLinuxAppVersion(realVersionStr);
				break;
			case 3: // 硬件版本
				versionInfo.setHardwareVersion(realVersionStr);
				break;
				default:
					break;
		}

		String versionStr=
				"Linux系统固件版本\n"+ versionInfo.getLinuxSystemFirmwareVersion() + "\n\n"+
				"FPGA固件版本\n"+ versionInfo.getFpgaFirmwareVersion() + "\n\n"+
				"Linux APP版本\n" + versionInfo.getLinuxAppVersion() + "\n\n"+
				"硬件版本\n" + versionInfo.getHardwareVersion() + "\n\n";
		versionlist.setText(versionStr);
		
	}
	
	/**
	 * 检查版本监听
	 * 
	 * @param v
	 */
	public void checkVersion(View v) {
		if (false || !BluetoothCommHelper.get().isConnected()) {
//			ToastUtil.showToastLong(this, "蓝牙未连接");
//			Intent intentCheckVersion = new Intent(this, BluetoothPairActivity.class);
//			startActivity(intentCheckVersion);
            jumpToBluetoothActivity();
            finish();
		} else {
			InteractiveManager.getInstance().sendRequestVersion();
		}
	}

	@Override
	protected void onDestroy() {
		//销毁广播
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	protected void jumpToBluetoothActivity(){
		EventBus.getDefault().post(new JumpToBluetoothSelectCmd());
	}
}
