package com.example.commblib.okhttpconnection;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/8/8
 * @描述: http post通讯时，使用到的request 每次通讯时，请初始化一个对象 包含通讯地址,通讯方法,参数map
 */

public class MbsRequest2{
    /** 请求方法 POST*/
    public static final String METHOD_POST = "POST";
    /** 请求方法 GET*/
    public static final String METHOD_GET = "GET";

    /** 通讯方法 get or  post  default= post*/
    protected  String mMethod = METHOD_POST;
    /** 通讯的url*/
    protected  String mProtocolUrl;
    protected  Map<String,String> mHeadParams;

    /** 上下文对象*/
    private Context mContext;
    private Map<String, String> mParams;
    public MbsRequest2(String url, String method, Context context, Map<String, String> params,Map<String,String> headParams) {
        mProtocolUrl = url;
        mMethod = method;
        mContext = context;
        mParams = params;
        mHeadParams = headParams;
    }

    /**
     * 执行通讯 获取结果
     *
     */
    public MbsResult2 http2Result(){
        return new MbsOkHttpConnection(mContext).http2Result(this);
    }



    public String getProtocolUrl() {
        return mProtocolUrl;
    }

    public void setProtocolUrl(String protocolUrl) {
        mProtocolUrl = protocolUrl;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        mMethod = method;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    public Map<String, String> getHeadParams() {
        return mHeadParams;
    }

    public void setHeadParams(HashMap<String, String> headParams) {
        mHeadParams = headParams;
    }

    public void setParams(HashMap<String, String> params) {
        mParams = params;
    }




    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(1024);
        sb.append("\n-------------request--------------");
        sb.append("\nurl=" + mProtocolUrl);
        sb.append("\nmethod=" + mMethod);
        sb.append("\nparams=" + (mParams == null ? "" : map2Str(mParams)));
        sb.append("\nheadParams=" + (mHeadParams == null ? "" : map2Str(mHeadParams)));
        return sb.toString().replace("\r\n", "\n");
    }
    private String map2Str(Map<String,String> map){
        if(map == null){
            return "";
        }
        StringBuffer sb  = new StringBuffer();
        for(String s :map.keySet()){
            sb.append(s).append("=").append(map.get(s)).append(",");
        }
        if(sb.length() > 0){
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
