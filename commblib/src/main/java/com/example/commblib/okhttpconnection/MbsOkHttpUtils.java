package com.example.commblib.okhttpconnection;

import okhttp3.OkHttpClient;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/8/8
 * @描述: http通讯工具，创建进行通讯的httpClient对象
 */

public class MbsOkHttpUtils {
    /**
     * 创建并返回进行通讯的HttpClient对象
     */
    public OkHttpClient getNewHttpClient(){
        OkHttpClient client = new OkHttpClient();
        return client;
    }
}
