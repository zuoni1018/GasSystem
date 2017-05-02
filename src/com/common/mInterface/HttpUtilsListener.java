package com.common.mInterface;

/**
 * Created by zangyi_shuai_ge on 2017/4/21
 * 网络访问接口回调
 */

public interface HttpUtilsListener {
    void OnError(okhttp3.Call call, Exception e, int id);

    void OnResponse(String response, int id);
}
