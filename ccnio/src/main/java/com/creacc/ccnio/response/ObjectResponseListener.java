package com.creacc.ccnio.response;

import com.creacc.ccnio.RequestListener;

/**
 * Created by Administrator on 2016/3/6.
 */
public abstract class ObjectResponseListener<ResultType> implements RequestListener<ObjectResponseEntity<ResultType>> {

    public abstract void onRequestComplete(boolean success, ResultType result);

    @Override
    public void onRequestComplete(ObjectResponseEntity<ResultType> result) {
        if (result.isSuccess()) {
            onRequestComplete(true, result.getResultEntity());
        } else {
            onRequestComplete(false, null);
        }
    }
}
