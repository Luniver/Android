package com.hrst.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 格式选择
 * Created by Sophimp on 2018/6/22.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE})
public @interface SmsPduMode {
    int PDU_MODE_ONE = 0;   //格式一
    int PDU_MODE_TWO = 1;   //格式二
    int PDU_MODE_THREE = 2; //格式三
}
