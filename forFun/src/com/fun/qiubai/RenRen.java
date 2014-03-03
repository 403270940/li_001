package com.fun.qiubai;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class RenRen {
	private static DefaultHttpClient httpClient;
	private static HttpResponse response;
	private static String userId;
	private static String requestToken;
	private static String rtk;
	private static String ak;

	public RenRen(String userName, String passWord) {
		httpClient = new DefaultHttpClient();
		String origURL = "http://www.renren.com/home";
		String domain = "renren.com";
		String loginURL = "http://www.renren.com/PLogin.do";
		HttpPost httpPost = new HttpPost(loginURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", userName));
		params.add(new BasicNameValuePair("password", passWord));
		params.add(new BasicNameValuePair("origURL", origURL));
		params.add(new BasicNameValuePair("domain", domain));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response = postMethod(httpPost);

		String url = response.getFirstHeader("Location").getValue();
		String content = getContent(url);
		System.out.println(content);
		StringBuffer sb = new StringBuffer(content);
		int startPos = sb.indexOf("http://www.renren.com/profile.do?id=");
		int endPos = sb.indexOf("\"><span stats=\"V6Hd_Profile\"");
		userId = sb.substring(startPos + 36, endPos);
		startPos = sb.indexOf("get_check:'");
		endPos = sb.indexOf("',get_check_x:'");
		requestToken = sb.substring(startPos + 11, endPos);
		startPos = sb.indexOf("get_check_x:'");
		endPos = sb.indexOf("',env:{domain:");
		rtk = sb.substring(startPos + 13, endPos);
	}

	public HttpResponse postMethod(HttpPost httpPost) {
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpPost.abort();
		}
		return httpResponse;
	}

	public HttpResponse getMethod(String url) {
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpGet.abort();
		}
		return httpResponse;
	}

	public String getContent(String url) {
		HttpGet httpGet = new HttpGet(url);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String content = null;
		try {
			content = httpClient.execute(httpGet, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpGet.abort();
		}
		return content;
	}

	public boolean updateStatus(String words) {
		String url = "http://shell.renren.com/" + userId + "/status";
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("content", words));
		params.add(new BasicNameValuePair("hostid", userId));
		params.add(new BasicNameValuePair("requestToken", requestToken));
		params.add(new BasicNameValuePair("_rtk", rtk));
		params.add(new BasicNameValuePair("channel", "renren"));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response = postMethod(httpPost);
		return true;
	}

	public boolean updateLog(String title, String words) {
		String url = "http://blog.renren.com/blog/0/addBlog";
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("title", title));
		params.add(new BasicNameValuePair("body", "<p>" + words + "</p>"));
		params.add(new BasicNameValuePair("requestToken", requestToken));
		params.add(new BasicNameValuePair("_rtk", rtk));
		params.add(new BasicNameValuePair("blogControl", "99"));
		params.add(new BasicNameValuePair("editBlogControl", "99"));
		params.add(new BasicNameValuePair("jf_vip_em", "-true"));
		params.add(new BasicNameValuePair("postFormId", requestToken));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response = postMethod(httpPost);
		return true;
	}

	public boolean share(String shareURL, String words) {
		String url = "http://shell.renren.com/" + userId + "/share?1";
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("comment", words));
		params.add(new BasicNameValuePair("link", shareURL));
		params.add(new BasicNameValuePair("meta", "%22%22"));
		params.add(new BasicNameValuePair("nothumb", "off"));
		params.add(new BasicNameValuePair("title", words));
		params.add(new BasicNameValuePair("summary", words));
		params.add(new BasicNameValuePair("thumbUrl", ""));
		params.add(new BasicNameValuePair("type", "6"));
		params.add(new BasicNameValuePair("url", shareURL));
		params.add(new BasicNameValuePair("hostid", userId));
		params.add(new BasicNameValuePair("requestToken", requestToken));
		params.add(new BasicNameValuePair("_rtk", rtk));
		params.add(new BasicNameValuePair("channel", "renren"));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response = postMethod(httpPost);
		return true;
	}

	public boolean visit(String id) {
		String url = "http://www.renren.com/profile.do?portal=profileFootprint&ref=profile_footprint&id="
				+ id;
		HttpPost httpPost = new HttpPost(url);
		try {
			response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("访问成功！id号：" + id);
		httpPost.abort();
		return true;
	}

	public boolean MsgToFriend(String id, String words) {
		String urlString = "http://www.renren.com/" + id
				+ "/profile?portal=homeFootprint&ref=home_footprint";
		String content = getContent(urlString);
		StringBuffer sb = new StringBuffer(content);
		int startPos = sb.indexOf("name=\"ak\" value=\"");
		ak = sb.substring(startPos + 17, startPos + 49);
		//System.out.println(ak);
		String url = "http://gossip.renren.com/gossip.do";
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("body", words));
		params.add(new BasicNameValuePair("curpage", ""));
		params.add(new BasicNameValuePair("from", "main"));
		params.add(new BasicNameValuePair("id", "" + id));
		params.add(new BasicNameValuePair("cc", "" + id));
		params.add(new BasicNameValuePair("ak", ak));
		params.add(new BasicNameValuePair("cccc", ""));
		params.add(new BasicNameValuePair("tsc", ""));
		params.add(new BasicNameValuePair("headUrl", ""));
		params.add(new BasicNameValuePair("largeUrl", ""));
		params.add(new BasicNameValuePair("profilever", "2008"));
		params.add(new BasicNameValuePair("only_to_me", "0"));
		params.add(new BasicNameValuePair("color", ""));
		params.add(new BasicNameValuePair("ref", "http://www.renren.com/" + id
				+ "/profile"));
		params.add(new BasicNameValuePair("mode", ""));
		params.add(new BasicNameValuePair("requestToken", requestToken));
		params.add(new BasicNameValuePair("_rtk", rtk));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = httpClient.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			httpPost.abort();
		}
		return true;
	}

	public List<String> getFriendsId() {
		List<String> friendsId = new ArrayList<String>();
		HttpGet get = new HttpGet("http://friend.renren.com/myfriendlistx.do");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			String friendPage = httpClient.execute(get, responseHandler);
			Pattern pattern = Pattern.compile("var friends=(.*);");
			Matcher matcher = pattern.matcher(friendPage);
			if (matcher.find()) {
				String str = matcher.group(1);
				// System.out.println("friends info： "+ str);
				Pattern p = Pattern.compile("\"id\":([1-9][0-9]{0,9})");
				Matcher m = p.matcher(str);
				while (m.find()) {
					friendsId.add(m.group(1));
				}
				return friendsId;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			get.abort();
		}
		return friendsId;
	}

	public static void main(String[] args) {
		RenRen renRen = new RenRen("liyongyuea@126.com", "a403270940");
//		renRen.updateStatus("发一条状态");
//	    renRen.updateLog("标题","内容");
//		renRen.share("http://www.abacusys.com", "陈浩");
//		renRen.visit("286127305");
//		renRen.MsgToFriend("286127305", "测试模拟留言");
		/*
		List<String> friendsId = renRen.getFriendsId();
		Iterator<String> iterator = friendsId.iterator();
		int count = 0;
		while(iterator.hasNext()){
			//renRen.MsgToFriend(iterator.next(), "测试模拟留言");
			renRen.visit(iterator.next());
			count++;
			System.out.println(count);
		}
		*/
	}
}