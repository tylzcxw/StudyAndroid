package framework.exception;

/**
 * Created by cxw on 2017/7/18.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/18
 * @描述: TODO
 */

public class TransactionRepeatSendException extends TransactionException {
    public TransactionRepeatSendException() {
    }

    public TransactionRepeatSendException(String detailMessage) {
        super(detailMessage);
    }

    public TransactionRepeatSendException(String detailMessage, String code) {
        super(detailMessage, code);
    }

    public TransactionRepeatSendException(String detailMessage, Throwable throwable, String code) {
        super(detailMessage, throwable, code);
    }

    public TransactionRepeatSendException(Throwable throwable, String code) {
        super(throwable, code);
    }
}
