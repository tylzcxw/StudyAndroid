package com.example.commblib.net.httpconnection;

import android.content.Context;

import java.io.File;
import java.util.HashMap;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/8/8
 * @描述: http post通讯时，使用到的request 每次通讯时，请初始化一个对象 包含通讯地址,通讯方法,参数map
 */

public class MbsRequest implements Cloneable{
    /** 通讯超时设定*/
    public static final int TIMEOUT_CONNECTION = 45*1000;
    /** 通讯超时设定*/
    public static final int TIMEOUT_READ = 45 * 1000;
    /** 参数上送时的编码格式*/
    public static final String ENCODING_UTF8 = "UTF-8";
    /** 请求方法 POST*/
    public static final String METHOD_POST = "POST";
    /** 请求方法 GET*/
    public static final String METHOD_GET = "GET";
    /** socket time out 如果不设置，则采用默认参数*/
    public int mSocketTimeOut = TIMEOUT_CONNECTION;
    /** 如果不设置，则采用默认参数*/
    public int mReadTimeOut = TIMEOUT_READ;
    /** 通讯方法 get or  post  default= post*/
    protected  String mMethod = METHOD_POST;
    /** 通讯的url*/
    protected  String mUrl;
    /** 通讯请求的参数*/
    protected HashMap<String,String> mParams;
    /** 文件参数*/
    protected HashMap<String,File> mFileParams;
    /** 请求报文头 requestProperty*/
    protected  HashMap<String,String> mHeader;
    /** 设置是否将流转换为String 格式，默认false*/
    private boolean isStringResult = false;
    /** 上下文对象*/
    private Context mContext;
    /** 编码格式*/
    private String mCharsetName;

    public MbsRequest(boolean isStringResult, Context context) {
        this.isStringResult = isStringResult;
        mContext = context;
    }

    public MbsRequest(String url, String method, Context context, HashMap<String, String> params) {
        mUrl = url;
        mMethod = method;
        mContext = context;
        mParams = params;
    }

    public MbsRequest(String url, String method, HashMap<String, String> params, Context context, String charsetName) {
        mUrl = url;
        mMethod = method;
        mParams = params;
        mContext = context;
        mCharsetName = charsetName;
    }
    /**
     * 执行通讯 获取结果,如果要关闭https证书验证,请在此方法前调用 {@link MbsNewHttpConnection}
     * 的方法:enableAllHttps() ;
     *
     */
    public MbsResult http4Result(){
        return new MbsNewHttpConnection(mContext).http4Result(this);
    }
    @Override
    protected MbsRequest clone() {
        MbsRequest request;
        /** 通讯请求参数*/
        if(mParams == null){
            mParams = new HashMap<>();
        }
        if(mHeader == null){
            mHeader = new HashMap<>();
        }
        HashMap<String,String> params = (HashMap<String, String>) mParams.clone();
        HashMap<String,String> header = (HashMap<String, String>) mHeader.clone();
        if(mContext != null){
            request = new MbsRequest(isStringResult,mContext);
            request.setHeader(header);
            request.setMethod(mMethod);
            request.setParams(params);
            request.setUrl(mUrl);
        }else{
            request = new MbsRequest(mUrl,mMethod,mContext,params);
            request.setHeader(header);
        }
        return request;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(1024);
        sb.append("\n-------------request--------------");
        sb.append("\nurl=" + mUrl);
        sb.append("\nmethod=" + mMethod);
        sb.append("\nhead=" + (mHeader == null ? "" : mHeader));
        sb.append("\nparams=" + (mParams == null ? "" : mParams));
        return sb.toString().replace("\r\n", "\n");
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        mMethod = method;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public HashMap<String, String> getParams() {
        return mParams;
    }

    public void setParams(HashMap<String, String> params) {
        mParams = params;
    }

    public HashMap<String, File> getFileParams() {
        return mFileParams;
    }

    public void setFileParams(HashMap<String, File> fileParams) {
        mFileParams = fileParams;
    }

    public HashMap<String, String> getHeader() {
        return mHeader;
    }

    public void setHeader(HashMap<String, String> header) {
        mHeader = header;
    }

    public boolean isStringResult() {
        return isStringResult;
    }

    public void setStringResult(boolean stringResult) {
        isStringResult = stringResult;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public String getCharsetName() {
        return mCharsetName;
    }

    public void setCharsetName(String charsetName) {
        mCharsetName = charsetName;
    }
}
