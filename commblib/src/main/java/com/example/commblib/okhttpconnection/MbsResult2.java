package com.example.commblib.okhttpconnection;

/**
 * Created by cxw on 2017/7/18.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/18
 * @描述:  http通讯返回的结果对象,包含返回码/通讯头/异常/输入流,数据完全没有被处理,注意：如果想获取流对象，不要使用result.toString()
 */

public class MbsResult2 {
    /** 通讯失败时的返回码*/
    public static final int INVALID_RESPONSE_CODE = -1;
    /** 通讯返回码*/
    public int mResponseCode = INVALID_RESPONSE_CODE;
    private String mStrContent = null;
    public String getStrContent(){

        return mStrContent;
    }
    /** 通讯过程中出现的异常*/
    private Exception mConnExp = null;
    public MbsResult2(){
        mResponseCode = INVALID_RESPONSE_CODE;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(int responseCode) {
        mResponseCode = responseCode;
    }



    public void setStrContent(String strContent) {
        mStrContent = strContent;
    }



    public Exception getConnExp() {
        return mConnExp;
    }

    public void setConnExp(Exception connExp) {
        mConnExp = connExp;
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
        //body
        if(mStrContent != null){
            sb.append("-------------body---------\n");
                sb.append(mStrContent);

        }
        return super.toString();
    }
}
