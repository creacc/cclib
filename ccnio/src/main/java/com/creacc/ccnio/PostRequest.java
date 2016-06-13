package com.creacc.ccnio;

import com.android.volley.Request;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/24.
 */
public abstract class PostRequest<ResultType> extends RequestBase<ResultType> {

    @Override
    public int method() {
        return Request.Method.POST;
    }

    @Override
    public String obtainParamUri() {
        return null;
    }

    @Override
    public Map<String, String> obtainParams() {
        Map<String, String> paramMap = new HashMap<String, String>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(NotParam.class)) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object object = field.get(this);
                if (object != null) {
                    if (field.isAnnotationPresent(ComplexParam.class)) {
                        Map<String, String> map = field.getAnnotation(ComplexParam.class).parser().newInstance().toRequestParam(object);
                        if (map != null) {
                            paramMap.putAll(map);
                        }
                    } else {
                        paramMap.put(field.getName(), URLEncoder.encode(String.valueOf(object)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return paramMap;
    }
}
