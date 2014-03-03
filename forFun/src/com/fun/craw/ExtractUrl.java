package com.fun.craw;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.omg.CORBA.PRIVATE_MEMBER;

import com.fun.qiubai.GrabHtml;

public class ExtractUrl {
	private static Set<String> result = new HashSet<String>();
	public static Set extractUrlFromString(String html)
	{
		Document doc = Jsoup.parse(html,"UTF-8");
		Elements links = doc.select("a");
		for(Element ele :links)
		{
			String url = ele.attr("href").trim();
			if(url.indexOf("http")!=-1)
			{
				result.add(url);
				System.out.println(url);
			}
		}
		return result;
	}
	public static void main(String []args)
	{
		try {
			String html = GrabHtml .getFile("http://www.lietu.com");
			extractUrlFromString(html);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
