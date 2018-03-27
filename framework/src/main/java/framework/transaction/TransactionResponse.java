package framework.transaction;

import com.google.gson.Gson;
import com.tylz.common.httpconnection.MbsResult;
import com.tylz.common.utils.LogManager;

import java.io.InputStream;

import framework.async.ResultListener;
import framework.exception.TransactionException;

/**
 * Created by cxw on 2017/7/18.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/18
 * @描述: 交易响应对象基础类，该类极其子类不能被混淆
 */

public class TransactionResponse {
    /**
     * 解析响应结果
     *
     * 当框架无法对响应报文进行正常解析时，可在此重写该方法进行返回报文体解析
     * 如果仅仅是不同业务功能对返回值的处理差异，则不应在此进行特别处理，而应在具体业务模块中实现
     *
     * @param result 请求返回报文
     * @param <T> 目标类型
     * @return 响应对象
     * @throws TransactionException 交易异常
     */
    public <T>  T parseResult(String result) throws TransactionException{
        Gson gson = new Gson();
        return (T) gson.fromJson(result,getClass());
    }
    /**
     * 解析响应结果
     *
     * 当框架无法对响应报文进行正常解析时，可在此重写该方法进行返回报文体解析
     * 如果仅仅是不同业务功能对返回值的处理差异，则不应在此进行特别处理，而应在具体业务模块中实现
     *
     * @param result 请求返回报文
     * @param <T> 目标类型
     * @param  orgStream 原输入流
     * @return 响应对象
     * @throws TransactionException 交易异常
     */
    public <T> T parseResult(String result, InputStream orgStream) throws TransactionException{
        try{
            orgStream.close();
        }catch (Exception e){
            LogManager.logE(e.toString());
        }
        return parseResult(result);
    }
    public <T> T parseResult(MbsResult mbsResult, TransactionRequest request, String txCode, ResultListener resultListener) throws TransactionException{
       return null;
    }
    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this).toString();
    }
}
