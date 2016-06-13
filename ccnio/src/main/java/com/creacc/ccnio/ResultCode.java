package com.creacc.ccnio;

/**
 * Created by Administrator on 2015/12/7.
 */
public enum  ResultCode {

    SUCCESS(true),
    FAILURE(false);

    private boolean mSuccess;

    ResultCode(boolean success) {
        mSuccess = success;
    }

    public boolean isSuccess() {
        return mSuccess;
    }
}
