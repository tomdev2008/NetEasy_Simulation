package com.simulation.neteasy.view;

import com.zifeng.wangyi.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @author 紫枫
 *
 */
public class ReListView extends ListView implements OnScrollListener {



	private final static int CAN_REFRESE = 0;
	private final static int CANT_REFRESE = 1;
	private final static int REFRESEING = 2;
	// private final static int DONE = 3;
	private View zifengHead;
	private TextView zifengTvHitn;
	private ImageView zifengIvHitn;
	private ProgressBar zifengPbRefresh;
	private RotateAnimation zifengAnimation;
	private RotateAnimation zifengReverseAnimation;
	private int zifengState;
	private Context zifengContext;

	public ReListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public class Receiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("接收广播");
			// 接收广播 可以取消更新状态
			zifengState = CAN_REFRESE;
			zifengHead.setPadding(0, -headHeight, 0, 0);
			zifengPbRefresh.setVisibility(View.GONE);
			zifengTvHitn.setText("下拉刷新");
			zifengIvHitn.setVisibility(View.VISIBLE);
			zifengIvHitn.clearAnimation();
			zifengHead.invalidate();
		}

	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		// 从界面解绑 取消广播注册
		zifengContext.unregisterReceiver(receiver);
	}

	private void init(Context context) {
		setOnScrollListener(this);
		this.zifengContext = context;
		receiver = new Receiver();
		IntentFilter filter = new IntentFilter("com.zifeng.news.refreshend");
		context.registerReceiver(receiver, filter);
		LayoutInflater inflater = LayoutInflater.from(context);

		zifengHead = inflater.inflate(R.layout.news_toutiao_head, null);
		zifengTvHitn = (TextView) zifengHead.findViewById(R.id.tv_news_state);
		zifengIvHitn = (ImageView) zifengHead.findViewById(R.id.iv_news_state);
		zifengPbRefresh = (ProgressBar) zifengHead.findViewById(R.id.pb_refresh);
		zifengPbRefresh.setVisibility(View.GONE);
		// measureView(zifengHead);
		// headHeight = zifengHead.getMeasuredHeight();
		headHeight = 60;
		zifengHead.setPadding(0, -headHeight, 0, 0);
		addHeaderView(zifengHead);

		// 动画效果 刷新提示图片的
		zifengAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		zifengAnimation.setInterpolator(new LinearInterpolator());
		zifengAnimation.setDuration(250);
		zifengAnimation.setFillAfter(true);
		zifengReverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		zifengReverseAnimation.setInterpolator(new LinearInterpolator());
		zifengReverseAnimation.setDuration(250);
		zifengReverseAnimation.setFillAfter(true);

		View footer = inflater.inflate(R.layout.news_toutiao_footer, null);
		addFooterView(footer);
		

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

	private void measureView(View child) {
		android.view.ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	private int rawY;
	private int headHeight;
	private Receiver receiver;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (zifengState == REFRESEING) {
				// 刷新状态 拦截点击事件
				return false;
			}
			rawY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_UP:
			int y = (int) ev.getY();
			// 刷新
			if (getFirstVisiblePosition() == 0) {
				if (y - rawY >= headHeight / 2) {
					// 刷新时候设置head为20高度
					zifengHead.setPadding(0, 20, 0, 0);
					zifengIvHitn.setVisibility(View.GONE);
					zifengPbRefresh.setVisibility(View.VISIBLE);
					zifengTvHitn.setText("正在刷新");
					zifengState = REFRESEING;
					zifengHead.invalidate();
					Intent intent = new Intent("com.zifeng.refresh");
					zifengContext.sendBroadcast(intent);
				} else {
					zifengHead.setPadding(0, -headHeight, 0, 0);
					zifengHead.invalidate();
				}
				zifengIvHitn.clearAnimation();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			int nowY = (int) ev.getY();
			// 不处于刷新状态就能向下拉动
			if (getFirstVisiblePosition() == 0) {
				if (zifengState != REFRESEING) {
					if (nowY > rawY) {
						if ((nowY - rawY) > headHeight) {
							zifengHead.setPadding(0, headHeight, 0, 0);
							zifengHead.invalidate();
						} else {
							if ((nowY - rawY) > headHeight / 2) {
								if (zifengState == CAN_REFRESE) {
									zifengIvHitn.startAnimation(zifengAnimation);
									zifengTvHitn.setText("松开刷新");
								}
								zifengState = CANT_REFRESE;
							} else {
								if (zifengState == CANT_REFRESE) {
									zifengIvHitn.startAnimation(zifengReverseAnimation);
									zifengTvHitn.setText("下拉刷新");
								}
								zifengState = CAN_REFRESE;
							}
							if (nowY > rawY) {
								zifengHead.setPadding(0, nowY - rawY, 0, 0);
								zifengHead.invalidate();
							}
						}
					}
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

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (totalItemCount != 0) {
			if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
				Intent intent = new Intent("com.zifeng.news.add");
				zifengContext.sendBroadcast(intent);
			}
		}
	}

}
