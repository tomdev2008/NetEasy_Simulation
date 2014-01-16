package com.simulation.neteasy.api;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;

import com.simulation.neteasy.domain.Comment;
import com.simulation.neteasy.domain.User;
import com.simulation.neteasy.inter.OnNewsListener;
import com.simulation.neteasy.util.StreamOperate;
/**
 * 
 * @author 紫枫
 *
 */
public class UserApi {

	private final static String REGIEST_URI = "http://10.0.2.2:8080/wangyiWeb/UserAction_save.do";//随便编
	private final static String LOGIN_URI = "http://10.0.2.2:8080/wangyiWeb/UserAction_login.do";//随便写
	private final static String USER_IMG_URI = "http://10.0.2.2:8080/wangyiWeb/UserAction_uploadImg.do";//随便填
	private final static String SUBMIT_COMMENT_URI = "http://10.0.2.2:8080/wangyiWeb/UserAction_submitcomment.do";//随便扔
	
	public void regiest(final User u,final OnNewsListener listener){
		new Thread(){
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(REGIEST_URI+"?u.email="+u.getEmail()+"&u.password="+u.getPassword());
					HttpResponse response = client.execute(request );
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == HttpStatus.SC_OK) {
						InputStream is = response.getEntity().getContent();
						byte[] data = StreamOperate.parseInputStream(is);
						String result = new String(data);
						listener.onComplete(result);
					} else{
						listener.onError("未知错误");
					}
				}catch (Exception e) {
					e.printStackTrace();
					listener.onException(e);
				}
			};
		}.start();
	}
	
	public void login(final String email,final String password, final OnNewsListener listener){
		new Thread(){
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(LOGIN_URI+"?u.email="+email+"&u.password="+password);
					HttpResponse response = client.execute(request );
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == HttpStatus.SC_OK) {
						InputStream is = response.getEntity().getContent();
						byte[] data = StreamOperate.parseInputStream(is);
						String result = new String(data);
						listener.onComplete(result);
					} else{
						listener.onError("未知错误");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	public void uploadByte(final File file,final int userId,final Context context){
		new Thread(){
			public void run() {
				Intent intent = new Intent("com.huaao.wangyi.head.success");
				try {
					HttpClient client = new DefaultHttpClient();
					HttpPost request = new HttpPost(USER_IMG_URI);
					MultipartEntity entity = new MultipartEntity();
					FileBody img = new FileBody(file);
					StringBody id = new StringBody(userId+"");
					entity.addPart("upload", img);
					entity.addPart("u.id", id);
					request.setEntity(entity);
					HttpResponse response = client.execute(request);
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == HttpStatus.SC_OK) {
						intent.putExtra("state", "success");
						context.sendBroadcast(intent);
					} else{
						intent.putExtra("state", "fail");
						context.sendBroadcast(intent);
					}
				} catch (Exception e) {
					intent.putExtra("state", "error");
					context.sendBroadcast(intent);
				}
			};
		}.start();
	}
	
	public void submitcomment(final Comment c,final OnNewsListener listener){
		new Thread(){
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpPost request = new HttpPost(SUBMIT_COMMENT_URI);
					List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					BasicNameValuePair userId = new BasicNameValuePair("c.userId", c.getUserId()+"");
					BasicNameValuePair newsId = new BasicNameValuePair("c.newsId", c.getNewsId()+"");
					BasicNameValuePair text = new BasicNameValuePair("c.text", c.getText());
					BasicNameValuePair time = new BasicNameValuePair("c.time", c.getTime());
					BasicNameValuePair userflag = new BasicNameValuePair("c.userflag", c.getUserFlag());
					parameters.add(userId);
					parameters.add(newsId);
					parameters.add(text);
					parameters.add(time);
					parameters.add(userflag);
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,"UTF-8" );
					request.setEntity(entity);
					HttpResponse response = client.execute(request );
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == HttpStatus.SC_OK) {
						InputStream is = response.getEntity().getContent();
						byte[] data = StreamOperate.parseInputStream(is);
						listener.onComplete(new String(data));
					} else{
						listener.onError("未知错误");
					}
				} catch (Exception e) {
					listener.onException(e);
				}
			};
		}.start();
	}
}
