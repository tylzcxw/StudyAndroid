package framework.exception;

/**
 * Created by cxw on 2017/7/18.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/18
 * @描述: TODO
 */

public class TransactionDataNotParseException extends TransactionException {
    public TransactionDataNotParseException() {
    }

    public TransactionDataNotParseException(String detailMessage) {
        super(detailMessage);
    }

    public TransactionDataNotParseException(String detailMessage, String code) {
        super(detailMessage, code);
    }

    public TransactionDataNotParseException(String detailMessage, Throwable throwable, String code) {
        super(detailMessage, throwable, code);
    }

    public TransactionDataNotParseException(Throwable throwable, String code) {
        super(throwable, code);
    }
}
