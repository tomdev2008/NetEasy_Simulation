package com.simulation.neteasy.api;

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;

import com.simulation.neteasy.inter.OnNewsListener;
import com.simulation.neteasy.inter.OnWeatherImgListener;
import com.simulation.neteasy.util.StreamOperate;
/**
 * 
 * @author 紫枫
 *
 */
public class WeatherApi {

	private final static String WEATHER_URI = "http://www.weather.com.cn/data/cityinfo/";//随便编
	private final static String WEATHER_IMG_URI = "http://m.weather.com.cn/img/";//随便编
	private final static String HTML = ".html";
	
	public void getWeather(final String code,final OnNewsListener listener){
		new Thread(){
			public void run() {
				try {
					System.out.println(WEATHER_URI+code+HTML);
					InputStream is = new URL(WEATHER_URI+code+HTML).openStream();
					byte[] data = StreamOperate.parseInputStream(is);
					listener.onComplete(new String(data));
				} catch (Exception e) {
					e.printStackTrace();
					listener.onException(e);
				}
			};
		}.start();
	}
	
	public void getWeatherImg(final String img1,final String img2,final OnWeatherImgListener listener){
		new Thread(){
			public void run() {
				try {
					InputStream isImg1 = new URL(WEATHER_IMG_URI+img1).openStream();
					InputStream isImg2 = new URL(WEATHER_IMG_URI+img2).openStream();
					Drawable img2Data = Drawable.createFromStream(isImg1, "image");
					Drawable img1Data = Drawable.createFromStream(isImg2, "image");
					listener.onComplete(img1Data, img2Data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
}
