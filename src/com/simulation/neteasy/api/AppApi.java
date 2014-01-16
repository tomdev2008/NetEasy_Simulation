package com.simulation.neteasy.api;

import java.io.InputStream;

import org.apache.http.HttpResponse;
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
public class AppApi {

	private final static String ALL_APP = "http://10.0.2.2:8080/wangyiWeb/AppAction_allApp.do";//随便写写
	
	public void allApp(final OnNewsListener listener){
		new Thread(){
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(ALL_APP);
					HttpResponse response = client.execute(request);
					InputStream is = response.getEntity().getContent();
					byte[] data = StreamOperate.parseInputStream(is);
					listener.onComplete(new String(data));
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
}
