package framework.exception;

/**
 * Created by cxw on 2017/7/18.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/18
 * @描述: TODO
 */

public class TransactionException extends Exception {
    private String mCode = "";
    public TransactionException() {
        super();
    }

    public TransactionException(String detailMessage) {
        super(detailMessage);
    }

    public TransactionException(String detailMessage, String code) {
        super(detailMessage);
        mCode = code;
    }

    public TransactionException(String detailMessage, Throwable throwable, String code) {
        super(detailMessage, throwable);
        mCode = code;
    }

    public TransactionException(Throwable throwable, String code) {
        super(throwable);
        mCode = code;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }
}
