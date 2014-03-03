package com.fun.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.omg.Dynamic.Parameter;
public class HttpsRequest {
	private DefaultHttpClient httpClient = new DefaultHttpClient();
	public DefaultHttpClient getHttpClient(){
		return httpClient;
	}
	public HttpsRequest(){
		HttpProtocolParams.setUserAgent(httpClient.getParams(),
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; " 
						+"rv:1.9.1.9) Gecko/20100315 Firefox/3.5.9"); 
		 try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager(){
	        	 public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	 	        }
	 	    
	 	        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	 	        }
	 	    
	 	        public X509Certificate[] getAcceptedIssuers() {
	 	            return new X509Certificate[]{};
	 	        }
	        };
		 	ctx.init(null, new TrustManager[]{tm}, null);
	        SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        ClientConnectionManager ccm = httpClient.getConnectionManager();
	        SchemeRegistry sr = ccm.getSchemeRegistry();
	        sr.register(new Scheme("https", 443, ssf));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	}
	public String getFile(String path,Map<String,String> headerMap)throws Exception{
		HttpGet httpGet = new HttpGet(path);
		if(headerMap != null){
			Iterator iter = headerMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String)entry.getKey();
				String val = (String)entry.getValue();
				httpGet.setHeader(key, val);
			}
		}
		HttpResponse httpResponse = httpClient.execute(httpGet);
		int status = httpResponse.getStatusLine().getStatusCode();
		String result = null;
		if(status == HttpStatus.SC_OK)
		{
			result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
//			System.out.println(result);
		}
		else 
		{
			System.out.println("Error : response code is " + status);
		}
		httpGet.releaseConnection();
		return result;
	}
	/**
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public String getFile(String path)throws Exception{
		HttpGet httpGet = new HttpGet(path);
		HttpResponse httpResponse = httpClient.execute(httpGet);
		int status = httpResponse.getStatusLine().getStatusCode();
		String result = null;
		if(status == HttpStatus.SC_OK)
		{
			result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
//			System.out.println(result);
		}
		else 
		{
			System.out.println("Error : response code is " + status);
		}
		httpGet.releaseConnection();
		return result;
	}
	public String postFile(String path,String content,Map<String,String> headerMap) throws Exception{
		HttpPost httpPost = new HttpPost(path);
		if(headerMap != null){
			Iterator iter = headerMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String)entry.getKey();
				String val = (String)entry.getValue();
				httpPost.setHeader(key, val);
			}
		}

		StringEntity stringEntity = new StringEntity(content);
		httpPost.setEntity(stringEntity);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		int status = httpResponse.getStatusLine().getStatusCode();
		String result = null;
		if(status == HttpStatus.SC_OK)
		{
			result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");

		}
		else 
		{
			System.out.println("Error :Url is :"+path+" response code is " + httpResponse.getStatusLine());
		}
		httpPost.releaseConnection();
		return result;
	}
	public String postFile(String path,String content) throws Exception{
		HttpPost httpPost = new HttpPost(path);
		StringEntity stringEntity = new StringEntity(content);
		httpPost.setEntity(stringEntity);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		int status = httpResponse.getStatusLine().getStatusCode();
		String result = null;
		if(status == HttpStatus.SC_OK)
		{
			result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");

		}
		else 
		{
			System.out.println("Error :Url is :"+path+" response code is " + httpResponse.getStatusLine());
		}
		httpPost.releaseConnection();
		return result;
	}
	public static void main(String []args)
	{
		try {

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

