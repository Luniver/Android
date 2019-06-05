package com.hrst.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 运营商类型
 * Created by Sophimp on 2018/7/10.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE})
public @interface SimOperator {

    /**
     * 未识别
     */
    String NONE = "none";

    /**
     * 移动
     */
    String MOBILE = "mobile";

    /**
     * 联通
     */
    String UNICOM = "unicom";

    /**
     * 电信
     */
    String TELECOM = "telecom";
}
