package com.example.commblib.net.httpconnection;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.example.commblib.utils.LogManager;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/8/8
 * @描述: 负责通讯的类，输入一个请求(request),输入一个结果(result=response)
 */

public class MbsNewHttpConnection {
    private Context mContext;


    public MbsNewHttpConnection(Context context) {
        mContext = context;
    }

    /**
     * 关闭https 证书校验 如果不需要https证书校验,请在http4Result/方法前调用
     */
    public static void enableAllHttps() {
        MbsX509TrustManager.httpsAllowHostNameVerifier();
    }

    public MbsResult http4Result(MbsRequest request) {
        if (request == null) {
            return null;
        }
        /**
         * 打印request
         */
        LogManager.logD(request.toString());
        String url = request.getUrl();
        String method = request.getMethod().trim();
        if (TextUtils.isEmpty(url)) {
            throw new RuntimeException("request url empty");
        }
        if (LogManager.CLOSE_VERIFY_CERTIFICATE) {
            enableAllHttps();
        }
        //获取result
        MbsResult result = null;
        if (MbsRequest.METHOD_GET.equalsIgnoreCase(method)) {
            result = httpGetConn(request);
        } else {
            result = httpPostConn(request);
        }
        //打印结果
        LogManager.logD(result.toString());
        return result;
    }

    private MbsResult httpPostConn(MbsRequest request) {
        MbsResult result = new MbsResult(request.isStringResult());
        LogManager.logD("----------------httpPostConn--------------------");
        try {
            MbsHttpUtils httpUtils = new MbsHttpUtils();
            HttpClient httpClient = httpUtils.getNewHttpClient(request.mSocketTimeOut);
            //生成post方式的请求对象
            HttpPost httpPost = new HttpPost(request.getUrl());
            HashMap<String, String> header = request.getHeader();
            if (header != null && header.size() > 0) {
                for (String field : header.keySet()) {
                    httpPost.setHeader(field, header.get(field));
                }
            }
            //封装实际通讯请求参数
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs = getNameValuePair(nameValuePairs, request.getParams());
            //设置报文体
            if (nameValuePairs != null && nameValuePairs.size() > 0) {

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, MbsConnectGlobal.DEFAULT_ENCORD));
            }
            //根据通讯状态是否代理
            String proxyHost = android.net.Proxy.getHost(mContext);
            int proxyPort = android.net.Proxy.getPort(mContext);
            WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            // 获取cookie信息，并上送报文头
            String cookies = MbsCookieManager.getInstance().getCookied(httpPost.getURI(),header);
            if(!TextUtils.isEmpty(cookies)){
                httpPost.setHeader(MbsConnectGlobal.COOKIE,cookies);
            }
            // 如果有代理主机（说明是wap）并且没有打开wifi，则设置代理
            HttpResponse response = null;
            if (proxyHost != null && wifi.isWifiEnabled() == false) {
                HttpHost proxy = new HttpHost(proxyHost, proxyPort);
                httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                response = httpClient.execute(proxy, httpPost);
            } else {
                response = httpClient.execute(httpPost);
            }
            //保存cookie
            MbsCookieManager.getInstance().saveCookies(httpPost.getURI(),response.getAllHeaders());
            int code = response.getStatusLine().getStatusCode();
            LogManager.logI("code =[" + code + "]");
            //返回码为200,正常通讯
            if(code == 200){
                String charsetName = request.getCharsetName();
                charsetName = TextUtils.isEmpty(charsetName) ? MbsConnectGlobal.DEFAULT_ENCORD:charsetName;
                result.setInputStream(response.getEntity().getContent(),charsetName);
                result.setHeader(getHeaders(response.getAllHeaders()));
            }
        } catch (Exception e) {
            LogManager.logE(e.toString());
            result.setConnExp(e);
        }

        return result;
    }

    /**
     * 请求参数，封装成List<NameValuePair>
     *
     * @param nameValuePairs
     * @param params
     * @return
     */
    private List<NameValuePair> getNameValuePair(List<NameValuePair> nameValuePairs, Map<String, String> params) {
        if (params == null)
            return null;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            if (key != null) {
                String value = entry.getValue() == null ? "" : entry.getValue();
                nameValuePairs.add(new BasicNameValuePair(key, value));
            }
        }
        return nameValuePairs;
    }

    /**
     * get请求
     *
     * @param request 请求对戏那个
     * @return {@link MbsResult}
     */
    private MbsResult httpGetConn(MbsRequest request) {
        MbsResult result = new MbsResult(request.isStringResult());
        try {
            LogManager.logD("----------------httpGetConn--------------------");
            MbsHttpUtils httpUtils = new MbsHttpUtils();
            HttpClient httpClient = httpUtils.getNewHttpClient(request.mSocketTimeOut);
            //组装请求路径:[url + ？ + 变量]
            StringBuffer sb = new StringBuffer(request.getUrl());
            sb.append(MbsConnectGlobal.WEN);
            String uri = dealParams(sb, request.getParams());
            HttpGet httpGet = new HttpGet(uri);
            //设置手机银行客户端通讯所需报文头
            HashMap<String, String> header = request.getHeader();
            if (header != null && header.size() > 0) {
                for (String field : header.keySet()) {
                    httpGet.setHeader(field, header.get(field));
                }
            }
            //根据通讯状态设置是否代理
            String proxyHost = android.net.Proxy.getHost(mContext);
            int proxyPort = android.net.Proxy.getPort(mContext);
            WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            //获取cookie信息，并上送报文头
            String cookies = MbsCookieManager.getInstance().getCookied(httpGet.getURI(), header);
            if (!TextUtils.isEmpty(cookies)) {
                httpGet.setHeader(MbsConnectGlobal.COOKIE, cookies);
            }
            HttpResponse response = null;
            // 如果有代理主机（说明是wap）并且没有打开wifi，则设置代理
            if (proxyHost != null && wifi.isWifiEnabled() == false) {
                HttpHost proxy = new HttpHost(proxyHost, proxyPort);
                httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                response = httpClient.execute(proxy, httpGet);
            } else {
                response = httpClient.execute(httpGet);
            }
            //保存cookie
            MbsCookieManager.getInstance().saveCookies(httpGet.getURI(), response.getAllHeaders());
            //判断通讯结果
            int code = response.getStatusLine().getStatusCode();
            result.setResponseCode(code);
            //打印请求结果日志
            LogManager.logI("code = [" + code + "]");
            //返回码200,正常通讯
            if (code == 200) {
                String charsetName = request.getCharsetName();
                charsetName = TextUtils.isEmpty(charsetName) ? MbsConnectGlobal.DEFAULT_ENCORD : charsetName;
                result.setInputStream(response.getEntity().getContent(), charsetName);
                result.setHeader(getHeaders(response.getAllHeaders()));
            }
        } catch (Exception e) {
            LogManager.logD(e.toString());
            result.setConnExp(e);
        }
        return result;
    }

    /**
     * 方法说明：headers转换
     *
     * @param headers
     */
    public static Map<String, List<String>> getHeaders(Header[] headers) {
        if (headers == null || headers.length <= 0) {
            return null;
        }
        Map<String, List<String>> heads = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            Header header = headers[i];
            if (header != null) {
                List<String> values = new ArrayList<String>();
                values.add(header.getValue());
                heads.put(header.getName(), values);
            }
        }
        return heads;
    }

    /**
     * 请求参数处理
     *
     * @param sb
     * @param params
     * @return
     */
    private String dealParams(StringBuffer sb, HashMap<String, String> params) {
        if (params != null) {
            try {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String key = entry.getKey();
                    if (key != null) {
                        String value = entry.getValue() == null ? "" : entry.getValue();
                        value = URLEncoder.encode(value, MbsConnectGlobal.DEFAULT_ENCORD);
                        sb.append(key).append(MbsConnectGlobal.ONE_EQUAL).append(value).append(MbsConnectGlobal.YU);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                LogManager.logE(e.toString());
            }
        }
        //删除最后一个&或者？
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
