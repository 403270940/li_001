package com.fun.craw;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class TrustSSL {

	private static class TrustAnyTrustManager implements X509TrustManager {
    
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
    
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static Map<String, String> startRequest(String Url,  String content, String proxyHost, String proxyPort) {
    	Map <String, String> resultMap = new HashMap<String, String>();
    	
    	try {
    	    System.setProperty("proxySet","false");
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort",proxyPort);
            
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, null);
	        URL console = new URL(Url);

	        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
	        conn.setSSLSocketFactory(sc.getSocketFactory());
	        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Length", "169"); 
	        conn.setRequestProperty("Content-Type","application/json; charset=UTF-8"); 
	        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:20.0) Gecko/20100101 Firefox/20.0"); 
	        conn.setDoOutput(true); 
	        conn.setDoInput(true); 

	        OutputStream os = conn.getOutputStream();
	        DataOutputStream output = new DataOutputStream(os);  

	        output.writeBytes(content);
	        output.flush();
	        output.close();
	        InputStream in = conn.getInputStream();
	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
	        Vector<String> contentCollection = new Vector<String>();
	        StringBuffer temp = new StringBuffer();
	        String line = bufferedReader.readLine();
	        String ecod = conn.getContentEncoding();
	        if (ecod == null) {
	        	ecod = Charset.defaultCharset().name();
	        }
	        while (line != null) {
	        	contentCollection.add(line);
	        	temp.append(line).append("\r\n");
	        	line = bufferedReader.readLine();
	        }
	        bufferedReader.close();
	        String result = new String(temp.toString().getBytes(), ecod);
	        System.out.println(result);
	        resultMap.put("resultJson", result);
    	} catch (Exception e) {
    		System.out.println(e.toString());
    	} finally {
    	
    	}
		return resultMap;
    }
    
    public static void main(String [] args){
    	TrustSSL.startRequest("https://10.239.45.110/json/login_session", "{\"method\":\"login\",\"user_login\":\"test\",\"password\":\"usertest\"}", "", "");
    }
}