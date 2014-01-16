package com.simulation.neteasy;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.simulation.neteasy.api.NewsTextApi;
import com.simulation.neteasy.api.UserApi;
import com.simulation.neteasy.constant.HandMsgConstant;
import com.simulation.neteasy.db.DBUtil;
import com.simulation.neteasy.domain.Comment;
import com.simulation.neteasy.domain.NewsToutiao;
import com.simulation.neteasy.domain.User;
import com.simulation.neteasy.inter.OnNewsListener;
import com.simulation.neteasy.util.JsonUtil;
import com.simulation.neteasy.util.NotifiUtil;
import com.zifeng.wangyi.R;

/**
 * 
 * @author 紫枫
 *
 */
public class NewsTextActivity extends Activity {
	
	// 写字框是否出现
	private boolean zifengIsWrite;
	private EditText zifengNewsTextWrite;
	private int zifengNewsId;
	private float zifengOldx;
	private Bitmap zifengBitmap;
	private boolean zifengExist;
	int zifengPosition = 1;
	
	private LinearLayout zifengNewsLoading;
	private LinearLayout zifengNewsLoaded;
	private TextView zifengNewsTextTitle;
	private ImageView zifengNewsTextImage;
	private TextView zifengNewsText;
	private TextView zifengNewsTextCommnet;
	private TextView zifengNewsTextTime;
	private LinearLayout zifengNewsTextSend;
	private boolean zifengIsJump = true;

	private boolean zifengPopWindow = true;
	private PopupWindow zifengWindow;
	private NewsToutiao zifengNews;
	private int zifengComment;
	private Handler zifengHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {

			case HandMsgConstant.DELETE_COLLECT_BYNEWSID:
				Toast.makeText(NewsTextActivity.this, "已取消收藏", 0).show();
				break;
			case HandMsgConstant.COLLECT_ERROR:
				Toast.makeText(NewsTextActivity.this, "收藏失败", 0).show();
				break;
			case HandMsgConstant.COLLECT_SUCCESS:
				Toast.makeText(NewsTextActivity.this, "收藏成功", 0).show();
				break;
			case HandMsgConstant.SUCCESS:
				zifengNewsLoading.setVisibility(View.GONE);
				zifengNewsLoaded.setVisibility(View.VISIBLE);
				zifengNewsTextTitle.setText(zifengNews.getTitle());
				zifengNewsText.setText(zifengNews.getText());
				zifengComment = zifengNews.getComment();
				zifengNewsTextCommnet.setText("跟帖:" + zifengComment);
				zifengNewsTextTime.setText("来源:网易" + zifengNews.getType() + "    "
						+ zifengNews.getTime());
				break;
			case HandMsgConstant.COMMENT_SUBMIT_SUCCESS:
				Toast.makeText(NewsTextActivity.this, "评论成功", 0).show();
				zifengNewsTextWrite.setText("");
				zifengComment = zifengComment + 1;
				zifengNewsTextCommnet.setText("跟帖：" + zifengComment);
				if (zifengIsWrite) {
					zifengNewsTextSend.setVisibility(View.GONE);
					zifengIsWrite = false;
				}
				NewsTextApi ntApi = new NewsTextApi();
				ntApi.update(zifengNews.getId(), zifengComment);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onRestart() {
		super.onRestart();
		zifengIsJump = true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			zifengOldx = ev.getX();
		}
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			if (ev.getX() + 100 < zifengOldx) {
				if (zifengIsJump) {
					Intent intent = new Intent(this, UserCommentActivity.class);
					intent.putExtra("newsId", zifengNewsId);
					startActivityForResult(intent, 100);
					overridePendingTransition(R.anim.otherin, R.anim.hold);
					zifengIsJump = false;
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_text);
		System.out.println("进入正文");
		// 时间来源
		zifengNewsTextTime = (TextView) findViewById(R.id.tv_news_text_time);
		// 评论
		zifengNewsTextCommnet = (TextView) findViewById(R.id.tv_news_text_commnet);
		// 新闻标题
		zifengNewsTextTitle = (TextView) findViewById(R.id.tv_news_text_title);
		// 新闻图片
		zifengNewsTextImage = (ImageView) findViewById(R.id.iv_news_text_image);
		// 新闻正文
		zifengNewsText = (TextView) findViewById(R.id.tv_news_text_text);
		// 加载
		zifengNewsLoading = (LinearLayout) findViewById(R.id.ll_news_loading);
		// 加载完成
		zifengNewsLoaded = (LinearLayout) findViewById(R.id.ll_news_loaded);
		// 发送评论
		zifengNewsTextSend = (LinearLayout) findViewById(R.id.ll_news_text_send);
		// 评论内容
		zifengNewsTextWrite = (EditText) findViewById(R.id.et_news_text_write);
		Intent intent = getIntent();
		zifengNewsId = intent.getIntExtra("newsId", 0);
		zifengBitmap = intent.getParcelableExtra("newsImage");
		zifengNewsTextImage.setImageBitmap(zifengBitmap);
		NewsTextApi ntApi = new NewsTextApi();
		ntApi.getNewest(zifengNewsId, new MyOnNewsListener());

		zifengNewsTextCommnet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NewsTextActivity.this,
						UserCommentActivity.class);
				intent.putExtra("newsId", zifengNewsId);
				startActivityForResult(intent, 100);
				overridePendingTransition(R.anim.otherin, R.anim.hold);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		int i = data.getIntExtra("i", 0);
		zifengComment = zifengComment + i;
		zifengNewsTextCommnet.setText("跟帖：" + zifengComment);
	}

	public void send(View v) {
		DBUtil db = new DBUtil(this);
		User user = db.getLoginUser();
		db.close();
		if (user == null) {
			// 请登录
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		} else {
			String content = zifengNewsTextWrite.getText().toString();
			if (content != null && content != "") {
				System.out.println("提交评论");
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				Date date = new Date();
				String time = format.format(date);
				Comment c = new Comment(user.get_id(), zifengNewsId, content, time,
						user.getFlag());
				UserApi uApi = new UserApi();
				uApi.submitcomment(c, new CommentListener());
			} else {
				Toast.makeText(this, "请输入内容", 0).show();
			}
		}
	}

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
			Toast.makeText(NewsTextActivity.this, error, 0).show();
		}

		@Override
		public void onException(Exception exception) {
			exception.printStackTrace();
		}

	}

	// 获取新闻内容回调
	class MyOnNewsListener implements OnNewsListener {
		@Override
		public void onComplete(String result) {
			zifengNews = JsonUtil.parseNewsTextJson(result);
			NotifiUtil.sendNotifi(NewsTextActivity.this, zifengNews.getTitle());
			Message msg = new Message();
			msg.arg1 = HandMsgConstant.SUCCESS;
			zifengHandler.sendMessage(msg);
		}

		@Override
		public void onError(String error) {
			System.out.println("新闻正文:__________________-" + error);
		}

		@Override
		public void onException(Exception exception) {
			exception.printStackTrace();
		}

	}

	public void back(View v) {
		finish();
		overridePendingTransition(0, R.anim.out_news_text);
	}

	// ActionBar
	public void actionBar(View v) {
		if (zifengPopWindow) {
			View view = View.inflate(this, R.layout.popwindow_bar, null);
			zifengWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, true);
			zifengWindow.setBackgroundDrawable(new BitmapDrawable());
			zifengWindow.setOutsideTouchable(false);
			int[] a = new int[2];
			v.getLocationInWindow(a);
			zifengWindow.showAtLocation(v, Gravity.TOP, a[0], a[1] + v.getHeight());
			LinearLayout ll_news_text_write = (LinearLayout) view
					.findViewById(R.id.ll_news_text_write);
			LinearLayout ll_news_text_collect = (LinearLayout) view
					.findViewById(R.id.ll_news_text_collect);
			LinearLayout ll_news_text_size = (LinearLayout) view
					.findViewById(R.id.ll_news_text_size);
			DBUtil db = new DBUtil(NewsTextActivity.this);
			zifengExist = db.getCollectByNewsId(zifengNewsId);
			if (zifengExist) {
				TextView tv_clolect = (TextView) ll_news_text_collect
						.findViewById(R.id.tv_clolect);
				tv_clolect.setText("取消收藏");
			}
			db.close();
			// 写跟帖
			ll_news_text_write.setOnClickListener(new MyOnClickListener());
			// 收藏
			ll_news_text_collect.setOnClickListener(new MyOnClickListener());
			// 字体大小
			ll_news_text_size.setOnClickListener(new MyOnClickListener());
		} else {
			zifengWindow.dismiss();
		}

	}


	/**
	 * actionBar的点击
	 */
	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			//字体大小
			case R.id.ll_news_text_size:
				zifengWindow.dismiss();
				Builder b = new Builder(NewsTextActivity.this);
				CharSequence[] items = new String[] { "小号", "中号", "大号" };
				b.setSingleChoiceItems(items, zifengPosition,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						zifengPosition = which;
					}
				});
				b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (zifengPosition) {
						case 0:
							zifengNewsText.setTextSize(10);
							break;
						case 1:
							zifengNewsText.setTextSize(15);
							break;
						case 2:
							zifengNewsText.setTextSize(20);
							break;
						default:
							break;
						}
					}
				});
				b.setNegativeButton("取消", null);
				b.create().show();
				break;
			// 写跟帖
			case R.id.ll_news_text_write:
				zifengNewsTextSend.setVisibility(View.VISIBLE);
				zifengWindow.dismiss();
				zifengIsWrite = true;
				break;
			// 收藏
			case R.id.ll_news_text_collect:
				zifengWindow.dismiss();
				DBUtil db = new DBUtil(NewsTextActivity.this);
				if (zifengExist) {
					db.clearCollectByNewsId(zifengNewsId);
					zifengExist = false;
					Message msg = new Message();
					msg.arg1 = HandMsgConstant.DELETE_COLLECT_BYNEWSID;
					zifengHandler.sendMessage(msg);
				} else {
					ContentValues values = new ContentValues();
					values.put("newsId", zifengNewsId);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					zifengBitmap.compress(CompressFormat.PNG, 100, bos);
					values.put("newsImg", bos.toByteArray());
					values.put("title", zifengNews.getTitle());
					boolean collect = db.collect(values);
					if (collect) {
						Message msg = new Message();
						msg.arg1 = HandMsgConstant.COLLECT_SUCCESS;
						zifengHandler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.arg1 = HandMsgConstant.COLLECT_ERROR;
						zifengHandler.sendMessage(msg);
					}
				}
				db.close();
				break;
			default:
				break;
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (zifengIsWrite) {
				zifengNewsTextSend.setVisibility(View.GONE);
				zifengIsWrite = false;
				return true;
			} else {
				finish();
				overridePendingTransition(0, R.anim.out_news_text);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
