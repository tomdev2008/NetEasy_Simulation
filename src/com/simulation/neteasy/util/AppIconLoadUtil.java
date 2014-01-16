package com.simulation.neteasy.util;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.simulation.neteasy.domain.AppInfo;
/**
 * 
 * @author 紫枫
 *
 */
public final class AppIconLoadUtil {

	private final static ExecutorService service = Executors.newCachedThreadPool();
	
	public static void loadIcon(List<AppInfo> apps,Context context){
		for (AppInfo info : apps) {
			service.submit(new Run(info,context));
		}
	}
	
	private static class Run implements Runnable{
		AppInfo info;
		Context context;
		public Run(AppInfo info,Context context) {
			super();
			this.info = info;
			this.context = context;
		}
		@Override
		public void run() {
			try {
				Bitmap bIcon = BitmapFactory.decodeStream(new URL(info.getIconUri()).openStream());
				BitmapDrawable icon = new BitmapDrawable(bIcon);
				info.setAppIcon(icon);
				Intent intent = new Intent("com.zifeng.wangui.app.icon.loaded");
				context.sendBroadcast(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
