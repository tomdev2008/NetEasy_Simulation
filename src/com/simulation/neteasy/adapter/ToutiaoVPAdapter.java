package com.simulation.neteasy.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.simulation.neteasy.domain.Popularize;
import com.zifeng.wangyi.R;
/**
 * 
 * @author 紫枫
 *
 */
public class ToutiaoVPAdapter extends PagerAdapter {

	List<View> zifengList;
	List<Popularize> zifengPopList;

	public ToutiaoVPAdapter(List<View> list, List<Popularize> popList) {
		super();
		this.zifengList = list;
		this.zifengPopList = popList;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return zifengList.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		View view = zifengList.get(position);
		ImageView zifengPopBar = (ImageView) view.findViewById(R.id.v_pop_bar);
		LayoutParams params = new LayoutParams(100, 2);
		if (position == 0) {
			params.gravity = Gravity.LEFT;
			zifengPopBar.setLayoutParams(params );
		} else if (position == 1) {
			params.gravity = Gravity.CENTER;
			zifengPopBar.setLayoutParams(params );
		} else{
			params.gravity = Gravity.RIGHT;
		}
		zifengPopBar.setLayoutParams(params);
		ImageView zifengToutiaoHead = (ImageView) view
				.findViewById(R.id.iv_toutiao_head);
		TextView zifengToutiaoHeads = (TextView) view
				.findViewById(R.id.tv_toutiao_head);
		Popularize popularize = zifengPopList.get(position);
		if (popularize.getImgDrawable() == null) {
			zifengToutiaoHead
					.setBackgroundResource(R.drawable.base_photoitem_default_icon);
		} else {
			zifengToutiaoHead.setBackgroundDrawable(popularize.getImgDrawable());
		}
		zifengToutiaoHeads.setText(popularize.getTitle());
		((ViewPager) container).addView(zifengList.get(position));
		return zifengList.get(position);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(zifengList.get(position));
	}

}
