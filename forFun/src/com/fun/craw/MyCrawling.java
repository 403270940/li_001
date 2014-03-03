package com.fun.craw;

import java.util.Set;

public class MyCrawling {

	private void initCrawlerWithSeeds(String[] seeds)
	{
		for(int i = 0; i < seeds.length; i++)
		{
			LinkQueue.addUnVisitedUrl(seeds[i]);
		}
	}
	
	public void crawling(String [] seeds)
	{
		initCrawlerWithSeeds(seeds);
		while(!LinkQueue.isUnVisitedUrlEmpty())
		{
			String url = (String)LinkQueue.deQueueUnVisitedUrl();
			DownLoadFile downLoadFile = new DownLoadFile();
			String html = downLoadFile.downloadFile(url);
			LinkQueue.addVisitedUrl(url);
			if(html != null){
			Set<String> links = ExtractUrl.extractUrlFromString(html);
			for(String link :links)
				LinkQueue.addUnVisitedUrl(link);
			}
		}
	}
	
	public static void main(String []args)
	{
		MyCrawling crawer = new MyCrawling();
		crawer.crawling(new String[]{"http://www.apicollect.com"});
	}
}
