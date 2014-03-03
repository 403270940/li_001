package com.fun.qiubai;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
public class CopyOfGrabHtml {
	public static final  String ACCEPT_LANGUAGE = "Accept-Language";
	public static HttpClient httpClient = new DefaultHttpClient();
	static
	{
		HttpProtocolParams.setUserAgent(httpClient.getParams(),
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; " 
						+"rv:1.9.1.9) Gecko/20100315 Firefox/3.5.9"); 
		httpClient = getHttpsClient(httpClient);
	}
	public static HttpClient getHttpsClient(HttpClient httpClient){
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
	public static boolean getFile(String path)throws Exception{
		HttpGet httpGet = new HttpGet(path);
		HttpResponse httpResponse = httpClient.execute(httpGet);
		int status = httpResponse.getStatusLine().getStatusCode();
		System.out.println(httpResponse.getAllHeaders());
		if(status == HttpStatus.SC_OK)
		{
			String result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
			System.out.println(result);
		}
		return true;
	}
	public static boolean postFile(String path,String content) throws Exception{
		HttpPost httpPost = new HttpPost(path);
//		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//		parameters.add(new BasicNameValuePair("user","dcm"));
//		parameters.add(new BasicNameValuePair("password", "123"));
//		UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(parameters);
//		httpPost.setEntity(urlEncodedFormEntity);
		StringEntity stringEntity = new StringEntity(content);
		httpPost.setEntity(stringEntity);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		int status = httpResponse.getStatusLine().getStatusCode();
		if(status == HttpStatus.SC_OK)
		{
			String result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
			System.out.println(result);
		}
	
		return true;
	}
	
	public static void main(String []args)
	{
		try {
			postFile("https://10.239.45.110/json/login_session","{\"method\":\"login\",\"user_login\":\"test\",\"password\":\"usertest\"}");
			getFile("https://10.239.45.110/json/active_sessions");
//			getFile("https://10.239.45.120/json/active_sessions?null&_=1381832094261");
//			postFile("http://www.renren.com/PLogin.do");
//			getFile("http://www.qiushibaike.com/month/page/1?s=4595690&slow");
//			getFile("http://www.renren.com/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
