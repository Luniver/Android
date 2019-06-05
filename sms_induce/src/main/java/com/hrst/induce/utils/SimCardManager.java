package com.hrst.induce.utils;

import android.app.Activity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.hrst.induce.InduceClient;

/**
 * SIM卡管理
 * Created by Lin on 2018/6/4.
 */

public class SimCardManager {

    /**
     * 获取当前Android版本号
     */
    private final static int ANDROID_VERSION = android.os.Build.VERSION.SDK_INT;
    /**
     * 获取手机厂商
     */
    private final static String MOBILE_PHONE_MANUFACTURERS = android.os.Build.MANUFACTURER;

    /**
     * 努比亚双卡双待手机设备管理
     */
    private final static String NUBIA_PACKET_NAME_KITKAT = "android.telephony.MSimTelephonyManager";
    private final static String NUBIA_PACKET_NAME_LOLLIPOP = "android.telephony.TelephonyManager";

    //运营商代码
    private static String[] mOperatorCode = new String[2];
    //sim subid
    private static int[] mSubId = new int[]{0, 0};

    private static Object mTelephonyManager;

    private static SimCardManager mInstance = new SimCardManager();

    private SimCardManager(){
        initSimCard();
    }

    public static SimCardManager create() {
        return mInstance;
    }

    /**
     * 初始化SIM卡数据
     */
    public static void initSimCard() {
        if (MOBILE_PHONE_MANUFACTURERS.equals("HUAWEI")) {
            //获取MSimTelephonyManager实例
            if (ANDROID_VERSION == 16) {
                LogUitl.i("DualSim--->ANDROID_VERSION == 16");
                mOperatorCode[0] = ((TelephonyManager) InduceClient.client().getSystemService(Activity.TELEPHONY_SERVICE)).getSubscriberId();
            }
        } else if (MOBILE_PHONE_MANUFACTURERS.equals("nubia")
                || android.os.Build.MODEL.contains("Mi")
                || android.os.Build.MODEL.contains("Redmi")) {
            try {
                //获取MSimTelephonyManager实例
                if (ANDROID_VERSION == 22) {
                    mTelephonyManager = Class.forName(NUBIA_PACKET_NAME_LOLLIPOP).getDeclaredMethod("getDefault").invoke(null);
                    mOperatorCode[0] = (String) mTelephonyManager.getClass().getDeclaredMethod("getSimOperator", int.class).invoke(mTelephonyManager, 0);
                    mOperatorCode[1] = (String) mTelephonyManager.getClass().getDeclaredMethod("getSimOperator", int.class).invoke(mTelephonyManager, 1);
                    for (int i = 0; i < 50; i++) {
                        mOperatorCode[1] = (String) mTelephonyManager.getClass().getDeclaredMethod("getSimOperator", int.class).invoke(mTelephonyManager, i);
                        if (!mOperatorCode[0].equals(mOperatorCode[1])) {
                            mSubId[1] = i;
                            break;
                        }
                    }
                } else if (ANDROID_VERSION == 21) {
                    LogUitl.i("DualSim--->ANDROID_VERSION == 21");
                    mTelephonyManager = Class.forName(NUBIA_PACKET_NAME_LOLLIPOP).getDeclaredMethod("getDefault").invoke(null);
                    mOperatorCode[0] = (String) mTelephonyManager.getClass().getDeclaredMethod("getSimOperator", long.class).invoke(mTelephonyManager, 0);
                    mOperatorCode[1] = (String) mTelephonyManager.getClass().getDeclaredMethod("getSimOperator", long.class).invoke(mTelephonyManager, 1);
                    for (int i = 0; i < 50; i++) {
                        mOperatorCode[1] = (String) mTelephonyManager.getClass().getDeclaredMethod("getSimOperator", long.class).invoke(mTelephonyManager, i);
                        if (!mOperatorCode[0].equals(mOperatorCode[1])) {
                            mSubId[1] = i;
                            break;
                        }
                    }
                } else if (ANDROID_VERSION == 19) {
                    LogUitl.i("DualSim--->ANDROID_VERSION == 19");
                    mTelephonyManager = Class.forName(NUBIA_PACKET_NAME_KITKAT).getDeclaredMethod("getDefault").invoke(null);
                    //获取卡一和卡二的代号
                    mOperatorCode[0] = (String) mTelephonyManager.getClass().getDeclaredMethod("getSimOperator", int.class).invoke(mTelephonyManager, 0);
                    mOperatorCode[1] = (String) mTelephonyManager.getClass().getDeclaredMethod("getSimOperator", int.class).invoke(mTelephonyManager, 1);
                } else if (ANDROID_VERSION >= 23) {
                    LogUitl.i("DualSim--->ANDROID_VERSION == 23");
                    mTelephonyManager = Class.forName(NUBIA_PACKET_NAME_LOLLIPOP).getDeclaredMethod("getDefault").invoke(null);
                    mOperatorCode[0] = (String) mTelephonyManager.getClass().getDeclaredMethod("getSimOperator", int.class).invoke(mTelephonyManager, 0);
                    mOperatorCode[1] = (String) mTelephonyManager.getClass().getDeclaredMethod("getSimOperator", int.class).invoke(mTelephonyManager, 1);
                    for (int i = 0; i < 50; i++) {
                        mOperatorCode[1] = (String) mTelephonyManager.getClass().getDeclaredMethod("getSimOperator", int.class).invoke(mTelephonyManager, i);
                        if (!mOperatorCode[0].equals(mOperatorCode[1])) {
                            mSubId[1] = i;
                            break;
                        }
                    }
                }
                reduceOperatorCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 精简运营商编码
     */
    private static void reduceOperatorCode(){
        // 46011和46005 CDMA 转为46003
        if (mOperatorCode[0].equals("46011") || mOperatorCode[0].equals("46005")) {
            mOperatorCode[0] = "46003";
        }
        if (mOperatorCode[1].equals("46011") || mOperatorCode[1].equals("46005")) {
            mOperatorCode[1] = "46003";
        }

        // 46006 W 转46001
        if (mOperatorCode[0].equals("46006")) {
            mOperatorCode[0] = "46001";
        }
        if (mOperatorCode[1].equals("46006")) {
            mOperatorCode[1] = "46001";
        }

        //46007 46002 TDS
        if (mOperatorCode[0].equals("46007") || mOperatorCode[0].equals("46002")) {
            mOperatorCode[0] = "46000";
        }
        if (mOperatorCode[1].equals("46007") || mOperatorCode[1].equals("46002")) {
            mOperatorCode[1] = "46000";
        }
    }

    /**
     * 判断是否双卡
     *
     * @return
     */
    public boolean isDualSim() {
        LogUitl.i("SimManager ----> isDualSim()");
        if (!TextUtils.isEmpty(mOperatorCode[0]) && !TextUtils.isEmpty(mOperatorCode[1])) {
            //卡一、卡二编号均不为空
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取运营商代码
     *
     * @return
     */
    public String[] getOperatorCode() {
        return mOperatorCode;
    }

    /**
     * 获取SubId
     *
     * @return
     */
    public int[] getSubId() {
        return mSubId;
    }

    /**
     * 获取号码制式 返回0，代表移动; 1代表联通; 2代表电信; 3代表无制式
     *
     * @param strPhoneNumber
     *            手机号码
     */
    public static int getPhoneFormat(String strPhoneNumber) {
        if (strPhoneNumber == null) {
            return -1;
        }

        if (strPhoneNumber.length() < 4) {
            return -1;
        }

        if (strPhoneNumber.length() != 11) {
            return 3;
        }
        //17开头的是4G
        //14开头的事上网卡
        if (strPhoneNumber.startsWith("131") || strPhoneNumber.startsWith("130")
                || strPhoneNumber.startsWith("132") || strPhoneNumber.startsWith("155")
                || strPhoneNumber.startsWith("156") || strPhoneNumber.startsWith("186")

                || strPhoneNumber.startsWith("185") || strPhoneNumber.startsWith("176")
                || strPhoneNumber.startsWith("145") //这里是新加的 7.25
                || strPhoneNumber.startsWith("175") //2016-11-29
                || strPhoneNumber.startsWith("166") //2018-02-28
                //虚拟运营商
                || strPhoneNumber.startsWith("1709") //这里是新加的 2016.8.11
                || strPhoneNumber.startsWith("1704") //2016-11-29
                || strPhoneNumber.startsWith("1707") //2016-11-29
                || strPhoneNumber.startsWith("1708") //2016-11-29
                || strPhoneNumber.startsWith("1709") //2016-11-29
                || strPhoneNumber.startsWith("171") //2016-11-29

                ) {// 判断是否为2G联通,3G联通
            return 1;
        } else if (strPhoneNumber.startsWith("133") || strPhoneNumber.startsWith("153")
                || strPhoneNumber.startsWith("189")

                || strPhoneNumber.startsWith("180") || strPhoneNumber.startsWith("181")
                || strPhoneNumber.startsWith("177") //这里是新加的 7.25
                || strPhoneNumber.startsWith("1700") //这里是新加的 2016.8.11
                || strPhoneNumber.startsWith("649") //这里是新加的 电信物联卡2016.8.11
                || strPhoneNumber.startsWith("173") //2016-11-29
                || strPhoneNumber.startsWith("149") //2016-11-29
                || strPhoneNumber.startsWith("199") //2018-02-28
                //虚拟运营商
                || strPhoneNumber.startsWith("1701") //2016-11-29
                || strPhoneNumber.startsWith("1702") //2016-11-29
                ) {// 判断是否为电信
            return 2;
        } else if (strPhoneNumber.startsWith("134") || strPhoneNumber.startsWith("135")
                || strPhoneNumber.startsWith("136") || strPhoneNumber.startsWith("137")
                || strPhoneNumber.startsWith("138") || strPhoneNumber.startsWith("139")
                || strPhoneNumber.startsWith("150") || strPhoneNumber.startsWith("151")
                || strPhoneNumber.startsWith("152") || strPhoneNumber.startsWith("158")
                || strPhoneNumber.startsWith("159") || strPhoneNumber.startsWith("182")
                || strPhoneNumber.startsWith("183") || strPhoneNumber.startsWith("187")
                || strPhoneNumber.startsWith("157") || strPhoneNumber.startsWith("188")

                || strPhoneNumber.startsWith("184") || strPhoneNumber.startsWith("178")
                || strPhoneNumber.startsWith("147")   //这里是新加的 7.25
                || strPhoneNumber.startsWith("198")   //这里是新加的 2018-02-28
                || strPhoneNumber.startsWith("1705")   //这里是新加的 2016.8.11

                || strPhoneNumber.startsWith("1703") //2016-11-29
                || strPhoneNumber.startsWith("1706") //2016-11-29
                ) {// 判断是否为2G移动,3G移动
            return 0;
        } else {
            return 3;
        }
    }
}
