package com.hrst.induce.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Lin on 2018/6/27.
 */

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface SendMethod {
    String METHOD_CDMA_SEND = "method_cdma_send";   //电信卡诱发
    String METHOD_NOT_CDMA_SEND = "method_cdma_not_send";   //非电信卡诱发
    String METHOD_LTE_NORMAL = "method_lte_normal"; //LTE常规捕获（常用短信）
    String METHOD_LTE_NEW = "method_lte_new";   //LTE新捕获
    String METHOD_LTE_PRECISE = "method_lte_precise";   //LTE精准捕获
}
