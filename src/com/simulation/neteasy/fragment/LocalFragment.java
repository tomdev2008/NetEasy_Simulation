package com.simulation.neteasy.fragment;


import com.zifeng.wangyi.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LocalFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("_______________________________local");
		View view = inflater.inflate(R.layout.local, null);
		return view;
	}
}
