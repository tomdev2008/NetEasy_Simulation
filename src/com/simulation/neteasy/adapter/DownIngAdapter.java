package com.simulation.neteasy.adapter;

import java.util.List;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simulation.neteasy.domain.AppDownInfo;
import com.simulation.neteasy.view.MyProgressBar;
import com.zifeng.wangyi.R;
/**
 * 
 * @author 紫枫
 *
 */
public class DownIngAdapter extends BaseAdapter {

	private List<AppDownInfo> zifengList;
	private FragmentActivity zifengContext;

	public DownIngAdapter(List<AppDownInfo> downList, FragmentActivity activity) {
		zifengList = downList;
		this.zifengContext = activity;
	}
	
	public void setList(List<AppDownInfo> list){
		this.zifengList = list;
	}

	@Override
	public int getCount() {
		return zifengList.size();
	}

	@Override
	public Object getItem(int position) {
		return zifengList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(zifengContext, R.layout.downing, null);
		} 
		ImageView zifengDowningIcon = (ImageView) convertView.findViewById(R.id.iv_downing_icon);
		TextView zifengDowningName = (TextView) convertView.findViewById(R.id.tv_downing_name);
		MyProgressBar zifengDowing = (MyProgressBar) convertView.findViewById(R.id.pb_downing);
		AppDownInfo zifengAppDownInfo = zifengList.get(position);
		zifengDowningName.setText(zifengAppDownInfo.getName());
		zifengDowningIcon.setImageBitmap(zifengAppDownInfo.getAppIcon());
		zifengDowing.setMax(zifengAppDownInfo.getMaxLength());
		zifengDowing.setProgress(zifengAppDownInfo.getDownLength());
		float i = zifengAppDownInfo.getDownLength()*100/zifengAppDownInfo.getMaxLength();
		zifengDowing.setText("下载:"+i+"%");
		return convertView;
	}

}
