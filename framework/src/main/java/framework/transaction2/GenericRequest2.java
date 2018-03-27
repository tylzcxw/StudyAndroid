package framework.transaction2;


import com.tylz.common.okhttpconnection.MbsRequest2;
import com.tylz.common.okhttpconnection.MbsResult2;

import framework.app.ActivityManager;
import framework.app.BaseActivity;
import framework.async2.ResultListener2;
import framework.exception.TransactionException;

/*
 *  @创建者:   tylz
 *  @创建时间:  2017/9/25
 *  @描述：    TODO
 */
public class GenericRequest2<T extends TransactionResponse2> extends TransactionRequest2<T> {
    public ResultListener2 mResultListener2;
    public GenericRequest2(Class<T> responseClass) {
        super(responseClass);
    }

    @Override
    public void send(ResultListener2<T> listener) {
        super.send(listener);
        mResultListener2 = listener;
    }

    @Override
    protected T send() throws TransactionException {
        resetValue();
        BaseActivity topActivity = ActivityManager.getInstance().getTopActivity();
        MbsRequest2 mbsRequest2 = new MbsRequest2(getProtocolUrl(), getMethod(), topActivity, getParams(), getHeadParams());
        MbsResult2 mbsResult2 = mbsRequest2.http2Result();
        T t = null;
        try {
            t = mResponseClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t.parseResult(mbsResult2,this,mProtocolName,mResultListener2);
    }

    protected void resetValue() {

    }
}
