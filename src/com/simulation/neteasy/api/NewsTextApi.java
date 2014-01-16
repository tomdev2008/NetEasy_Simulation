package com.simulation.neteasy.api;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.simulation.neteasy.inter.OnNewsListener;
import com.simulation.neteasy.util.StreamOperate;
/**
 * 
 * @author 紫枫
 *
 */
public class NewsTextApi {

	private final static String NEWS_TEXT_URI = "http://10.0.2.2:8080/wangyiWeb/NewsAction_getNewest.do";//随便写
	private final static String NEWS_TEXT_UPDATE_URI = "http://10.0.2.2:8080/wangyiWeb/NewsAction_update.do";//随便编
	
	
	public void getNewest(final int id,final OnNewsListener listener){
		new Thread(){
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpPost request = new HttpPost(NEWS_TEXT_URI);
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					BasicNameValuePair value = new BasicNameValuePair("news.id", id+"");
					parameters.add(value);
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters);
					request.setEntity(entity);
					HttpResponse response = client.execute(request);
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == HttpStatus.SC_OK) {
						InputStream is = response.getEntity().getContent();
						byte[] data = StreamOperate.parseInputStream(is);
						listener.onComplete(new String(data));
					} else{
						listener.onError("未知错误...");
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.onException(e);
				}
			};
		}.start();
	}
	
	public void update(final int id,final int comment){
		new Thread(){
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpPost request = new HttpPost(NEWS_TEXT_UPDATE_URI);
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					BasicNameValuePair u_id = new BasicNameValuePair("news.id", id+"");
					BasicNameValuePair u_comment = new BasicNameValuePair("news.commnet", comment+"");
					parameters.add(u_id);
					parameters.add(u_comment);
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters);
					request.setEntity(entity);
					HttpResponse response = client.execute(request);
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == HttpStatus.SC_OK) {
					} else{
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
}
