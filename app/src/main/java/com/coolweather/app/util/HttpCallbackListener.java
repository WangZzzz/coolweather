package com.coolweather.app.util;

/**
 * Created by WangZ on 2015/5/11.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
