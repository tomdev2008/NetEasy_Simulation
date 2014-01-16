package com.simulation.neteasy.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.simulation.neteasy.CollectActivity;
import com.simulation.neteasy.LoginActivity;
import com.simulation.neteasy.RecommendActivity;
import com.simulation.neteasy.SearchActivity;
import com.simulation.neteasy.WeatherActivity;
import com.simulation.neteasy.api.UserApi;
import com.simulation.neteasy.constant.DBConstant.TABLE.TABLE_LINE;
import com.simulation.neteasy.db.DBUtil;
import com.simulation.neteasy.domain.User;
import com.zifeng.wangyi.R;
/**
 * 
 * @author 紫枫
 *
 */
public class RightFragment extends Fragment {
	private Receiver zifengReceiver;
	private DBUtil zifengDBUtil;
	private TextView zifengUserEmail;
	private ImageView zifengUserHead;
	private LinearLayout zifengUser;
	private View zifengView;
	
	private PopupWindow zifengUserOperate;
	private Bitmap zifengBitMap;
	private Uri zifengUri;
	private ProgressBar zifengUpdateHead;
	private Builder zifengBuilder;

	// com.huaao.login.success
	class Receiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 退出登录的广播
			if (intent.getAction().equals("com.zifeng.wangyi.logout")) {
				zifengUserEmail.setText("我的帐号");
				zifengUserHead
						.setBackgroundResource(R.drawable.biz_pc_default_small_profile_bg);
			} 
			//更新头像
			else if (intent.getAction().equals(
					"com.zifeng.wangyi.head.success")) {
				zifengUpdateHead.setVisibility(View.GONE);
				String state = intent.getStringExtra("state");
				if (state.equals("success")) {
					Toast.makeText(getActivity(), "更新头像成功", 0).show();
					ContentValues values = new ContentValues();
					//图片转byte 
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					zifengBitMap.compress(CompressFormat.JPEG, 100, bos);
					values.put(TABLE_LINE.HEADIMAGE, bos.toByteArray());
					zifengDBUtil.upDateHead(values , zifengDBUtil.getLoginUser().get_id()+"");
					BitmapDrawable bd = new BitmapDrawable(zifengBitMap);
					zifengUserHead.setBackgroundDrawable(bd);
					zifengDBUtil.close();
				} else if (state.equals("fail")) {
					Toast.makeText(getActivity(), "更新头像失败", 0).show();
				} else{
					Toast.makeText(getActivity(), "未知错误", 0).show();
				}
			}
			// 登录的
			else {
				User user = zifengDBUtil.getLoginUser();
				zifengDBUtil.close();
				zifengUserEmail.setText(user.getEmail());
				System.out.println("更换用户咯...."+user.getHeadImage());
				if (user.getHeadImage() == null || user.getHeadImage().equals("") || user.getHeadImage().equals("null")  ) {
					zifengUserHead.setBackgroundResource(R.drawable.biz_pc_default_small_profile_bg);
				} else{
					zifengUserHead.setBackgroundDrawable(user.getHeadImage());
				}
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		zifengDBUtil = new DBUtil(activity);
		zifengReceiver = new Receiver();
		IntentFilter filter = new IntentFilter("com.zifeng.login.success");
		filter.addAction("com.zifeng.wangyi.logout");
		filter.addAction("com.zifeng.wangyi.head.success");
		getActivity().registerReceiver(zifengReceiver, filter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		zifengView = inflater.inflate(R.layout.right, container, false);
		// 更新头像进度
		zifengUpdateHead = (ProgressBar) zifengView.findViewById(R.id.pb_update_head);
		User u = zifengDBUtil.getLoginUser();
		zifengDBUtil.close();
		// 用户
		zifengUser = (LinearLayout) zifengView.findViewById(R.id.ll_user);
		// 用户名
		zifengUserEmail = (TextView) zifengView.findViewById(R.id.tv_user_email);
		zifengUserHead = (ImageView) zifengView.findViewById(R.id.iv_user_head);
		if (u != null) {
			zifengUserEmail.setText(u.getEmail());
			if (u.getHeadImage() != null) {
				zifengUserHead.setBackgroundDrawable(u.getHeadImage());
			}
		}
		// 天气
		LinearLayout zifengWeather = (LinearLayout) zifengView
				.findViewById(R.id.ll_weather);
		zifengWeather.setOnClickListener(new MyWeatherOnClickListener());
		//搜索新闻
		LinearLayout zifengSearch = (LinearLayout) zifengView.findViewById(R.id.ll_search);
		zifengSearch.setOnClickListener(new SearchClickListener());
		//推荐应用
		LinearLayout zifengRecommend = (LinearLayout) zifengView.findViewById(R.id.ll_recommend);
		zifengRecommend.setOnClickListener(new RecommendOnClickListener());
		
		//我的收藏
		ImageButton zifengCollectSelf = (ImageButton) zifengView.findViewById(R.id.ib_collect_self);
		zifengCollectSelf.setOnClickListener(new UserOperate());
		return zifengView;
	}
	
	/**
	 * 应用推荐
	 */
	class RecommendOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), RecommendActivity.class);
			getActivity().startActivity(intent);
			getActivity().overridePendingTransition(R.anim.otherin, R.anim.hold);
		}
		
	}
	
	/**
	 * 搜索新闻
	 */
	class SearchClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), SearchActivity.class);
			getActivity().startActivity(intent);
			getActivity().overridePendingTransition(R.anim.otherin, R.anim.hold);
		}
		
	}
	
	/**
	 * 用户相关操作 ： 我的跟帖;收藏
	 * @author Lx-1019
	 *
	 */
	public class UserOperate implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			//我的收藏
			case R.id.ib_collect_self:
				startActivity(new Intent(getActivity(), CollectActivity.class));
				getActivity().overridePendingTransition(R.anim.otherin, R.anim.hold);
				break;

			default:
				break;
			}
		}
		
	}

	

	@Override
	public void onResume() {
		super.onResume();
		// 上块 有关用户的点击
		zifengUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				User u = zifengDBUtil.getLoginUser();
				zifengDBUtil.close();
				if (u == null) {
					startActivity(new Intent(getActivity(), LoginActivity.class));
					getActivity().overridePendingTransition(R.anim.otherin, R.anim.hold);
				} else {
					View userView = View.inflate(getActivity(),
							R.layout.user_operate, null);
					zifengUserOperate = new PopupWindow(userView,
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, true);
					zifengUserOperate.setBackgroundDrawable(new BitmapDrawable());
					zifengUserOperate.setOutsideTouchable(false);
					zifengUserOperate.showAtLocation(zifengUserHead, Gravity.TOP,
							zifengUserHead.getWidth(), zifengUserHead.getHeight());
					LinearLayout ll_user_change_head = (LinearLayout) userView
							.findViewById(R.id.ll_user_change_head);
					LinearLayout ll_changge_loginuser = (LinearLayout) userView
							.findViewById(R.id.ll_changge_loginuser);
					LinearLayout ll_logout_user = (LinearLayout) userView
							.findViewById(R.id.ll_logout_user);
					// 改变头像
					ll_user_change_head
							.setOnClickListener(new UserOperateClickListener());
					// 切换用户
					ll_changge_loginuser
							.setOnClickListener(new UserOperateClickListener());
					// 退出登录
					ll_logout_user
							.setOnClickListener(new UserOperateClickListener());
				}
			}
		});
	}

	private class UserOperateClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			zifengUserOperate.dismiss();
			switch (v.getId()) {
			case R.id.ll_user_change_head:
				Builder b = new Builder(getActivity());
				b.setItems(R.array.touxiang,
						new DialogInterface.OnClickListener() {
							private Intent intent;

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									intent = new Intent(
											"android.intent.action.PICK");
									intent.setType("image/*");
									intent.putExtra("return-data", true);
									startActivityForResult(intent, 0);
									break;
								case 1:
									intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									startActivityForResult(intent, 100);
									break;
								default:
									break;
								}
							}
						});
				b.create().show();
				break;
			case R.id.ll_changge_loginuser:
				startActivity(new Intent(getActivity(), LoginActivity.class));
				break;
			case R.id.ll_logout_user:
				zifengDBUtil.clear();
				zifengDBUtil.clearCollect();
				zifengDBUtil.close();
				Intent intent = new Intent("com.zifeng.wangyi.logout");
				getActivity().sendBroadcast(intent);
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 返回码是-1就是有数据
		if (resultCode == -1) {
			// 得到选中的图片
			zifengBitMap = (Bitmap) data.getExtras().get("data");
			// 得到图片地址
			zifengUri = data.getData();

			zifengBuilder = new Builder(getActivity());
			View photo = View.inflate(getActivity(), R.layout.photo_view, null);
			ImageView iv_user_select_pick = (ImageView) photo
					.findViewById(R.id.iv_user_select_pick);
			iv_user_select_pick.setImageBitmap(zifengBitMap);
			zifengBuilder.setView(photo);
			// 上传图片
			zifengBuilder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					zifengUpdateHead.setVisibility(View.VISIBLE);
					ContentResolver cr = getActivity().getContentResolver();
					Cursor cursor = cr.query(zifengUri, null, null, null, null);
					cursor.moveToNext();
					String address = cursor.getString(cursor
							.getColumnIndex("_data"));
					File file = new File(address);
					UserApi uApi = new UserApi();
					uApi.uploadByte(file, zifengDBUtil.getLoginUser().get_id(),
							getActivity());
					zifengDBUtil.close();
				}
			});
			zifengBuilder.setPositiveButton("取消", null);
			zifengBuilder.create().show();
		}
	}

	/**
	 * 天气
	 * 
	 * @author 
	 * 
	 */
	private class MyWeatherOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), WeatherActivity.class);
			getActivity().startActivity(intent);
			getActivity().overridePendingTransition(R.anim.otherin, R.anim.hold);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		getActivity().unregisterReceiver(zifengReceiver);
	}
}
