package com.hrst.common.ui;


import android.app.Activity;

import com.hrst.common.base.BaseApp;
import com.hrst.common.ui.errorlog.CrashHandler;
import com.hrst.common.util.DualSimUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 沈月美
 * @describe 退出程序应用类
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class HrstApplication extends BaseApp {

	public List<Activity> activityList = new LinkedList<Activity>();

	@Override
	public void createInit() {
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		// 先读一次卡槽信息
		DualSimUtils.readSimOperator(getApplicationContext());

	}

	/**
	 * 添加Activity到容器中
	 * 
	 */
	public void addActivity(Activity activity) {
		if (activityList.size() > 0) {
			if (!activityList.contains(activity)) {
				activityList.add(activity);
			}
		} else {
			activityList.add(activity);
		}

	}

	/**
	 *  遍历所有Activity 并finish退出APP
	 *  
	 */
	public void exit() {
		// 循环退出容器中的activity
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
	
	/**
	 *  遍历所有Activity 并finish
	 *  
	 */
	public void finishActivity() {
		// 循环退出容器中的activity
		for (Activity activity : activityList) {
//			if (!activity.toString().contains("hrst.InduceConfigActivity")) {
				activity.finish();
//			}
		}
	}
}
