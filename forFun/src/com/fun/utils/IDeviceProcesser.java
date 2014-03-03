package com.fun.utils;

import net.sf.json.JSONArray;

public interface IDeviceProcesser {
	public boolean login()throws Exception;
	public boolean getActiveSessions()throws Exception;
	public boolean releaseSession()throws Exception;
	public JSONArray getUserList() throws Exception;
	public int getUserId(String username) throws Exception;
	public boolean updateUser(String username) throws Exception;
	public boolean deleteUser(String username) throws Exception;
	public boolean addUser() throws Exception;
	
}
