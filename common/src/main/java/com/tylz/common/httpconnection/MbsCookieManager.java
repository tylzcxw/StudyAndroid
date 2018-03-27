package com.tylz.common.httpconnection;

import android.text.TextUtils;

import com.tylz.common.utils.LogManager;

import org.apache.http.Header;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @创建者: xuanwen
 * @创建日期: 2017/8/9
 * @描述: 按uri存储Cookie
 */

public class MbsCookieManager {
    private static MbsCookieManager mInstance = null;
    private final CookieManager mProxy;
    private MbsCookieManager(){
        mProxy = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
    }
    public synchronized static MbsCookieManager getInstance(){
        if(mInstance == null){
            mInstance = new MbsCookieManager();
        }
        return mInstance;
    }
    public String getCookied(URI uri, Map<String,String> requestHeaders){
        if(uri == null){
            return null;
        }
        String result = null;
        Map<String,List<String>> headers = new HashMap<>();
        if (requestHeaders != null && !requestHeaders.isEmpty()) {
            for (Map.Entry<String, String> e : requestHeaders.entrySet()) {
                String key = e.getKey();
                String value = e.getValue();
                if (!TextUtils.isEmpty(key) && value != null) {
                    List<String> values = new ArrayList<String>();
                    values.add(value);
                    headers.put(key, values);
                }
            }
        }
        try {
            Map<String, List<String>> cookies = mProxy.get(uri, headers);
            if(cookies != null && cookies.size() > 0){
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, List<String>> entry : cookies.entrySet()) {
                    String key = entry.getKey();
                    if (!TextUtils.isEmpty(key)) {
                        List<String> values = entry.getValue();
                        int n = values.size();
                        for (int i = 0; i < n; i++) {
                            String val = values.get(i);
                            if (!TextUtils.isEmpty(val)) {
                                if (sb.length() > 0) {
                                    sb.append("; ");
                                }
                                sb.append(val);
                            }
                        }
                    }
                }
                result = sb.toString();
            }
        } catch (IOException e) {
            LogManager.logW(String.format("获取[%s]Cookies失败[%s]", uri, e.toString()));
        }
        LogManager.logD(String.format("请求[%s]Cookies[%s]", uri, result));
        return result;
    }
    /**
     * 保存指定url的Cookies
     */
    public void saveCookies(URI uri, Header[] headers){
        if(uri == null || headers == null || headers.length == 0){
            return;
        }
        Map<String,List<String>> _headers = new HashMap<>();
        for(Header h : headers){
            String key = h.getName();
            String value = h.getValue();
            if(!TextUtils.isEmpty(key) && value != null){
               List<String> values =  _headers.get(key);
                if(values == null){
                    values = new ArrayList<>();
                }
                values.add(value);
                _headers.put(key,values);
            }
            try {
                mProxy.put(uri,_headers);
            } catch (IOException e) {
                LogManager.logW(String.format("获取[%s]Cookies失败[%s]", uri, e.toString()));
            }
            LogManager.logD(String.format("[%s]Cookies已保存", uri));
        }
    }
}
