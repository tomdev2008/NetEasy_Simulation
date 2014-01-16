package com.simulation.neteasy.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simulation.neteasy.domain.NewsImage;
import com.zifeng.wangyi.R;
/**
 * 
 * @author 紫枫
 *
 */
public class NewsToutiaoLvAdapter extends BaseAdapter {
	
	private List<NewsImage> zifengList;
	private Context zifengContext;
	public NewsToutiaoLvAdapter(Context context, List<NewsImage> list) {
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
		return position;
	}
	
	public void setList(List<NewsImage> list){
		this.zifengList = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHold hold;
		if (convertView == null) {
			convertView = View.inflate(zifengContext, R.layout.news_toutiao_item, null);
			ImageView zifengNewsToutiaoImage = (ImageView) convertView.findViewById(R.id.iv_news_toutiao_image);
			TextView zifengNewsToutiaoText = (TextView) convertView.findViewById(R.id.tv_news_toutiao_text);
			hold = new ViewHold(zifengNewsToutiaoImage, zifengNewsToutiaoText);
			convertView.setTag(hold);
		} else{
			hold = (ViewHold) convertView.getTag();
		}
		
		NewsImage newsImage = zifengList.get(position);
		if (newsImage.getDrawable() != null) {
			hold.zifengNewsToutiaoImage.setImageBitmap(newsImage.getDrawable());
		} else{
			hold.zifengNewsToutiaoImage.setImageResource(R.drawable.base_list_default_icon);
		}
		hold.zifengNewsToutiaoText.setText(newsImage.getTitle());
		return convertView;
	}

	class ViewHold{
		ImageView zifengNewsToutiaoImage;
		TextView zifengNewsToutiaoText;
		public ViewHold(ImageView zifengNewsToutiaoImage,
				TextView zifengNewsToutiaoText) {
			super();
			this.zifengNewsToutiaoImage = zifengNewsToutiaoImage;
			this.zifengNewsToutiaoText = zifengNewsToutiaoText;
		}
	}
}
