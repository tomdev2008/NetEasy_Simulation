package com.simulation.neteasy.adapter;

import java.util.List;



import com.zifeng.wangyi.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
 * @author 紫枫
 *
 */
public class CityAdapter extends BaseAdapter {
	
	private List<String> zifengList;
	private Context zifengContext;

	public CityAdapter(List<String> list, Context context) {
		this.zifengList = list;
		this.zifengContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return zifengList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return zifengList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(zifengContext, R.layout.city_item, null);
		}
		TextView zifengCity = (TextView) convertView.findViewById(R.id.tv_city);
		String cityName = zifengList.get(position);
		zifengCity.setText(cityName);
		if (cityName.length()  == 1) {
			zifengCity.setTextSize(20);
			zifengCity.setBackgroundResource(R.drawable.city_color);
		} else{
			zifengCity.setTextSize(15);
			zifengCity.setBackgroundColor(Color.WHITE);
		}
		return convertView;
	}

}
