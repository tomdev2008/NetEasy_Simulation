package com.simulation.neteasy.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simulation.neteasy.domain.Comment;
import com.zifeng.wangyi.R;
/**
 * 
 * @author 紫枫
 *
 */
public class CommentAdapter extends BaseAdapter {

	private Context zifengContext;
	private List<Comment> zifengList;

	public CommentAdapter(List<Comment> list, Context context) {
		super();
		this.zifengList = list;
		this.zifengContext = context;
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
		ViewHold hold;
		if (convertView == null) {
			convertView = View.inflate(zifengContext, R.layout.comment_item, null);
			ImageView zifengCommentUser = (ImageView) convertView.findViewById(R.id.iv_comment_user);
			TextView zifengCommentName = (TextView) convertView.findViewById(R.id.tv_comment_name);
			TextView zifengCommentText = (TextView) convertView.findViewById(R.id.tv_comment_text);
			TextView zifengCommentTime = (TextView) convertView.findViewById(R.id.tv_comment_time);
			hold = new ViewHold(zifengCommentUser, zifengCommentName,
					zifengCommentText, zifengCommentTime);
			convertView.setTag(hold);
		} else {
			hold = (ViewHold) convertView.getTag();
		}
		Comment comment = zifengList.get(position);
		if (comment.getDrawable() == null) {
			hold.zifengCommentUser
					.setBackgroundResource(R.drawable.biz_topic_micro_detail_header);
		} else {
			hold.zifengCommentUser.setBackgroundDrawable(comment.getDrawable());
		}
		hold.zifengCommentName.setText(comment.getUserFlag() + "网友  "
				+ comment.getEmail() + ":");
		hold.zifengCommentTime.setText(comment.getTime());
		hold.zifengCommentText.setText(comment.getText());
		return convertView;
	}
	
	public void setList(List<Comment> list){
		this.zifengList = list;
	}

	class ViewHold {
		ImageView zifengCommentUser;
		TextView zifengCommentName;
		TextView zifengCommentText;
		TextView zifengCommentTime;

		public ViewHold(ImageView zifengCommentUser, TextView zifengCommentName,
				TextView zifengCommentText, TextView zifengCommentTime) {
			super();
			this.zifengCommentUser = zifengCommentUser;
			this.zifengCommentName = zifengCommentName;
			this.zifengCommentText = zifengCommentText;
			this.zifengCommentTime = zifengCommentTime;
		}

	}

}
