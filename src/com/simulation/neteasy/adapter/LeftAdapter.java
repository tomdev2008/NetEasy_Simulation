package com.simulation.neteasy.adapter;

import java.util.List;

import com.zifeng.wangyi.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 
 * @author 紫枫
 *
 */
public class LeftAdapter extends BaseAdapter {

	private List<Integer> zifengLeftList;
	private Context zifengContext;

	public LeftAdapter(Context context, List<Integer> leftList) {
		super();
		this.zifengLeftList = leftList;
		this.zifengContext = context;
	}

	@Override
	public int getCount() {
		return zifengLeftList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return zifengLeftList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHold hold;
		if (convertView == null) {
			convertView = View.inflate(zifengContext, R.layout.left_item, null);
			ImageView zifengLeftItem = (ImageView) convertView
					.findViewById(R.id.iv_left_item);
			hold = new ViewHold(zifengLeftItem);
			convertView.setTag(hold);
		} else {
			hold = (ViewHold) convertView.getTag();
		}
		if (position == 0) {
			convertView.setBackgroundResource(R.drawable.biz_navigation_tab_bg_pressed);
		} else{
			convertView.setBackgroundResource(R.drawable.leftbg);
		}
		hold.zifengLeftItem.setImageResource(zifengLeftList.get(position));
		return convertView;
	}

	private class ViewHold {
		ImageView zifengLeftItem;

		public ViewHold(ImageView zifengLeftItem) {
			super();
			this.zifengLeftItem = zifengLeftItem;
		}

	}

}
