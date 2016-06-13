package com.creacc.ccnio.response;

import android.text.TextUtils;

import com.creacc.ccbox.utils.JsonUtils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/6.
 */
public abstract class ResponseEntity<ResultType> {

    private boolean mIsSuccess;

    private String mMessage;

    public static <T> ListResponseEntity<T> parseList(String response, Class<T> resultClass) {
        return new ListResponseEntity<T>(response, resultClass);
    }

    public static <T> ObjectResponseEntity<T> parseObject(String response, Class<T> resultClass) {
        return new ObjectResponseEntity<T>(response, resultClass);
    }

    protected ResponseEntity(String response, Class<ResultType> resultClass) {
        if (TextUtils.isEmpty(response) == false) {
            JSONObject jsonObject = JsonUtils.asJsonObject(response);
            if (jsonObject != null) {
                if (mIsSuccess = isSuccess(jsonObject)) {
                    parseResult(jsonObject, resultClass);
                } else {
                    mMessage = parseMessage(jsonObject);
                }
            }
        }
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }

    public String getMessage() {
        return mMessage;
    }

    protected boolean isSuccess(JSONObject jsonObject) {
        return JsonUtils.getBooleanValue(jsonObject, "success",  false);
    }

    protected String parseMessage(JSONObject jsonObject) {
        return JsonUtils.getStringValue(jsonObject, "msg", "");
    }

    protected abstract void parseResult(JSONObject jsonObject, Class<ResultType> resultClass);
}
