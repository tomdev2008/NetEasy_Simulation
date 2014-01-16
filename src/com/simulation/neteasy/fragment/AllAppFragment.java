package com.simulation.neteasy.fragment;

import java.io.File;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.simulation.neteasy.adapter.AllAppAdapter;
import com.simulation.neteasy.api.AppApi;
import com.simulation.neteasy.constant.HandMsgConstant;
import com.simulation.neteasy.constant.XmlConstant;
import com.simulation.neteasy.domain.AppInfo;
import com.simulation.neteasy.inter.OnNewsListener;
import com.simulation.neteasy.util.AppIconLoadUtil;
import com.simulation.neteasy.util.JsonUtil;
import com.zifeng.wangyi.R;
/**
 * 
 * @author 紫枫
 *
 */
public class AllAppFragment extends Fragment {
	
	private RelativeLayout zifengAnimation;
	private View zifengView;
	private ListView zifengAllApp;
	private List<AppInfo> zifengApps;
	private AnimationDrawable zifengLoad;
	private ImageView zifengAppLoad;
	private Receiver zifengReceiver;
	private AllAppAdapter zifengAppAdapter;
	private Handler zifengHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			// app数据加载完成
			case HandMsgConstant.SUCCESS:
				zifengAppAdapter = new AllAppAdapter(getActivity(), zifengApps,zifengAllApp,zifengAnimation);
				zifengAllApp.setAdapter(zifengAppAdapter);
				zifengAllApp.setVisibility(View.VISIBLE);
				if (zifengLoad.isRunning()) {
					zifengLoad.stop();
				}
				zifengAppLoad.setVisibility(View.GONE);
				AppIconLoadUtil.loadIcon(zifengApps, getActivity());
				break;

			default:
				break;
			}
		};
	};
	

	class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// app icon加载完成
			if (intent.getAction().equals("com.zifeng.wangui.app.icon.loaded")) {
				zifengAppAdapter.notifyDataSetChanged();
			} else if (intent.getAction()
					.equals("com.zifeng.down.notifiAdapter")) {
				zifengAppAdapter.notifyDataSetChanged();
			} else if (intent.getAction().equals("com.zifeng.wangyi.install")) {
				String appName = intent.getStringExtra("appName");
				Intent install = new Intent(Intent.ACTION_VIEW);
				File file = new File(Environment.getExternalStorageDirectory(),
						XmlConstant.WANGYI + "/" +appName +".apk");
				install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
				getActivity().startActivity(install);
			}
		}

	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		zifengReceiver = new Receiver();
		IntentFilter filter = new IntentFilter(
				"com.zifeng.wangui.app.icon.loaded");
		filter.addAction("com.zifeng.down.notifiAdapter");
		filter.addAction("com.zifeng.wangyi.install");
		getActivity().registerReceiver(zifengReceiver, filter);
		zifengView = inflater.inflate(R.layout.allapp, null);
		zifengAnimation = (RelativeLayout) zifengView.findViewById(R.id.rl_animation);
		zifengAllApp = (ListView) zifengView.findViewById(R.id.lv_all_app);
		zifengAllApp.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getActivity(),
						"没做......只是为了蛋疼的证明:解决了listView里包含Button响应两个事件共存", 1)
						.show();
			}
		});
		return zifengView;
	}

	@Override
	public void onResume() {
		super.onResume();
		AppApi aApi = new AppApi();
		aApi.allApp(new MyAllAppListener());
	}

	class MyAllAppListener implements OnNewsListener {
		@Override
		public void onComplete(String result) {
			zifengApps = JsonUtil.parseApp(result);
			Message msg = new Message();
			msg.arg1 = HandMsgConstant.SUCCESS;
			zifengHandler.sendMessage(msg);
		}

		@Override
		public void onError(String error) {

		}

		@Override
		public void onException(Exception exception) {

		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		zifengAppLoad = (ImageView) zifengView.findViewById(R.id.iv_app_load);
		zifengLoad = (AnimationDrawable) zifengAppLoad.getBackground();
		zifengLoad.start();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getActivity().unregisterReceiver(zifengReceiver);

	}
}
