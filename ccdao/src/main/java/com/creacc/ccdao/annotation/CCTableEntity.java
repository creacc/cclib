package com.creacc.ccdao.annotation;

import android.support.annotation.IntDef;

import com.creacc.ccdao.CCUpgrader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识此Entity可进行数据库存取<br/>
 * <br/>
 * Created by Creacc on 2016/5/8.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CCTableEntity {

    /**
     * 除去带有{@link CCTableExcludeColumn}标识的成员，其余全部导入数据库
     * @see CCTableEntity#rule()
     */
    int EXCLUDE = 0;
    /**
     * 只包括带有{@link CCColumn}标识的成员，其余全部不导入数据库
     * @see CCTableEntity#rule()
     */
    int INCLUDE = 1;

    /**
     * 数据库表名称
     */
    String name() default "";

    Class<? extends CCUpgrader>[] upgraders() default CCUpgrader.class;

    /**
     *  用于标识将{@link CCTableEntity}中成员导入数据库的规则<br/>
     *  当不设置此注解时，默认采用{@link CCTableEntity#EXCLUDE}规则<br/>
     * @see CCTableEntity#EXCLUDE
     * @see CCTableEntity#INCLUDE
     */
    @RULE int rule() default EXCLUDE;

    @IntDef({EXCLUDE, INCLUDE})
    @Retention(RetentionPolicy.SOURCE)
    @interface RULE {}
}
