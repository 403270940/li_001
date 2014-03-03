package com.fun.utils;

public class Device {
	
	private String type;
	private String ip;
	private String username;
	private String password;
	
	public Device(String type,String ip,String username,String password){
		this.type = type;
		this.ip = ip;
		this.username = username;
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
