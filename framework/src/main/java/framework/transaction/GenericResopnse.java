package framework.transaction;

import android.text.TextUtils;

import com.example.commblib.net.httpconnection.MbsResult;

import framework.exception.SecurityException;
import framework.exception.TransactionException;
import framework.security.LoginUtils;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/22
 *  @描述：    TODO
 */
public class GenericResopnse extends BaseTransactionResponse {
    @Override
    public <T> T parseNormal(MbsResult mbsResult) throws TransactionException {
        int responseCode = mbsResult.getResponseCode();
        //登录超时 自定义状态码
        if(responseCode == 701 || responseCode == 702){
            //清除登录保存的参数
            LoginUtils.logout();
            if (isUiThreadListener) {
                ((RunUiThreadResultListener) mResultListener).handleLoginTimeoutException(mRequest);
                return null;
            } else {
                throw new SecurityException("请重新登陆", null, String.valueOf(responseCode));
            }
        }
        String result = mbsResult.getStrContent();
        try{
            return parseResult(result,mbsResult.getInputStream());
        }catch (Throwable e1){
            if(isUiThreadListener){
                ((RunUiThreadResultListener)mResultListener).handleParseErrorException();
                return  null;
            }
            throw new TransactionException(String.format("交易%s无法创建Response对象", TextUtils.isEmpty(mTxCode) ? "" : mTxCode));
        }
    }
}
