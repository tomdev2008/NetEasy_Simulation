package com.simulation.neteasy.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.simulation.neteasy.adapter.LeftAdapter;
import com.zifeng.wangyi.R;
/**
 * 
 * @author 紫枫
 *
 */
public class LeftFragment extends Fragment {

	private int zifengP;
	private FragmentManager zifengManager;
	private ContentNewsFragment zifengNews;
	private LocalFragment zifengLocal = new LocalFragment();;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left, container, false);
		ListView zifengLvLeft = (ListView) view.findViewById(R.id.lv_left);
		List<Integer> zifengLeftList = new ArrayList<Integer>();
		zifengLeftList.add(R.drawable.biz_navigation_tab_news_pressed);
		zifengLeftList.add(R.drawable.biz_navigation_tab_local_news_pressed);
		zifengLeftList.add(R.drawable.biz_navigation_tab_ties_pressed);
		zifengLeftList.add(R.drawable.biz_navigation_tab_pics_pressed);
		zifengLeftList.add(R.drawable.biz_navigation_tab_ugc_pressed);
		zifengLeftList.add(R.drawable.biz_navigation_tab_vote_pressed);
		zifengLeftList.add(R.drawable.biz_navigation_tab_micro_pressed);
		LeftAdapter leftAdapter = new LeftAdapter(getActivity(), zifengLeftList);
		zifengLvLeft.setAdapter(leftAdapter);
		zifengManager = getActivity().getSupportFragmentManager();
		zifengLvLeft.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FragmentTransaction transaction = zifengManager.beginTransaction();
				// view.setSelected(true);
				// 不使用选择器.  实在没办法了。。。想默认第一行select zifengLvLeft.selection(0) 它不根据选择器变色
				parent.getChildAt(zifengP).setBackgroundResource(R.drawable.leftbg);
				view.setBackgroundResource(R.drawable.biz_navigation_tab_bg_pressed);
				zifengP = position;
				//隐藏首页新闻 把其他模块加入  当点新闻就把所有其他模块移除
				switch (position) {
				case 0:
					//隐藏
					/*transaction.hide(zifengLocal);*/
					//展示
					transaction.show(zifengNews);
					transaction.remove(zifengLocal);
					/*transaction.detach(zifengLocal);*/
					transaction.commit();
					break;
				case 1:
					transaction.hide(zifengNews);
					transaction.add(R.id.contentFragment, zifengLocal);
					transaction.commit();
					break;

				default:
					break;
				}
				
			}
		});
		return view;
	}
	//通过setter方法找到fragment
	public void setFragment(ContentNewsFragment zifengNews){
		this.zifengNews = zifengNews;
	}
}
