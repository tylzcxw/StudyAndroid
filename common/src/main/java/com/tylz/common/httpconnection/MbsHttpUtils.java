package com.tylz.common.httpconnection;


import com.tylz.common.utils.LogManager;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.security.KeyStore;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/8/8
 * @描述: http通讯工具，创建进行通讯的httpClient对象
 */

public class MbsHttpUtils {
    /**
     * 创建并返回进行通讯的HttpClient对象
     */
    public HttpClient getNewHttpClient(int timeOut){
        HttpClient httpClient = null;
        try{
            //设置参数列表对象
            HttpParams params = new BasicHttpParams();
            //设置http协议版本
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            //设置编码类型
            HttpProtocolParams.setContentCharset(params,HTTP.UTF_8);
            //设置异常是否继续
            HttpProtocolParams.setUseExpectContinue(params,true);
            HttpProtocolParams.setHttpElementCharset(params,HTTP.UTF_8);
            //设置客户端参数
            HttpClientParams.setRedirecting(params,true);//设置是否重定向
            HttpClientParams.setAuthenticating(params,false);
            //设置超时
            HttpConnectionParams.setStaleCheckingEnabled(params,true);
            HttpConnectionParams.setConnectionTimeout(params,timeOut);//设置超时时间
            HttpConnectionParams.setSoTimeout(params,timeOut);//设置socket超时时间
            HttpConnectionParams.setSocketBufferSize(params,MbsConnectGlobal.SOCKET_BUFFER_SIZE);
            //注册通讯http和https模式对应的socket工厂对象
            SchemeRegistry registry = new SchemeRegistry();
            //注册http模式，普通socket
            registry.register(new Scheme(MbsConnectGlobal.HTTP, PlainSocketFactory.getSocketFactory(),MbsConnectGlobal.HTTP_PROXY_PORT));
            //注册https模式 SSLsocket
            if(LogManager.CLOSE_VERIFY_CERTIFICATE){
                //生成证书，帐号密码都为null
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null,null);
                //生成自定义的socket工厂对象
                SSLSocketFactory sf = new MbsSSLSocketFactory(trustStore);
                //设置信任主机
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                // ===测试环境不验证书
                registry.register(new Scheme(MbsConnectGlobal.HTTPS, sf, MbsConnectGlobal.HTTPS_PROXY_PORT));
            }else{
                // ===生产环境，校验证书
                registry.register(new Scheme(MbsConnectGlobal.HTTPS, SSLSocketFactory.getSocketFactory(),
                        MbsConnectGlobal.HTTPS_PROXY_PORT));
            }
            // 生成链接管理对象，把设置的参数和模式设置进去
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            // 返回HttpClient对象
            httpClient = new DefaultHttpClient(ccm, params);
        }catch (Exception e){
            LogManager.logE(e.toString());
            httpClient = new DefaultHttpClient();
        }
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),timeOut);// 设置链接超时时间
        HttpConnectionParams.setSoTimeout(httpClient.getParams(),timeOut);// 设置socket超时时间
        return httpClient;
    }
}
