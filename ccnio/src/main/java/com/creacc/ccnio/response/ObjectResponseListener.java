package com.creacc.ccnio.response;

import com.creacc.box.nio.RequestListener;
import com.example.administrator.community.Utils.UIUtils;

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
            UIUtils.showToast(result.getMessage());
            onRequestComplete(false, null);
        }
    }
}
