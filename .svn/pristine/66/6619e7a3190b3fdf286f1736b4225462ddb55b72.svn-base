package hrst.sczd.utils;

import android.content.Context;
import android.widget.Toast;
/**
 * @author 刘兰
 * @describe 提示信息工具类
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class UserConfig{
	public static boolean IsDay = false;
	static Toast toast=null;

	/**
	 * 打印数据
	 * 
	 * @param c 类
	 * @param string 内容
	 */
	public static void p(Object c, String string) {
		System.out.println(c.getClass().getName() + "单兵====" + string);
	}

	/**
	 * 显示tomcat
	 * 
	 * @param context 上下文
	 * @param info 内容
	 */
	public static void ShowToast(Context context, String info) {
		ToastUtil.showToast(context, info);
//		ToastMgr.getInstance(context).display(info, Toast.LENGTH_SHORT);
	}

	/**
	 * 获取制式
	 * 
	 * @param context 上下文
	 * @return short 制式
	 */
	public static short getfreq(Context context) {
//		String string = Global.targetFormat;
//		if (string==null||string.equals("")) {
		SaveData_withPreferences sPreferences=SaveData_withPreferences.getInstance(context);
		String string=sPreferences.getData_String("strTargetFormat");
//		}
		if (string==null) {
			UserConfig.ShowToast(context,"请重新登录");
			return -1;
		}
		short protocol = -1;
		if (string.equals("GSM")) {
			protocol = 1;
		} else if (string.equals("CDMA")) {
			protocol = 2;
		} else if (string.equals("WCDMA")) {
			protocol = 3;
		} else if (string.equals("TD-SCDMA")) {
			protocol = 4;
		}
		// 2016-07-19 关玲基
		else if (string.equals("LTE")) {
			protocol=5;
		} 
		
		else if (string.equals("非CDMA")) {
			
		}
		return protocol;
	}

}
