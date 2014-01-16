package com.simulation.neteasy.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.simulation.neteasy.adapter.NewsPageFragmentAdapter;
import com.simulation.neteasy.inter.MyPageChangeListener;
import com.simulation.neteasy.view.MyViewPager;
import com.simulation.neteasy.view.SlidingMenu;
import com.zifeng.wangyi.R;
/**
 * 
 * @author 紫枫
 *
 */
public class ContentNewsFragment extends Fragment {

	private MyPageChangeListener zifengPageChangeListener;
	private MyViewPager zifengContentNews;
	private List<Fragment> zifengNewsItemList;
	private SlidingMenu zifengSlidingMenu;
	private View zifengView;
	private HorizontalScrollView zifengHorizontalScrollView;
	private RadioGroup zifengRadioGroup;
	private int zifengWidth;

	public void setMyPageChangeListener(
			MyPageChangeListener zifengPageChangeListener) {
		this.zifengPageChangeListener = zifengPageChangeListener;
	}

	public void setSlidingMenu(SlidingMenu sm) {
		this.zifengSlidingMenu = sm;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//新闻模块
		zifengView = inflater.inflate(R.layout.content_news, container, false);
		zifengHorizontalScrollView = (HorizontalScrollView) zifengView
				.findViewById(R.id.hcv);
		//上部滑动导航
		zifengRadioGroup = (RadioGroup) zifengView.findViewById(R.id.rg);
		//内容Viewpager
		zifengContentNews = (MyViewPager) zifengView.findViewById(R.id.vp_content_news);
		//viewPager的条目
		zifengNewsItemList = new ArrayList<Fragment>();
		ToutiaoFragment toutiao = new ToutiaoFragment();
		TiyuFragment tiyu = new TiyuFragment();
		YuleFragment yule = new YuleFragment();
		CaijinFragment caijin = new CaijinFragment();
		JunshiFragment junshi = new JunshiFragment();
		zifengNewsItemList.add(toutiao);
		zifengNewsItemList.add(tiyu);
		zifengNewsItemList.add(yule);
		zifengNewsItemList.add(caijin);
		zifengNewsItemList.add(junshi);
		zifengContentNews.setAdapter(new NewsPageFragmentAdapter(zifengNewsItemList,
				getFragmentManager()));
		//水平滑动的上部导航 是单选框
		RadioButton zifengRbOne = (RadioButton) zifengView.findViewById(R.id.rb_1);
		RadioButton zifengRbTwo = (RadioButton) zifengView.findViewById(R.id.rb_2);
		RadioButton zifengRbThree = (RadioButton) zifengView.findViewById(R.id.rb_3);
		RadioButton zifengRbFour = (RadioButton) zifengView.findViewById(R.id.rb_4);
		RadioButton zifengRbFive = (RadioButton) zifengView.findViewById(R.id.rb_5);
		ImageView zifengContentTitle = (ImageView) zifengView.findViewById(R.id.iv_content_title);
		//得到显示的一些属性
		Display zifengDisplay = getActivity().getWindowManager().getDefaultDisplay();
		//显示4个导航目 屏幕宽度除4就是每个导航目的宽
		zifengWidth = zifengDisplay.getWidth()/4;
		zifengWidth = zifengWidth - zifengContentTitle.getWidth();
		zifengRbOne.setWidth(zifengWidth);
		zifengRbTwo.setWidth(zifengWidth);
		zifengRbThree.setWidth(zifengWidth);
		zifengRbFour.setWidth(zifengWidth);
		zifengRbFive.setWidth(zifengWidth);
		//设置单选组的选择监听
		zifengRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_1:
					//第1个radioButton被点击设置ViewPager展示第1页
					zifengContentNews.setCurrentItem(0);
					//定位水平滑动控件
					zifengHorizontalScrollView.smoothScrollTo(0, 0);
					break;
				case R.id.rb_2:
					zifengContentNews.setCurrentItem(1);
					zifengHorizontalScrollView.smoothScrollTo(0, 0);
					break;
				case R.id.rb_3:
					zifengContentNews.setCurrentItem(2);
					//定位水平滑动控件 第三个要滑动zifengWidth的宽度 这个滑动的按照水平整个长度来的 包括超出屏幕的部分
					zifengHorizontalScrollView.smoothScrollTo(zifengWidth, 0);
					break;
				case R.id.rb_4:
					zifengContentNews.setCurrentItem(3);
					zifengHorizontalScrollView.smoothScrollTo(2*zifengWidth, 0);
					break;
				case R.id.rb_5:
					zifengContentNews.setCurrentItem(4);
					break;

				default:
					break;
				}
			}
		});
		ImageView iv_news2left = (ImageView) zifengView
				.findViewById(R.id.iv_news2left);
		//展示左导航
		iv_news2left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				zifengSlidingMenu.showLeftView();
			}
		});
		ImageView iv_news2right = (ImageView) zifengView
				.findViewById(R.id.iv_news2right);
		//显示右导航
		iv_news2right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				zifengSlidingMenu.showRightView();
			}
		});
		zifengContentNews.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				zifengPageChangeListener.onPageSelected(position);
				switch (position) {
				case 0:
					//改变Viewpager页数定位RadioButton选中哪一个
					zifengRadioGroup.check(R.id.rb_1);
					zifengHorizontalScrollView.smoothScrollTo(0, 0);
					break;
				case 1:
					zifengRadioGroup.check(R.id.rb_2);
					zifengHorizontalScrollView.smoothScrollTo(0, 0);
					break;
				case 2:
					zifengRadioGroup.check(R.id.rb_3);
					zifengHorizontalScrollView.smoothScrollTo(zifengWidth, 0);
					break;
				case 3:
					zifengRadioGroup.check(R.id.rb_4);
					zifengHorizontalScrollView.smoothScrollTo(2*zifengWidth, 0);
					break;
				case 4:
					zifengRadioGroup.check(R.id.rb_5);
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		return zifengView;
	}

	
	


	public boolean isFirst() {
		if (zifengContentNews.getCurrentItem() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEnd() {
		if (zifengContentNews.getCurrentItem() == zifengNewsItemList.size() - 1) {
			return true;
		} else {
			return false;
		}
	}
	
	
	@Override
	public void onDestroy() {
		System.out.println("销毁这个");
		super.onDestroy();
	}

}
