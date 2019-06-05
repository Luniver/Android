package hrst.sczd.utils;

/**
 * 手机工具类
 * @author glj
 * 2017-12-12
 */
public class PhoneUtil {
	
	/**
	 * 是否需允许短信诱发
	 * @return
	 */
	public static boolean isMsgInduce () {
		//检测手机牌子
		if (android.os.Build.BRAND.equalsIgnoreCase("nubia")
				|| android.os.Build.MODEL.toLowerCase().contains("mi")) {
			return true;
		}
		return false;
	}

}
