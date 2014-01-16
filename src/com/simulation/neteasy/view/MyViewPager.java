package com.simulation.neteasy.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * 
 * @author 紫枫
 *
 */
public class MyViewPager extends ViewPager {

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * HorizontalScrollView和viewpager冲突解决 这里没用 
	 * 不清楚我这里就算不写也没冲突、、、、 写上是为了以后遇到这种问题好解决
	 */
	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof HorizontalScrollView) {
			return true;
		}
		return super.canScroll(v, checkV, dx, x, y);
	}

}
