package com.creacc.ccdao;

import com.creacc.ccdao.resolver.CCColumnResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识此成员可进行数据库读取<br/>
 * <br/>
 * Created by Creacc on 2016/5/8.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CCColumn {

    /**
     * 该成员在数据库中的列名称，默认为成员名
     */
    String name() default "";

    /**
     * 该成员的数据解析器
     */
    Class<? extends CCColumnResolver> serializer() default CCColumnResolver.class;

}
