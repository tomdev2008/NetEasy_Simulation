package com.simulation.neteasy.view;

import com.zifeng.wangyi.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Scroller;


/**
 * 
 * @author 紫枫
 *
 */

public class SlidingMenu extends RelativeLayout {

	private View zifengSlidingView;
	private View zifengMenuView;
	private View zifengDetailView;
	private RelativeLayout zifengBgShade; // 模块间的背景阴影
	private int zifengScreenWidth;
	private int zifengScreenHeight;
	private Context zifengContext;
	private Scroller zifengScroller;
	private VelocityTracker zifengVelocityTracker;
	private int zifengTouchSlop;
	private float zifengLastMotionX;
	private float zifengLastMotionY;
	private static final int VELOCITY = 50;
	private boolean zifengIsBeingDragged = true;
	private boolean zifengCanSlideLeft = true;
	private boolean zifengCanSlideRight = false;
	private boolean zifengHasClickLeft = false;
	private boolean zifengHasClickRight = false;
	//默认允许左滑 不允许右滑
	private boolean zifengCanSlideLeftS = true;
	private boolean zifengCanSlideRightS = false;

	public SlidingMenu(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {

		zifengContext = context;
		// 创建一个Viewgroup RelativeLayout
		zifengBgShade = new RelativeLayout(context);
		zifengScroller = new Scroller(getContext());

		zifengTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		// 得到windowManager 以得到屏幕显示的一些信息和
		// Context.getSystemService(Context.WINDOW_SERVICE)一样
		WindowManager windowManager = /*
									 * ((Activity) context).getWindow()
									 * .getWindowManager();
									 */(WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		// 得到显示内容的宽、高
		// 和
		// DisplayMetrics metrics = getResources().getDisplayMetrics();
		// System.out.println(metrics.widthPixels); 一样
		Display display = windowManager.getDefaultDisplay();
		// System.out.println(display.getWidth());
		zifengScreenWidth = display.getWidth();
		zifengScreenHeight = display.getHeight();
		// 创建布局参数 这里是Relativelayout、相对布局的 设置它的宽、高以及设置位置为父布局的中间
		LayoutParams bgParams = new LayoutParams(zifengScreenWidth, zifengScreenHeight);
		bgParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		// 把这个布局的属性设置到新建的绝对布局
		zifengBgShade.setLayoutParams(bgParams);

	}
	
	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	// 添加view的方法
	public void addViews(View left, View center, View right) {
		setLeftView(left);
		setRightView(right);
		setCenterView(center);
	}

	// 添加到左边的view
	public void setLeftView(View view) {
		// 设置参数
		LayoutParams behindParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT);
		// 把这个布局以设置的参数添加到自己重写的此view
		addView(view, behindParams);
		// 记录这个view
		zifengMenuView = view;
	}

	// 右边的view
	public void setRightView(View view) {
		LayoutParams behindParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT);
		// 靠右显示
		behindParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		addView(view, behindParams);
		zifengDetailView = view;
	}

	// 中间的
	public void setCenterView(View view) {
		// 以屏幕大小建立一个布局参数
		LayoutParams bgParams = new LayoutParams(zifengScreenWidth, zifengScreenHeight);
		// 居中
		bgParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		// 新建一个view
		View bgShadeContent = new View(zifengContext);
		// 设置这个view的背景图片 阴影
		bgShadeContent.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.shade_bg));
		// 把这个view添加到init()中建立的RelativeLayout——背景
		zifengBgShade.addView(bgShadeContent, bgParams);
		// 把在Init建立的RelativeLayout加入到SlidingMenu
		addView(zifengBgShade, bgParams);
		// 上面的就是给自己重写的View里面再加入一个view 并设置背景添加一个这样的布局目的就是加背景 也就是阴影效果

		LayoutParams aboveParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		addView(view, aboveParams);
		zifengSlidingView = view;
		// 改变这个view的Z轴为(树状) 所以它会在和它同级view的前面 其实没什么用
		zifengSlidingView.bringToFront();
	}

	// 滑动这个view，并且会通知更新
	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		// 这个方法只能在绑定了一个屏幕(activity)后 非UI线程通知更新view
		postInvalidate();
	}

	// 滑动的时候调用 （move事件）
	@Override
	public void computeScroll() {
		// 如果滑动没结束
		if (!zifengScroller.isFinished()) {
			if (zifengScroller.computeScrollOffset()) {
				// 返回view显示出来内容 的左边缘
				int oldX = zifengSlidingView.getScrollX();
				// 最上面的
				int oldY = zifengSlidingView.getScrollY();
				// 当前的X Y
				int x = zifengScroller.getCurrX();
				int y = zifengScroller.getCurrY();
				if (oldX != x || oldY != y) {
					if (zifengSlidingView != null) {
						zifengSlidingView.scrollTo(x, y);
						if (x < 0){
							//这种情况是显示出左边的页面 那么作为和内容一样大小的阴影背景
							//这时候当前的X为-
							//就要向左移动才能显示出来
							zifengBgShade.scrollTo(x + 10, y);// 背景阴影右偏
						}
						else
							zifengBgShade.scrollTo(x - 10, y);// 背景阴影左偏
					}
				}
				invalidate();
			}
		}
	}



	//设置允许滑动
	public void setCanSliding(boolean left, boolean right) {
		zifengCanSlideLeftS = left;
		zifengCanSlideRightS = right;
	}

	/* 拦截touch事件 */
//	事件拦截器
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		final int action = ev.getAction();
		//触发事件的x、y
		final float x = ev.getX();
		final float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			//按下记录x、y
			zifengLastMotionX = x;
			zifengLastMotionY = y;
			//没消耗 不拦截
			zifengIsBeingDragged = false;
			if (zifengCanSlideLeftS) {
				//设置左边为可见右边为不可见
				//内容被放在最top 底部两个View就是并排 如果不设置 就可能出现 向左滑也能看见右边的内容
				zifengMenuView.setVisibility(View.VISIBLE);
				zifengDetailView.setVisibility(View.INVISIBLE);
			}
			if (zifengCanSlideRightS) {
				zifengMenuView.setVisibility(View.INVISIBLE);
				zifengDetailView.setVisibility(View.VISIBLE);
			}
			break;

		case MotionEvent.ACTION_MOVE:
			//滑动了多少的X
			final float dx = x - zifengLastMotionX;
			final float xDiff = Math.abs(dx);
			//y
			final float yDiff = Math.abs(y - zifengLastMotionY);
			//第一个条件不懂 输出是16   滑动的X大于y
			if (xDiff > zifengTouchSlop && xDiff > yDiff) {
				if (zifengCanSlideLeftS) {
					float oldScrollX = zifengSlidingView.getScrollX();
					if (oldScrollX < 0) {
						//消耗了 拦截
						zifengIsBeingDragged = true;
						zifengLastMotionX = x;
					} else {
						if (dx > 0) {
							zifengIsBeingDragged = true;
							zifengLastMotionX = x;
						}
					}

				} else if (zifengCanSlideRightS) {
					float oldScrollX = zifengSlidingView.getScrollX();
					if (oldScrollX > 0) {
						zifengIsBeingDragged = true;
						zifengLastMotionX = x;
					} else {
						if (dx < 0) {
							zifengIsBeingDragged = true;
							zifengLastMotionX = x;
						}
					}
				}

			}
			break;

		}
		return zifengIsBeingDragged;
	}

	/* 处理拦截后的touch事件 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (zifengVelocityTracker == null) {
			zifengVelocityTracker = VelocityTracker.obtain();
		}
		//追踪事件
		zifengVelocityTracker.addMovement(ev);

		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!zifengScroller.isFinished()) {
				//停止滑动
				zifengScroller.abortAnimation();
			}
			zifengLastMotionX = x;
			zifengLastMotionY = y;
			if (zifengSlidingView.getScrollX() == -getMenuViewWidth()
					&& zifengLastMotionX < getMenuViewWidth()) {
				return false;
			}

			if (zifengSlidingView.getScrollX() == getDetailViewWidth()
					&& zifengLastMotionX > getMenuViewWidth()) {
				return false;
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (zifengIsBeingDragged) {
				final float deltaX = zifengLastMotionX - x;
				zifengLastMotionX = x;
				float oldScrollX = zifengSlidingView.getScrollX();
				float scrollX = oldScrollX + deltaX;
				if (zifengCanSlideLeftS) {
					if (scrollX > 0)
						scrollX = 0;
				}
				if (zifengCanSlideRightS) {
					if (scrollX < 0)
						scrollX = 0;
				}
				if (deltaX < 0 && oldScrollX < 0) { // left view
					final float leftBound = 0;
					final float rightBound = -getMenuViewWidth();
					if (scrollX > leftBound) {
						scrollX = leftBound;
					} else if (scrollX < rightBound) {
						scrollX = rightBound;
					}
				} else if (deltaX > 0 && oldScrollX > 0) { // right view
					final float rightBound = getDetailViewWidth();
					final float leftBound = 0;
					if (scrollX < leftBound) {
						scrollX = leftBound;
					} else if (scrollX > rightBound) {
						scrollX = rightBound;
					}
				}
				if (zifengSlidingView != null) {
					zifengSlidingView.scrollTo((int) scrollX,
							zifengSlidingView.getScrollY());
					if (scrollX < 0)
						zifengBgShade.scrollTo((int) scrollX + 10,
								zifengSlidingView.getScrollY());
					else
						zifengBgShade.scrollTo((int) scrollX - 10,
								zifengSlidingView.getScrollY());
				}

			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (zifengIsBeingDragged) {
				final VelocityTracker velocityTracker = zifengVelocityTracker;
				velocityTracker.computeCurrentVelocity(100);
				float xVelocity = velocityTracker.getXVelocity();// 滑动的速度
				int oldScrollX = zifengSlidingView.getScrollX();
				int dx = 0;
				if (oldScrollX <= 0 && zifengCanSlideLeftS) {// left view
					if (xVelocity > VELOCITY) {
						dx = -getMenuViewWidth() - oldScrollX;
					} else if (xVelocity < -VELOCITY) {
						dx = -oldScrollX;
						if (zifengHasClickLeft) {
							zifengHasClickLeft = false;
							setCanSliding(zifengCanSlideLeft, zifengCanSlideRight);
						}
					} else if (oldScrollX < -getMenuViewWidth() / 2) {
						dx = -getMenuViewWidth() - oldScrollX;
					} else if (oldScrollX >= -getMenuViewWidth() / 2) {
						dx = -oldScrollX;
						if (zifengHasClickLeft) {
							zifengHasClickLeft = false;
							setCanSliding(zifengCanSlideLeft, zifengCanSlideRight);
						}
					}

				}
				if (oldScrollX >= 0 && zifengCanSlideRightS) {
					if (xVelocity < -VELOCITY) {
						dx = getDetailViewWidth() - oldScrollX;
					} else if (xVelocity > VELOCITY) {
						dx = -oldScrollX;
						if (zifengHasClickRight) {
							zifengHasClickRight = false;
							setCanSliding(zifengCanSlideLeft, zifengCanSlideRight);
						}
					} else if (oldScrollX > getDetailViewWidth() / 2) {
						dx = getDetailViewWidth() - oldScrollX;
					} else if (oldScrollX <= getDetailViewWidth() / 2) {
						dx = -oldScrollX;
						if (zifengHasClickRight) {
							zifengHasClickRight = false;
							setCanSliding(zifengCanSlideLeft, zifengCanSlideRight);
						}
					}
				}

				smoothScrollTo(dx);

			}

			break;
		}

		return true;
	}

	private int getMenuViewWidth() {
		if (zifengMenuView == null) {
			return 0;
		}
		return zifengMenuView.getWidth();
	}

	private int getDetailViewWidth() {
		if (zifengDetailView == null) {
			return 0;
		}
		return zifengDetailView.getWidth();
	}

	void smoothScrollTo(int dx) {
		int duration = 500;
		int oldScrollX = zifengSlidingView.getScrollX();
		zifengScroller.startScroll(oldScrollX, zifengSlidingView.getScrollY(), dx,
				zifengSlidingView.getScrollY(), duration);
		invalidate();
	}

	/*
	 * 显示左侧边的view
	 */
	public void showLeftView() {
		int menuWidth = zifengMenuView.getWidth();
		int oldScrollX = zifengSlidingView.getScrollX();
		if (oldScrollX == 0) {
			zifengMenuView.setVisibility(View.VISIBLE);
			zifengDetailView.setVisibility(View.INVISIBLE);
			smoothScrollTo(-menuWidth);
			zifengCanSlideLeft = zifengCanSlideLeftS;
			zifengCanSlideRight = zifengCanSlideRightS;
			zifengHasClickLeft = true;
			setCanSliding(true, false);
		} else if (oldScrollX == -menuWidth) {
			smoothScrollTo(menuWidth);
			if (zifengHasClickLeft) {
				zifengHasClickLeft = false;
				setCanSliding(zifengCanSlideLeft, zifengCanSlideRight);
			}
		}
	}

	/* 显示右侧边的view */
	public void showRightView() {
		int menuWidth = zifengDetailView.getWidth();
		int oldScrollX = zifengSlidingView.getScrollX();
		if (oldScrollX == 0) {
			zifengMenuView.setVisibility(View.INVISIBLE);
			zifengDetailView.setVisibility(View.VISIBLE);
			smoothScrollTo(menuWidth);
			zifengCanSlideLeft = zifengCanSlideLeftS;
			zifengCanSlideRight = zifengCanSlideRightS;
			zifengHasClickRight = true;
			setCanSliding(false, true);
		} else if (oldScrollX == menuWidth) {
			smoothScrollTo(-menuWidth);
			if (zifengHasClickRight) {
				zifengHasClickRight = false;
				setCanSliding(zifengCanSlideLeft, zifengCanSlideRight);
			}
		}
	}

}
