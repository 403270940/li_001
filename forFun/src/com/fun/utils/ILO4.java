package com.fun.utils;


import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ILO4 implements IDeviceProcesser{
	public String ip = "";
	public String session_key = null;
	private String username = null;
	private String password = null;
	HttpsRequest httpsRequest = new HttpsRequest();
	
	public ILO4(Device device){
		this.ip = device.getIp();
		this.username = device.getUsername();
		this.password = device.getPassword();
	}
	
	public ILO4(String ip,String username, String password){
		this.ip = ip;
		this.username = username;
		this.password = password;
	}
	
	public  boolean login() throws Exception{
		String path = "https://"+ip+"/json/login_session";
		String content = "{\"method\":\"login\",\"user_login\":\""+username+"\",\"password\":\""+password+"\"}";
		String result = httpsRequest.postFile(path, content);
		Pattern pattern = Pattern.compile("session_key\":\"(.*)\",\"user_name");
		session_key = Utils.find(result, pattern);
		return true;
	}
	
	public  boolean getActiveSessions() throws Exception{
		if(session_key == null)
			login();
		String path = "https://" + ip + "/json/active_sessions";
		String result = httpsRequest.getFile(path);
		JSONObject dataJson = JSONObject.fromObject(result);
		JSONArray dataList=dataJson.getJSONArray("sessions");
		System.out.println(dataList.size());
			for(int i = 0 ; i < dataList.size();i++ ){
				JSONObject info=dataList.getJSONObject(i);
				System.out.println(info);
		}
		return true;
	}
	
	public JSONArray getUserList() throws Exception{
		String path = "https://"+ip+"/json/user_info";
		String userListString = httpsRequest.getFile(path);
		JSONObject dataJson = JSONObject.fromObject(userListString);
		JSONArray dataList=dataJson.getJSONArray("users");
		for(int i = 0 ; i < dataList.size();i++ ){
			JSONObject info=dataList.getJSONObject(i);
			System.out.println(info);
		}
		return dataList;
	}
	
	public int getUserId(String username) throws Exception{
		int id = -1;
		JSONArray dataList = getUserList();
		for(int i = 0 ; i < dataList.size();i++ ){
			JSONObject info=dataList.getJSONObject(i);
			if(info.get("user_name").equals(username))
				id = (int)info.get("id");
			System.out.println(info);
		}
		System.out.println("get id " + id);
		return id;
	}
	
	public boolean updateUser(String username) throws Exception{
		int id = getUserId(username);
		JSONObject ojsonObjectbj = new JSONObject();
		ojsonObjectbj.put("login_name", "testnokvm");
		ojsonObjectbj.put("password","testnokvm");
		ojsonObjectbj.put("user_name", "testnokvm"); 
		ojsonObjectbj.put("remote_cons_priv", 0); 
		ojsonObjectbj.put("virtual_media_priv", 1); 
		ojsonObjectbj.put("reset_priv", 1); 
		ojsonObjectbj.put("config_priv", 1); 
		ojsonObjectbj.put("user_priv", 1); 
		ojsonObjectbj.put("method", "mod_user"); 
		ojsonObjectbj.put("id", id); 
		ojsonObjectbj.put("user_id", id); 
		ojsonObjectbj.put("session_key", session_key); 
		String path = "https://" + ip + "/json/user_info";
		httpsRequest.postFile(path, ojsonObjectbj.toString());
		return false;
	}
	
	public boolean deleteUser(String username) throws Exception{
		int id = getUserId(username);
		if(id == -1) return false;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method","del_user");
		jsonObject.put("user_id",id);
		jsonObject.put("id",id);
		jsonObject.put("session_key",session_key);
		String path = "https://"+ip+"/json/user_info";
		String delResult = httpsRequest.postFile(path,jsonObject.toString());
		return true;
	}
	
	public boolean addUser() throws Exception{
		String path = "https://"+ip+"/json/user_info";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("login_name","123");
		jsonObject.put("password","user@123");
		jsonObject.put("user_name","123");
		jsonObject.put("remote_cons_priv",1);
		jsonObject.put("virtual_media_priv",1);
		jsonObject.put("reset_priv",1);
		jsonObject.put("config_priv",1);
		jsonObject.put("user_priv",1);
		jsonObject.put("method","add_user");
		jsonObject.put("session_key",session_key);
		String addResult = httpsRequest.postFile(path, jsonObject.toString());
		System.out.println("add result:"+addResult);
		return false;
	}
	
	public  boolean releaseSession() throws Exception{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("method","logout");
		jsonObject.put("session_key", session_key);
		String content = jsonObject.toString();
		String path  = "https://"+ ip +"/json/login_session";
		String result = httpsRequest.postFile(path,content);
		System.out.println(result);
		return true;
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ILO4 hpILo4 = new ILO4("10.239.45.110","test","usertest");
		// TODO Auto-generated method stub
		try {
			hpILo4.login();
			hpILo4.getActiveSessions();
//			hpILo4.listUser();
//			hpILo4.deleteUser("123");
//			hpILo4.addUser();
//			hpILo4.updateUser();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			hpILo4.releaseSession();
		}
	}

}
