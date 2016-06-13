package com.creacc.ccnio.response;

import com.creacc.box.nio.RequestListener;
import com.example.administrator.community.Utils.UIUtils;

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
            UIUtils.showToast(result.getMessage());
            onRequestComplete(false, null);
        }
    }
}
