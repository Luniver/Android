package com.hrst.common.ui;

import com.hrst.common.annotation.SimOperator;
import com.hrst.induce.annotation.SmsPduMode;
import com.hrst.induce.annotation.TargetFormat;

/**
 * 号码, 制式选择配置
 * <p>
 * 诱发状态配置
 * Created by Sophimp on 2018/7/10.
 */
public class InduceConfigure {

    /**
     * 目标诱发号码
     */
    public static String targetPhone = "";

    /**
     * 目标号码的运营商
     */
    public static @SimOperator
    String targetPhoneOperator = SimOperator.NONE;

    /**
     * 目标诱发制式
     */
    public static @TargetFormat
    String targetInduceFormat = TargetFormat.TARGET_FORMAT_GSM;

    /**
     * 诱发格式选择
     */
    @SmsPduMode
    public static int pduMode = SmsPduMode.PDU_MODE_TWO;

    /**
     * 国家名称
     */
    public static String countryName = "";

    /**
     * 国家码
     */
    public static String countryNum = "";

    /**
     * 当前默认或选择后的诱发方式
     */
    public static String sendMethod;

    /**
     * 打印当前的配置信息
     */
    public static String loggerConfigure() {
        return "induce configure: \n" + "countryNum: " + countryNum
                + "\n countryName: " + countryName
                + "\n targetPhone: " + targetPhone
                + "\n targetPhone operator: " + targetPhoneOperator
                + "\n pduMode: " + pduMode
                + "\n sendMethod: " + sendMethod
                + "\n targetFormat: " + targetInduceFormat;
    }
}
