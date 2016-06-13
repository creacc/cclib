package com.creacc.ccnio;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creacc.ccbox.utils.LogUtils;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/7.
 */
public abstract class RequestBase<ResultType> {

    @NotParam
    private Request mVolleyRequest;

    protected abstract String obtainFunctionUri();

    protected abstract String obtainParamUri();

    protected abstract ResultType parseResult(String response);

    protected abstract int method();

    protected String obtainBaseUrl() {
        return null;
    }

    protected Map<String, String> obtainParams() {
        return null;
    }

    protected Map<String, String> obtainHeaders() {
        return Collections.emptyMap();
    }

    public void cancelRequest() {
        if (mVolleyRequest != null) {
            mVolleyRequest.cancel();
        }
    }

    Request buildRequest(final String baseUrl, final RequestListener<ResultType> listener) {
        String requestUrl = buildRequestUrl(baseUrl);
        LogUtils.print("request", requestUrl);
        return mVolleyRequest = new StringRequest(method(), requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ResultType parseResult = parseResult(response);
                    try {
                        listener.onRequestComplete(parseResult);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    listener.onRequestComplete(null);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestComplete(null);
                LogUtils.print(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return obtainParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return obtainHeaders();
            }

        };

    }

    @NonNull
    private String buildRequestUrl(String baseUrl) {
        String requestBaseUrl = obtainBaseUrl();
        if (TextUtils.isEmpty(requestBaseUrl)) {
            requestBaseUrl = baseUrl;
        }
        String url = requestBaseUrl + obtainFunctionUri();
        String paramUri = obtainParamUri();
        if (TextUtils.isEmpty(paramUri) == false) {
            url += paramUri;
        }
        return url;
    }

}
