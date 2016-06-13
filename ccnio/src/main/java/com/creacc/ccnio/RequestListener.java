package com.creacc.ccnio;

/**
 * Created by Administrator on 2015/12/7.
 */
public interface RequestListener<ResultType> {

    void onRequestComplete(ResultType result);
}
