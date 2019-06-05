package com.hrst.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * LTE 捕获方式
 * Created by Sophimp on 2018/6/22.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE})
public @interface CaptureMethod {
    /**
     * 常用短信
     */
    int NORMAL_CAPTURE = 0;
    /**
     * 新捕获
     */
    int NEW_CAPTURE = 1;

    /**
     * 精准捕获
     */
    int PRECISION_CAPTURE = 2;
}
