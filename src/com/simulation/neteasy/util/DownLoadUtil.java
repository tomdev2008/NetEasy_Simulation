package com.simulation.neteasy.util;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;

import com.simulation.neteasy.constant.XmlConstant;
import com.simulation.neteasy.db.AppDBUtil;
import com.simulation.neteasy.domain.AppDownInfo;
import com.simulation.neteasy.inter.OnDownloadListener;
/**
 * 
 * @author 紫枫
 *
 */
public class DownLoadUtil implements Runnable {

	private int appId;
	private String appName;
	private String appUri;
	private boolean isContinue;
	private Context context;
	private int length;
	private long mainLength;
	private OnDownloadListener listener;
	private Bitmap icon;

	public long getMainLength() {
		return mainLength;
	}

	public void setMainLength(long mainLength) {
		this.mainLength = mainLength;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppUri() {
		return appUri;
	}

	public void setAppUri(String appUri) {
		this.appUri = appUri;
	}

	public boolean isContinue() {
		return isContinue;
	}

	public void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	

	public Bitmap getIcon() {
		return icon;
	}

	public DownLoadUtil(int appId, String appName, String appUri,
			boolean isContinue, Context context,OnDownloadListener listener,Bitmap icon) {
		super();
		this.appId = appId;
		this.appName = appName;
		this.appUri = appUri;
		this.isContinue = isContinue;
		this.context = context;
		this.listener = listener;
		this.icon = icon;
	}

	@Override
	public void run() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(appUri);
			HttpResponse response = client.execute(request);
			//得到文件大小
			mainLength = response.getEntity().getContentLength();
			AppDBUtil db = new AppDBUtil(context);
			File file = new File(Environment.getExternalStorageDirectory(),
					XmlConstant.WANGYI + "/" + appName+".apk");
			RandomAccessFile raf = new RandomAccessFile(file, "rwd");
			raf.setLength(mainLength);
			raf.close();
			length = db.queryDownLength(appId);
			// 不存在下载记录
			//这里放在点击事件里比较好 
			if (length == 0) {
				//数据库保存3条线程信息
				for (int i = 0; i < 3; i++) {
					AppDownInfo downInfo = new AppDownInfo();
					downInfo.setAppId(appId);
					downInfo.setName(appName);
					downInfo.setThreadId(i);
					downInfo.setState(0);
					downInfo.setAppIcon(icon);
					downInfo.setMaxLength(mainLength);
					db.save(downInfo);
				}
			}
			db.close();
			listener.downloadLength(appId);
			// 开三条线程下载
			long block = (mainLength % 3 == 0) ? (mainLength / 3)
					: (mainLength / 3 + 1);
			for (int i = 0; i < 3; i++) {
				new DownloadThread(block, i, file).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			NotifiUtil.sendNotifi(context, "下载:"+appName +"失败");
			AppDBUtil db = new AppDBUtil(context);
			AppDownInfo info = new AppDownInfo();
			info.setAppId(appId);
			info.setState(1);
			info.setDownLength(getLength());
			info.setThreadId(0);
			db.update(info);
			db.close();
			Intent intent = new Intent("com.huaao.down.notifiAdapter");
			context.sendBroadcast(intent);
		}
	}
	

	class DownloadThread extends Thread {
		private int threadId;
		private File file;
		private long startPosition;
		private long endPosition;

		public DownloadThread(long block, int threadId, File file) {
			super();
			this.threadId = threadId;
			this.file = file;
			startPosition = block * threadId;
			endPosition = startPosition + block - 1;
		}

		@Override
		public void run() {
			try {
				AppDBUtil db = new AppDBUtil(context);
				//得到上次下载进度
				int downLength = db.queryDownLengthByThreadId(appId, appName,
						threadId);
				startPosition += downLength;
				System.out.println("开始下载:"+threadId+"     上次进度："+downLength);
				URL url = new URL(appUri);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestProperty("Range", "bytes=" + startPosition + "-"
						+ endPosition);
				InputStream is = conn.getInputStream();
				RandomAccessFile raf = new RandomAccessFile(file, "rwd");
				//设置此次下载写入外文件的位置
				raf.seek(startPosition);
				byte[] b = new byte[1024];
				int len = 0;
				int count = 0;
				AppDownInfo info = new AppDownInfo();
				info.setAppId(appId);
				info.setThreadId(threadId);
				while ((len = is.read(b)) != -1) {
					if (!isContinue) {
						//上次下载了downlength 这次下载count 加起来就是暂停时候下载了多少
						info.setDownLength(downLength + count);
						System.out.println("停止下载:"+threadId +"        保存进度："+ (downLength + count));
						//state 1 暂停状态
						info.setState(1);
						//更新
						db.update(info);
						db.close();
						break;
					}
					//设置当前进度 以前进度+len(1024 每次下载进度)
					setLength(getLength() + len);
					count += len;
					if ((count / len)% 11 == 0) {
						//使用接口通信
						listener.nowDown(appId,appName);
					}
					raf.write(b, 0, len);
				}
				System.out.println(threadId +"         :" + "下载到"+length);
				if (getLength() == mainLength) {
					//使用接口通信
					System.out.println("发送完成广播________________");
					listener.nowDown(appId,appName);
				}
				db.close();
				raf.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
				NotifiUtil.sendNotifi(context, "下载:"+appName +"失败");
				AppDBUtil db = new AppDBUtil(context);
				AppDownInfo info = new AppDownInfo();
				info.setAppId(appId);
				info.setThreadId(0);
				info.setState(1);
				info.setDownLength(getLength());
				db.update(info);
				db.close();
				Intent intent = new Intent("com.zifeng.down.notifiAdapter");
				context.sendBroadcast(intent);
			}
		}

	}

}
