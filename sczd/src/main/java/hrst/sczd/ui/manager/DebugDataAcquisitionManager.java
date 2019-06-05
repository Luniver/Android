package hrst.sczd.ui.manager;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hrst.common.ui.InduceConfigure;
import com.hrst.common.ui.RuntimeStatus;
import com.hrst.common.util.ToastUtils;
import com.hrst.induce.annotation.TargetFormat;

import java.text.DecimalFormat;

import hrst.sczd.R;
import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_U_DISK_INSERTION;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_KEEP_NUMBER;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_KEEP_NUMBER_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_U_DISK_SIZE_ACK;
import hrst.sczd.ui.activity.DebugDataAcquisitionActivity;
import hrst.sczd.ui.activity.U_DiskKeepNumActivity;
import hrst.sczd.view.CustomToast;

/**
 * 调试数据获取管理类
 * @author glj
 * 2018-01-25
 */
public class DebugDataAcquisitionManager {
	
	/** 关闭存数弹窗命令字 */
	private static final int DISS_KEEP_NUM_DIALOG = 0x01;
	
	private DebugDataAcquisitionActivity activity;
	

	/**
	 * 构造函数
	 * @param activity
	 */
	public DebugDataAcquisitionManager(DebugDataAcquisitionActivity activity) {
		this.activity =activity;
	}
	
	/**
	 * 显示当前制式
	 * @param currentModeTxt
	 */
	public void showCurrentMode(TextView currentModeTxt) {
		switch (InduceConfigure.targetInduceFormat) {
		case TargetFormat.TARGET_FORMAT_GSM:
			currentModeTxt.setText("GSM");
			break;
		case TargetFormat.TARGET_FORMAT_CDMA:
			currentModeTxt.setText("CDMA");
			break;
		case TargetFormat.TARGET_FORMAT_WCDMA:
			currentModeTxt.setText("WCDMA");
			break;
		case TargetFormat.TARGET_FORMAT_TDSCDMA:
			currentModeTxt.setText("TD-SCDMA");
			break;
		case TargetFormat.TARGET_FORMAT_LTE:
			currentModeTxt.setText("LTE");
			break;
		}
		
	}
	
	/**
	 * 获取抓数模式并下发指令
	 * @param selectMode
	 * 			0常规 1同步
	 * @param frame		帧数
	 */
	public void getModeInfo(int selectMode, int frame) {
		//检测输入帧数并进行帧数下发
		if(selectMode == 0xff || checkParam(frame)){
			PDA_TO_MCU_REQUEST_KEEP_NUMBER obj = new PDA_TO_MCU_REQUEST_KEEP_NUMBER();
			obj.setMode_a(selectMode);
			obj.setLength_b(frame);
			InteractiveManager.getInstance().sendRequestKeepNumber(obj);
		}
	}
	

	/**
	 * 显示TF存数结果
	 * @param obj
	 */
	public void showKeepNumberAck(PDA_TO_MCU_REQUEST_KEEP_NUMBER_ACK obj) {
		byte result = obj.getcResult_a();
		
		switch (result) {
		
		//存数失败
		case (byte) 0xFF:
			ToastUtils.showToast( "存数失败");
			break;
		
		//TF卡空间不足
		case (byte) 0xFE:
			ToastUtils.showToast("TF卡空间不足");
			break;
		default:
			//显示进度弹窗
            if (result > 0){
                showDialog(result);
            }
			break;
		}
	}
	
	/**
	 * 显示进度弹窗
	 * @param result
	 */
	private void showDialog(byte result) {

		//如果弹窗为null时创建弹窗
        activity.showProgress(result);

		//停留1秒后关闭弹窗
		if (result == 100) {
		    activity.hideProgress();
		}
		
	}
	
	/**
	 * 显示U盘容量信息
	 */
	public void showU_DiskSizeInfo(PDA_TO_MCU_REQUEST_U_DISK_SIZE_ACK obj,
			ProgressBar uProgress, TextView uAvailableSpaceSizeTxt,
			TextView uTotalSpaceSizeTxt) {
		//剩余容量
		float availableSpaceSize = obj.getUDiskRestSize_b()/1024f;
		
		//总容量
		float totalSpaceSize = obj.getUDiskSize_a()/1024f;
		
		//按照四舍五入方式换算成GB单位
		String availableStr = conversionOfTwoBits(availableSpaceSize);
		String totalStr = conversionOfTwoBits(totalSpaceSize);
		
		uAvailableSpaceSizeTxt.setText(availableStr + " GB可用");
		uTotalSpaceSizeTxt.setText("总 "+ totalStr + "GB");
		
		//计算剩余空间
		float fAvailable = Float.valueOf(availableStr).floatValue();
		float fTotal = Float.valueOf(totalStr).floatValue();
		
		float size = fAvailable / fTotal;
		
		//显示剩余空间
		uProgress.setProgress((int)(100-size));
	}
	
	/**
	 * 显示TF容量信息
	 * @param obj
	 * @param tfProgress
	 * @param tfAvailableSpaceSizeTxt
	 * @param tfTotalSpaceSizeTxt
	 */
	public void showTFSizeInfo(PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE_ACK obj,
			ProgressBar tfProgress, TextView tfAvailableSpaceSizeTxt,
			TextView tfTotalSpaceSizeTxt) {
		//剩余容量
		float availableSpaceSize = obj.getTFUsedCapacity_b()/1024f;
		
		//总容量
		float totalSpaceSize = obj.getTFTotalCapacity_a()/1024f;
		
		//按照四舍五入方式换算成GB单位
		String availableStr = conversionOfTwoBits(availableSpaceSize);
		String totalStr = conversionOfTwoBits(totalSpaceSize);
		
		tfAvailableSpaceSizeTxt.setText(availableStr);
		tfTotalSpaceSizeTxt.setText("总 "+ totalStr + "GB");
		
		//计算剩余空间
		float fAvailable = Float.valueOf(availableStr).floatValue();
		float fTotal = Float.valueOf(totalStr).floatValue();
		
		float size = fAvailable / fTotal;
		
		//显示剩余空间
		tfProgress.setProgress((int)(100-size));
	}
	
	/**
	 * 进入U盘存数界面
	 */
	public void toU_DiskKeepNum() {
		Intent intent = new Intent(activity, U_DiskKeepNumActivity.class);
		intent.putExtra("tv_back", "调试数据获取");
		activity.startActivity(intent);
	}
	
	/**
	 * 保留两位小数
	 * @param size
	 * @return
	 */
	private String conversionOfTwoBits (float size) {
		DecimalFormat fnum = new DecimalFormat("##0.00");  
		return fnum.format(size);     
	}
	

	/**
	 * 检测输入参数
	 * @param framesNum
	 * 			帧数
	 * @return
	 */
	public boolean checkParam(int framesNum) {
		
		switch (InduceConfigure.targetInduceFormat) {
		//GSM
		case TargetFormat.TARGET_FORMAT_GSM:
			if (framesNum < 1 || framesNum > 32*8) {
				// 32 是针对256MB U盘存数空间, 现在是512 MB, 所以 *2, 下同
				ToastUtils.showToast("GSM帧数范围:1~256");
				return false;
			}
			break;
		//CDMA
		case TargetFormat.TARGET_FORMAT_CDMA:
			if (framesNum < 1 || framesNum > 21*2) {
				ToastUtils.showToast( "CDMA帧数范围:1~42");
				return false;
			}
			break;
		//WCDMA
		case TargetFormat.TARGET_FORMAT_WCDMA:
			if (framesNum < 1 || framesNum > 54*2) {
				ToastUtils.showToast( "WCDMA帧数范围:1~108");
				return false;
			}
			break;
		//TD-SCDMA
		case TargetFormat.TARGET_FORMAT_TDSCDMA:
			if (framesNum < 1 || framesNum > 163*2) {
				ToastUtils.showToast( "TD-SCDMA帧数范围:1~326");
				return false;
			}
			break;
		//LTE
		case TargetFormat.TARGET_FORMAT_LTE:
			if (framesNum < 1  || framesNum > 27 * 2) {
				ToastUtils.showToast( "LTE帧数范围:1~54");
				return false;
			}
			break;
		}
		
		return true;
	}

	/**
	 * 显示U盘插入/拔出状态
	 * @param obj
	 * @param uDiskLlayout 
	 */
	public void showU_DiskInsertion(MCU_TO_PDA_U_DISK_INSERTION obj, LinearLayout uDiskLlayout) {
		switch (obj.cResult_a) {
		
		case 0:
			new CustomToast().showToast(activity, R.layout.toast_u_disk_state, "U盘插入");
			//请求U盘大小
			InteractiveManager.getInstance().sendRequestUDiskSize();
			uDiskLlayout.setVisibility(View.VISIBLE);
			break;
			
		case 1:
			new CustomToast().showToast(activity, R.layout.toast_u_disk_state, "U盘拔出");
			uDiskLlayout.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

}
