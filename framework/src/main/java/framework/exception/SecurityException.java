package framework.exception;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/7/31
 * @描述: TODO
 */

public class SecurityException extends TransactionException {
    public SecurityException(String detailMessage, Throwable throwable, String code) {
        super(detailMessage, throwable, code);
    }
}
