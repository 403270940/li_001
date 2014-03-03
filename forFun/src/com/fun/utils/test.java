package com.fun.utils;


public class test {

	/**
	 * @param args
	 */
	public static String convert(String raw){
		String result = "";
		for(int i = 0; i < raw.length(); i++)
		{
			result += "@0" + Integer.toHexString(raw.charAt(i) + 0);
		}
		return result;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String result = convert("intel123");
		System.out.println(result);

	}

}
