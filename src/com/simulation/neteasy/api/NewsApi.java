package com.simulation.neteasy.api;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.simulation.neteasy.inter.OnNewsListener;
import com.simulation.neteasy.util.StreamOperate;
/**
 * 
 * @author 紫枫
 *
 */
public class NewsApi {
	// 10.0.2.2
	private final static String CATCH_NEWS = "http://10.0.2.2:8080/wangyiWeb/NewsImageAction_catchImage.do";//随便写
	private final static String REFRESH = "http://10.0.2.2:8080/wangyiWeb/NewsImageAction_refresh.do";//随便填
	private final static String ADD = "http://10.0.2.2:8080/wangyiWeb/NewsImageAction_add.do";//随便输
	private final static String TIYU_URI = "http://10.0.2.2:8080/wangyiWeb/NewsImageAction_tiyu.do";//随便塞
	private final static String YULE_URI = "http://10.0.2.2:8080/wangyiWeb/NewsImageAction_yule.do";//随便编
	private final static String CAIJING_URI = "http://10.0.2.2:8080/wangyiWeb/NewsImageAction_caijing.do";//随便扔
	private final static String JUNSHI_URI = "http://10.0.2.2:8080/wangyiWeb/NewsImageAction_junshi.do";//随便丢
	
	public void junshiNews(final OnNewsListener listener){
		new Thread(){
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(JUNSHI_URI);
					HttpResponse response = client.execute(request);
					int code = response.getStatusLine().getStatusCode();
					if (code == HttpStatus.SC_OK) {
						byte[] data = StreamOperate.parseInputStream(response
								.getEntity().getContent());
						listener.onComplete(new String(data));
					} else {
						listener.onError("服务器无响应。。。");
					}
				} catch (Exception e) {
					listener.onException(e);
				}
			};
		}.start();
	
	}
	public void caijingNews(final OnNewsListener listener){
		new Thread(){
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(CAIJING_URI);
					HttpResponse response = client.execute(request);
					int code = response.getStatusLine().getStatusCode();
					if (code == HttpStatus.SC_OK) {
						byte[] data = StreamOperate.parseInputStream(response
								.getEntity().getContent());
						listener.onComplete(new String(data));
					} else {
						listener.onError("服务器无响应。。。");
					}
				} catch (Exception e) {
					listener.onException(e);
				}
			};
		}.start();
	
	}
	
	public void yuleNews(final OnNewsListener listener){
		new Thread(){
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(YULE_URI);
					HttpResponse response = client.execute(request);
					int code = response.getStatusLine().getStatusCode();
					if (code == HttpStatus.SC_OK) {
						byte[] data = StreamOperate.parseInputStream(response
								.getEntity().getContent());
						listener.onComplete(new String(data));
					} else {
						listener.onError("服务器无响应。。。");
					}
				} catch (Exception e) {
					listener.onException(e);
				}
			};
		}.start();
	
	}
	
	public void tiyuNews(final OnNewsListener listener){
		new Thread(){
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(TIYU_URI);
					HttpResponse response = client.execute(request);
					int code = response.getStatusLine().getStatusCode();
					if (code == HttpStatus.SC_OK) {
						byte[] data = StreamOperate.parseInputStream(response
								.getEntity().getContent());
						listener.onComplete(new String(data));
					} else {
						listener.onError("服务器无响应。。。");
					}
				} catch (Exception e) {
					listener.onException(e);
				}
			};
		}.start();
	}
	
	
	public void add(final int id,final OnNewsListener listener){
		new Thread(){
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(ADD+"?newsImage.id="+id);
					HttpResponse response = client.execute(request);
					int code = response.getStatusLine().getStatusCode();
					if (code == HttpStatus.SC_OK) {
						byte[] data = StreamOperate.parseInputStream(response
								.getEntity().getContent());
						listener.onComplete(new String(data));
					} else {
						listener.onError("服务器无响应。。。");
					}
				} catch (Exception e) {
					listener.onException(e);
				}
			};
		}.start();
	}
	
	public void catchNews(final OnNewsListener listener) {
		new Thread() {
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(CATCH_NEWS);
					HttpResponse response = client.execute(request);
					int code = response.getStatusLine().getStatusCode();
					if (code == HttpStatus.SC_OK) {
						byte[] data = StreamOperate.parseInputStream(response
								.getEntity().getContent());
						listener.onComplete(new String(data));
					} else {
						listener.onError("服务器无响应。。。");
					}
				} catch (Exception e) {
					listener.onException(e);
				}
			};
		}.start();
	}

	public void refresh(final int newsId,final OnNewsListener listener) {
		new Thread() {
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(REFRESH+"?newsImage.id="+newsId);
					HttpResponse response = client.execute(request);
					int code = response.getStatusLine().getStatusCode();
					if (code == HttpStatus.SC_OK) {
						byte[] data = StreamOperate.parseInputStream(response
								.getEntity().getContent());
						listener.onComplete(new String(data));
					} else {
						listener.onError("服务器无响应。。。");
					}
				} catch (Exception e) {
					listener.onException(e);
				}
			};
		}.start();

	}
}
