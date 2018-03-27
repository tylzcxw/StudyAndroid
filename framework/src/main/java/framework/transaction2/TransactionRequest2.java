package framework.transaction2;

import android.app.Activity;
import android.os.Looper;
import android.text.TextUtils;

import com.tylz.common.config.C;
import com.tylz.common.config.HttpAddress;
import com.tylz.common.okhttpconnection.MbsResult2;
import com.tylz.common.utils.LogManager;
import com.tylz.common.utils.NetUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import framework.app.ActivityManager;
import framework.app.BaseActivity;
import framework.async2.AsyncTask2;
import framework.async2.AsyncTaskExecutor2;
import framework.async2.ResultListener2;
import framework.exception.SecurityException;
import framework.exception.TransactionException;
import framework.ui.LoadingDialog;
import framework.utils.LogUtils;

/**
 * Created by cxw on 2017/7/18.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/18
 * @描述: 交易请求基类，对请求基本功能的封装
 */

public abstract class TransactionRequest2<T extends TransactionResponse2> {
    public static final String TAG = TransactionRequest2.class.getSimpleName();
    /**
     * 交易结果对象类
     */
    protected Class<T> mResponseClass;

    public TransactionRequest2(Class<T> responseClass) {
        mResponseClass = responseClass;
    }

    private String mUrl          = HttpAddress.getHostAddress();
    public  String mProtocolName = "";
    public  String mMethod       = "GET";
    protected HashMap<String, String> mHeadParams;
    private   Map<String, String>     mParams;
    /**
     * 是否解析成javaBean，false则不解析，可从mbsresult中取原数据
     */
    private boolean isParseJavaBean = true;

    /**
     * 发送请求方法
     * @return 结果对象
     * @throws TransactionException 交易异常
     */
    protected abstract T send() throws TransactionException;

    protected boolean mShowUi = true;


    /**
     * 发送请求的方法
     * @param listener 结果监听程序
     */
    public void send(final ResultListener2<T> listener) {
        final boolean showLoading = isShowUi();
        //设置监听loading标识
        if (null != listener) {
            listener.setShowUi(showLoading);
        }
        //需要loading
        if (showLoading) {
            //处理多层交易嵌套 避免在子线程中操作UI
            final Activity activity = ActivityManager.getInstance().getTopActivity();
            LogManager.logE(String.format("=====================当前交易%s在%s页面开启loading=====================", getProtocolName(), activity.getClass().getCanonicalName()));
            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogManager.logE(String.format("=====================当前交易%s非UI线程，runOnUiThread" + "=====================", getProtocolName()));
                        LoadingDialog.getInstance().showLoading(activity);
                        doTask(listener, showLoading);
                    }
                });
            } else {
                LogManager.logE(String.format("=====================当前交易%s在UI线程，直接执行=====================", getProtocolName()));
                LoadingDialog.getInstance().showLoading(activity);
                doTask(listener, showLoading);
            }
        } else {
            doTask(listener, showLoading);
        }
    }

    private void doTask(ResultListener2<T> listener, boolean showLoading) {
        //任务标志loading
        AsyncTask2 currentTask = new AsyncTask2(listener, showLoading) {
            @Override
            protected Object doInBackground() throws TransactionException {
                //不使用缓存直接请求
                TransactionResponse2 response = send();
                return response;
            }
        };
        if (!showLoading) {
            AsyncTaskExecutor2.getInstance().executeLowPriorityTask(currentTask);
        } else {
            AsyncTaskExecutor2.getInstance().execute(currentTask);
        }
        RequestController2.getInstance().addRequestAndTask(this, currentTask);
    }


    /**
     * 处理网络通讯结果
     * @param result 通讯结果
     * @return 字符串结果
     * @throws TransactionException 处理异常
     */
    protected String processResult(MbsResult2 result) throws TransactionException {
        if (result == null) {
            LogUtils.error(TAG, "Fail to request " + getProtocolUrl() + " MbsResult is null");
            throw new TransactionException("请求结果为空");
        }
        if (result.getConnExp() != null) {
            LogUtils.error(TAG, "Fail to request " + getProtocolUrl(), result.getConnExp());
            BaseActivity topActivity = ActivityManager.getInstance().getTopActivity();
            String errMsg = NetUtils.isMobile(topActivity) || NetUtils.isWIFI(topActivity) ? C.Error.NETWORK_ERROR : C.Error.NETWORK_NOT_CONN;
            throw new TransactionException(errMsg);
        }
        int responseCode = result.getResponseCode();
        // todo 返回的错误码 可自定义
        if (responseCode == 701 || responseCode == 702) { //自定义的
            LogUtils.error(TAG, "Fail to request " + getProtocolUrl() + " Response code [" + responseCode + "]");
            throw new SecurityException("请重新登陆", null, String.valueOf(responseCode));
        }
        if (responseCode != 200) {
            LogUtils.error(TAG, "Fail to request " + getProtocolUrl() + " Response code [" + responseCode + "]");
            throw new TransactionException(C.Error.SER_ERR);
        }
        String content = result.getStrContent();
        if (TextUtils.isEmpty(content)) {
            LogUtils.debug(TAG, "Request result is empty");
        } else {
            LogUtils.debug(TAG, "Request result[" + content.replaceAll("\r", "") + "]");
        }
        return content;
    }

    public enum Method {
        GET("GET"),
        POST("POST");
        String mMethod;

        Method(String method) {
            mMethod = method;
        }

        public String getMethod() {
            return mMethod;
        }

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface Parameter {
        /**
         * 参数名称
         * @return
         */
        String name() default "";
    }

    private Map<String,String> parameter2Map() {
        Map<String, String> paramsMap = new HashMap<>();
        Field[]             fields = this.getClass().getFields();
        if (fields != null && fields.length > 0) {

            for (Field field : fields) {
                field.setAccessible(true);
                Parameter parameter = field.getAnnotation(Parameter.class);
                if (parameter == null) {
                    continue;
                }
                try {
                    String name = parameter.name();
                    if (TextUtils.isEmpty(name)) {
                        name = field.getName();
                    }
                    Object object = field.get(this);
                    String value = null;
                    if(object != null){
                        if(object instanceof String){
                            value = (String) object;
                        }else{
                            value = object.toString();
                        }
                    }
                   paramsMap.put(name, value);
                } catch (Exception e) {
                    LogManager.logD("Failed to get parameter [" + field.getName() + "]"+ e);
                    e.printStackTrace();
                }
            }

        }
        return paramsMap;
    }
    public HashMap<String, String> getHeadParams() {
        return mHeadParams;
    }

    public void setHeadParams(HashMap<String, String> headParams) {
        mHeadParams = headParams;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(Method method) {

        mMethod = method.getMethod();
    }

    public boolean isShowUi() {
        return mShowUi;
    }

    public void setShowUi(boolean showUi) {
        mShowUi = showUi;
    }

    public String getProtocolUrl() {
        return mUrl + mProtocolName;
    }

    public String getProtocolName() {
        return mProtocolName;
    }

    public void setProtocolName(String protocolName) {
        mProtocolName = protocolName;
    }

    public Map<String, String> getParams() {
        Map<String, String> parameterMap = parameter2Map();
        if(mParams == null){
           return parameterMap;
        }else{ //将外部设置的值赋值给内部的,优先使用外部的
            for(Map.Entry<String,String> entry: mParams.entrySet()){
                String key = entry.getKey();
                if(parameterMap.containsKey(key)){
                    parameterMap.put(key,mParams.get(key));
                }
            }

        }
        LogManager.logD(parameterMap.toString());
        return parameterMap;
    }

    public void setParams(Map<String, String> params) {
        mParams = params;
    }

    public boolean isParseJavaBean() {
        return isParseJavaBean;
    }

    public void setParseJavaBean(boolean parseJavaBean) {
        isParseJavaBean = parseJavaBean;
    }

}
