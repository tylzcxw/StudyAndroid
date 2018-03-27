package com.tylz.common.httpconnection;


import com.tylz.common.utils.LogManager;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 描述:https证书工具类
 * 
 * @version 1.00
 * @author gaobin
 * 
 */
public class MbsX509TrustManager implements X509TrustManager {
	X509TrustManager myJSSEX509TrustManager;

	public MbsX509TrustManager() throws Exception {
		KeyStore ks = KeyStore.getInstance("BKS");
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
		tmf.init(ks);
		TrustManager tms[] = tmf.getTrustManagers();
		for (int i = 0; i < tms.length; i++) {
			if (tms[i] instanceof X509TrustManager) {
				myJSSEX509TrustManager = (X509TrustManager) tms[i];
				return;
			}
		}
	}

	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

	/**
	 * 方法说明：设置不验证证书
	 * 
	 */
	public static void httpsAllowHostNameVerifier() {
		try {
			X509HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
			MbsX509TrustManager mtm = new MbsX509TrustManager();
			TrustManager[] tms = new TrustManager[]{mtm};
			// 初始化X509TrustManager中的SSLContext
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, tms, new java.security.SecureRandom());
			// 为javax.net.ssl.HttpsURLConnection设置默认的SocketFactory和HostnameVerifier
			if (sslContext != null) {
				HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
			}
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
		} catch (Exception e) {
			LogManager.logE(e.toString());
		}
	}
}
