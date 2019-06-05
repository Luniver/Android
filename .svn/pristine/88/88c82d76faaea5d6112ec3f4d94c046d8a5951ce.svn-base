package com.hrst.common.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.hrst.common.annotation.SimOperator;
import com.hrst.common.base.BaseApp;
import com.hrst.common.entity.CardState;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 手机卡槽sim 卡工具类
 * 对外提供:
 * 卡槽的卡状态
 * 当前手机卡槽状态 {@link #cardState}
 * 卡槽一 operator {@link #simOperator1} cardId {@link #cardId1}
 * 卡槽二 operator {@link #simOperator2} cardId {@link #cardId2}
 * <p>
 * 各运营商标准码
 * 联通{@link #UNICOM_STANDARD_CODE}
 * 电信{@link #TELECOM_STANDARD_CODE}
 * 移动{@link #MOBILE_STANDARD_CODE}
 * 提供一些方法:
 * 判断运营商
 * {@link #isMobile(String)}
 * {@link #isTelecom(String)}}
 * {@link #isUnicom(String)}}
 * <p>
 * create by Sophimp on 2018/7/10
 */
public class DualSimUtils {

    private final static String TAG = "goo-DualSimUtils";

    // 获取手机厂商
    private final static String MOBILE_PHONE_MANUFACTURERS = Build.MANUFACTURER;

    //努比亚双卡双待手机设备管理
    private final static String PHONE_MANAGER_PACKAGE = "android.telephony.TelephonyManager";
    private final static String HAI_SI_CHIP_PHONE_MANAGER = "android.telephony.MSimTelephonyManager";
    private final static String GAO_TONG_CHIP_SIM_PHONE_MANAGER = "android.telephony.MSimTelephonyManager";
    private final static String MTK_CHIP_SIM_PHONE_MANAGER = "com.android.internal.telephony.Phone";

    /**
     * 卡一 operator
     */
    public static String simOperator1 = "";

    /**
     * 卡二 operator
     */
    public static String simOperator2 = "";

    /**
     * 卡槽1 对应的id
     */
    public static int cardId1 = -1;

    /**
     * 卡槽2 对应的id
     */
    public static int cardId2 = -1;

    /**
     * 移动标准码
     */
    public static final String MOBILE_STANDARD_CODE = "46000";

    /**
     * 联通标准码
     */
    public static final String UNICOM_STANDARD_CODE = "46001";

    /**
     * 电信标准码
     */
    public static final String TELECOM_STANDARD_CODE = "46003";

    /**
     * 手机sim卡状态
     */
    public static CardState cardState = CardState.NO_CARD;

    /**
     * 移动占有的 MCC,MNC
     */
    private final static String[] MOBILE_OPERATORS = {"46000", "46002", "46007"};
    /**
     * 联通占有的 MCC,MNC
     */
    private final static String[] UNICOM_OPERATORS = {"46001", "46006"};
    /**
     * 电信占有的 MCC,MNC
     */
    private final static String[] TELECOM_OPERATORS = {"46003", "46005"};
    /**
     * 获取当前Android版本号
     */
    private final static int ANDROID_VERSION = Build.VERSION.SDK_INT;

    private final static String ANDROID_MODEL = Build.MODEL;

    /**
     * 读取当前手机卡状态
     */
    public static void readSimOperator(Context context) {

        // 清理状态
        clearState();

        /**
         * 已踩得坑:
         * 四种芯片:
         * 高通(小米, nubia)
         * 联发科(魅族)
         * 麒麟海思(华为)
         * 猎户座(三星)
         *
         * 我们的诱发机目前只涉及到 高通芯片
         * 反射类  TelephonyManager
         * 反射方法 getSubscriberId(int id)
         * 反射方法 getSubscriberId(long id)
         *
         * 华为手机
         * 反射类  MSimTelephonyManager
         * 反射方法 getDefault()
         * 反射方法 getSubscriberId(int id)
         * 反射方法 getSubscriberId(long id)
         */
        Log.d(TAG, "android version: " + ANDROID_VERSION);
        TelephonyManager telephonyManager = null;
        if (ANDROID_VERSION == 19) {
            telephonyManager = readCardIdAndOperatorForSDK19(int.class);
        } else if (ANDROID_VERSION == 21) {
            telephonyManager = readCardIdAndOperatorForSDK21(long.class);
        } else if (ANDROID_VERSION >= 22) {
            telephonyManager = readCardIdAndOperatorForSDK22(int.class);
        }

        // 暂时没有mtk诱发机, 酷柏手机已淘汰
//        readMtkSimImsi();

//        Log.d(TAG, "goo sim operator0: " + simOperator1 + " - " + cardId1);
//        Log.d(TAG, "goo sim operator1: " + simOperator2 + " - " + cardId2);

        // 更新当前卡槽状态
        if (-1 == cardId1 && -1 == cardId2) {
            cardState = CardState.NO_CARD;
            return;
        }

        // 转成标准码
        simOperator1 = toStandardCode(simOperator1);
        simOperator2 = toStandardCode(simOperator2);

        int phoneType = 0;
        if (null != telephonyManager){
             phoneType =  telephonyManager.getPhoneType();
        }
        Logger.d("raw sim operator: " + phoneType);
        if (-1 == cardId1) {
            cardState = CardState.ONLY_CARD_TWO;
            if (phoneType == 1){ //GSM
                simOperator2 = "46001";
            }else if(phoneType == 2){ //CDMA
                simOperator2 = "46003";
            }
        } else if (cardId2 == -1) {
            cardState = CardState.ONLY_CARD_ONE;
            if (phoneType == 1){ //GSM
                simOperator1 = "46001";
            }else if(phoneType == 2){ //CDMA
                simOperator1 = "46003";
            }
        } else {
            // 如果是同一个运营商, 或者一张联通, 一张电信, 都默认使用卡一
            if (simOperator1.equals(simOperator2) || (!isTelecom(simOperator1) && !isTelecom(simOperator2))) {
                // 两张卡同一运营商, 默认使用卡1
                cardState = CardState.ONLY_CARD_ONE;
            } else {
                // 必然为一张电信, 一张非电信卡
                cardState = CardState.BOTH_CARD;
            }
        }

        Logger.d(DualSimUtils.loggerShare());
    }

    /**
     * 22api 以上获取 operator 和 cardId
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private static TelephonyManager readCardIdAndOperatorForSDK22(Class paramClz) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) Class.forName(PHONE_MANAGER_PACKAGE).getDeclaredMethod("getDefault").invoke(null);
//            simOperator1 = telephonyManager.getSubscriberId();
            // 获取默认cardId
            int[] subArr = null;
            SubscriptionManager subscriptionManager = SubscriptionManager.from(BaseApp.getCtx());
            Method getSubId = subscriptionManager.getClass().getDeclaredMethod("getSubId", new Class[]{Integer.TYPE});
            /*
                判断卡槽1
                如果卡槽没有卡, 会返回一个异常大的值
             */
            subArr = (int[]) getSubId.invoke(subscriptionManager, 0);
            if (subArr[0] < 50 && subArr[0] >= 0) {
//                Global.nubia_card_0 = subArr[0];
                // 到底是subId, 还是 slotId, 针对卡一有卡的情况, 赋值0是有效果的
                cardId1 = 0;
            }
            Logger.d(TAG, "slot 0 ids: " + Arrays.toString(subArr));
            // 判断卡槽2
            subArr = (int[]) getSubId.invoke(subscriptionManager, 1);
            if (subArr[0] < 50 && subArr[0] >= 0) {
                cardId2 = subArr[0];
            }
//            if (-1 == cardId1 && -1 == cardId2) {
                // 两个槽都没有卡
//                return;
//            }

            Logger.d(TAG, "slot 1 ids: " + Arrays.toString(subArr));

            // 反射 getSubscriberId 方法
//            Class clz = telephonyManager.getClass();
//            Method[] methods = clz.getDeclaredMethods();
//            for (Method m : methods) {
//                if (m.getName().contains("getSubscriberId")) {
//                    Type[] types = m.getGenericParameterTypes();
//                    Logger.d("goo-x " + m.getName() + " params:" + Arrays.toString(types));
//                }
//            }

            // 获取 subscriberId 只适用于sdk22+
            Method getSubscriberId = telephonyManager.getClass().getDeclaredMethod("getSubscriberId", paramClz);
//            for (int i=0; i<50; i++){
//            }
            if (-1 != cardId1 && null != getSubscriberId) {
                simOperator1 = (String) getSubscriberId.invoke(telephonyManager, cardId1);
                if (TextUtils.isEmpty(simOperator1))
                    simOperator1 = telephonyManager.getNetworkOperator();
                if (TextUtils.isEmpty(simOperator1))
                    simOperator1 = telephonyManager.getSimOperator();
            }
            if (-1 != cardId2 && null != getSubscriberId) {
                simOperator2 = (String) getSubscriberId.invoke(telephonyManager, cardId2);
                if (TextUtils.isEmpty(simOperator2))
                    simOperator2 = telephonyManager.getNetworkOperator();
                if (TextUtils.isEmpty(simOperator2))
                    simOperator2 = telephonyManager.getSimOperator();
            }
            return telephonyManager;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            // 可能是华为手机
            readHaisiSimImsi();
        }
        return null;
    }

    private static TelephonyManager readCardIdAndOperatorForSDK19(Class paramClz) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) Class.forName(PHONE_MANAGER_PACKAGE).getDeclaredMethod("getDefault").invoke(null);
//            for (Method method : telephonyManager.getClass().getDeclaredMethods()) {
//                Log.d(TAG, "readCardIdAndOperatorForSDK19: " + method.getName() + Arrays.toString(method.getParameterTypes()));
//                if ("getSubscriberId".equals(method.getName())
//                        || "getSimOperator".equals(method.getName())
//                        || "getSimOperatorName".equals(method)
//                        || "getSubscriberId".equals(method)
//                        || "getSubscriberInfo".equals(method)
//                        || "getSimSerialNumber".equals(method)
//                        || "getSimCountryIso".equals(method)
//                        ){
//
//                    Object ree = method.invoke(telephonyManager);
//                    Log.d(TAG, "result: " + ree);
//                    if (null != ree){
//                        Log.d(TAG, "result: " + ree.toString());
//                    }
//                }
//            }
            // sdk19 getSubscriberId() 默认找到有卡的那一个槽, 如果有两张卡, 默认卡一
            Method getSubscriberId = telephonyManager.getClass().getDeclaredMethod("getSimOperator");
            Object res1 = getSubscriberId.invoke(telephonyManager);
            if (null != res1) {
                simOperator1 = res1.toString();
            }
            if (!TextUtils.isEmpty(simOperator1)) {
                cardId1 = 0;
            }
            for (int i = 1; i < 50; i++) {
                // android
                Object res2 = getSubscriberId.invoke(telephonyManager);
                if (null != res2) {
                    simOperator2 = res2.toString();
                }
                // 要换成全码的
                if (!TextUtils.isEmpty(simOperator2) && !simOperator2.equals(simOperator1)) {
                    cardId2 = i;
                    Log.d(TAG, "readCardIdAndOperatorForSDK21: " + simOperator2);
                    break;
                }
            }
//            if (-1 == cardId1 && -1 == cardId2) {
//                // 两个槽都没有卡
//                return telephonyManager;
//            }

            return telephonyManager;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            // 可能是华为手机
            readHaisiSimImsi();
        }
        return null;
    }

    /**
     * android 21 api 读取 operator  cardId
     */

    private static TelephonyManager readCardIdAndOperatorForSDK21(Class paramClz) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) Class.forName(PHONE_MANAGER_PACKAGE).getDeclaredMethod("getDefault").invoke(null);
            Method getSubscriberId = telephonyManager.getClass().getDeclaredMethod("getSubscriberId", paramClz);
//            for (Method method : telephonyManager.getClass().getDeclaredMethods()) {
//                Log.d(TAG, "readCardIdAndOperatorForSDK21: " + method.getName() + Arrays.toString(method.getParameterTypes()));
//            }
            Object res1 = getSubscriberId.invoke(telephonyManager, 0);
            if (null != res1) {
                simOperator1 = res1.toString();
            }
            if (!TextUtils.isEmpty(simOperator1)) {
                cardId1 = 0;
            }
            for (int i = 1; i < 50; i++) {
                Object res2 = getSubscriberId.invoke(telephonyManager, i);
                if (null != res2) {
                    simOperator2 = res2.toString();
                }
                // 要换成全码的
                if (!TextUtils.isEmpty(simOperator2) && !simOperator2.equals(simOperator1)) {
                    cardId2 = i;
                    Log.d(TAG, "readCardIdAndOperatorForSDK21: " + simOperator2);
                    break;
                }
            }
//            if (-1 == cardId1 && -1 == cardId2) {
//                 两个槽都没有卡
//                return;
//            }
            return telephonyManager;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            // 可能是华为手机
            readHaisiSimImsi();
        }
        return null;
    }

    private static void readMtkSimImsi() {
        try {
            Class phoneClz = Class.forName(MTK_CHIP_SIM_PHONE_MANAGER);
            Field gemini_sim_1 = phoneClz.getField("GEMINI_SIM_1");
            gemini_sim_1.setAccessible(true);
            cardId1 = (int) gemini_sim_1.get(null);
            Field gemini_sim_2 = phoneClz.getField("GEMINI_SIM_2");
            gemini_sim_2.setAccessible(true);
            cardId2 = (int) gemini_sim_2.get(null);
            TelephonyManager tm = (TelephonyManager) TelephonyManager.class.getDeclaredMethod("getDefault").invoke(null);
            Method getSubscriberId = TelephonyManager.class.getDeclaredMethod("getSubscriberIdGemini", int.class);
            simOperator1 = (String) getSubscriberId.invoke(tm, cardId1);
            simOperator2 = (String) getSubscriberId.invoke(tm, cardId2);

            loggerShare();

        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取海思芯片 sim imsi
     */
    private static void readHaisiSimImsi() {
        try {
            Object mTelephonyManager = Class.forName(HAI_SI_CHIP_PHONE_MANAGER).getDeclaredMethod("getDefault").invoke(null);
            simOperator1 = (String) mTelephonyManager.getClass().getDeclaredMethod("getSimOperator", long.class).invoke(mTelephonyManager, 0);
            for (int i = 1; i < 50; i++) {
                simOperator2 = (String) mTelephonyManager.getClass().getDeclaredMethod("getSimOperator", long.class).invoke(mTelephonyManager, i);
                if (!TextUtils.isEmpty(simOperator2)) {
                    // 跟双卡相关的常量都放在此类, 后续重构不用 Global, 此后的业务, 皆用此变量
                    cardId2 = i;
                    break;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将各运营商sim 卡id 统一成各自标准(初始)的code
     *
     * @param simOperator 实际的operator
     * @return 标准code, 没有在所选卡范围, 返回空字符
     */
    public static String toStandardCode(String simOperator) {
        String standCode = "";
        if (TextUtils.isEmpty(simOperator)) return standCode;
        // 移动判断
        if (isMobile(simOperator)) return MOBILE_STANDARD_CODE;
        // 联通判断
        if (isUnicom(simOperator)) return UNICOM_STANDARD_CODE;
        // 电信判断
        if (isTelecom(simOperator)) return TELECOM_STANDARD_CODE;
        return standCode;
    }

    /**
     * 是否是移动卡
     */
    public static boolean isMobile(String simOperator) {
        for (int i = 0; i < MOBILE_OPERATORS.length; i++) {
            if (simOperator.contains(MOBILE_OPERATORS[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是联通卡
     */
    public static boolean isUnicom(String simOperator) {
        for (int i = 0; i < UNICOM_OPERATORS.length; i++) {
            if (simOperator.contains(UNICOM_OPERATORS[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是电信卡
     */
    public static boolean isTelecom(String simOperator) {
        for (int i = 0; i < TELECOM_OPERATORS.length; i++) {
            if (simOperator.contains(TELECOM_OPERATORS[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否有电信卡
     */
    public static boolean hasCdmaCard() {
        return isTelecom(simOperator1) || isTelecom(simOperator2);
    }

    /**
     * 获取手机号码运营商
     */
    public static @SimOperator
    String getSimOperator(String strPhoneNumber) {
        String operator = SimOperator.NONE;
        if (TextUtils.isEmpty(strPhoneNumber) || strPhoneNumber.length() < 4 || strPhoneNumber.length() != 11) {
            return operator;
        }

        // 获取前三位号码
        String strTopThree = strPhoneNumber.substring(0, 3);
        String strTopFour = strPhoneNumber.substring(0, 4); //这里是新加的 2016.8.11
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
                || strTopThree.equals("166") //2018-06-01
                ) {// 判断是否为2G联通,3G联通
            operator = SimOperator.UNICOM;
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
            operator = SimOperator.TELECOM;
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
            operator = SimOperator.MOBILE;
        }
        return operator;
    }

    /**
     * 判断卡槽是否是电信卡
     *
     * @param slot 卡槽 1, 2
     */
    public static boolean isTelecom(int slot) {
        switch (slot) {
            case 1: // 卡槽一
                return isTelecom(simOperator1);
            case 2: // 卡槽二
                return isTelecom(simOperator2);
            default: // 卡槽一
                return isTelecom(simOperator1);
        }
    }

    /**
     * 根据卡槽状态获取默认cardId
     *
     * @return 默认cardId
     */
    public static int getDefaultCardId(String targetPhone) {
        int cardId = cardId1;
        switch (cardState) {
            case ONLY_CARD_ONE:
                cardId = cardId1;
                break;
            case ONLY_CARD_TWO:
                cardId = cardId2;
                break;
            case BOTH_CARD:
                if (SimOperator.TELECOM.equals(getSimOperator(targetPhone))) {
                    cardId = cardId1;
                } else {
                    // 如果目标号是非电信号, 默认选择非电信卡
                    cardId = isTelecom(simOperator1) ? cardId2 : cardId1;
                }
                break;
        }
        return cardId;
    }

    public static void clearState(){
        cardState = CardState.NO_CARD;
        cardId1 = -1;
        cardId2 = -1;
        simOperator1 = "";
        simOperator2 = "";
    }

    /**
     * 打印共享状态值
     */
    public static String loggerShare() {
        return "DualSimUtitlsShared: \n"
                + "simOperator1: " + simOperator1
                + "\n cardId1: " + cardId1
                + "\n sim1 is cdma: " + isTelecom(simOperator1)
                + "\n simOperator2: " + simOperator2
                + "\n cardId2: " + cardId2
                + "\n sim2 is cdma: " + isTelecom(simOperator2)
                + "\n cardState: " + cardState;
    }
}