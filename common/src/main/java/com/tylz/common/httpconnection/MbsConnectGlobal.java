package com.tylz.common.httpconnection;

import android.net.Uri;
/**
 * 描述:通讯常量类
 *
 */

public class MbsConnectGlobal {

    public static final int REQUEST_COUNT = 3; // http请求时的请求次数
    public static final String SIM_OPERATOR_CM_1 = "46000";// 移动运营商代号1
    public static final String SIM_OPERATOR_CM_2 = "46002";// 移动运营商代号2
    public static final String SIM_OPERATOR_CU = "46001";// 联通运营商代号
    public static final String SIM_OPERATOR_CT = "46003";// 电信运营商代号

    public static final String CM_PROXY_HOST_KEY = "http.proxyHost";// 移动代理主机关键字
    public static final String CM_PROXY_POST_KEY = "http.proxyPort";// 移动代理端口关键字
    public static final String TLS = "TLS";// 测试环境自定义的通讯证书时的协议

    // 超时机制的确定
    public static final int LOGIN_GET_USERID_TIMEOUT = 45 * 1000;
    public static final int IN_BTW_TIMEOUT = 45 * 1000;
    public static final int OUT_BTW_TIMEOUT = 45 * 1000;
    public static final int MOBILE_PAY_BTW_TIMEOUT = 60 * 1000;

    public static final String CONNECT_METHOD_POST = "POST";// post请求方式
    public static final String CONNECT_METHOD_GET = "GET";// get请求方式

    // ==通讯http头的数据
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";
    // 自定http头的数据
    public static final String USER_AGENT = "MBC-User-Agent";// 每次发送请求时在请求头中设置的参数的key
    public static final String USER_TOKEN = "UserToken";// 每次发送请求时在请求头中设置的参数的key
    public static final String DN = "DN";// 每次发送请求时在请求头中设置的参数的key
    public static final String USER_INFO = "MBC-User-Info";// 每次发送请求时在请求头中设置的用户信息

    public static final String TXCODE = "TXCODE";// 交易码数据字段
    public static final String DEFAULT_ENCORD = "UTF-8";// 默认通讯编码方式
    public static final String ENCORD_GBK = "GBK";//GBK编码
    public static final String ENCORD_ISO_8859_1 = "ISO_8859-1";//ISO_8859-1编码
    // ===================
    public static final int BANK_TIME_OUT_CODE = 701;// “手机银行”未登录时返回码
    public static final int BANK_NOT_LOGIN_CODE = 702;// “手机银行”未登录时返回码
    public static final String CONNECT_RESULT_CODE_OK = "200";// 通讯正常返回码
    public static final String CONNECT_RESULT_CODE_NOLOGIN = "702";// 未登录时返回码
    public static final String CONNECT_RESULT_CODE_TIMEOUT = "701";// 访问超时返回码
    public static final String CONNECT_RESULT_CODE_KEY = "RESULT_CODE";// 通讯返回,返回码关键字
    public static final String CONNECT_RESULT_VALUE_KEY = "RESULT_VALUE";// 通讯返回,返回内容关键字
    public static final String CONNECT_RESULT_EXCEPTION = "connect_exception";// 通讯异常时的关键字
    public static final int GET_STRING_FROM_SERVER_SUCCESS = 200;// 取字符串通讯正常handler处理码
    public static final int GET_STRING_FROM_SERVER_FAILE = 201;// 取字符串通讯异常handler处理码

    public static final String URL_START_HTTP = "http";// http 通讯头
    public static final String URL_START_HTTPS = "https";// https通讯头

    // =======================
    public static final String NOT_LOGIN_SHOW_WELCOME_KEY = "IsShowWelcomePage";
    public static final String NOT_LOGIN_SHOW_RESET_KEY = "IsShowResetDialog";
    // 通讯请求必须参数
    public static final String BTW_MUST_PARAM_SKEY = "SKEY";
    public static final String BTW_MUST_PARAM_MBSKEY = "MBSKEY";
    public static final String BTW_MUST_PARAM_USERID = "USERID";
    public static final String BTW_MUST_PARAM_BRANCHID = "BRANCHID";
    // 通讯返回必须参数
    public static final String BTW_RETURN_MUST_PARAM_SKEY = "HEAD_SKEY";
    public static final String BTW_RETURN_MUST_PARAM_MBSKEY = "HEAD_MBSKEY";
    public static final String BTW_RETURN_MUST_PARAM_USERID = "HEAD_USERID";
    public static final String BTW_RETURN_MUST_PARAM_BRANCHID = "HEAD_BRANCHID";

    // 通讯类cookie变量
    public final static String SET_COOKIE = "Set-Cookie";
    public final static String COOKIE = "Cookie";

    public static final String YU = "&";
    public static final String WEN = "?";
    public static final String ONE_EQUAL = "=";

    /**
     *
     * 接入点相关数据定义
     *
     */
	/*--------运营商代码--------*/
    public final static String OPREATER_CODE_CMCC = "00"; // 移动的供应商代码
    public final static String OPREATER_CODE_CUCC = "01";// 联通的供应商代码
    public final static String OPREATER_CODE_CMCC_2 = "02";// 移动的供应商代码2
    public final static String OPREATER_CODE_CT = "03";// 电信的供应商代码
    /*---------建行接入点的apn名称---------*/
    public final static String CMW_CMWAP = "CMWCmWap"; // 移动wap接入点
    public final static String CMW_CMNET = "CMWCmNet";// 移动net接入点
    public final static String CMW_UNIWAP = "CMWUniWap";// 联通wap接入点
    public final static String CMW_UNINET = "CMWUniNet";// 联通net接入点
    public final static String CMW_3GWAP = "CMW3gWap";// 联通3gwap接入点
    public final static String CMW_3GNET = "CMW3gNet";// 联通3gnet接入点
    public final static String CMW_CTWAP = "ctwap";// 电信wap接入点
    public final static String CMW_CTNET = "ctnet";// 电信net接入点
    /*---------建行接入点apn名称对应值---------*/
    public final static String CMWAP = "cmwap";
    public final static String CMNET = "cmnet";
    public final static String UNIWAP = "uniwap";
    public final static String TGWAP = "3gwap";
    public final static String UNINET = "uninet";
    public final static String TGNET = "3gnet";
    public final static String CTAPN = "#777";// 电信的无论wap和net都是#777
    /*------------接入点数据库字段------------*/
    public final static String APN_ID = "_id";
    public final static String APN_NAME = "name";
    public final static String APN = "apn";
    public final static String APN_PROXY = "proxy";
    public final static String APN_PORT = "port";
    public final static String APN_MCC = "mcc";
    public final static String APN_MNC = "mnc";
    public final static String APN_NUNERIC = "numeric";
    public final static String APN_USER = "user";
    public final static String APN_MMSC = "mmsc";
    public final static String APN_PASSWORD = "password";
    /*------------接入点数据库字段值------------*/
    public final static String CT_WAP_USERNAME = "ctwap@mycdma.cn";// 电信wap用户名
    public final static String CT_NET_USERNAME = "ctnet@mycdma.cn";// 电信net用户名
    public final static String CT_PASSWORD = "vnet.mobis";// 电信接入点秘密，wap和net都一样
    public final static String CT_PROXY = "10.0.0.200";// 电信代理地址
    public final static String WAP_PROXY = "10.0.0.172";// 移动、联通的代理地址
    public final static String WAP_PROXY_PORT = "80";// 代理端口
    /*------------普通使用------------*/
    public final static String APNID = "apn_id";// 修改默认接入点时需要用到的字段
    public static final Uri APN_URI = Uri.parse("content://telephony/carriers");// 访问apn数据库的uri
    public static final Uri CURRENT_APN_URI = Uri.parse("content://telephony/carriers/preferapn");// 取得当前默认接入点的uri

    public static final String LEFT_SLASH = "/"; // 文件夹目录符号
    public static final String CCB_MBC_VERSION = "1.09";// 客户端程序版本号

    // E路护航交易码集合
    public static String TXCODES = "S70020;S70021;SU7020;SM7021;SM7020;SC4101;SR5020;S50020;SHZH05;SHZD06;STZ410;SUDZH6;SEP003;SJ0005;SU0005;SU4112;S41001;SR5020;S50020;S50020;SPA003;S50020;S50021;S50200;S50306;S50020;S50021;SP4004;SP4011;";

    public static final String HTTP = "http";// 注册通讯访问方式
    public static final String HTTPS = "https";// 注册通讯访问方式
    public static final int SOCKET_BUFFER_SIZE = 512 * 1024;// 设置sokect缓存最大字节数
    public static final int HTTP_PROXY_PORT = 80;// http协议代理端口
    public static final int HTTPS_PROXY_PORT = 443;// https协议代理端口

}
