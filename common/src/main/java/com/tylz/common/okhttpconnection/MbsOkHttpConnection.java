package com.tylz.common.okhttpconnection;

import android.content.Context;
import android.text.TextUtils;

import com.tylz.common.utils.LogManager;

import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/25
 *  @描述：    负责通讯的类，输入一个请求(request),输入一个结果(result=response)
 */
public class MbsOkHttpConnection {
    private Context mContext;


    public MbsOkHttpConnection(Context context) {
        mContext = context;
    }

    public MbsResult2 http2Result(MbsRequest2 request) {
        if (request == null) {
            return null;
        }
        /**
         * 打印request
         */
        LogManager.logD(request.toString());
        String url    = request.getProtocolUrl();
        String method = request.getMethod().trim();
        if (TextUtils.isEmpty(url)) {
            throw new RuntimeException("request url empty");
        }

        //获取result
        MbsResult2 result = null;
        if (MbsRequest2.METHOD_GET.equalsIgnoreCase(method)) {
            result = OkHttpGetConn(request);
        } else {
            result = OkHttpPostConn(request);
        }
        //打印结果
        LogManager.logD(result.toString());
        return result;
    }

    private MbsResult2 OkHttpPostConn(MbsRequest2 mbsRequest2) {
        MbsResult2              mbsResult2     = new MbsResult2();
        Map<String, String> params         = mbsRequest2.getParams();
        Map<String, String> headParams     = mbsRequest2.getHeadParams();
        OkHttpClient            client         = new OkHttpClient();
        Request.Builder         requestBuilder = new Request.Builder();


        if (headParams != null) {
            Set<String> keySet = headParams.keySet();
            for (String key : keySet) {
                if (!TextUtils.isEmpty(key)) {
                    requestBuilder.addHeader(key, headParams.get(key));
                }
            }
        }
        FormBody.Builder paramsBuilder = new FormBody.Builder();
        if (params != null) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                if (!TextUtils.isEmpty(key)) {
                    paramsBuilder.add(key, params.get(key));
                }
            }
        }
        requestBuilder.url(mbsRequest2.getProtocolUrl()).post(paramsBuilder.build());
        try {
            Response response = client.newCall(requestBuilder.build()).execute();
            if (response.isSuccessful()) {
                mbsResult2.setConnExp(null);
                String      strResult   = response.body().string();
                mbsResult2.setStrContent(strResult);
                mbsResult2.setResponseCode(response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogManager.logD(e.toString());
            mbsResult2.setConnExp(e);
        }
        return mbsResult2;
    }

    private MbsResult2 OkHttpGetConn(MbsRequest2 mbsRequest2) {
        MbsResult2      mbsResult2 = new MbsResult2();
        OkHttpClient    client     = new OkHttpClient();
        Request.Builder builder    = new Request.Builder();
        builder.url(getUrlBydealParams(mbsRequest2.getProtocolUrl(), mbsRequest2.getParams()));
        builder.get();
        Map<String, String> headParams = mbsRequest2.getParams();
        if (headParams != null) {
            Set<String> keySet = headParams.keySet();
            for (String key : keySet) {
                if (!TextUtils.isEmpty(key)) {
                    builder.addHeader(key, headParams.get(key));
                }
            }
        }
        try {
            Response response = client.newCall(builder.build()).execute();
            if (response.isSuccessful()) {
                mbsResult2.setConnExp(null);
                String      strResult   = response.body().string();
                mbsResult2.setStrContent(strResult);
                mbsResult2.setResponseCode(response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogManager.logD(e.toString());
            mbsResult2.setConnExp(e);
        }
        return mbsResult2;
    }

    private String getUrlBydealParams(String protocolUrl, Map<String, String> params) {
        if (params == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        if (!protocolUrl.contains("?")) {
            sb.append("?");

        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey())
              .append("=")
              .append(TextUtils.isEmpty(entry.getValue()) ? "" : entry.getValue())
              .append("&");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        LogManager.logD("protocolUrl = " + protocolUrl + sb.toString());
        return protocolUrl + sb.toString();
    }
}
