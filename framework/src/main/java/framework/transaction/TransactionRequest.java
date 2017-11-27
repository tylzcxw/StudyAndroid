package framework.transaction;

import android.app.Activity;
import android.os.Looper;
import android.text.TextUtils;

import com.example.commblib.config.C;
import com.example.commblib.net.httpconnection.MbsResult;
import com.example.commblib.utils.NetUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import framework.app.ActivityManager;
import framework.app.BaseActivity;
import framework.async.AsyncTask;
import framework.async.AsyncTaskExecutor;
import framework.async.ResultListener;
import framework.exception.SecurityException;
import framework.exception.TransactionException;
import framework.exception.TransactionParamsCheckException;
import framework.ui.LoadingDialog;
import framework.utils.LogManager;
import framework.utils.LogUtils;

import static framework.transaction.TransactionRequest.ParameterType.NULL;

/**
 * Created by cxw on 2017/7/18.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/18
 * @描述: 交易请求基类，对请求基本功能的封装
 */

public abstract class TransactionRequest<T extends TransactionResponse> {
    public static final String TAG = TransactionRequest.class.getSimpleName();
    /**
     * 交易结果对象类
     */
    protected Class<T> mResponseClass;
    public TransactionRequest(Class<T> responseClass){
        mResponseClass = responseClass;
    }

    /**
     * 请求参数注释，标志该属性为请求参数
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface Parameter{
        /**
         * 参数名称
         * @return
         */
        String name() default "";

        /**
         * 是否为必须参数，即如果是空值也要上传
         * @return
         */
        boolean required() default true;

        /**
         * 参数的字符编码
         * @return
         */
        String charset() default "";
    }

    /**
     * 是否使用请求缓存标志
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface RequestCache{

    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ParameterChecker{
        /**
         * 是否允许空值
         * @return true不能为空，false可为空
         */
        boolean notNull() default true;

        /**
         * 长度
         * @return -1 为任意长度
         */
        int length() default -1;

        /**
         * 类型{@link ParameterType}
         * @return 默认NULL 不做转换检查
         */
        ParameterType type() default NULL;

        /**
         * 正则表达式
         * @return 默认为空，不做正则匹配
         */
        String regex() default "";

    }
    public enum ParameterType{
        INT,FLOAT,LONG,BOOLEAN,DOUBLE,BYTE,NULL
    }

    /**
     * 通讯url
     */
    private String mUrl;
    /**
     * 请求方式，get或post
     */
    private String mMethod = "GET";
    /**
     * 请求头
     */
    private HashMap<String,String> mHeaders = new HashMap<>();
    private boolean mLowPriorityTask = false;

    /**
     * 发送请求方法
     * @return 结果对象
     * @throws TransactionException 交易异常
     */
    protected abstract T send() throws TransactionException;
    private boolean mCanShutDown = true;
    protected boolean mUseRequestCache = false;
    protected  boolean mShowUi = true;
    protected boolean mCanRepeatSendRequest = false;
    protected long mTimeInterval = 1000; //时间间隔
    private  long mSendTime;
    protected boolean isVerifyGraphCode;
    /**
     * 是否显示倒计时
     */
    private String mShowCountDown;
    /**
     * 延迟显示倒计时时间
     */
    private String mDelayShowCountDown;
    /**
     * laoding关闭时间
     */
    private String mLoadingDismissTime;
    /**
     * 交易超时时间
     */
    private String mRequestTimeout;
    protected int mRequestTimeOutOffset = 0;

    /**
     * 发送请求的方法
     * @param listener 结果监听程序
     */
    public void send(final ResultListener<T> listener){
        //记录交易发送时间
        mSendTime = System.currentTimeMillis();
        final boolean showLoading = isShowUi();
        // todo 倒计时 交易超时时间 loading关闭时间等判断
        List<String> checkList = parameterCheck();
        if(C.F.LOCAL_PARAMS_CHECK && null != checkList && !checkList.isEmpty()){
            StringBuffer sb = new StringBuffer();
            for(String s : checkList){
                sb.append(s + "、");
            }
            if(sb.toString().endsWith("、")){
                sb.deleteCharAt(sb.length() -1);
            }
            listener.onRefused(new TransactionParamsCheckException(String.format(C.Error.LOCAL_ERROR_CODE_PARAMS_CHECK_FAILED, sb.toString())));
            return;
        }
        // todo 不能重复发送交易判定 前一支相同交易不为空 + 当前交易不能重复发送 + 前一支交易时间与当前交易时间间隔小于设定的重复交易时间间隔

        //设置监听loading标识
        if(null != listener){
            listener.setShowUi(showLoading);
        }
        //需要loading
        if(showLoading){
            //处理多层交易嵌套 避免在子线程中操作UI
            final Activity activity = ActivityManager.getInstance().getTopActivity();
            LogManager.logE(String.format("=====================当前交易%s在%s页面开启loading=====================","**",activity.getClass().getName()));
            if(Thread.currentThread() != Looper.getMainLooper().getThread()){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogManager.logE(String.format("=====================当前交易%s非UI线程，runOnUiThread=====================", "****"));
                        LoadingDialog.getInstance().showLoading(activity);
                        doTask(listener,showLoading);
                    }
                });
            }else{
                LogManager.logE(String.format("=====================当前交易%s在UI线程，直接执行=====================", "****"));
                LoadingDialog.getInstance().showLoading(activity);
                doTask(listener,showLoading);
            }
        }else{
            doTask(listener,showLoading);
        }
    }

    private void doTask(ResultListener<T> listener, boolean showLoading) {
        //任务标志loading
        AsyncTask currentTask = new AsyncTask(listener,showLoading) {
            @Override
            protected Object doInBackground() throws TransactionException {
                //不使用缓存直接请求
                if(!isUseRequestCache()){
                    TransactionResponse response = send();
                    return response;
                }

                return null;
            }
        };
        if(!showLoading){
            AsyncTaskExecutor.getInstance().executeLowPriorityTask(currentTask);
        }else{
            AsyncTaskExecutor.getInstance().execute(currentTask);
        }
        RequestController.getInstance().addRequestAndTask(this,currentTask);
    }

    /**
     * 取消当前请求
     */
    public void cancel(){

    }
    public String getCharsetName(){
        return "";
    }
    /**
     * 将请求参数转换成参数映射表
     * @return
     */
    public HashMap<String,String> toMap(){
        HashMap<String,String> parameters = new HashMap<>();
        Field[] fields = getClass().getFields();
        if(fields != null && fields.length > 0){
            for(Field f : fields){
                Parameter p = f.getAnnotation(Parameter.class);
                if(p == null){
                    continue;
                }
                try{
                    f.setAccessible(true);
                    String name = p.name();
                    if(TextUtils.isEmpty(name)){
                        name = f.getName();
                    }
                    Object value = f.get(this);
                    if(value != null){
                        String s = null;
                        if(value instanceof String){
                            s = (String)value;
                        }else{
                            s = value.toString();
                        }
                        String charset = p.charset();
                        if(charset != null && charset.trim().length() > 0){
                            s = URLEncoder.encode(s,charset);
                        }
                        parameters.put(name,s);
                    }else{
                        if(p.required()){
                            parameters.put(name,"");
                        }
                    }
                }catch (Exception e){
                    LogUtils.warn(TAG, "Failed to get parameter [" + f.getName() + "]", e);
                }
            }
        }
        LogUtils.debug(TAG,parameters.toString());
        return null;
    }

    private List<String> parameterCheck(){
        List<String> checkFailList = Collections.synchronizedList(new LinkedList<String>());
        Field[] allFields = getClass().getFields();
        for(Field field : allFields){
            field.setAccessible(true);
            String name = field.getName();
            try{
                Object value = field.get(this);
                boolean valueIsEffective = null != value && !TextUtils.isEmpty(value.toString());
                //无参数注解直接跳过
                if(!field.isAnnotationPresent(Parameter.class)){
                    continue;
                }
                //如果无参数检查注解 只判断空值检查
                if(!field.isAnnotationPresent(ParameterChecker.class)){
                    //不为空跳过
                    if(valueIsEffective){
                        continue;
                    }
                    //加入检查不通过列表
                    checkFailList.add(name);
                }else{//根据检查规则检查
                    ParameterChecker checker = field.getAnnotation(ParameterChecker.class);
                    //值为空 加入检查不通过列表
                    if(checker.notNull() && !valueIsEffective){
                        checkFailList.add(name);
                        continue;
                    }
                    int length = checker.length();
                    //长度不符合 加入检查不通过列表
                    if(-1 != length && value.toString().length() != length){
                        checkFailList.add(name);
                        continue;
                    }
                    //类型不符合，转换异常 加入检查不通过列表
                    ParameterType type = checker.type();
                    if(NULL != type && !parseType(type,value)){
                        checkFailList.add(name);
                        continue;
                    }
                    //正则表达式匹配不通过 加入检查不通过列表
                    String regex = checker.regex();
                    if(!TextUtils.isEmpty(regex)){
                        Pattern pattern = Pattern.compile(regex);
                        if(!pattern.matcher(value.toString()).matches()){
                            checkFailList.add(name);
                            continue;
                        }
                    }
                }

            }catch (Exception e){
                LogManager.logD("==================check failed===================");
            }
        }
        return checkFailList;
    }
    private boolean parseType(ParameterType type,Object value){
        boolean parseResult = true;
        try{
            switch (type){
                case INT:
                    Integer.parseInt(value.toString());
                    break;
                case FLOAT:
                    Float.parseFloat(value.toString());
                    break;
                case LONG:
                    Long.parseLong(value.toString());
                    break;
                case BOOLEAN:
                    Boolean.parseBoolean(value.toString());
                    break;
                case DOUBLE:
                    Double.parseDouble(value.toString());
                    break;
                case BYTE:
                    Byte.parseByte(value.toString());
                    break;
            }
        }catch (Exception e){
            parseResult = false;
        }
        return parseResult;
    }
    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        mMethod = method;
    }

    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    public void setHeaders(HashMap<String, String> headers) {
        mHeaders = headers;
    }

    public boolean isLowPriorityTask() {
        return mLowPriorityTask;
    }

    public void setLowPriorityTask(boolean lowPriorityTask) {
        mLowPriorityTask = lowPriorityTask;
    }

    public boolean isCanShutDown() {
        return mCanShutDown;
    }

    public void setCanShutDown(boolean canShutDown) {
        mCanShutDown = canShutDown;
    }

    public boolean isUseRequestCache() {
        return mUseRequestCache;
    }

    public void setUseRequestCache(boolean useRequestCache) {
        mUseRequestCache = useRequestCache;
    }

    public boolean isShowUi() {
        return mShowUi;
    }

    public void setShowUi(boolean showUi) {
        mShowUi = showUi;
    }

    public boolean isCanRepeatSendRequest() {
        return mCanRepeatSendRequest;
    }

    public void setCanRepeatSendRequest(boolean canRepeatSendRequest) {
        mCanRepeatSendRequest = canRepeatSendRequest;
    }

    public long getTimeInterval() {
        return mTimeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        mTimeInterval = timeInterval;
    }

    public long getSendTime() {
        return mSendTime;
    }

    public void setSendTime(long sendTime) {
        mSendTime = sendTime;
    }

    public boolean isVerifyGraphCode() {
        return isVerifyGraphCode;
    }

    public void setVerifyGraphCode(boolean verifyGraphCode) {
        isVerifyGraphCode = verifyGraphCode;
    }

    public String getShowCountDown() {
        return mShowCountDown;
    }

    public void setShowCountDown(String showCountDown) {
        mShowCountDown = showCountDown;
    }

    public String getDelayShowCountDown() {
        return mDelayShowCountDown;
    }

    public void setDelayShowCountDown(String delayShowCountDown) {
        mDelayShowCountDown = delayShowCountDown;
    }

    public String getLoadingDismissTime() {
        return mLoadingDismissTime;
    }

    public void setLoadingDismissTime(String loadingDismissTime) {
        mLoadingDismissTime = loadingDismissTime;
    }

    public String getRequestTimeout() {
        return mRequestTimeout;
    }

    public void setRequestTimeout(String requestTimeout) {
        mRequestTimeout = requestTimeout;
    }

    public int getRequestTimeOutOffset() {
        return mRequestTimeOutOffset;
    }

    public void setRequestTimeOutOffset(int requestTimeOutOffset) {
        mRequestTimeOutOffset = requestTimeOutOffset;
    }

    /**
     * 处理网络通讯结果
     * @param result 通讯结果
     * @return  字符串结果
     * @throws TransactionException 处理异常
     */
    protected String processResult(MbsResult result) throws TransactionException{
        if(result == null){
            LogUtils.error(TAG,"Fail to request " + getUrl() + " MbsResult is null");
            throw new TransactionException("请求结果为空");
        }
        if(result.getConnExp() != null){
            LogUtils.error(TAG, "Fail to request " + getUrl(), result.getConnExp());
            BaseActivity topActivity = ActivityManager.getInstance().getTopActivity();
            String errMsg = NetUtils.isMobile(topActivity) || NetUtils.isWIFI(topActivity) ? C.Error.NETWORK_ERROR : C.Error.NETWORK_NOT_CONN;
            throw new TransactionException(errMsg);
        }
        int responseCode = result.getResponseCode();
        // todo 返回的错误码 可自定义
        if(responseCode == 701 || responseCode == 702){ //自定义的
            LogUtils.error(TAG,"Fail to request " + getUrl() + " Response code [" + responseCode + "]");
            throw new SecurityException("请重新登陆", null, String.valueOf(responseCode));
        }
        if(responseCode != 200){
            LogUtils.error(TAG,"Fail to request " + getUrl() + " Response code [" + responseCode + "]");
            throw  new TransactionException(C.Error.SER_ERR);
        }
        String content = result.getStrContent();
        if(TextUtils.isEmpty(content)){
            LogUtils.debug(TAG, "Request result is empty");
        }else{
            LogUtils.debug(TAG, "Request result[" + content.replaceAll("\r", "") + "]");
        }
        return content;
    }
}
