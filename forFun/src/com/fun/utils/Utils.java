package com.fun.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	/**
	 * @param args
	 */
	public static String find(String data,Pattern pattern){
		
		String result = null;
		Matcher matcher = pattern.matcher(data);
		if(matcher.find())
			result = matcher.group(1);
		else 
			System.out.println("find no matcher");
		return result;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
