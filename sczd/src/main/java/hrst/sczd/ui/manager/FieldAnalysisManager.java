package hrst.sczd.ui.manager;

import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE;
import hrst.sczd.systemsettings.SettingUtils;
import hrst.sczd.ui.model.CommunityModel;
import hrst.sczd.utils.SaveData_withPreferences;
import hrst.sczd.utils.ToastUtil;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * 强场分析管理类
 * @author glj
 * 2018-01-19
 */
public class FieldAnalysisManager {
	private static final String TAG = "sfx-FieldManager";
	
	private Context context;
	
	private SaveData_withPreferences sPreferences;
	
	/** 记录上次温度值 */
	private long lastTempHighTime;
	
	/** 记录上次电量值 */
	private long lastElectricityLowTime;
	
	/**
	 * 构造函数
	 */
	public FieldAnalysisManager (Context context) {
		this.context = context;
		sPreferences = SaveData_withPreferences.getInstance(context);
	}

	/**
	 * 处理温度电量信息
	 * @param obj
	 */
	public void showTemperatureAndElectricity(MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE obj) {
		//获取设备上报的温度和电量
		short digTemper = obj.getDigitalTemperature_a();
		short rfTemper = obj.getrFTemperature_b();
		short electricity = obj.getElectricity_c();
		
		//设置高温和低电量的值
		short tempHigh = Short.valueOf(sPreferences.getData_String(SettingUtils.TempHighValue, "65"));
		short electricityLow = Short.valueOf(sPreferences.getData_String(SettingUtils.HostDeficiencyValue, "20"));
		boolean tempAlarm = sPreferences.getData_Boolean(SettingUtils.TempHigh, false);
		boolean elecAlarm = sPreferences.getData_Boolean(SettingUtils.HostDeficiency, false);

//		Log.d(TAG, "showTemperatureAndElectricity: temperature: " + tempHigh + " -- elect: " + electricityLow);
		//获取当前时间
		long nowTime = System.currentTimeMillis();
		
		//温度过高并且3分钟播放一次
		if (tempAlarm && (digTemper >= tempHigh || rfTemper >= tempHigh) && (nowTime-lastTempHighTime)/1000 > 120) {
			lastTempHighTime = nowTime;
			ToastUtil.showToastLong(context, "设备温度过高!");
			playSounds(12);
		}
		
		//电量过低并且3分钟播放一次
		if (elecAlarm && (electricity <= electricityLow) && ((nowTime-lastElectricityLowTime)/1000 > 120)) {
			lastElectricityLowTime = nowTime;
			ToastUtil.showToastLong(context, "设备电量过低!");
			playSounds(13);
		}
	}
	/**
	 * 设备上报固件启动异常
	 */
	public void showFirmwareStartupAnomaly(
			MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY obj) {
		byte errorCode = obj.getcErrorCode_a();
		switch (errorCode) {
			case 0x1:
				ToastUtil.showToastLong(context, "FPGA固件异常");
				break;
			case 0x2:
				ToastUtil.showToastLong(context, "设备树异常");
				break;
			case 0x3:
				ToastUtil.showToastLong(context, "内核固件异常");
				break;
			case 0x4:
				ToastUtil.showToastLong(context, "Linux APP程序异常");
				break;
		}
		InteractiveManager.getInstance().sendFirmwareStartupAnomalyAck();
	}
	/**
	 * 语音播报类
	 * 
	 * @param sound
	 *            声音类型
	 */
	private void playSounds(int sound) {
		Log.d(TAG, "playSounds: " + sound);
		AudioManager am = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float audioCurrentVolumn = am
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float volumnRatio = audioCurrentVolumn / audioMaxVolumn;
		CommunityModel
				.getInstance(context)
				.getSoundPool()
				.play(CommunityModel.getInstance(context).getSoundHashMap()
						.get(sound), volumnRatio, volumnRatio, 1, 0, 1);
	}
	
	

}
