package com.simulation.neteasy.fragment;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.simulation.neteasy.adapter.DownIngAdapter;
import com.simulation.neteasy.db.AppDBUtil;
import com.simulation.neteasy.domain.AppDownInfo;
import com.simulation.neteasy.view.MyProgressBar;
import com.zifeng.wangyi.R;
/**
 * 
 * @author 紫枫
 *
 */
public class DownLoadingFragment extends Fragment {

	private View zifengView;
	private List<AppDownInfo> zifengDownList;
	private ListView zifengAllApp;
	private ImageView zifengAppLoad;
	private BroadcastReceiver zifengReceiver;
	private DownIngAdapter zifengDownIngAdapter;
	private Handler zifengHandler = new Handler();

	class Receiver extends BroadcastReceiver {
		// log 如果太频繁 会堵塞主线程
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.zifeng.down.setMax")) {
				String appName = intent.getStringExtra("appName");
				Bitmap appIcon = intent.getParcelableExtra("icon");
				long max = intent.getLongExtra("max", 0);
				AppDownInfo info = new AppDownInfo();
				info.setName(appName);
				info.setAppIcon(appIcon);
				info.setMaxLength(max);
				boolean isAdd = true;
				if (zifengDownList.size() > 0) {
					for (AppDownInfo app : zifengDownList) {
						if (app.getName().equals(appName)) {
							isAdd = false;
							break;
						}
					}
					if (isAdd) {
						zifengDownList.add(info);
						zifengDownIngAdapter.notifyDataSetChanged();
					}
				} else {
					zifengDownList.add(info);
					zifengDownIngAdapter = new DownIngAdapter(zifengDownList, getActivity());
					zifengAllApp.setAdapter(zifengDownIngAdapter);
				}
			} else if (intent.getAction().equals("com.zifeng.down.setProgress")) {
				String appName = intent.getStringExtra("appName");
				int progress = intent.getIntExtra("progress", 0);
				for (int i = 0; i < zifengDownList.size(); i++) {
					AppDownInfo downInfo = zifengDownList.get(i);
					if (appName.equals(downInfo.getName())) {
					/*	System.out.println("得到" + downInfo.getName() + "  进度: "
								+ progress);*/
						downInfo.setDownLength(progress);
						View child = zifengAllApp.getChildAt(i);
						if (child != null) {
//							System.out.println("得到孩子：" + child);
							MyProgressBar pb_downing = (MyProgressBar) child
									.findViewById(R.id.pb_downing);
							pb_downing.setProgress(progress);
							float j = downInfo.getDownLength()*100/downInfo.getMaxLength();
							pb_downing.setText("下载:"+j+"%");
						}
						break;
					}
				}
			}else if (intent.getAction()
					.equals("com.zifeng.down.notifiAdapter")) {
				String appName = intent.getStringExtra("appName");
				AppDownInfo downInfo = null;
				for (AppDownInfo info : zifengDownList) {
					if (info.getName().equals(appName)) {
						downInfo  = info;
						break;
					}
				}
				zifengDownList.remove(downInfo);
				zifengDownIngAdapter.notifyDataSetChanged();
			}
		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		System.out.println("注册广播___________-");
		IntentFilter filter = new IntentFilter("com.zifeng.down.setMax");
		filter.addAction("com.zifeng.down.notifiAdapter");
		filter.addAction("com.zifeng.down.setProgress");
		zifengReceiver = new Receiver();
		getActivity().registerReceiver(zifengReceiver, filter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		zifengView = inflater.inflate(R.layout.allapp, null);
		zifengAppLoad = (ImageView) zifengView.findViewById(R.id.iv_app_load);
		zifengAllApp = (ListView) zifengView.findViewById(R.id.lv_all_app);
		zifengAppLoad.setVisibility(View.GONE);
		zifengAllApp.setVisibility(View.VISIBLE);
		AppDBUtil db = new AppDBUtil(getActivity());
		zifengDownList = db.queryDownAll();
		db.close();
		return zifengView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (zifengDownList.size() > 0) {
			zifengDownIngAdapter = new DownIngAdapter(zifengDownList, getActivity());
			zifengAllApp.setAdapter(zifengDownIngAdapter);
		}
	}

	@Override
	public void onDetach() {
		getActivity().unregisterReceiver(zifengReceiver);
		super.onDetach();
	}
}
