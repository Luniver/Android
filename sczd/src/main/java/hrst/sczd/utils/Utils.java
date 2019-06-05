package hrst.sczd.utils;

import hrst.sczd.ExitApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.hrst.common.ui.InduceConfigure;

/**
 * @author 沈月美
 * @describe 工具类
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */
public class Utils {

    private static ProgressDialog proDialog; // 进度条控制
    private static Boolean isExit = false;
    private static Context mcontext;

    /**
     * 闪窗
     * 
     * @param splash
     *            类
     * @param main
     */
    public static void splash(final Activity splash, final Class main) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent splashIntent = new Intent();
                splashIntent.setClass(splash, main);
                splash.startActivity(splashIntent);
                splash.finish();
            }
        }, 3000);
    }

    /**
     * 双击退出函数
     * 
     * @param context
     *            上下文
     */
    public static void exitBy2Click(Activity context) {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            UserConfig.ShowToast(context, "再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            ExitApplication.getInstance().exit();
        }
    }

    /**
     * 退出系统
     * 
     * @param context
     *            上下文
     */
    public static void exitSystem(Context context) {
    	mcontext=context;
        ExitDialog.Builder builder = new ExitDialog.Builder(mcontext);
        builder.setMessage("是否确定关闭设备？");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 获取号码制式 返回0，代表移动; 1代表联通; 2代表电信; 3代表无制式
     * 
     * @param strPhoneNumber
     *            手机号码
     */
    public static int getPhoneFormat(String strPhoneNumber) {
    	
    	if (strPhoneNumber == null)
    	{
    		return 3;
    	}
    	
    	if (strPhoneNumber.length() < 4)
    	{
    		return 3;
    	}
    	
    	if (strPhoneNumber.length() != 11)
    	{
    		return 3;
    	}
    	
        // 获取前三位号码
        String strTopThree = strPhoneNumber.substring(0, 3);
        String strTopFour = strPhoneNumber.substring(0, 4); //这里是新加的 2016.8.11
        int iPhoneFormat = 0;
        //17开头的是4G
        //14开头的事上网卡
        if (strTopThree.equals("131") || strTopThree.equals("130")
                || strTopThree.equals("132") || strTopThree.equals("155")
                || strTopThree.equals("156") || strTopThree.equals("186")
                
        		|| strTopThree.equals("185") || strTopThree.equals("176")
        		|| strTopThree.equals("145") //这里是新加的 7.25
                || strTopThree.equals("175") //2016-11-29
                //虚拟运营商
                || strTopFour.equals("1709") //这里是新加的 2016.8.11
                || strTopFour.equals("1704") //2016-11-29
                || strTopFour.equals("1707") //2016-11-29
                || strTopFour.equals("1708") //2016-11-29
                || strTopFour.equals("1709") //2016-11-29
                || strTopThree.equals("171") //2016-11-29
        		) {// 判断是否为2G联通,3G联通
            iPhoneFormat = 1;
        } else if (strTopThree.equals("133") || strTopThree.equals("153")
                || strTopThree.equals("189")
                
                || strTopThree.equals("180") || strTopThree.equals("181")
                || strTopThree.equals("177") //这里是新加的 7.25
                || strTopFour.equals("1700") //这里是新加的 2016.8.11
                || strTopThree.equals("649") //这里是新加的 电信物联卡2016.8.11
                || strTopThree.equals("173") //2016-11-29
                || strTopThree.equals("149") //2016-11-29
                //虚拟运营商
                || strTopFour.equals("1701") //2016-11-29
                || strTopFour.equals("1702") //2016-11-29
        		) {// 判断是否为电信
            iPhoneFormat = 2;
        } else if (strTopThree.equals("134") || strTopThree.equals("135")
                || strTopThree.equals("136") || strTopThree.equals("137")
                || strTopThree.equals("138") || strTopThree.equals("139")
                || strTopThree.equals("150") || strTopThree.equals("151")
                || strTopThree.equals("152") || strTopThree.equals("158")
                || strTopThree.equals("159") || strTopThree.equals("182")
                || strTopThree.equals("183") || strTopThree.equals("187")
                || strTopThree.equals("157") || strTopThree.equals("188")
              
                || strTopThree.equals("184") || strTopThree.equals("178")
                || strTopThree.equals("147")   //这里是新加的 7.25
                || strTopFour.equals("1705")   //这里是新加的 2016.8.11
                
                || strTopFour.equals("1703") //2016-11-29
                || strTopFour.equals("1706") //2016-11-29
        		) {// 判断是否为2G移动,3G移动
            iPhoneFormat = 0;
        } else {
            iPhoneFormat = 3;
        }
        return iPhoneFormat;
    }

    /**
     * 判断手机号码可用性
     * 
     * @return boolean 是否可用
     */
    public static boolean judgePhoneAvailability(String phoneNumber) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,1,5-9]))\\d{8}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    /**
     * 判断号码位数与有效性
     * 
     * @param number
     *            号码
     * @param textName
     *            "目标号码"文本
     * @param tvTargetNumber
     *            目标号码的Text（提示报错)
     * @return boolean 是否有效
     */
    public static boolean checkNumberAvailable(String number, String textName,
            TextView tvTargetNumber) {
        // 当输入的号码长度等于1时
        if (number.equals("")) {
        	tvTargetNumber.setText(textName);
            return false;
        }
        if (InduceConfigure.countryNum.contains("86")) {
            if ((!number.substring(0, 1).equals("1"))) {// 判断号码长度为1时的情况
                tvTargetNumber.setText(textName + "(" + "输入首位不正确!" + ")");
                return false;
            } else if (number.length() > 0 && number.length() < 11) {
                tvTargetNumber.setText(textName + "(" + "长度不够!" + ")");
            } else if (number.length() == 11) {
                tvTargetNumber.setText(textName);
                return true;
            } else if (number.length() > 11) {// 如果输入的号码长度大于11时，给予客户提示
                tvTargetNumber.setText(textName + "(" + "输入的号码长度不对" + ")");
                return false;
            } else if (number.length() == 0) {
            	 tvTargetNumber.setText(textName);
            	 return false;
            }
        } else {
        	return true;
        }
        return false;

    }

    /**
     * 判断文本的有效性
     * 
     * @param name
     *            判断的文本
     * @param text
     *            提示文本的首
     * @param tvTargetName
     *            提示文本的控件
     * @param lenght
     *            文本的长度
     * @return boolean 是否有效
     */
    public static boolean checkTextAvailable(String name, String text,
            TextView tvTargetName, int lenght) {
        if (!name.equals("")) {
            if (!verifySymbol(name)) {
                tvTargetName.setText(text);
                if (byteNum(name) > lenght * 2) {
                    tvTargetName.setText(text + "（最多只能输入" + lenght + "个汉字或"
                            + lenght * 2 + "个字母、数字）");
                    return false;
                } else {
                    tvTargetName.setText(text);
                    return true;
                }
            } else {
                tvTargetName.setText(text + "（不能输入特殊字符）");
                return false;
            }
        }

        return false;
    }

    /**
     * 这个是判断不是必填字段的输入文本的有效性
     * 
     * @param name
     *            判断的文本
     * @param text
     *            提示文本的首
     * @param tvTargetName
     *            提示文本的控件
     * @param lenght
     *            文本的长度
     * @return boolean 是否有效
     */
    public static boolean checkNoteAvailable(String name, String text,
            TextView tvTargetName, int lenght) {
        if (!name.equals("")) {
            if (!verifySymbol(name)) {
                tvTargetName.setText(text);
                if (byteNum(name) > lenght * 2) {
                    tvTargetName.setText(text + "（最多只能输入" + lenght + "个汉字或"
                            + lenght * 2 + "个字母、数字）");
                    return false;
                } else {
                    tvTargetName.setText(text);
                    return true;
                }
            } else {
                tvTargetName.setText(text + "（不能输入特殊字符）");
                return false;
            }
        }
        return true;
    }

    /**
     * 不能输入特殊符号
     * 
     * @param str
     *            内容
     * @return boolean 是否正确
     */
    public static boolean verifySymbol(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>《》/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean verify = m.find();
        if (verify == false) {
            String surplusEx = "\"_-\\～﹏『』－＿×÷±√↑↓←→°○●△▲☆★♡♥♀♂◎";
            for (int i = 0; i < surplusEx.length(); i++) {
                if (str.indexOf(surplusEx.substring(i, i + 1)) != -1) {
                    return true;
                }
            }
        }
        return verify;
    }

    /**
     * 判断文本长度
     * 
     * @param str
     * @param lenght
     * @return
     */
    public static int judgeTextFormat(String str, int lenght) {
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return lenght + 3;
        }
        p = Pattern.compile("[\u4e00-\u9fa5]");
        m = p.matcher(str);
        if (m.matches()) {
            return lenght;
        } else {
            return lenght + 1;
        }
    }

    /**
     * 获取字节长度
     * 
     * @param str
     * @return int 长度
     */
    public static int byteNum(String str) {
        int num = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.substring(i, i + 1).matches("[\\u4e00-\\u9fa5]+")) {
                num += 2;
            } else {
                num += 1;
            }
        }
        return num;
    }

    /**
     * 过滤文本
     * 
     * @param str
     * @return String 过滤后的内容
     */
    public static String stringFilter(String str) {
        String regEx = "[/\\:*?<>|\"\n\t]";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(str);
        return matcher.replaceAll("").trim();
    }

    /**
     * 获取配置
     * 
     * @param context
     *            上下文
     * @param name
     *            Key
     * @param defaultValue
     *            默认值
     * @return boolean
     */
    public static boolean getSetting(Context context, String name,
            boolean defaultValue) {
        if (context != null) {
            final SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            boolean value = prefs.getBoolean(name, defaultValue);
            return value;
        }
        return false;
    }

    /**
     * 获取配置
     * 
     * @param context
     *            上下文
     * @param name
     *            key
     * @param defaultValue
     *            默认值
     * @return String
     */
    public static String getSetting(Context context, String name,
            String defaultValue) {
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String value = prefs.getString(name, defaultValue);
        return value;
    }

    /**
     * 保存配置
     * 
     * @param context
     *            上下文
     * @param name
     *            key
     * @param value
     *            默认值
     * @return boolean
     */
    public static boolean setSetting(Context context, String name, boolean value) {
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putBoolean(name, value);
        return editor.commit(); // 提交
    }

    /**
     * 保存配置
     * 
     * @param context
     *            上下文
     * @param name
     *            key
     * @param value
     *            默认值
     * @return boolean
     */
    public static boolean setSetting(Context context, String name, String value) {
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putString(name, value);
        return editor.commit(); // 提交
    }

    /**
     * 判断service是否运行
     * 
     * @param context
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 显示进度条
     * 
     * @param title
     *            标题
     * @param message
     *            内容
     * @param context
     *            上下文
     */
    @SuppressLint("NewApi")
    public static void showProgress(String title, String message,
            Context context) {
        proDialog = new ProgressDialog(context);
        if (!title.isEmpty()) {
            proDialog.setTitle(title);
        }
        if (!message.isEmpty()) {
            proDialog.setMessage(message);
        }
        proDialog.show();
        proDialog.setCancelable(false);
    }

    /**
     * 隐藏进度条
     * 
     */
    public static void progressDismiss() {
        if (proDialog != null) {
            proDialog.dismiss();
        }
    }

    /**
     *  拼接mcu与dsp组成的主版本号与当前pda版本的主版本号匹配
     * 
     * @param McuVersion
     * @param DspVersion
     * @param LocalVersion
     * @return
     */
    public static boolean versionPairing(String McuVersion, String DspVersion,
            String LocalVersion) {
        StringBuffer RomateStr = new StringBuffer();
        RomateStr.append(McuVersion.substring(0, 1) + ".");
        RomateStr.append(DspVersion.substring(0, 1));
        String LocalStr = LocalVersion.substring(0, 3);
        if (LocalStr.toString().equals(RomateStr.toString())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 匹配新旧版本的主版本号，更新界面用到
     * 
     * @param oldVersion
     * @param newVersion
     * @return
     */
    public static boolean oldPairingNew(String oldVersion, String newVersion) {
        String LocalStr = oldVersion.substring(0, 3);
        String newStr = newVersion.substring(0, 3);
        String localVer1=oldVersion.substring(oldVersion.length()-3, oldVersion.length()-2);
        String localver2=oldVersion.substring(oldVersion.length()-1, oldVersion.length());
        String newVer1=newVersion.substring(newVersion.length()-3, newVersion.length()-2);
        String newVer2=newVersion.substring(newVersion.length()-1, newVersion.length());
        System.out.println("本地版本："+localVer1+","+localver2+"网络版本："+newVer1+","+newVer2);
        if (LocalStr.equals(newStr)) {
        	//主版本匹配
        	if (Integer.parseInt(localVer1)<Integer.parseInt(newVer1)) {
        		 return true;
			}else if (Integer.parseInt(localVer1)==Integer.parseInt(newVer1)) {
				if (Integer.parseInt(localver2)<Integer.parseInt(newVer2)) {
					return true;
				}else {
					return false;
				}
				
			}else {
				return false;
			}
           
        } else {
            return false;
        }
    }

    /**
     * 在一段字符串中匹配另一段字符的个数
     * 
     * @param str 内容
     * @param findstr 要匹配的字符
     * @return int 个数
     */
    public static int findstr(String str, String findstr) {
        int total = 0;
        Pattern p = Pattern.compile(findstr);
        Matcher m = p.matcher(str);
        while (m.find()) {
            total++;
        }
        return total;
    }

    /**
     * 获取当前时间
     * 
     * @return String 时间字符串
     */
    public static String getNowTime() {
        SimpleDateFormat objsDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String strcurrentTime = objsDateFormat.format(curDate);
        return strcurrentTime;
    }
}
