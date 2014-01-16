package com.simulation.neteasy;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.simulation.neteasy.fragment.AppFragment;
import com.simulation.neteasy.inter.AppPagerEnableListener;
import com.zifeng.wangyi.R;

/**
 * 
 * @author 紫枫
 *
 */
public class RecommendActivity extends FragmentActivity implements
		AppPagerEnableListener {

	private TextView zifengSelect;
	private ViewPager zifengPager;
	private AppFragment zifengApp;
	private RadioGroup zifengRadioGroup;
	private int zifengWidth;
	
	int zifengFrom;
	int zifengTo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommend);
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		zifengApp = new AppFragment();
		transaction.add(R.id.ll_recommend_text, zifengApp);
		transaction.commit();
		zifengSelect = (TextView) findViewById(R.id.v_select);
		zifengRadioGroup = (RadioGroup) findViewById(R.id.rg_app);
		zifengWidth = getWindowManager().getDefaultDisplay().getWidth() / 3;
		zifengSelect.setWidth(zifengWidth);
	}

	

	public void move(int from, int to) {
		TranslateAnimation animation = new TranslateAnimation(from, to, 0, 0);
		animation.setFillAfter(true);
		zifengSelect.startAnimation(animation);
	}

	@Override
	protected void onResume() {
		super.onResume();
		zifengRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_1:
					// 是第一个反正to是0
					// 以后要移动 那另一个动画的from也就是0
					zifengPager.setCurrentItem(0);
					zifengTo = 0;
					move(zifengFrom, zifengTo);
					zifengFrom = 0;
					break;
				case R.id.rb_2:
					zifengPager.setCurrentItem(1);
					zifengTo = zifengWidth;
					move(zifengFrom, zifengTo);
					zifengFrom = zifengWidth;
					// zifengTo = zifengWidth;
					break;
				case R.id.rb_3:
					zifengTo = 2 * zifengWidth;
					zifengPager.setCurrentItem(2);
					move(zifengFrom, zifengTo);
					zifengFrom = 2 * zifengWidth;
					break;
				default:
					break;
				}
			}
		});
	}

	public void back(View v) {
		finish();
		overridePendingTransition(0, R.anim.out_news_text);
	}

	//接口回调 ViewPager初始化完成获得Viewpager
	@Override
	public void OnPagerEnable(ViewPager vp) {
		zifengPager = vp;
		vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				zifengRadioGroup.check(position);
				switch (position) {
				case 0:
					zifengPager.setCurrentItem(0);
					zifengTo = 0;
					move(zifengFrom, zifengTo);
					zifengFrom = 0;
					break;
				case 1:
					zifengPager.setCurrentItem(1);
					zifengTo = zifengWidth;
					move(zifengFrom, zifengTo);
					zifengFrom = zifengWidth;
					break;
				case 2:
					zifengTo = 2 * zifengWidth;
					zifengPager.setCurrentItem(2);
					move(zifengFrom, zifengTo);
					zifengFrom = 2 * zifengWidth;
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
	}
}
