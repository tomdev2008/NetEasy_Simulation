package com.simulation.neteasy;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.simulation.neteasy.adapter.CommentAdapter;
import com.simulation.neteasy.api.CommentApi;
import com.simulation.neteasy.api.NewsTextApi;
import com.simulation.neteasy.api.UserApi;
import com.simulation.neteasy.constant.HandMsgConstant;
import com.simulation.neteasy.db.DBUtil;
import com.simulation.neteasy.domain.Comment;
import com.simulation.neteasy.domain.User;
import com.simulation.neteasy.inter.OnNewsListener;
import com.simulation.neteasy.util.JsonUtil;
import com.zifeng.wangyi.R;

/**
 * 偷懒了......................
 * 
 * @author 紫枫
 * 
 */
public class UserCommentActivity extends Activity {
	
	private List<Comment> zifengComments;
	private int zifengNewsId;
	private String zifengTime;
	private User zifengUser;
	// 偷懒 多跟帖就 + 1;
	private int i = 1;
	// 返回去
	private int j = 0;
	private String zifengContent;

	private ProgressBar zifengCommentLoading;
	private ListView zifengComment;
	private LinearLayout zifengNewsTextWrite;
	private EditText zifengNewsText;
	private boolean zifengWrite = false;
	private CommentApi zifengCommentApi;
	private CommentAdapter zifengCommentAdapter;
	Handler zifengHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case HandMsgConstant.ERROR:
				Toast.makeText(UserCommentActivity.this, "暂无评论", 0).show();
				zifengCommentLoading.setVisibility(View.GONE);
				break;
			case HandMsgConstant.SUCCESS:
				zifengCommentLoading.setVisibility(View.GONE);
				zifengCommentAdapter = new CommentAdapter(zifengComments, UserCommentActivity.this);
				zifengComment.setAdapter(zifengCommentAdapter);
				zifengComment.setVisibility(View.VISIBLE);
				break;
			case HandMsgConstant.COMMENT_SUBMIT_SUCCESS:
				if (zifengComments == null) {
					zifengComments = new ArrayList<Comment>();
				}
				Toast.makeText(UserCommentActivity.this, "评论成功", 0).show();
				j++;
				zifengNewsText.setText("");
				Comment zifeng = new Comment();
				zifeng.setNewsId(zifengNewsId);
				zifeng.setUserFlag(zifengUser.getFlag());
				zifeng.setDrawable(zifengUser.getHeadImage());
				zifeng.setTime(zifengTime);
				zifeng.setText(zifengContent);
				zifeng.setEmail(zifengUser.getEmail());
				List<Comment> list = new ArrayList<Comment>();
				list.add(zifeng);
				for (Comment comment : zifengComments) {
					list.add(comment);
				}
				zifengComments = list;
				System.out.println(zifengComments +"           "+zifengCommentAdapter);
				if (zifengCommentAdapter == null) {
					zifengCommentAdapter = new CommentAdapter(zifengComments, UserCommentActivity.this);
					zifengComment.setAdapter(zifengCommentAdapter);
				} else{
					zifengCommentAdapter.setList(zifengComments);
				}
				zifengCommentAdapter.notifyDataSetChanged();
				NewsTextApi ntApi = new NewsTextApi();
				ntApi.update(zifengNewsId, zifengComments.size() + i);
				i++;
				zifengComment.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		};
	};


	class MyOnListener implements OnNewsListener {

		@Override
		public void onComplete(String result) {
			if (result.equals("error")) {
				Message msg = new Message();
				msg.arg1 = HandMsgConstant.ERROR;
				zifengHandler.sendMessage(msg);
			} else {
				zifengComments = JsonUtil.parseComment(result);
				if (zifengComments.size() == 0 || zifengComments.isEmpty()) {
					Message msg = new Message();
					msg.arg1 = HandMsgConstant.ERROR;
					zifengHandler.sendMessage(msg);
				} else {
					for (Comment c : zifengComments) {
						try {
							Drawable drawable = Drawable.createFromStream(
									new URL(c.getHead()).openStream(), "imag");
							c.setDrawable(drawable);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					Message msg = new Message();
					msg.arg1 = HandMsgConstant.SUCCESS;
					zifengHandler.sendMessage(msg);
				}
			}
		}

		@Override
		public void onError(String error) {

		}

		@Override
		public void onException(Exception exception) {

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usercomment);
		Intent intent = getIntent();
		zifengNewsId = intent.getIntExtra("newsId", 0);
		zifengCommentApi = new CommentApi();
		zifengCommentApi.getCommentByNewsId(zifengNewsId, new MyOnListener());
		// 进度条
		zifengCommentLoading = (ProgressBar) findViewById(R.id.pb_comment_loading);
		// 所有评论
		zifengComment = (ListView) findViewById(R.id.lv_comment);
		zifengNewsTextWrite = (LinearLayout) findViewById(R.id.ll_news_text_write);
		zifengNewsText = (EditText) findViewById(R.id.et_news_text_write);

		zifengNewsTextWrite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				zifengNewsText.setVisibility(View.VISIBLE);
				zifengNewsTextWrite.setVisibility(View.GONE);
				zifengWrite = true;
			}
		});
	}

	public void send(View v) {
		DBUtil db = new DBUtil(this);
		zifengUser = db.getLoginUser();
		db.close();
		if (zifengUser == null) {
			// 请登录
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		} else {
			zifengContent = zifengNewsText.getText().toString();
			if (zifengContent != null && zifengContent != "") {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				Date date = new Date();
				zifengTime = format.format(date);
				Comment c = new Comment(zifengUser.get_id(), zifengNewsId, zifengContent, zifengTime,
						zifengUser.getFlag());
				UserApi uApi = new UserApi();
				uApi.submitcomment(c, new CommentListener());
			} else {
				Toast.makeText(this, "请输入内容", 0).show();
			}
		}
	}

	// 完成跟帖回调
	class CommentListener implements OnNewsListener {
		@Override
		public void onComplete(String result) {
			Message msg = new Message();
			msg.arg1 = HandMsgConstant.COMMENT_SUBMIT_SUCCESS;
			msg.obj = result;
			zifengHandler.sendMessage(msg);
		}

		@Override
		public void onError(String error) {

		}

		@Override
		public void onException(Exception exception) {
			exception.printStackTrace();
		}

	}

	// 如果写字框显现 退出键不退出 让鞋子框影调
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (zifengWrite) {
				zifengNewsText.setVisibility(View.GONE);
				zifengNewsTextWrite.setVisibility(View.VISIBLE);
				zifengWrite = false;
				return true;
			} else {
				Intent data = new Intent();
				data.putExtra("i", j);
				setResult(1001, data);
				finish();
				overridePendingTransition(0, R.anim.out_news_text);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void back(View v) {
		Intent data = new Intent();
		data.putExtra("i", j);
		setResult(1001, data);
		finish();
		overridePendingTransition(0, R.anim.out_news_text);
	}
}
