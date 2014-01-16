package com.simulation.neteasy.fragment;

import java.util.ArrayList;
import java.util.List;

import com.simulation.neteasy.inter.AppPagerEnableListener;
import com.zifeng.wangyi.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 
 * @author 紫枫
 *
 */
public class AppFragment extends Fragment {

	private List<Fragment> zifengList;
	private ViewPager zifengAppInfo;
	private AppPagerEnableListener zifengListener;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		zifengListener = (AppPagerEnableListener)getActivity();
		View zifengView = inflater.inflate(R.layout.appinfo, null);
		zifengAppInfo = (ViewPager) zifengView.findViewById(R.id.vp_app_info);
		zifengList = new ArrayList<Fragment>();
		AllAppFragment allApp = new AllAppFragment();
		DownedAppFragment downApp = new DownedAppFragment();
		DownLoadingFragment downIngApp = new DownLoadingFragment();
		zifengList.add(allApp);
		zifengList.add(downIngApp);
		zifengList.add(downApp);
		zifengAppInfo.setAdapter(new MyAppVpAdapter(getFragmentManager()));
		zifengListener.OnPagerEnable(zifengAppInfo);
		return zifengView;
	}
	
	
	class MyAppVpAdapter extends FragmentPagerAdapter{
		public MyAppVpAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int position) {
			return zifengList.get(position);
		}
		@Override
		public int getCount() {
			return zifengList.size();
		}
		
	}
}
