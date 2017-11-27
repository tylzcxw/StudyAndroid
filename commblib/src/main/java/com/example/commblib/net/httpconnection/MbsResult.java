package com.example.commblib.net.httpconnection;

import com.example.commblib.config.C;
import com.example.commblib.utils.LogManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by cxw on 2017/7/18.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/18
 * @描述:  http通讯返回的结果对象,包含返回码/通讯头/异常/输入流,数据完全没有被处理,注意：如果想获取流对象，不要使用result.toString()
 */

public class MbsResult {
    /** 铜鼓县年失败时的返回码*/
    public static final int INVALID_RESPONSE_CODE = -1;
    /** 通讯返回码*/
    public int mResponseCode = INVALID_RESPONSE_CODE;
    /** 通讯返回的流*/
    private InputStream mInputStream = null;
    private InputStream mOrgInputStream;
    /** 流转换成的字符串*/
    private String mStrContent = null;
    /** 设置是否将流转换为String格式,默认false */
    private boolean isStringResult = false;
    public String getStrContent(){
        if(!isStringResult){
            throw new RuntimeException("this result::putStream should not be convert to String");
        }
        return mStrContent;
    }
    /** 返回的通讯头*/
    private Map<String,List<String>> mHeader = null;
    /** 通讯过程中出现的异常*/
    private Exception mConnExp = null;
    /** 通讯结果的编码格式*/
    private String mEncoding = null;
    /** 通讯内容的大小*/
    private int mContentLength;
    public int getContentLength(){
        return mContentLength;
    }
    public void setContentLength(int contentLength){
        this.mContentLength = contentLength;
    }
    public MbsResult(boolean isStringResult){
        this.isStringResult = isStringResult;
        mResponseCode = INVALID_RESPONSE_CODE;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(int responseCode) {
        mResponseCode = responseCode;
    }

    public InputStream getInputStream() {
        return mInputStream;
    }

    public void setInputStream(InputStream inputStream) throws Exception {
        mInputStream = inputStream;
        if(isStringResult){
            try{
                mStrContent = inputStream2String(inputStream);
            }catch (IOException e){
                LogManager.logE(e.toString());
            }
        }
    }
    public void setInputStream(InputStream inputStream,String charsetName) throws Exception {
        mInputStream = inputStream;
        if(isStringResult){
            try{
                mStrContent = inputStream2String(inputStream,charsetName);
            }catch (IOException e){
                LogManager.logE(e.toString());
            }
        }
    }
    private String inputStream2String(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = inputStream.read(buffer)) != -1){
            outputStream.write(buffer,0,len);
        }
        mOrgInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        outputStream.close();
        inputStream.close();
        String result = new String(outputStream.toByteArray(), C.Encode.DEFAULT_ENCODE);
        return result;
    }
    private String inputStream2String(InputStream inputStream,String charsetName) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = inputStream.read(buffer)) != -1){
            outputStream.write(buffer,0,len);
        }
        mOrgInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        outputStream.close();
        inputStream.close();
        String result = new String(outputStream.toByteArray(), charsetName);
        return result;
    }
    public InputStream getOrgInputStream() {
        return mOrgInputStream;
    }

    public void setOrgInputStream(InputStream orgInputStream) {
        mOrgInputStream = orgInputStream;
    }

    public void setStrContent(String strContent) {
        mStrContent = strContent;
    }

    public boolean isStringResult() {
        return isStringResult;
    }

    public void setStringResult(boolean stringResult) {
        isStringResult = stringResult;
    }

    public Map<String, List<String>> getHeader() {
        return mHeader;
    }

    public void setHeader(Map<String, List<String>> header) {
        mHeader = header;
    }

    public Exception getConnExp() {
        return mConnExp;
    }

    public void setConnExp(Exception connExp) {
        mConnExp = connExp;
    }

    public String getEncoding() {
        return mEncoding;
    }

    public void setEncoding(String encoding) {
        mEncoding = encoding;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(2 << 10);//2k
        sb.append("\n-------http response--------\n");
        //如果有异常，则直接返回
        if(mConnExp != null){
            sb.append(mConnExp.toString());
            return sb.toString();
        }
        //没有异常的情况
        sb.append("\n response = " + mResponseCode + "\n");
        //header
        sb.append("header\n");
        if(mHeader != null){
            for(String key : mHeader.keySet()){
                List<String> list = mHeader.get(key);
                String value = "";
                if(list != null){
                    int size = list.size();
                    for(int i = 0; i < size; i++){
                        value += (" " + list.get(i));
                    }
                }
                sb.append(" " + key + ":" + value + "\n");
            }
        }
        //body
        if(mStrContent != null){
            sb.append("-------------body---------\n");
                sb.append(mStrContent);

        }
        return super.toString();
    }
}
