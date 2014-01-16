package com.simulation.neteasy;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.simulation.neteasy.fragment.ContentNewsFragment;
import com.simulation.neteasy.fragment.LeftFragment;
import com.simulation.neteasy.fragment.RightFragment;
import com.simulation.neteasy.inter.MyPageChangeListener;
import com.simulation.neteasy.view.SlidingMenu;
import com.zifeng.wangyi.R;

/**
 * 
 * @author 紫枫
 *
 */
public class MainActivity extends FragmentActivity implements MyPageChangeListener {

	private SlidingMenu zifengSlidingMenu;
	private ContentNewsFragment zifengNews;
	private boolean zifengBack = false;
	private long zifengDownTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		zifengSlidingMenu = (SlidingMenu) findViewById(R.id.sm);
		zifengSlidingMenu.setLeftView(getLayoutInflater().inflate(R.layout.leftfragment, null));
		zifengSlidingMenu.setRightView(getLayoutInflater().inflate(R.layout.rightfragment,
				null));
		zifengSlidingMenu.setCenterView(getLayoutInflater().inflate(R.layout.contentfragment,
				null));
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		LeftFragment left = new LeftFragment();
		transaction.replace(R.id.leftFragment, left);
		RightFragment right = new RightFragment();
		transaction.replace(R.id.rightFragment, right);
		zifengNews = new ContentNewsFragment();
		transaction.replace(R.id.contentFragment, zifengNews);
		transaction.commit();
		zifengNews.setMyPageChangeListener(this);
		zifengNews.setSlidingMenu(zifengSlidingMenu);
		left.setFragment(zifengNews);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println(zifengBack);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!zifengBack) {
				Toast.makeText(this, "2秒之内，再按一次退出", 0).show();
				zifengDownTime = event.getDownTime();
				zifengBack = true;
				return true;
			} else{
				if (event.getDownTime() - zifengDownTime <= 2000) {
					finish();
				} else{
					Toast.makeText(this, "2秒之内，再按一次退出", 0).show();
					zifengDownTime = event.getDownTime();
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		//super.onSaveInstanceState(outState);
	}

	@Override
	public void onPageSelected(int position) {
		if (zifengNews.isFirst()) {
			zifengSlidingMenu.setCanSliding(true, false);
		} else if (zifengNews.isEnd()) {
			zifengSlidingMenu.setCanSliding(false, true);
		} else {
			zifengSlidingMenu.setCanSliding(false, false);
		}
	}
}

