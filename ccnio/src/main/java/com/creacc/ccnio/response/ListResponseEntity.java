package com.creacc.ccnio.response;

import com.creacc.box.utils.JsonUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/6.
 */
public class ListResponseEntity<ResultType> extends ResponseEntity<ResultType> {

    private List<ResultType> mResultEntity;

    protected ListResponseEntity(String response, Class<ResultType> resultClass) {
        super(response, resultClass);
    }

    public List<ResultType> getResultEntity() {
        return mResultEntity;
    }

    @Override
    protected void parseResult(JSONObject jsonObject, Class<ResultType> resultClass) {
        JSONArray elements = JsonUtils.getJsonArray(jsonObject, "result", null);
        int elementCount = elements.length();
        mResultEntity = new ArrayList<ResultType>(elementCount);
        for (int i = 0; i < elementCount; i++) {
            try {
                mResultEntity.add(new Gson().fromJson(elements.getString(i), resultClass));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
