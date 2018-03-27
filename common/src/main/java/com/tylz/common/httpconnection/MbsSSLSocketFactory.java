package com.tylz.common.httpconnection;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 类功能说明：设置SSL通讯时的socket工厂类，继承于org.apache.http.conn.ssl.SSLSocketFactory，
 * 主要是解决https通讯时的证书和通讯握手的问题。
 * 
 * 
 */
public class MbsSSLSocketFactory extends SSLSocketFactory {
	// 实例化SSLContext对象
	private SSLContext sslContext = SSLContext.getInstance("TLS");

	/**
	 * 方法说明：重写继承的构造函数，解决证书问题
	 * 
	 * @param truststore
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 */
	public MbsSSLSocketFactory(KeyStore truststore)
			throws NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException, UnrecoverableKeyException {
		super(truststore);
		// 定义X509证书处理对象，不做任何处理
		TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

		};
		// 用X509证书处理对象初始化sslContext相关内容
		sslContext.init(null, new TrustManager[] { tm }, null);
	}

	/**
	 * 方法说明：继承的方法，创建通讯socket
	 */
	@Override
	public Socket createSocket(Socket socket, String host, int port,
							   boolean autoClose) throws IOException, UnknownHostException {
		return sslContext.getSocketFactory().createSocket(socket, host, port,
				autoClose);
	}

	/**
	 * 方法说明：继承的方法，创建通讯socket
	 */
	@Override
	public Socket createSocket() throws IOException {
		return sslContext.getSocketFactory().createSocket();
	}
}
