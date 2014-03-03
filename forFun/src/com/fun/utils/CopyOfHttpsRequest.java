package com.fun.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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
public class CopyOfHttpsRequest {
	public static final  String ACCEPT_LANGUAGE = "Accept-Language";
	public static HttpClient httpClient = new DefaultHttpClient();

	public static HttpClient getHttpsClient(){
		HttpClient httpClient = new DefaultHttpClient();
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
		  
		
		return httpClient;
	}
	/**
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String getFile(String path)throws Exception{
		HttpGet httpGet = new HttpGet(path);
		HttpResponse httpResponse = httpClient.execute(httpGet);
		int status = httpResponse.getStatusLine().getStatusCode();
//		System.out.println(httpResponse.getAllHeaders());
		String result = null;
		if(status == HttpStatus.SC_OK)
		{
			result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
			System.out.println(result);
		}

		httpGet.releaseConnection();
		return result;
	}
	public static String postFile(String path,String content) throws Exception{
		HttpPost httpPost = new HttpPost(path);
		StringEntity stringEntity = new StringEntity(content);
		httpPost.setEntity(stringEntity);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		int status = httpResponse.getStatusLine().getStatusCode();
		String result = null;
		if(status == HttpStatus.SC_OK)
		{
			result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
			System.out.println(result);
//			String[] result1 = result.split("<forwardUrl>");
//			String[] result2 = result1[1].split("</forwardUrl>");
//			String url = "https://10.239.45.64" + result2[0];
//			System.out.println(result1[0]);
		}
		
		httpPost.releaseConnection();
		return result;
	}
	
	public static void main(String []args)
	{
		try {
//			postFile("https://10.239.45.31/Applications/dellUI/RPC/WEBSES/create.asp","WEBVAR_PASSWORD=16951599&WEBVAR_USERNAME=dcm&WEBVAR_ISCMCLOGIN=0");
			postFile("https://10.239.45.120/json/login_session","{\"method\":\"login\",\"user_login\":\"dcm\",\"password\":\"55494384\"}");
			getFile("https://10.239.45.120/json/active_sessions");
			httpClient.getConnectionManager().shutdown();
//			postFile("https://10.239.45.64/data/login","user=dcm&password=21162109");
//			postFile("https://10.239.45.64/data?get=activeSessions","");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

