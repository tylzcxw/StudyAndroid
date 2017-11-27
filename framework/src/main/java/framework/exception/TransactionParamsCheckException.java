package framework.exception;

/**
 * Created by cxw on 2017/7/18.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/18
 * @描述: TODO
 */

public class TransactionParamsCheckException extends TransactionException {
    public TransactionParamsCheckException() {
    }

    public TransactionParamsCheckException(String detailMessage) {
        super(detailMessage);
    }

    public TransactionParamsCheckException(String detailMessage, String code) {
        super(detailMessage, code);
    }

    public TransactionParamsCheckException(String detailMessage, Throwable throwable, String code) {
        super(detailMessage, throwable, code);
    }

    public TransactionParamsCheckException(Throwable throwable, String code) {
        super(throwable, code);
    }
}
