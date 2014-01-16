package com.simulation.neteasy;

import java.util.ArrayList;
import java.util.List;

import com.zifeng.wangyi.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * 
 * @author 紫枫
 *
 */
public class AbstructActivity extends Activity {

	private List<Integer> zifengData;
	private View zifengAbstructItem0;
	private View zifengAbstructItem1;
	private List<View> zifengViewList;
	private SharedPreferences zifengSharedPreferences;
	private ViewPager zifengAbstruct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.abstruct);
		zifengSharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		init();
		zifengAbstruct = (ViewPager) findViewById(R.id.vp_abstruct);
		zifengViewList = new ArrayList<View>();
		zifengViewList.add(zifengAbstructItem0);
		zifengViewList.add(zifengAbstructItem1);
		MyPagerAdapter adapter = new MyPagerAdapter();
		zifengAbstruct.setAdapter(adapter);
	}

	private void init() {
		zifengAbstructItem0 = View.inflate(this, R.layout.abstruct_item_0, null);
		zifengAbstructItem1 = View.inflate(this, R.layout.abstruct_item_1, null);
		GridView gv_abstruct_item = (GridView) zifengAbstructItem1
				.findViewById(R.id.gv_abstruct_item);
		zifengData = new ArrayList<Integer>();
		zifengData.add(R.drawable.yule_selected);
		zifengData.add(R.drawable.tiyu_selected);
		zifengData.add(R.drawable.caijing_selected);
		zifengData.add(R.drawable.keji_selected);
		zifengData.add(R.drawable.lingyimian_selected);
		zifengData.add(R.drawable.zhongchao_selected);
		zifengData.add(R.drawable.junshi_selected);
		zifengData.add(R.drawable.nba_selected);
		zifengData.add(R.drawable.qiche_selected);
		
		ImageButton iv_abstruct_item1_start = (ImageButton) zifengAbstructItem1.findViewById(R.id.iv_abstruct_item1_start);
		iv_abstruct_item1_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor edit = zifengSharedPreferences.edit();
				edit.putBoolean("isFirst", false);
				edit.commit();
				Log.i("Abstruct:", "简介页面:保存");
				Intent intent = new Intent(AbstructActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		gv_abstruct_item.setAdapter(new MyBaseAdapter());
	}

	class MyBaseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return zifengData.size();
		}

		@Override
		public Object getItem(int position) {
			return zifengData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHold hold;
			if (convertView == null) {
				convertView = View.inflate(AbstructActivity.this,
						R.layout.abstruct_item_1_item, null);
				ImageView iv_abstruct_item_1_item = (ImageView) convertView
						.findViewById(R.id.iv_abstruct_item_1_item);
				hold = new ViewHold(iv_abstruct_item_1_item);
				convertView.setTag(hold);
			} else {
				hold = (ViewHold) convertView.getTag();
			}
			Integer src = zifengData.get(position);
			hold.iv_abstruct_item_1_item.setBackgroundResource(src);
			return convertView;
		}

		class ViewHold {
			ImageView iv_abstruct_item_1_item;

			public ViewHold(ImageView iv_abstruct_item_1_item) {
				super();
				this.iv_abstruct_item_1_item = iv_abstruct_item_1_item;
			}
		}
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return zifengViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(View container, int position) {
			ViewPager vp = (ViewPager) container;
			vp.addView(zifengViewList.get(position));
			return zifengViewList.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			ViewPager vp = (ViewPager) container;
			vp.removeView(zifengViewList.get(position));
		}
	}
}
