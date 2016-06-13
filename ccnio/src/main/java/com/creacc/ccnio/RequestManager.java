package com.creacc.ccnio;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2015/12/7.
 */
public class RequestManager {

    private static RequestManager instanceObject;

    private RequestQueue mRequestQueue;

    private RequestOptions mRequestOptions;

    public static RequestManager instance() {
        if (RequestManager.instanceObject == null) {
            synchronized (RequestManager.class) {
                if (RequestManager.instanceObject == null) {
                    RequestManager.instanceObject = new RequestManager();
                }
            }
        }
        return RequestManager.instanceObject;
    }

    private RequestManager() {
    }

    public synchronized void initialize(Context context, RequestOptions options) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        mRequestOptions = options;
    }

    public <T> void doRequest(RequestBase<T> request, RequestListener<T> listener) {
        mRequestQueue.add(request.buildRequest(mRequestOptions.getBaseUrl(), listener));
    }

}
