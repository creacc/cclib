package com.creacc.ccnio;

/**
 * Created by Administrator on 2016/3/6.
 */
public class RequestOptions {

    private String mBaseUrl;

    String getBaseUrl() {
        return mBaseUrl;
    }

    public static class Builder {

        private String mBaseUrl;

        public Builder setBaseUrl(String baseUrl) {
            mBaseUrl = baseUrl;
            return this;
        }

        public RequestOptions build() {
            RequestOptions options = new RequestOptions();
            options.mBaseUrl = mBaseUrl;
            return options;
        }
    }

}
