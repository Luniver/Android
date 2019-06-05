package com.hrst.common.util;

import android.text.TextUtils;
import android.widget.Toast;

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
	/**
	 * 判断手机号码是否正确
	 *
	 * @return
	 */
	public static Boolean isPhoneNumber(final String phoneNumber) {
		if (TextUtils.isEmpty(phoneNumber)){
			return false;
		} else if (!phoneNumber.startsWith("1") || phoneNumber.length() < 11) {
			return false;
		}
		return true;
	}
}
