package com.simulation.neteasy.view;

import com.zifeng.wangyi.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * 完全版下拉刷新
 * @author 紫枫
 *
 */
public class RefreshListView extends ListView implements OnScrollListener {

	private TextView zifengRefreshHitn;
	private ImageView zifengRefreshHitns;
	private ProgressBar zifengRefreshHitnPB;
	private View zifengView;
	private final static int HEAD_HEIGHT = 60;
	private RotateAnimation zifengUp;
	private RotateAnimation zifengDown;
	
	private float zifengDownY;
	private Receiver zifengReceiver;
	private float y;
	private float oldY;
	private ProgressBar zifengPbLoadAdd;
	private TextView zifengTvLoadAdd;
	private View zifengFooter;
	
	//加载更多的状态
	private int add = 0;
	// 正在刷新
	private final static int REFRESHING = 2;
	// 松开可以刷新
	private final static int LOSSEN = 1;
	// 下拉刷新
	private final static int PULL_DOWN = 0;
	// 标示当前处于什么状态
	private int state = PULL_DOWN;
	private Context context;
	//标记当前Y是否进入过超出
	private boolean isMore = false;
	
	class Receiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//接收到了刷新完成的广播
			if (intent.getAction().equals("com.zifeng.news.refreshend")) {
				state = PULL_DOWN;
				zifengRefreshHitn.setText("下拉可刷新");
				zifengRefreshHitnPB.setVisibility(View.GONE);
				zifengRefreshHitns.setVisibility(View.VISIBLE);
				zifengRefreshHitns.clearAnimation();
				zifengView.setPadding(0, -HEAD_HEIGHT, 0, 0);
				zifengView.clearAnimation();
				zifengView.invalidate();
			} else if (intent.getAction().equals("com.zifeng.menu.added")) {
				add = 0;
				System.out.println("-------------加载完成");
			} else if (intent.getAction().equals("com.zifeng.menu.nomore")) {
				add = 1;
				zifengPbLoadAdd.setVisibility(View.GONE);
				zifengTvLoadAdd.setText("暂无更多");
				zifengFooter.invalidate();
			}
		}
		
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 分发事件 默认总是true 拦截器是不拦截的 分发器也会分发 但是在分发到item上？？？
		// （默认）不能作用在Item上，但是能作用在listView 的headView的Viewpager的Item上，也能作用在headViewpager上,但是viewpager就是不滑
		// 分发事件注册 如果是true 就是child （item、viewpager等 listView的child）不希望list被拦截
		if (ev.getY() <= 100) {
			requestDisallowInterceptTouchEvent(true);
		}
		/*boolean dis = super.dispatchTouchEvent(ev);
		if (dis) {
			requestDisallowInterceptTouchEvent(true);
		}*/
		
		return super.dispatchTouchEvent(ev);
	}
	//从window解绑
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		//取消注册
		context.unregisterReceiver(zifengReceiver);
	}
	
	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		//接收刷新完成的广播
		IntentFilter filter = new IntentFilter("com.zifeng.news.refreshend");
		//这个是接收加载完成
		filter.addAction("com.zifeng.menu.added");
		//没有更多
		filter.addAction("com.zifeng.menu.nomore");
		zifengReceiver = new Receiver();
		context.registerReceiver(zifengReceiver, filter );
		// 把刷新提示的vIEW加载进来
		zifengView = View.inflate(context, R.layout.news_toutiao_head, null);
		//上啦加载更多的view
		zifengFooter = View.inflate(context, R.layout.news_toutiao_footer, null);
		//加载更多进度条
		zifengPbLoadAdd = (ProgressBar) zifengFooter.findViewById(R.id.pb_load_add);
		//加载更多提示框
		zifengTvLoadAdd = (TextView) zifengFooter.findViewById(R.id.tv_load_add);
		
		zifengRefreshHitn = (TextView) zifengView.findViewById(R.id.tv_news_state);
		zifengRefreshHitns = (ImageView) zifengView
				.findViewById(R.id.iv_news_state);
		zifengRefreshHitnPB = (ProgressBar) zifengView
				.findViewById(R.id.pb_refresh);
		// 把这个View加入到ListView的头部
		addHeaderView(zifengView);
		// 设置内边距
		zifengView.setPadding(0, -HEAD_HEIGHT, 0, 0);
		// 松开可以刷新
		zifengUp = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF,
				0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		zifengUp.setDuration(500);
		zifengUp.setFillAfter(true);
		// 下拉可以刷新
		zifengDown = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF,
				0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		zifengDown.setFillAfter(true);
		zifengDown.setDuration(500);
		
		addFooterView(zifengFooter);
		setOnScrollListener(this);
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 如果处于刷新状态 就不允许操作ListView
			if (state == REFRESHING) {
				return false;
			}
			// 记录zifengDown下去的Y坐标
			zifengDownY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			// 当前可见的条目是第一个 0
			if (getFirstVisiblePosition() == 0) {
				if (!isMore) {
					// 拉动的距离不大于刷新的View才让它动
					if (ev.getY() - zifengDownY < HEAD_HEIGHT) {
							// 拉动的Y轴距离 大于等于 刷新的View的一半
							if (ev.getY() - zifengDownY >= HEAD_HEIGHT / 2) {
								// 当前状态处于下拉可以刷新状态
								if (state == PULL_DOWN) {
									//变为松开可以刷新
									zifengRefreshHitns.startAnimation(zifengUp);
									zifengRefreshHitn.setText("松开可刷新");
									state = LOSSEN;
								}
							} else{
								if (state == LOSSEN) {
									zifengRefreshHitns.startAnimation(zifengDown);
									zifengRefreshHitn.setText("下拉刷新");
									state = PULL_DOWN;
								}
							}
							float glide = ev.getY() - zifengDownY;
							// 让刷新的View根据滑动的距离调整内边剧
							zifengView.setPadding(0, (int) glide, 0, 0);
							// 更新View
							zifengView.invalidate();
					} 
					//拉动的距离一旦要超过HEAD_HEIGHT
					else if (ev.getY() - zifengDownY >= HEAD_HEIGHT) {
						isMore = true;
					}  
				} else{
					//超出过 开始进入另一模式
					// 马上记录
					y = ev.getY();
					//如果上一次比当前大 就是开始向上滑了
					if (y < oldY) {
						//与上次相比滑动了多少 
						float padding= oldY - y;
						//刷新的界面还能够看见
						if (zifengView.getPaddingTop() >= -HEAD_HEIGHT) {
							//开搞 切换状态
							if (zifengView.getPaddingTop() >= HEAD_HEIGHT / 2) {
								
							} else{
								if (state == LOSSEN) {
									zifengRefreshHitns.startAnimation(zifengDown);
									zifengRefreshHitn.setText("下拉刷新");
									state = PULL_DOWN;
								}
							}
							// 得到当前的PaddingTop 让他滑动 padding的距离
							zifengView.setPadding(0, (int) (zifengView.getPaddingTop() - padding), 0, 0);
							zifengView.invalidate();
						}
					} 
					//不然就是向下滑
					else{
						if (zifengView.getPaddingTop() >= - HEAD_HEIGHT) {
							//如果处于下拉状态 要变为松开可刷新
							if (state == PULL_DOWN) {
								zifengRefreshHitns.startAnimation(zifengUp);
								zifengRefreshHitn.setText("松开可刷新");
								state = LOSSEN;
							}
						}
					}
					//事件结束前再次记录 此记录就是上一次的Y
					oldY = ev.getY();
				
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			//管你怎样 重置
			y = 0 ;
			oldY = 0;
			isMore = false;
			//第一个可见的时候
			if (getFirstVisiblePosition() == 0) {
				//需要刷新
				if (state == LOSSEN) {
					//显示进度条 隐藏箭头 把状态变为正在刷新
					zifengRefreshHitnPB.setVisibility(View.VISIBLE);
					zifengRefreshHitns.clearAnimation();
					zifengRefreshHitns.setVisibility(View.INVISIBLE);
					zifengRefreshHitn.setText("正在刷新");
					state = REFRESHING;
					//处于刷新状态 让这个View高度显示为 HEAD_HEIGHT/2
					zifengView.setPadding(0, HEAD_HEIGHT/2, 0, 0);
					zifengView.invalidate();
					Intent intent = new Intent("com.zifeng.refresh");
					context.sendBroadcast(intent);
				}else if (state == PULL_DOWN) {
					//不需要刷新回到初始状态
					zifengView.setPadding(0, -HEAD_HEIGHT, 0, 0);
					zifengView.clearAnimation();
					zifengView.invalidate();
					setSelection(0);
				}
			}
			
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	//int firstVisibleItem,int visibleItemCount, int totalItemCount
	//当前第一个可见的条目的下标(算上了头的) ,当前可见的条目的总数，//这个listView拥有的所有条目的总数
	//这里如果没有更多条目就不处理了
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
			if (totalItemCount != 0) {
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					System.out.println("--------------add-----------");
					if (add == 0) {
						Intent intent = new Intent("com.zifeng.news.add");
						context.sendBroadcast(intent);
						add = 1;
					}
				}
			}
	}
	
}
