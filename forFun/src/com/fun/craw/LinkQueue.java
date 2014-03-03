package com.fun.craw;

import java.util.HashSet;
import java.util.Set;

public class LinkQueue {

	private static Set<String> visitedUrl = new HashSet<String>();
	private static Queue unVisitedUrl = new Queue();
	
	public static void addVisitedUrl(String url)
	{
		visitedUrl.add(url);
	}
	
	public static void addUnVisitedUrl(String url)
	{
		if(url != null && ! url.trim().equals("")
				&& !unVisitedUrl.contains(url) 
				&& !visitedUrl.contains(url))
		{
			unVisitedUrl.enQueue(url);
		}
	}
	
	public static boolean isUnVisitedUrlEmpty()
	{
		return unVisitedUrl.empty();
	}
	
	public static Object deQueueUnVisitedUrl()
	{
		return unVisitedUrl.deQueue();
	}
	
	public static void enQueueUnvisitedUrl(String url)
	{
		unVisitedUrl.enQueue(url);
	}
}
