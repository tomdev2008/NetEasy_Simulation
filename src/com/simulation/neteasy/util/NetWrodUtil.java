package com.simulation.neteasy.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
/**
 * 
 * @author 紫枫
 *
 */
public class NetWrodUtil {

	public static boolean isNetworking(Context context) {
		// 获得网络状态管理器
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 有网络
		if (connectivityManager != null) {
			// 得到所有网络连接的信息（wifi。。。）
			NetworkInfo[] networkInfos = connectivityManager
					.getAllNetworkInfo();
			if (networkInfos != null) {
				Log.i("NewWrodUtil:", "所有网络连接信息："+networkInfos.length);
				for (NetworkInfo networkInfo : networkInfos) {
					// CONNECTING, CONNECTED, SUSPENDED, DISCONNECTING,
					// DISCONNECTED, UNKNOWN
					if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
					
				}
			}
		}
		return false;
	}
}
