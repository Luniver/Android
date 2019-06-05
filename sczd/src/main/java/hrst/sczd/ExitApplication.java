package hrst.sczd;


import hrst.sczd.agreement.sczd.McuDataDispatcher;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

import com.hrst.BluetoothCommHelper;
import com.hrst.common.ui.HrstApplication;
import com.hrst.common.ui.errorlog.CrashHandler;
import com.hrst.induce.InduceClient;

/**
 * @author 沈月美
 * @describe 退出程序应用类
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
 */
public class ExitApplication extends HrstApplication {

    public List<Activity> activityList = new LinkedList<Activity>();

    public static ExitApplication instance;

    public ExitApplication() {
    }

    public static ExitApplication getInstance() {
        return instance;
    }

    @Override
    public void createInit() {
        super.createInit();
        instance = this;
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        // 蓝牙数据接收监听
        BluetoothCommHelper.get().addOnDataReceiveListener(McuDataDispatcher::dataReceiveListener);

        // 初始化诱发模块
        InduceClient.init(this);
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
//			System.out.println(activity.toString());
//			if (!activity.toString().contains("hrst.SelectEntryActivity")) {
            activity.finish();
//			}
        }
    }
}
