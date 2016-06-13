package com.creacc.ccdao;

/**
 * 当数据库版本小于或等于0时，抛出此异常<br/>
 * <br/>
 * Created by Creacc on 2016/5/8.
 */
public class CCDaoVersionException extends RuntimeException {

    public CCDaoVersionException() {
        super("The version must be larger than 0");
    }
}
