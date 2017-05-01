package com.creacc.ccnio.response;

import com.creacc.ccnio.RequestListener;

import java.util.List;

/**
 * Created by Administrator on 2016/3/6.
 */
public abstract class ListResponseListener<ResultType> implements RequestListener<ListResponseEntity<ResultType>> {

    public abstract void onRequestComplete(boolean success, List<ResultType> result);

    @Override
    public void onRequestComplete(ListResponseEntity<ResultType> result) {
        if (result.isSuccess()) {
            onRequestComplete(true, result.getResultEntity());
        } else {
            onRequestComplete(false, null);
        }
    }
}
