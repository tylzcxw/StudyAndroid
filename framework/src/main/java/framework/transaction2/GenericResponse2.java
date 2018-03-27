package framework.transaction2;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.tylz.common.config.C;
import com.tylz.common.okhttpconnection.MbsResult2;
import com.tylz.common.utils.LogManager;

import framework.async2.ResultListener2;
import framework.exception.TransactionException;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/22
 *  @描述：    TODO
 */
public class GenericResponse2 extends TransactionResponse2 {
    public  ResultListener2     mResultListener2;
    public  TransactionRequest2 mRequest2;
    private MbsResult2 mResult2;

    @Override
    public <T> T parseResult(MbsResult2 mbsResult,
                             TransactionRequest2 request,
                             String txCode,
                             ResultListener2 resultListener) throws TransactionException {
        mRequest2 = request;
        mResult2 = mbsResult;
        mResultListener2 = resultListener;

        int responseCode = mbsResult.getResponseCode();
        if (responseCode == 200) {
            //正常业务逻辑
            LogManager.logD("================正常业务逻辑 code=" + responseCode + "=================");
            T t = parseNormal(mbsResult);
            return t;
        }
        //超时处理
        if (mbsResult.getConnExp() != null) {
            String                     errorMsg  = NetworkUtils.isWifiAvailable() ? C.Error
                    .NETWORK_ERROR : C.Error.NETWORK_NOT_CONN;
            TransactionException       exception = new TransactionException(errorMsg);
            RunUiThreadResultListener2 listener2 = (RunUiThreadResultListener2) resultListener;
            LogManager.logD("================超时处理 交给RunUiThreadResultListener处理===============");
            listener2.handleInnerConnException(exception);
            return null;


        } else {
            //默认错误处理
            LogManager.logD("====================code：" + responseCode +
                                    "交给RunUiThreadResultListener处理=========================");
            ((RunUiThreadResultListener2) resultListener).handleInnerDefaultException(new TransactionException(C.Error.SER_ERR));
            return null;

        }
    }

    private <T> T parseNormal(MbsResult2 mbsResult) throws TransactionException {
        String result = mbsResult.getStrContent();
        LogManager.logD(mbsResult.toString());
        String protocolUrl = mRequest2.getProtocolUrl();
        T      t           = null;
        try {
            if (mRequest2.isParseJavaBean()) {
                t = parseResult(result);
            }else{
                ((RunUiThreadResultListener2) mResultListener2).handleInnerDataNotParseError(result);
            }
            return t;
        }catch (Exception e1) {
            ((RunUiThreadResultListener2) mResultListener2).handleInnerParseErrorException();
            return null;
        }
    }

    public MbsResult2 getResult2() {
        return mResult2;
    }

    @Override
    public String toString() {
        return EmptyUtils.isEmpty(mResult2) ? "数据转化失败！" : mResult2.getStrContent();
    }
}
