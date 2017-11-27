package framework.transaction;

import android.text.TextUtils;

import com.example.commblib.config.HttpAddress;
import com.example.commblib.net.httpconnection.MbsRequest;
import com.example.commblib.net.httpconnection.MbsResult;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import framework.async.ResultListener;
import framework.exception.TransactionException;
import framework.utils.ContextUtils;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/8/8
 * @描述: 普通请求，直接拼url
 */

public class GenericRequest <T extends BaseTransactionResponse> extends TransactionRequest<T> {
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
    protected boolean isUiThreadListener = false;
    private ResultListener mResultListener;
    protected String TXCODE;
    /**
     * 错误码
     */
    private String mErrCode;
    private String mJsonData;
    /**
     * 是否使用缓存数据
     */
    private boolean isCache = false;
    public HashMap<String,String> mRequestParams = new HashMap<>();
    public GenericRequest(Class<T> responseClass) {
        super(responseClass);
        /**         * 请求地址         **/
        setUrl(String.format("%s/cmccb/servlet/ccbNewClient", HttpAddress.getHostAddress()));
    }

    /**
     * 需要登录后取值的参数放入这里赋值
     */
    protected  void resetParam(){}
    @Override
    protected T send() throws TransactionException {
        resetParam();
        isUiThreadListener = mResultListener instanceof RunUiThreadResultListener;
        if(isCache()){
            //有缓存
        }
        MbsRequest request = new MbsRequest(true, ContextUtils.getCcbContext().getApplicationContext());
        if(mRequestTimeOutOffset != 0){
            request.mSocketTimeOut = mRequestTimeOutOffset;
        }
        request.setUrl(getUrl());
        HashMap<String,String> headers = new HashMap<>();
        headers.put("User-Agent",USER_AGENT);
        headers.putAll(getHeaders());
        request.setHeader(headers);
        HashMap<String, String> params = toMap();
        if(mRequestParams != null && !mRequestParams.isEmpty()){
            params.putAll(mRequestParams);
        }
        request.setParams(params);
        request.setMethod(getMethod());
        if(!TextUtils.isEmpty(getCharsetName())){
            request.setCharsetName(getCharsetName());
        }
        MbsResult mbsResult = request.http4Result();
        try{
            return mResponseClass.newInstance().parseResult(mbsResult,this,TXCODE,mResultListener);
        }catch (Exception e){
            throw new TransactionException(e.getMessage());
        }
    }

    @Override
    public void send(ResultListener<T> listener) {
        super.send(listener);
        this.mResultListener = listener;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }
    public Map<String, String> getParam(){
        Map<String, String> map = new HashMap<String, String>();
        Class<?> clazz = getClass();
        Field[] field = clazz.getDeclaredFields();

        for (Field f : field) {
            String key = f.getName();
            String value = "";
            try {
                Field field2 = clazz.getDeclaredField(key);
                value = (String) field2.get(this);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            map.put(key, value);
        }
        return map;
    }
}
