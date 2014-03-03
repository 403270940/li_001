package com.fun.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
/*
 * IDrac7 require ST1 to be set in cookie and ST2 to be set in http header ,without that 
 * you can not get active session,you will be warned 401 Unauthorized
 * 
 * we can get ST1 from the response of post request of https://"+ip+"/data/login
 * 
 * we can get ST1 form the response of post request of "https://" + ip + "/" + ST1;
 */
public class IDRAC7 implements IDeviceProcesser{
	HttpsRequest httpsRequest = new HttpsRequest();
	public String ip = "";
	public String session_key = null;
	private String username = null;
	private String password = null;
	public String administrator = "511";
	public String operator = "499";
	public String readonly = "1";
	public String none = "000000000";
	Map<String, String> headerMap = new HashMap<String,String>();
	public IDRAC7(Device device){
		this.ip = device.getIp();
		this.username = device.getUsername();
		this.password = device.getPassword();
	}
	
	public IDRAC7(String ip,String username, String password){
		this.ip = ip;
		this.username = username;
		this.password = password;
	}
/*
 * (non-Javadoc)
 * @see com.fun.utils.IDeviceProcesser#login()
 */
	public boolean login() throws Exception {
		// TODO Auto-generated method stub
		
		//login and get seesion and get ST1 value which is required in cookie
		String path = "https://"+ip+"/data/login";
		String content = "user="+username+"&password=" + password;
		String result = httpsRequest.postFile(path, content);
		Pattern pattern = Pattern.compile("<forwardUrl>(.*)</forwardUrl>");
		String ST1 = Utils.find(result, pattern);
		
		//Get ST2 value which is required in Http header
		String redirectPath = "https://" + ip + "/" + ST1;
		String result2 = httpsRequest.getFile(redirectPath);
		Pattern st2Pattern = Pattern.compile("var TOKEN_VALUE = \"(.*)\";");
		String ST2 = Utils.find(result2, st2Pattern);
		
		//form cookie and add ST2 header 
		CookieStore cs = httpsRequest.getHttpClient().getCookieStore();
		String value = cs.getCookies().get(0).getValue();
		cs.addCookie(new BasicClientCookie("tokenvalue",ST1));
		headerMap.put("ST2", ST2);
		return false;
	}

	public boolean getActiveSessions() throws Exception {
		// TODO Auto-generated method stub
		String path = "https://"+ip+"/data?get=activeSessions";
		String content = "";
		String sessionResult = httpsRequest.postFile(path, content,headerMap);
		System.out.println(sessionResult);
		return false;
	}

	public boolean releaseSession() throws Exception {
		// TODO Auto-generated method stub
		String path = "https://"+ip+"/data/logout";
		String content = "";
		String result = httpsRequest.postFile(path, content,headerMap);
//		System.out.println(result);
		return false;
	}
	
	public JSONArray getUserList() throws Exception {
		// TODO Auto-generated method stub
		String path = "https://10.239.45.64/data?get=user,";
		String userlist = httpsRequest.getFile(path,headerMap);
		System.out.println(userlist);
		return null;
	}

	public int getUserId(String username) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
//(16,1,@069@06e@074@065@06c@031@032@033,511,@075@073@065@072@040@031@032@033,15,15,0,0,0,0)Administrator
	public boolean updateUser(String username) throws Exception {
		// TODO Auto-generated method stub
		String path = "https://"+ip+"/chuser";
		int userId = getUserId(username);//user id 
		String enable = "1";// 1 is true 
//		String username = "";
		String password = "";
		String privilage = administrator;
		String postDate = "("+userId+","+enable+","+convert(username)+","+
							privilage+","+convert(password)+",15,15,0,0,0,0)";
		return false;
	}
	public String convert(String raw){
		String result = "";
		for(int i = 0; i < raw.length(); i++)
		{
			result += "@0" + Integer.toHexString(raw.charAt(i) + 0);
		}
		return result;
	}
	public boolean deleteUser(String username) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addUser() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String[] args) throws Exception{
		IDRAC7 device = new IDRAC7("10.239.45.64","dcm","21162109");
		device.login();
//		device.getActiveSessions();
		device.getUserList();
		device.releaseSession();
	}
	
//	String cookie = "_appwebSessionId_="+value+";"+
//	"batteriesIcon=status_ok; " +
//"removableFlashMediaIcon=status_ok; " +
//"temperaturesIcon=status_ok; " +
//"voltagesIcon=status_ok; " +
//"powerSuppliesIcon=status_unknown; " +
//"tokenvalue=" + ST1;
//headerMap.put("Cookie", cookie);
//httpsRequest.getHttpClient().getParams().setParameter("ST2", ST2);
//cs.addCookie(new BasicClientCookie("batteriesIcon","status_ok"));
//cs.addCookie(new BasicClientCookie("removableFlashMediaIcon","status_ok"));
//cs.addCookie(new BasicClientCookie("temperaturesIcon","status_ok"));
//cs.addCookie(new BasicClientCookie("voltagesIcon","status_ok"));
//cs.addCookie(new BasicClientCookie("powerSuppliesIcon","status_ok"));

}
