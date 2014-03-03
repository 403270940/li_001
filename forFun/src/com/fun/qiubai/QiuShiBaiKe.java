package com.fun.qiubai;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

public class QiuShiBaiKe {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		System.out.println("Please input the page:");
		while(true) {
			Scanner sc = new Scanner(System.in);
			sb = getUrl(sc.nextInt());
			System.out.println(sb);
		}
	}
	
	private static StringBuffer getUrl(int page) {
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL("http://www.qiushibaike.com/month/page/"+page+"?s=4595690&slow");
			System.out.println(url);
			InputStreamReader in = new InputStreamReader(url.openStream(),"UTF-8");
			BufferedReader br = new BufferedReader(in);
			String line;
			while((line = br.readLine())!=null) {
				if(line!=null && !line.isEmpty() &&line.charAt(0) == '<'){
					continue;
				}
				sb.append("\n"+changeLine(line));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb;
	}
	
	private static String changeLine(String line) {
		StringBuffer sb = new StringBuffer(line);
		for(int i = 0;i < line.length()/70 ; i++) {
			sb.insert((i+1)*70, "\n");
		}
		return sb.toString();
	}
}
