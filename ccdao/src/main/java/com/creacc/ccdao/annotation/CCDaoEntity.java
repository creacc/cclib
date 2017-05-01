package com.creacc.ccdao.annotation;

import com.creacc.ccdao.exception.CCDaoInitializeException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识此表存在于独立数据库文件中<br/>
 * 此注解必须与{@link CCTableEntity}同时使用<br/>
 * <br/>
 * Created by Creacc on 2016/5/8.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CCDaoEntity {

    /**
     * 数据库文件名称，默认与表名相同
     */
    String name() default "";

    /**
     * 数据库版本，默认为1
     * @throws CCDaoInitializeException 当数据库版本小于或等于0时，抛出此异常
     */
    int version() default 1;
}
