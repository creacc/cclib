package com.creacc.ccnio;

import java.util.Map;

/**
 * Created by Administrator on 2016/3/3.
 */
public abstract class ComplexParamParser<T> {

    public ComplexParamParser() {

    }

    public abstract Map<String, String> toRequestParam(T object);
}
