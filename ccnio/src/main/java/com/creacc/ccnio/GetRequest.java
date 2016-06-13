package com.creacc.ccnio;

import com.android.volley.Request;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/24.
 */
public abstract class GetRequest<ResultType> extends RequestBase<ResultType> {

    @Override
    public int method() {
        return Request.Method.GET;
    }

    @Override
    public String obtainParamUri() {
        StringBuilder builder = new StringBuilder();
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
                    if (builder.length() == 0) {
                        builder.append("?");
                    } else {
                        builder.append("&");
                    }
                    if (field.isAnnotationPresent(ComplexParam.class)) {
                        Map<String, String> map = field.getAnnotation(ComplexParam.class).parser().newInstance().toRequestParam(object);
                        if (map != null) {
                            boolean isFirstParam = true;
                            for (Map.Entry<String, String> entry : map.entrySet()) {
                                if (isFirstParam == false) {
                                    builder.append("&");
                                }
                                builder.append(entry.getKey());
                                builder.append("=");
                                builder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                                isFirstParam = false;
                            }
                        }
                    } else {
                        builder.append(field.getName());
                        builder.append("=");
                        builder.append(URLEncoder.encode(String.valueOf(object), "UTF-8"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    @Override
    public Map<String, String> obtainParams() {
        return null;
    }
}
