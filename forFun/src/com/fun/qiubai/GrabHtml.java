package com.fun.qiubai;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpProtocolParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GrabHtml {
	public static final  String ACCEPT_LANGUAGE = "Accept-Language";
	public static HttpClient httpClient = new DefaultHttpClient();
	static
	{
		HttpHost proxy = new HttpHost("proxy.pd.intel.com", 911);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		HttpProtocolParams.setUserAgent(httpClient.getParams(),
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; " 
						+"rv:1.9.1.9) Gecko/20100315 Firefox/3.5.9"); 
	}
	
	public static String getFile(String path) throws Exception{
		InputStream inputStream = null;
		OutputStream outputStream = null;
		HttpGet httpGet = new HttpGet(path);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String content = "";
		HttpResponse httpResponse;
		try {
			content = httpClient.execute(httpGet, responseHandler);
			System.out.println(content);
			//			Document doc = Jsoup.parse(content);
//			Elements links = doc.select("article");
//			for(Element link : links)
//			{
//				System.out.println(link);
//			}
//			httpResponse = httpClient.execute(httpGet,responseHandler);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(content);
//		int status = httpResponse.getStatusLine().getStatusCode();
//		if(status == HttpStatus.SC_OK)
//		{
//			inputStream = httpResponse.getEntity().getContent();
//			String fileName = "get.html";
//			outputStream = new FileOutputStream(fileName);
//			int tmpByte = -1;
//			while((tmpByte = inputStream.read())!=-1)
//			{
//				outputStream.write(tmpByte);
//			}
//		}
//		else
//		{
//			System.out.println(status);
//			return false;
//		}
//		if(inputStream != null)
//		{
//			inputStream.close();
//		}
//		if(outputStream != null)
//		{
//			outputStream.close();
//		}
		return content;
	}
	
	public static boolean postFile(String path) throws Exception{
		InputStream inputStream = null;
		OutputStream outputStream = null;
		HttpPost httpPost = new HttpPost(path);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("email","liyongyuea@126.com"));
		parameters.add(new BasicNameValuePair("password", "a403270940"));
		UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(parameters);
		httpPost.setEntity(urlEncodedFormEntity);
		
		HttpResponse httpResponse = httpClient.execute(httpPost);
				
		int status = httpResponse.getStatusLine().getStatusCode();
		if(status == HttpStatus.SC_OK)
		{
			inputStream = httpResponse.getEntity().getContent();
			String fileName = "post.html";
			int tmpByte = -1;
			while((tmpByte = inputStream.read())!=-1)
			{
				outputStream.write(tmpByte);
			}
		}
		else if(status == 302)
		{
			String url = httpResponse.getFirstHeader("Location").getValue();
			HttpClient client = new DefaultHttpClient();
			System.out.println(url);
			httpPost.abort();
			getFile(url);
		}
		else
		{
			System.out.println(status);
		}
		return true;
	}
	
	public static void main(String []args)
	{
		try {
			getFile("https://10.239.45.120");

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
