package com.simulation.neteasy.fragment;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.simulation.neteasy.NewsTextActivity;
import com.simulation.neteasy.adapter.NewsToutiaoLvAdapter;
import com.simulation.neteasy.adapter.ToutiaoVPAdapter;
import com.simulation.neteasy.api.NewsApi;
import com.simulation.neteasy.api.PopularizeApi;
import com.simulation.neteasy.constant.HandMsgConstant;
import com.simulation.neteasy.domain.NewsImage;
import com.simulation.neteasy.domain.Popularize;
import com.simulation.neteasy.inter.OnNewsListener;
import com.simulation.neteasy.util.ImageUtil;
import com.simulation.neteasy.util.JsonUtil;
import com.simulation.neteasy.util.NetWrodUtil;
import com.simulation.neteasy.view.RefreshListView;
import com.zifeng.wangyi.R;
/**
 * 本页面对网络进行了判断 其他页面没有-------------
 * @author 紫枫
 *
 */
public class ToutiaoFragment extends Fragment {
	
	List<NewsImage> zifengNewsList = new ArrayList<NewsImage>();
	SoftReference<List<NewsImage>> zifengSoft = new SoftReference<List<NewsImage>>(
			zifengNewsList);
	private LinearLayout zifengNewsLoading;
	private RefreshListView zfiengNewsToutiao;
	private ImageView zifengNoNetwork;
	NewsApi zifengApi = new NewsApi();
	private ToutiaoVPAdapter zifengToutiaoVPAdapter;
	
	private NewsToutiaoLvAdapter zifengNewsToutiaoLvAdapter;
	private Receiver zifengReceiver;
	
	private View zifengViewPager;
	private ViewPager zifengToutiaoHead;
	private ArrayList<View> zifengPagerlist;
	private List<Popularize> zifengPopList;
	private SoftReference<List<Popularize>> zifengSoftPop = new SoftReference<List<Popularize>>(
			zifengPopList);

	Handler zifengHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.arg1) {
			case HandMsgConstant.JUMP:
				NewsImage newsImage = (NewsImage) msg.obj;
				Intent jumpIntent = new Intent(getActivity(),
						NewsTextActivity.class);
				jumpIntent.putExtra("newsId", newsImage.getNewsId());
				jumpIntent.putExtra("newsImage", newsImage.getDrawable());
				System.out.println(newsImage.getTitle());
				getActivity().startActivity(jumpIntent);
				System.out.println("动画"+jumpIntent);
				getActivity().overridePendingTransition(R.anim.otherin,
						R.anim.hold);
				break;
			case HandMsgConstant.VP_TO_CurrentItem:
				if (zifengToutiaoHead.getCurrentItem() == 0) {
					zifengToutiaoHead.setCurrentItem(1);
				} else if (zifengToutiaoHead.getCurrentItem() == 1) {
					zifengToutiaoHead.setCurrentItem(2);
				} else{
					zifengToutiaoHead.setCurrentItem(0);
				}
				break;
			case HandMsgConstant.SUCCESS_POP:
				zifengToutiaoVPAdapter = new ToutiaoVPAdapter(zifengPagerlist, zifengPopList);
				zifengToutiaoHead.setAdapter(zifengToutiaoVPAdapter);
				ImageUtil.loadPopImage(getActivity(), zifengPopList);
//				zifengSoft_pop = new SoftReference<List<Popularize>>(zifengPopList);
				break;

			case HandMsgConstant.ADD_SUCCESS:
				zifengNewsToutiaoLvAdapter.notifyDataSetChanged();
				System.out.println("回发广播 加载完成");
				ImageUtil.loadImage(getActivity(), zifengOldList);
				Intent intent = new Intent("com.zifeng.menu.added");
				getActivity().sendBroadcast(intent );
//				zifengSoft = new SoftReference<List<NewsImage>>(zifengNewsList);
				break;
			case HandMsgConstant.NO_ADD:
				Intent mIntent = new Intent("com.zifeng.menu.nomore");
				getActivity().sendBroadcast(mIntent );
				Toast.makeText(getActivity(), "暂无更多内容", 0).show();
				break;
			case HandMsgConstant.SUCCESS:
				zifengNewsLoading.setVisibility(View.GONE);
				zfiengNewsToutiao.setVisibility(View.VISIBLE);
				// 异步加载图片
				setNewsAdapter();
				/*
				 * for (NewsImage news : zifengNewsList) { Intent service = new
				 * Intent("com.zifeng.wangyi.image"); service.putExtra("news",
				 * news); getActivity().startService(service); }
				 */
				break;
			case HandMsgConstant.IMAGE_LOAD_COMPLETE:
				System.out.println("这里没有对下一个页面网络进行判断，，，，如果不加上这个条件，那下一个页面会发送刷新界面广播这里就null了");
				if (zifengNewsToutiaoLvAdapter != null) {
					zifengNewsToutiaoLvAdapter.notifyDataSetChanged();
				}
//				zifengSoft = new SoftReference<List<NewsImage>>(zifengNewsList);
				break;
			case HandMsgConstant.NO_REFRESH:
				Toast.makeText(getActivity(), "没有新内容", 0).show();
				break;
			case HandMsgConstant.ERROR:
				Toast.makeText(getActivity(), msg.obj.toString(), 0).show();
				break;
			case HandMsgConstant.REFRESH_SUCCESS:
				// 需要更新 加载图片
				for (NewsImage iterable_element : zifengNewsList) {
					System.out.println("完成转换后的集合________________"
							+ iterable_element.getTitle());
				}
				zifengNewsToutiaoLvAdapter.setList(zifengNewsList);
				zifengNewsToutiaoLvAdapter.notifyDataSetChanged();
				System.out.println("更新成功------------");
				Toast.makeText(getActivity(), "更新成功", 0).show();
				ImageUtil.loadImage(getActivity(), zifengNewlist);
//				zifengSoft = new SoftReference<List<NewsImage>>(zifengNewsList);
				break;
			default:
				break;
			}
		}
	};
	private List<NewsImage> zifengOldList;
	// 加载更多
	public class AddNewsListener implements OnNewsListener {


		@Override
		public void onComplete(String result) {

			if (result.equals("error")) {
				Message msg = new Message();
				msg.arg1 = HandMsgConstant.NO_ADD;
				zifengHandler.sendMessage(msg);
			} else {
				zifengOldList = JsonUtil.parseJsonArray(result);
				for (NewsImage news : zifengOldList) {
					zifengNewsList.add(news);
				}
				Message msg = new Message();
				msg.arg1 = HandMsgConstant.ADD_SUCCESS;
				zifengHandler.sendMessage(msg);
			}
		}

		@Override
		public void onError(String error) {

		}

		@Override
		public void onException(Exception exception) {

		}

	}

	

	// 添加listView 一个viewpager的headView 
	public void addHeadView() {
		zifengViewPager = View.inflate(getActivity(), R.layout.toutiao_head_pager,
				null);
		zifengToutiaoHead = (ViewPager) zifengViewPager
				.findViewById(R.id.vp_toutiao_head);
		zifengPagerlist = new ArrayList<View>();
		//要加载三次 必须的。。。
		View head1 = View.inflate(getActivity(), R.layout.toutiao_head_item,
				null);
		View head2 = View.inflate(getActivity(), R.layout.toutiao_head_item,
				null);
		View head3 = View.inflate(getActivity(), R.layout.toutiao_head_item,
				null);
		zifengPagerlist.add(head1);
		zifengPagerlist.add(head2);
		zifengPagerlist.add(head3);
		zfiengNewsToutiao.addHeaderView(zifengViewPager);

		if (zifengSoftPop.get() != null) {
			System.out.println("soft  toutiao___________________");
			zifengPopList = zifengSoftPop.get();
			zifengToutiaoVPAdapter = new ToutiaoVPAdapter(zifengPagerlist, zifengPopList);
			zifengToutiaoHead.setAdapter(zifengToutiaoVPAdapter);
		} else {
			PopularizeApi pApi = new PopularizeApi();
			pApi.getAll(new OnNewsListener() {

				@Override
				public void onException(Exception exception) {

				}

				@Override
				public void onError(String error) {

				}

				@Override
				public void onComplete(String result) {
					if (!result.equals("error")) {
						zifengPopList = JsonUtil.parsePopularize(result);
						Message msg = new Message();
						msg.arg1 = HandMsgConstant.SUCCESS_POP;
						zifengHandler.sendMessage(msg);
					}
				}
			});
		}
		//防止此页面被多次加载 先remove再加入消息队列
		//不然会在消息队列加入多个run  图片变换跟飞一样
		zifengHandler.removeCallbacks(run);
		zifengHandler.postDelayed(run, 5000);
	}

	Runnable run = new Runnable() {
		
		@Override
		public void run() {
			Message msg = new Message();
			msg.arg1 = HandMsgConstant.VP_TO_CurrentItem;
			zifengHandler.sendMessage(msg);
			zifengHandler.postDelayed(run, 5000);
		}
	};
	
	// 新的内容
	private List<NewsImage> zifengNewlist;

	class RefreshNewsListener implements OnNewsListener {
		Intent intent;

		@Override
		public void onComplete(String result) {
			if ("error".equals(result)) {
				Message msg = new Message();
				msg.arg1 = HandMsgConstant.NO_REFRESH;
				zifengHandler.sendMessage(msg);
			} else {
				zifengNewlist = JsonUtil.parseJsonArray(result);
				// 暂时的list 用来把老新闻与新新闻排列组合
				List<NewsImage> temporary = new ArrayList<NewsImage>();
				for (int i = 0; i < zifengNewlist.size(); i++) {
					// 有几个新内容 从当前老内容集合 最尾巴处(最老的 ，老集合是降序 id)删除几个
					System.out.println("删除掉了___________"
							+ zifengNewsList.get(zifengNewsList.size() - 1 - i));
					zifengNewsList.remove(zifengNewsList.size() - 1 - i);
					// 通知把新内容添加到老集合 刷新的内容也是降序 id 需要从zifengNewsList的第0个插
				}
				// 先把新内容加进去
				for (NewsImage newsImage : zifengNewlist) {
					System.out.println("临时集合添加新内容____________:"
							+ newsImage.getTitle());
					temporary.add(newsImage);
				}
				// 再添加老内容
				for (NewsImage newsImage : zifengNewsList) {
					System.out.println("临时集合添加老内容_________"
							+ newsImage.getTitle());
					temporary.add(newsImage);
				}
				zifengNewsList = temporary;
//				soft = new SoftReference<List<NewsImage>>(zifengNewsList);
				Message msg = new Message();
				msg.arg1 = HandMsgConstant.REFRESH_SUCCESS;
				zifengHandler.sendMessage(msg);
			}
			intent = new Intent("com.zifeng.news.refreshend");
			// 发广播通知更新完成
			getActivity().sendBroadcast(intent);
		}

		@Override
		public void onError(String error) {
			Toast.makeText(getActivity(), error, 0).show();
		}

		@Override
		public void onException(Exception exception) {
			exception.printStackTrace();
		}
	}

	class Receiver extends BroadcastReceiver {
		private NewsApi zifengApi;

		@Override
		public void onReceive(Context context, Intent intent) {
			// 刷新
			if (intent.getAction().equals("com.zifeng.refresh")) {
				System.out.println("刷新");
				zifengApi = new NewsApi();
				// 拿最前面的去对比 新的在最前面
				zifengApi.refresh(zifengNewsList.get(0).getId(), new RefreshNewsListener());
			}

			else if (intent.getAction().equals(
					"com.zifeng.imageservice.pop.success")) {
				/*
				 * View head = View.inflate(getActivity(),
				 * R.layout.toutiao_head_item, null); zifengPagerlist.remove(0);
				 * zifengPagerlist.add(0,head);
				 */
				// zifengToutiaoVPAdapter.notifyDataSetChanged();
				int position = intent.getIntExtra("id", 0);
				ImageView iv_toutiao_head = (ImageView) zifengPagerlist.get(position)
						.findViewById(R.id.iv_toutiao_head);
				iv_toutiao_head.setBackgroundDrawable(zifengPopList.get(position)
						.getImgDrawable());
			}
			// 上啦加载更多
			else if (intent.getAction().equals("com.zifeng.news.add")) {
				System.out.println("加载更多");
				zifengApi = new NewsApi();
				zifengApi.add(zifengNewsList.get(zifengNewsList.size() - 1).getId(),
						new AddNewsListener());
			}
			// 正常加载图片./...
			else {
				// NewsImage news = (NewsImage)
				// intent.getSerializableExtra("news");
				// Log.i("toutiaoFragment:", "接收到广播:"+news);
				System.out.println("接到广播 图片加载完成");
				Message msg = new Message();
				msg.arg1 = HandMsgConstant.IMAGE_LOAD_COMPLETE;
				zifengHandler.sendMessage(msg);
			}
		}

	}



	private void setNewsAdapter() {
		System.out.println("setNewsAdapter-------------------");
		zifengNewsToutiaoLvAdapter = new NewsToutiaoLvAdapter(getActivity(), zifengNewsList);
		zfiengNewsToutiao.setAdapter(zifengNewsToutiaoLvAdapter);
		ImageUtil.loadImage(getActivity(), zifengNewsList);
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("进入");
		View view = inflater.inflate(R.layout.toutiao, container, false);
		zifengNewsLoading = (LinearLayout) view
				.findViewById(R.id.ll_news_loading);
		zfiengNewsToutiao = (RefreshListView) view.findViewById(R.id.lv_news_toutiao);
		zfiengNewsToutiao.setDivider(getResources().getDrawable(
				R.drawable.base_list_divider_drawable));
		// 隐藏滑动条
		zfiengNewsToutiao.setVerticalScrollBarEnabled(false);
		addHeadView();
		// 无网络显示
		zifengNoNetwork = (ImageView) view.findViewById(R.id.iv_no_network);
		// 软引用中没数据显示加载界面
		if (zifengSoft.get().isEmpty() || zifengSoft.get() == null) {
			zifengNoNetwork.setVisibility(View.VISIBLE);
			zfiengNewsToutiao.setVisibility(View.GONE);
			// 如果有网络 直接获取数据
			if (NetWrodUtil.isNetworking(getActivity())) {
				zifengNewsLoading.setVisibility(View.VISIBLE);
				zifengNoNetwork.setVisibility(View.GONE);
				// 加载
				zifengApi.catchNews(new MyOnNewsListener());
			} else {
				System.out.println("没有网络----------");
				zifengNoNetwork.setVisibility(View.VISIBLE);
				zfiengNewsToutiao.setVisibility(View.GONE);
			}
		} else {
			System.out.println("soft~~~");
			zifengNewsList = zifengSoft.get();
			setNewsAdapter();
		}
		zifengNoNetwork.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果当前网络关闭 就提示
				if (!NetWrodUtil.isNetworking(getActivity())) {
					Toast.makeText(getActivity(), "网络不给力", 1).show();
				} else {
					zifengNewsLoading.setVisibility(View.VISIBLE);
					zifengNoNetwork.setVisibility(View.GONE);
					// 加载
					zifengApi.catchNews(new MyOnNewsListener());
				}
			}
		});
		zfiengNewsToutiao.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println(position);
				//两个头不响应点击 最后zifengNewsList.size()+1是加上了两个头(下标)再加一加上一个地板 都不响应
				if (position != 0 && position != 1 && position != zifengNewsList.size() + 2) {
					NewsImage newsImage = zifengNewsList.get(position - 2);
					
					Message msg = new Message();
					msg.arg1 = HandMsgConstant.JUMP;
					msg.obj = newsImage;
					zifengHandler.sendMessage(msg );
				}
			}
		});
		return view;
	}

	

	private class MyOnNewsListener implements OnNewsListener {
		@Override
		public void onComplete(String result) {
			zifengNewsList = JsonUtil.parseJsonArray(result);
//			zifengSoft = new SoftReference<List<NewsImage>>(zifengNewsList);
			Message msg = new Message();
			msg.arg1 = HandMsgConstant.SUCCESS;
			zifengHandler.sendMessage(msg);
		}

		@Override
		public void onError(String error) {
			Message msg = new Message();
			msg.arg1 = HandMsgConstant.ERROR;
			msg.obj = error;
			zifengHandler.sendMessage(msg);
		}

		@Override
		public void onException(Exception exception) {
			exception.printStackTrace();
		}

	}

	@Override
	public void onStop() {
		super.onStop();
		getActivity().unregisterReceiver(zifengReceiver);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		zifengSoft = new SoftReference<List<NewsImage>>(zifengNewsList);
		zifengSoftPop = new SoftReference<List<Popularize>>(zifengPopList);
	}

	@Override
	public void onResume() {
		// 注册广播 分别监听 加载、下拉刷新、上啦加载更多
		IntentFilter filter = new IntentFilter("com.zifeng.imageservice.success");
		filter.addAction("com.zifeng.refresh");
		filter.addAction("com.zifeng.news.add");
		filter.addAction("com.zifeng.imageservice.pop.success");
		zifengReceiver = new Receiver();
		getActivity().registerReceiver(zifengReceiver, filter);
		super.onResume();
	}

}
