package com.tylz.common.config;

/**
 * Created by cxw on 2017/7/5.
 *
 * @创建者: xuanwen
 * @创建日期: 2017/7/5
 * @描述: TODO
 */

public final class C {
    /**框架级静态变量*/
    public final class F {
        /** 客户端程序版本号 */
        public static final String  MBC_VERSION                    = "1.00";
        /**
         * sp名称
         */
        public static final String  SP_NAME                        = "mmsapling";
        /**
         * 框架日志tag
         */
        public static final String  TAG                            = "mmsapling";
        /**
         * 本地交易检查
         */
        public static final boolean LOCAL_PARAMS_CHECK_SHOW_DIALOG = false;
        /**
         * 弹窗关闭时间
         */
        public static final long    DIALOG_DISSMISS_TIME_OFFSET    = 30;
        /**
         * 是否开启本地参数检查
         */
        public static final boolean LOCAL_PARAMS_CHECK             = false;

    }

    public final class Encode {
        public static final String ENCODE_GBK        = "GBK";
        public static final String ENCODE_ISO_8859_1 = "ISO_8859-1";
        public static final String ENCODE_UTF_8      = "UTF-8";
        public static final String DEFAULT_ENCODE    = ENCODE_UTF_8;
    }


    public final class Error {
        public static final String NETWORK_ERROR              = "服务或网络繁忙，请稍后再试。如有疑问，请拨打****咨询客服。";
        public static final String NETWORK_ERROR_SHUT_DOWN    = "服务或网络繁忙，请稍后再试。如有疑问，请拨打****咨询客服！";
        public static final String NETWORK_NOT_CONN           = "您的手机未连接网络，请检查网络设置。";
        public static final String NETWORK_NOT_CONN_SHUT_DOWN = "您的手机未连接网络，请检查网络设置！";
        public static final String SER_ERR                    = "后端服务异常，请稍后再试。";

        public static final String INVALID_RESPONSE = "服务响应非法！";

        public static final String SYSTEM_ERROR  = "系统错误，请联系客服！";
        public static final String UNKNOWN_ERROR = "未知错误，请联系客服！";

        public static final String FAILED_TO_LOCATE                     = "定位失败，请重试。";
        public static final String LOCAL_ERROR_CODE_PARAMS_CHECK_FAILED = "因参数%s检查不通过没有发起交易%s,请稍后再试或联系*****。";
        public static final String PARSE_ERROR                          = "解析异常";
    }
}
