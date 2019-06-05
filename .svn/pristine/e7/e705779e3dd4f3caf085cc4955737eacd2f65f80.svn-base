package hrst.sczd.utils;

import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_GAIN_SETTING;
import android.content.Context;

/**
 * 
 * @author glj
 * 增益调整工具
 * 2016-09-06
 *
 */
public class GainUtils {
	
	private static SaveData_withPreferences sPreferences;
	
	/**
	 * 自动设置高增益
	 * @param context
	 */
	public static void setHighGain(Context context) {
		sPreferences = SaveData_withPreferences.getInstance(context);
		String automatic = "手动增益";
		String string = "高增益";
		int i, j = 0;
		if (automatic.equals("自动增益")) {
			sPreferences.saveDatas_String("automatic", "自动增益");
			i = 1;
		} else {
			sPreferences.saveDatas_String("automatic", "手动增益");
			i = 0;
		}
		if (string.equals("调试增益")) {
			sPreferences.saveDatas_String("gainway", "调试增益");
			j = 3;
		} else if (string.equals("低增益")) {
			sPreferences.saveDatas_String("gainway", "低增益");
			j = 2;
		} else if (string.equals("中增益")) {
			sPreferences.saveDatas_String("gainway", "中增益");
			j = 1;
		} else if(string.equals("高增益")){
			sPreferences.saveDatas_String("gainway","高增益");
			j=0;
		}
		else if (string == null || string.equals("") || string.equals(0)) {
			i = 0;
			j = 1;
		}
		PDA_TO_MCU_REQUEST_GAIN_SETTING obj = new  PDA_TO_MCU_REQUEST_GAIN_SETTING();
		obj.setcGain_a((byte)j);
		InteractiveManager.getInstance().sendRequestGainSetting(obj);
	}
	
}
