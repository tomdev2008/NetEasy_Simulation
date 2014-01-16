package com.simulation.neteasy.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * 
 * @author 紫枫
 *
 */
public class NewsPageFragmentAdapter extends FragmentPagerAdapter {

	private List<Fragment> zifengNewsItemList;

	public NewsPageFragmentAdapter(List<Fragment> newsItemList,FragmentManager fm) {
		super(fm);
		this.zifengNewsItemList = newsItemList;
	}

	@Override
	public Fragment getItem(int position) {
		return zifengNewsItemList.get(position);
	}

	@Override
	public int getCount() {
		return zifengNewsItemList.size();
	}

}
