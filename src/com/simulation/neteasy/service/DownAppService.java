package com.simulation.neteasy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.IBinder;

import com.simulation.neteasy.db.AppDBUtil;
import com.simulation.neteasy.inter.OnDownloadListener;
import com.simulation.neteasy.util.DownLoadUtil;
import com.simulation.neteasy.util.NotifiUtil;
/**
 * 
 * @author 紫枫
 *
 */
public class DownAppService extends Service implements OnDownloadListener{

	private ExecutorService zifengService;
	private DownLoadUtil zifengDownLoadUtil;
	private List<DownLoadUtil> zifengList = new ArrayList<DownLoadUtil>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		zifengService = Executors.newCachedThreadPool();
	}
	boolean isAdd;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		int appId = intent.getIntExtra("appId", 0);
		int state = intent.getIntExtra("state", 0);
		Bitmap icon = intent.getParcelableExtra("icon");
		String appName = intent.getStringExtra("appName");
		String appUri = intent.getStringExtra("appUri");
		//如果下载集合有数据
		if (!zifengList.isEmpty() && zifengList.size() > 0) {
			for (DownLoadUtil down : zifengList) {
				//存在下载集合就代表不需要在加入集合 且不必为他加线程
				if (down.getAppId() == appId) {
					switch (state) {
					case 0:
						//暂停
						down.setContinue(false);
						break;
					case 1:
						//继续
						down.setContinue(true);
						zifengService.submit(down);
						break;
					default:
						break;
					}//不需要加入集合
					isAdd = false;
				}  else{
					//集合没有该记录 加入集合
					isAdd = true;
				}
			}
			if (isAdd) {
				zifengDownLoadUtil = new DownLoadUtil(appId, appName, appUri, true,
						DownAppService.this,DownAppService.this,icon);
				zifengList.add(zifengDownLoadUtil);
				zifengService.submit(zifengDownLoadUtil);
			}
		}
		//集合不存在直接开线程下载 加入集合
		else{
			
				zifengDownLoadUtil = new DownLoadUtil(appId, appName, appUri, true,
						DownAppService.this,DownAppService.this,icon);
				zifengList.add(zifengDownLoadUtil);
				zifengService.submit(zifengDownLoadUtil);
		}
			
	}
	
	@Override
	public void downloadLength(int appId) {
		for (DownLoadUtil down : zifengList) {
			if (down.getAppId() == appId) {
				Intent intent = new Intent("com.zifeng.down.setMax");
				intent.putExtra("appName", down.getAppName());
				intent.putExtra("icon", down.getIcon());
				intent.putExtra("max", down.getMainLength());
				System.out.println("服务           发送："+down.getAppName()+"   加入下载   最大："+down.getMainLength());
				sendBroadcast(intent);
				break;
			}
		}
	}

	@Override
	public void nowDown(int appId,String appName) {
		for (DownLoadUtil d : zifengList) {
			if (d.getAppId() == appId) {
				//下载完成
				if (d.getLength() == d.getMainLength()) {
					System.out.println("下载完成_________________________");
					SharedPreferences sp = getSharedPreferences("downInfo", Context.MODE_PRIVATE);
					Editor edit = sp.edit();
					edit.putString(d.getAppName(), d.getAppName());
					edit.commit();
					AppDBUtil db = new AppDBUtil(this);
					db.clearByApp(appId);
					db.close();
					//发送广播\通知 下载完成
					Intent intent = new Intent("com.zifeng.down.notifiAdapter");
					intent.putExtra("appName", d.getAppName());
					sendBroadcast(intent);
					NotifiUtil.sendNotifi(this, "下载完成:"+d.getAppName());
					zifengList.remove(d);
					break;
				} else{
					//发送广播 下载进度
					Intent intent = new Intent("com.zifeng.down.setProgress");
					intent.putExtra("appName", appName);
					intent.putExtra("progress", d.getLength());
					sendBroadcast(intent);
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
}
