package com.creacc.ccnio.response;

import com.creacc.ccbox.utils.JsonUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/6.
 */
public class ObjectResponseEntity<ResultType> extends ResponseEntity<ResultType> {

    private ResultType mResultEntity;

    protected ObjectResponseEntity(String response, Class<ResultType> resultClass) {
        super(response, resultClass);
    }

    public ResultType getResultEntity() {
        return mResultEntity;
    }

    @Override
    protected void parseResult(JSONObject jsonObject, Class<ResultType> resultClass) {
        mResultEntity = new Gson().fromJson(JsonUtils.getStringValue(jsonObject, "result", ""), resultClass);
    }

}
