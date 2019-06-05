package com.hrst.induce.bean;

import com.hrst.induce.utils.InduceConstants;

/**
 * 诱发结果封装
 * Created by Lin on 2018/6/6.
 */

public class InduceResult {

    public boolean isSuccess;
    public int index;
    public String code;

    public static InduceResult createSmsResult(boolean isSuccess, String code){
        InduceResult result = new InduceResult();
        result.isSuccess = isSuccess;
        result.code = code;
        return result;
    }

    public static InduceResult createCallResult(boolean isSuccess){
        InduceResult result = new InduceResult();
        result.isSuccess = isSuccess;
        result.code = "";
        return result;
    }

    /**
     * 获取短信发送失败原因
     * @param resultCode
     * @return
     */
    public static String getResultReason(String resultCode){
        String reason = "";
        switch (resultCode){
            case InduceConstants.SMS_SENDDING_TIME_OUT:
                reason = "发送超时";
                break;
            case InduceConstants.SMS_SENDDING_ERROR_GENERIC_FAILURE:
                reason = "发送失败";
                break;
            case InduceConstants.SMS_SENDDING_ERROR_RADIO_OFF:
                reason = "广播错误";
                break;
            case InduceConstants.SMS_SENDDING_ERROR_NULL_PDU:
                reason = "PDU错误";
                break;
            case InduceConstants.SMS_SENDDING_ERROR_NO_SERVICE:
                reason = "没有服务";
                break;
            case InduceConstants.SMS_SENDDING_UNKNOWN_ERROR:
                reason = "未知错误";
                break;
        }
        return reason;
    }
}
