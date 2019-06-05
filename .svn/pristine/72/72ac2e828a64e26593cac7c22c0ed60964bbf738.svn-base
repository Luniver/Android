package hrst.sczd.ui.manager;

import hrst.sczd.R;
import android.app.Activity;

import com.hrst.common.util.SharedPreferencesUtils;

/**
 * @author 沈月美
 * @describe 更换皮肤的管理类
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class SkinSettingManager {
	public final static String SKIN_PREF = "skinSetting";
	private int[] skinResources = { 0, 1 };
	private Activity activity;

	/**
	 * 构造方法
	 * 
	 * @param activity
	 */
	public SkinSettingManager(Activity activity) {
		this.activity = activity;
	}

	/**
	 * 获取当前程序的皮肤号
	 * 
	 * @return int 皮肤id
	 */
	public int getSkinType() {
		String key = "skin_type";
		return SharedPreferencesUtils.getInt(key);
	}

	/**
	 * 获取当前皮肤的id
	 * 
	 * @return id
	 */
	public int getCurrentSkinRes() {
		int skinLen = skinResources.length;
		int getSkinLen = getSkinType();
		if (getSkinLen >= skinLen) {
			getSkinLen = 0;
		}
		return skinResources[getSkinLen];
	}

	/**
	 * 用于初始化皮肤
	 * 
	 */
	public void initSkins() {
		if (getCurrentSkinRes() == 0) {
			activity.setTheme(R.style.NightModeTheme);
		} else {
			activity.setTheme(R.style.DayTimeModeTheme);
		}
	}

}
