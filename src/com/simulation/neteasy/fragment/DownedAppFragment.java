package com.simulation.neteasy.fragment;

import com.zifeng.wangyi.R;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
/**
 * 
 * @author 紫枫
 *
 */
public class DownedAppFragment extends Fragment {

	private View zifengView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		zifengView = inflater.inflate(R.layout.allapp, null);
		ListView zifengAllApp = (ListView) zifengView.findViewById(R.id.lv_all_app);
		return zifengView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ImageView zifengAppLoad = (ImageView) zifengView.findViewById(R.id.iv_app_load);
		AnimationDrawable  zifengLoad = (AnimationDrawable) zifengAppLoad.getBackground();
		zifengLoad.start();
	}
}
