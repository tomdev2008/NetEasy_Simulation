package com.simulation.neteasy.util;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simulation.neteasy.domain.AppInfo;
import com.simulation.neteasy.domain.Comment;
import com.simulation.neteasy.domain.NewsImage;
import com.simulation.neteasy.domain.NewsToutiao;
import com.simulation.neteasy.domain.Popularize;
import com.simulation.neteasy.domain.User;
import com.simulation.neteasy.domain.WeatherInfo;
/**
 * 
 * @author 紫枫
 *
 */
public class JsonUtil {
	
	
	public static List<AppInfo> parseApp(String result){
		List<AppInfo> list = new ArrayList<AppInfo>();
		try {
			JSONArray array = new JSONArray(result);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				int id = object.getInt("id");
				String name = object.getString("name");
				String version = object.getString("version");
				String size = object.getString("size");
				int level = object.getInt("level");
				String uri = object.getString("uri");
				String iconUri = object.getString("iconUri");
				AppInfo info = new AppInfo(id, name, version, size, level, uri, iconUri);
				list.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<Popularize> parsePopularize(String json){
		List<Popularize> list = new ArrayList<Popularize>();
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				int id = object.getInt("id");
				String title = object.getString("title");
				String img = object.getString("img");
				list.add(new Popularize(id, title, img));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<NewsImage> parseJsonArray(String json) {
		List<NewsImage> list = new ArrayList<NewsImage>();
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				int id = object.getInt("id");
				String title = object.getString("title");
				String uri = object.getString("uri");
				int newsId = object.getInt("newsId");
				NewsImage newsImage = new NewsImage();
				newsImage.setId(id);
				newsImage.setNewsId(newsId);
				newsImage.setTitle(title);
				newsImage.setUri(uri);
				list.add(newsImage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static NewsToutiao parseNewsTextJson(String result) {
		NewsToutiao news = new NewsToutiao();
		try {
			JSONObject object = new JSONObject(result);
			int id = object.getInt("id");
			String title = object.getString("title");
			String text = object.getString("text");
			String time = object.getString("time");
			String address = object.getString("address");
			int commnet = object.getInt("commnet");
			String type = object.getString("type");
			news = new NewsToutiao(id, title, text, time, address, commnet, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return news;
	}
	
	public static WeatherInfo parseWeatherJson(String result){
		WeatherInfo info = null;
		try {
			JSONObject object = new JSONObject(result);
			JSONObject cityObject = object.getJSONObject("weatherinfo");
			String city = cityObject.getString("city");
			String cityid = cityObject.getString("cityid");
			String temp1 = cityObject.getString("temp1");
			String temp2 = cityObject.getString("temp2");
			String weather = cityObject.getString("weather");
			String img1 = cityObject.getString("img1");
			String img2 = cityObject.getString("img2");
			String ptime = cityObject.getString("ptime");
			info = new WeatherInfo(city, cityid, temp1, temp2, weather, ptime, img1, img2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
	
	public static User parseUserJson(String json){
		User u = null;
		try {
			JSONObject object = new JSONObject(json);
			int id = object.getInt("id");
			System.out.println(id);
			String email = object.getString("email");
			String password = object.getString("password");
			String headImage = object.getString("headImage");
			String address = object.getString("address");
			String flag = object.getString("flag");
			byte[] img = null;
			if (!headImage.equals("null") && headImage != null && headImage.length() != 0) {
				InputStream is = new URL(headImage).openStream();
				img = StreamOperate.parseInputStream(is);
			}
			u = new User(id, email, password, address, flag, img);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return u;
	}
	
	public static List<Comment> parseComment(String json){
		List<Comment> cList = new ArrayList<Comment>();
		try{
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				Comment c  = new Comment();
				JSONObject object = array.getJSONObject(i);
				int id = object.getInt("id");
				String text = object.getString("text");
				String time = object.getString("time");
				String email = object.getString("email");
				String img = object.getString("img");
				if (img.equals("") || img.equals("null") || img == null) {
					img = "";
				}
				String flag = object.getString("flag");
				c.setId(id);
				c.setText(text);
				c.setUserFlag(flag);
				c.setTime(time);
				c.setHead(img);
				c.setEmail(email);
				cList.add(c);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return cList;
	}
}
