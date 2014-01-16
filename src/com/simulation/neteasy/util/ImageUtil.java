package com.simulation.neteasy.util;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.simulation.neteasy.domain.NewsImage;
import com.simulation.neteasy.domain.Popularize;
/**
 * 
 * @author 紫枫
 *
 */
public class ImageUtil {
	
	private static ExecutorService service = Executors.newCachedThreadPool();
	
	
	public static void loadPopImage(Context context,List<Popularize> popList){
		for (int i = 0; i < popList.size(); i++) {
			service.submit(new Run(popList.get(i), context,i));
		}
	}
	static class Run implements Runnable{
		private Popularize p;
		private Context context;
		private int position;
		public Run(Popularize p,Context context,int i) {
			super();
			this.p = p;
			this.context = context;
			this.position = i;
		}

		@Override
		public void run() {
			Intent mIntent;
			try {
				InputStream is = new URL(p.getImg()).openStream();
				Drawable stream = BitmapDrawable.createFromStream(is, "img");
				p.setImgDrawable(stream);
				mIntent = new Intent("com.zifeng.imageservice.pop.success");
				mIntent.putExtra("id", position);
				context.sendBroadcast(mIntent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	

	public static void loadImage(Context context, List<NewsImage> newsList){
		System.out.println("加载"+newsList);
		for (NewsImage news : newsList) {
			if (news.getUri() != null && news.getUri() != "") {
				service.submit(new Run1(news,context));
			}
		}
//		for(int i=0;i<10;i++){
//			NewsImage image = new NewsImage();
//			image.setUri("http://10.0.2.2:8080/wangyiWeb/image/iamage3.jpg");
//			service.submit(new Run(image,context));
//		}
	}
	static class Run1 implements Runnable{
		private NewsImage news;
		private Context context;
		public Run1(NewsImage news,Context context) {
			super();
			this.news = news;
			this.context = context;
		}

		@Override
		public void run() {
			Intent mIntent;
			try {
				InputStream is = new URL(news.getUri()).openStream();
				Bitmap drawable = BitmapFactory.decodeStream(is);
				news.setDrawable(drawable);
				mIntent = new Intent("com.zifeng.imageservice.success");
//				mIntent.putExtra("news", news);
				mIntent.putExtra("newsid", news.getId());
//				Log.e("ImageUtil:", "发送完成结果:"+news.toString());
				context.sendBroadcast(mIntent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	
}
