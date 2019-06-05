package hrst.sczd.ui.manager;

import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE_ACK;
import hrst.sczd.utils.ToastUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * 关于我们管理类
 * @author glj
 * 2018-01-30
 */
public class AboutUsManager {
	
	private static final int CANCEL_DIALOG = 0x1;
	
	private Activity activity;
	
	private ProgressDialog progressDialog;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CANCEL_DIALOG:
				if(progressDialog != null){
					progressDialog.cancel();
					progressDialog = null;
				}
				break;

			default:
				break;
			}
		}
	};

	public AboutUsManager(Activity activity) {
		this.activity = activity;
	}

	/**
	 * 发送固件升级指令
	 */
	public void sendFirmwareUpdate() {
		InteractiveManager.getInstance().sendRequestUpdateFirmware();
	}
	
	/**
	 * 显示固件升级进度
	 * @param obj
	 */
	public void showFirmwareUpdateInfo(PDA_TO_MCU_REQUEST_UPDATE_FIRMWARE_ACK obj) {
		//		0xFF文件校验失败
		//		0xFE 固件升级失败
		//		0 ~ 100(%)固件升级进度
		byte result = obj.getcResult_a();
		switch (result) {
		case (byte) 0xFF:
			ToastUtil.showToastduan(activity, "文件校验失败");
			break;
		case (byte) 0xFE:
			ToastUtil.showToastduan(activity, "固件升级失败");
			break;
		case (byte) 0xFB:
			ToastUtil.showToastduan(activity, "固件解压出错");
			break;
		default:
			showUpdateDialog(result);
			break;
		}
	}
	/**
	 * 显示更新进度
	 * @param result
	 */
	private void showUpdateDialog(byte result) {
		// 升级成功会发3次100, 因为蓝牙会丢包 
		if(result == 100 && progressDialog == null) return;
		if (progressDialog == null ) {
//			dialog = new KeepNumDialog(activity, R.style.Theme_CustomDialog, "固件正在升级中...");
			showProgressDialog("固件正在升级中...");
//			dialog.show();
		}
		progressDialog.setProgress(result);
		
		if (result == 100 ) {
//			dialog.setContent("固件升级完成");
			handler.removeMessages(CANCEL_DIALOG);
			handler.sendEmptyMessageDelayed(CANCEL_DIALOG, 500);
		}
	}
	
	/**
	 * 显示进度条
	 */
	private void showProgressDialog(String title) {
	    final int MAX_PROGRESS = 100;
	    progressDialog = new ProgressDialog(activity);
	    progressDialog.setProgress(0);
	    progressDialog.setTitle(title);
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    progressDialog.setMax(MAX_PROGRESS);
//	    progressDialog.setCancelable(false);
	    progressDialog.setCanceledOnTouchOutside(false);
	    progressDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {

				ToastUtil.showToast(activity, "升级完成");
				progressDialog = null;
			}
		});
	    progressDialog.show();
	}
	


	/**
	 * 设置版本名称
	 * @param tvVersion
	 */
	public void setVersionText(TextView tvVersion) {
		tvVersion.setText(getVersionCode());
	}
	
	/**
	 * 获取版本号
	 * 
	 * @return String
	 */
	public String getVersionCode() {
		String versionCode = "";
		try {
			versionCode = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 查询因件版本号
	 */
	public void sendQueryFirmwareVersion() {

		InteractiveManager.getInstance().sendRequestUDiskFirmwareName();
	}
	
}
