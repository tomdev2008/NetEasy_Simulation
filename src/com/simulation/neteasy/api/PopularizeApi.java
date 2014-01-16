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
public class PopularizeApi {

	private final static String GET_FRIST_URI = "http://10.0.2.2:8080/wangyiWeb/PopularizeAction_getAll.do";////随便编
	
	public void getAll(final OnNewsListener listener){
		new Thread(){
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(GET_FRIST_URI);
					HttpResponse response = client.execute(request );
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
