package com.simulation.neteasy.fragment;

import java.lang.ref.SoftReference;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.simulation.neteasy.NewsTextActivity;
import com.simulation.neteasy.adapter.NewsToutiaoLvAdapter;
import com.simulation.neteasy.api.NewsApi;
import com.simulation.neteasy.constant.HandMsgConstant;
import com.simulation.neteasy.domain.NewsImage;
import com.simulation.neteasy.inter.OnNewsListener;
import com.simulation.neteasy.util.ImageUtil;
import com.simulation.neteasy.util.JsonUtil;
import com.zifeng.wangyi.R;
/**
 * 
 * @author 紫枫
 *
 */
public class CaijinFragment extends Fragment {
	private NewsApi zifengApi;
	private ListView zifengCaijing;
	private LinearLayout zifengNewsLoading;
	private Receiver zifengReceiver;
	private List<NewsImage> zifengTiyuList;
	private SoftReference<List<NewsImage>> zifengSoft = new SoftReference<List<NewsImage>>(
			zifengTiyuList);

	class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Message msg = new Message();
			msg.arg1 = HandMsgConstant.IMAGE_LOAD_COMPLETE;
			zifengHandler.sendMessage(msg);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter("com.zifeng.imageservice.success");
		zifengReceiver = new Receiver();
		getActivity().registerReceiver(zifengReceiver, filter);
	}
	
	private NewsToutiaoLvAdapter zifengTiyuAdapter;
	private Handler zifengHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case HandMsgConstant.SUCCESS:
				zifengCaijing.setVisibility(View.VISIBLE);
				zifengNewsLoading.setVisibility(View.GONE);
				zifengTiyuAdapter = new NewsToutiaoLvAdapter(getActivity(), zifengTiyuList);
				zifengCaijing.setAdapter(zifengTiyuAdapter);
				zifengSoft = new SoftReference<List<NewsImage>>(zifengTiyuList);
				ImageUtil.loadImage(getActivity(), zifengTiyuList);
				break;
			case HandMsgConstant.IMAGE_LOAD_COMPLETE:
				zifengTiyuAdapter.notifyDataSetChanged();
				zifengSoft = new SoftReference<List<NewsImage>>(zifengTiyuList);
				break;
			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.caijing, null);
		zifengCaijing = (ListView) view.findViewById(R.id.lv_caijing);
		zifengNewsLoading = (LinearLayout) view
				.findViewById(R.id.ll_news_loading);
		if (zifengSoft.get() != null) {
			zifengCaijing.setVisibility(View.VISIBLE);
			zifengNewsLoading.setVisibility(View.GONE);
			zifengTiyuList = zifengSoft.get();
			zifengTiyuAdapter = new NewsToutiaoLvAdapter(getActivity(), zifengTiyuList);
			zifengCaijing.setAdapter(zifengTiyuAdapter);
		} else {
			zifengApi = new NewsApi();
			// 体育有关新闻
			zifengApi.caijingNews(new MyListener());
		}
		zifengCaijing.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NewsImage newsImage = zifengTiyuList.get(position);
				Intent intent = new Intent(getActivity(),
						NewsTextActivity.class);
				intent.putExtra("newsId", newsImage.getNewsId());
				intent.putExtra("newsImage", newsImage.getDrawable());
				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(R.anim.otherin,
						R.anim.hold);
			}
		});
		return view;

	}

	class MyListener implements OnNewsListener {

		@Override
		public void onComplete(String result) {
			zifengTiyuList = JsonUtil.parseJsonArray(result);
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
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(zifengReceiver);
	}
}
