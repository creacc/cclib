package com.creacc.ccnio;

import com.android.volley.Request;

/**
 * Created by Administrator on 2016/3/3.
 */
public abstract class PutRequest<ResultType> extends PostRequest<ResultType> {

    @Override
    public int method() {
        return Request.Method.PUT;
    }
}
