package com.simulation.neteasy.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simulation.neteasy.db.AppDBUtil;
import com.simulation.neteasy.domain.AppDownInfo;
import com.simulation.neteasy.domain.AppInfo;
import com.simulation.neteasy.util.NotifiUtil;
import com.zifeng.wangyi.R;

/**
 * 
 * @author 紫枫
 *
 */
public class AllAppAdapter extends BaseAdapter {
	Intent zifengService = new Intent("com.zifeng.wangyi.downService");
	private Context zifengContext;
	private List<AppInfo> zifengList;
	private SharedPreferences zifengSharedPreferences;
	int zifengState;
	private ListView zifengAllApp;
	private int zifengFirstVisiblePosition;
	private int zifengLastVisiblePosition;
	private RelativeLayout zifengParentView;

	public AllAppAdapter(Context zifengContext, List<AppInfo> list, ListView lv_all_app,RelativeLayout view) {
		super();
		this.zifengContext = zifengContext;
		this.zifengList = list;
		zifengSharedPreferences = zifengContext.getSharedPreferences("downInfo", Context.MODE_PRIVATE);
		this.zifengAllApp = lv_all_app;
		zifengParentView = view;
	}

	@Override
	public int getCount() {
		return zifengList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHold hold;
		AppDBUtil db = new AppDBUtil(zifengContext);
		if (convertView == null) {
			convertView = View.inflate(zifengContext, R.layout.all_app_item, null);
			ImageView zifengAllAppIcon = (ImageView) convertView.findViewById(R.id.iv_all_app_icon);
			TextView zifengAllAppName = (TextView) convertView.findViewById(R.id.tv_all_app_name);
			ImageView zifengAllAppLevel = (ImageView) convertView.findViewById(R.id.iv_all_app_level);
			TextView zifengAllAppSize = (TextView) convertView.findViewById(R.id.tv_all_app_size);
			TextView zifengAllAppVersion = (TextView) convertView.findViewById(R.id.tv_all_app_version);
			Button zifengAllAppDown = (Button) convertView.findViewById(R.id.bt_all_app_down);
			
			hold = new ViewHold(zifengAllAppIcon, zifengAllAppName,
					zifengAllAppLevel, zifengAllAppSize, zifengAllAppVersion,
					zifengAllAppDown);
			convertView.setTag(hold);
		} else {
			hold = (ViewHold) convertView.getTag();
		}
		AppInfo appInfo = zifengList.get(position);
		if (appInfo.getAppIcon() == null) {
			hold.zifengAllAppIcon.setBackgroundResource(R.drawable.ic_launcher);
		} else {
			hold.zifengAllAppIcon.setBackgroundDrawable(appInfo.getAppIcon());
		}
		hold.zifengAllAppName.setText(appInfo.getName());
		hold.zifengAllAppLevel.setImageLevel(appInfo.getLevel());
		hold.zifengAllAppSize.setText(appInfo.getSize() + "MB");
		hold.zifengAllAppVersion.setText("版本：" + appInfo.getVersion());
		//通过id和name查询记录   每个app开3条线程下载
		List<AppDownInfo> downList = db.queryDownLength(appInfo.getId(),
				appInfo.getName());
		// 下载完成的应用
		String downed = zifengSharedPreferences.getString(appInfo.getName(), "");
		// 有下载记录
		if (!downList.isEmpty() && downList.size() > 0) {
			// 0是正在下载 要给暂停的图片
			if (downList.get(0).getState() == 0) {
				hold.zifengAllAppDown
						.setBackgroundResource(R.drawable.quick_downloading_icon_1);
				zifengState = 0;
			}
			// 1是暂停下载 要给继续下载的图片
			else if (downList.get(0).getState() == 1) {
				hold.zifengAllAppDown
						.setBackgroundResource(R.drawable.quick_continue_to_download_icon);
				zifengState = 1;
			}
		}
		// 下载完成 给完成下载图片
		else if (downed.equals(appInfo.getName())) {
			hold.zifengAllAppDown
					.setBackgroundResource(R.drawable.quick_patching_icon);
			zifengState = 2;
		}
		// 其他。。。。给开始下载
		else {
			hold.zifengAllAppDown
					.setBackgroundResource(R.drawable.quick_download_icon);
			zifengState = 3;
		}
		DownState down = new DownState(position, zifengState,hold.zifengAllAppIcon,new ImageView(zifengContext));
		hold.zifengAllAppDown.setTag(down);
		hold.zifengAllAppDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final DownState state = (DownState) v.getTag();
				AppInfo info = zifengList.get(state.position);
				switch (state.state) {
				// 正在下载 点击要暂停 并且给继续下载图片
				case 0:
					state.state = 1;
					System.out.println("暂停下载");
					v.setBackgroundResource(R.drawable.quick_continue_to_download_icon);
					zifengService.putExtra("appId", info.getId());
					zifengService.putExtra("appName", info.getName());
					zifengService.putExtra("appUri", info.getUri());
					zifengService.putExtra("state", 0);
					zifengContext.startService(zifengService);
					NotifiUtil.sendNotifi(zifengContext, "暂停下载:" + info.getName());
					break;
				// 暂停下载 点击要继续下载 并且给暂停图片
				case 1:
					System.out.println("继续下载");
					state.state = 0;
					v.setBackgroundResource(R.drawable.quick_downloading_icon_1);
					zifengService.putExtra("appId", info.getId());
					zifengService.putExtra("appName", info.getName());
					zifengService.putExtra("appUri", info.getUri());
					zifengService.putExtra("state", 1);
					zifengContext.startService(zifengService);
					NotifiUtil.sendNotifi(zifengContext, "继续下载:" + info.getName());
					break;
				// 下载完成 点击安装
				case 2:
					// can't：
					/*
					 * Intent intent = new Intent();
					 * intent.setAction(Intent.ACTION_VIEW); File file = new
					 * File(Environment.getExternalStorageDirectory(),
					 * XmlConstant.WANGYI + "/" + info.getName()+".apk");
					 * intent.setDataAndType(Uri.fromFile(file),
					 * "application/vnd.android.package-archive"); Activity
					 * activity = (Activity)zifengContext;
					 * activity.startActivityForResult(intent, 100);
					 */

					// so:
					Intent intent = new Intent("com.zifeng.wangyi.install");
					intent.putExtra("appName", info.getName());
					zifengContext.sendBroadcast(intent);
					// 额....两种都不能在Fragment里面用onActivityResult接收返回的信息..............
					break;
				// 还没开始 点击要给暂停图片 并开始下载
				case 3:
					System.out.println("开始下载");
					AppDBUtil db = new AppDBUtil(zifengContext);
					List<AppDownInfo> queryDownAll = db.queryDownAll();
					System.out.println(queryDownAll);
					System.out.println("限制下载个数：+++++++++++"+queryDownAll.size());
					//如果是第2个那数据库是1 就会下载 如果条件是>2 那就会下载3个
					if (queryDownAll.size() > 1) {
						System.out.println("太多了_____________");
						Toast.makeText(zifengContext, "已经有两个正在下载咯，请稍候zzz", 0).show();
						break;
					}
					db.close();
					state.state = 0;
					//得到listView当前显示的第一个和最后一行的下标
					zifengFirstVisiblePosition = zifengAllApp.getFirstVisiblePosition();
					zifengLastVisiblePosition = zifengAllApp.getLastVisiblePosition();
					int oldY = zifengFirstVisiblePosition;
					int i = 0;
					//如果当前点击的在他们中间
					if (zifengFirstVisiblePosition <= state.position && state.position <= zifengLastVisiblePosition) {
						//找到该条目在显示的第几行
						for (; oldY <= zifengLastVisiblePosition; oldY++) {
							//循环一次i+1 最后得到i就是当前显示的第几个条目的下标
							i++;
							if (state.position == oldY) {
								System.out.println("___________"+oldY);
								break;
							}
						}
					}
					//得到点击条目所在显示中的第几个  如果 能显示3个 那zifengLastVisiblePosition - zifengFirstVisiblePosition = 2 再加1 就是3个 就能得到每个条目的高,再乘以点击的是第几个条目
					//能拿到大致Y轴坐标条目下面 要拿到条目开始的Y坐标 所以i要-1;
					int y = zifengAllApp.getHeight()/(zifengLastVisiblePosition - zifengFirstVisiblePosition + 1) * (i-1);
					//用来执行动画的控件
					state.iv.setBackgroundDrawable(state.zifengAllAppIcon.getBackground());
					LayoutParams params = new LayoutParams(state.zifengAllAppIcon.getWidth(), state.zifengAllAppIcon.getHeight());
					params.topMargin = y;
//					iv.setLayoutParams(params);
					//zifengParentView是listView所在的ViewGroup
					zifengParentView.addView(state.iv, params);
					//X从0到listView的一半 Y从当前到0
					TranslateAnimation animation = new TranslateAnimation(0, zifengAllApp.getWidth()/2, y, 0);
					animation.setDuration(500);
					//设置动画监听
					animation.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
						}
						@Override
						public void onAnimationRepeat(Animation animation) {
						}
						@Override
						public void onAnimationEnd(Animation animation) {
							//动画结束移除控件
							zifengParentView.removeView(state.iv);
						}
					});
					//开始动画
					state.iv.startAnimation(animation);
					v.setBackgroundResource(R.drawable.quick_downloading_icon_1);
					//BitMapDrawable：为了使用意图传递 得到BitMap(Parcelable) 
					BitmapDrawable appIcon = info.getAppIcon();
					Bitmap bitmap = appIcon.getBitmap();
					System.out.println("传递图片。。。。。。。。。。。"+bitmap);
					zifengService.putExtra("icon", bitmap);
					zifengService.putExtra("appId", info.getId());
					zifengService.putExtra("appName", info.getName());
					zifengService.putExtra("appUri", info.getUri());
					zifengService.putExtra("state", 3);
					zifengContext.startService(zifengService);
					NotifiUtil.sendNotifi(zifengContext, "开始下载:" + info.getName());
					break;
				default:
					break;
				}
			}
		});
		db.close();
		return convertView;
	}

	class ViewHold {
		ImageView zifengAllAppIcon;
		TextView zifengAllAppName;
		ImageView zifengAllAppLevel;
		TextView zifengAllAppSize;
		TextView zifengAllAppVersion;
		Button zifengAllAppDown;

		public ViewHold(ImageView zifengAllAppIcon, TextView zifengAllAppName,
				ImageView zifengAllAppLevel, TextView zifengAllAppSize,
				TextView zifengAllAppVersion, Button zifengAllAppDown) {
			super();
			this.zifengAllAppIcon = zifengAllAppIcon;
			this.zifengAllAppName = zifengAllAppName;
			this.zifengAllAppLevel = zifengAllAppLevel;
			this.zifengAllAppSize = zifengAllAppSize;
			this.zifengAllAppVersion = zifengAllAppVersion;
			this.zifengAllAppDown = zifengAllAppDown;
		}
	}
//用来保存Button的一些信息
	class DownState {
		int position;
		int state;
		ImageView zifengAllAppIcon;
		ImageView iv;

		public DownState(int position, int state, ImageView zifengAllAppIcon,ImageView iv) {
			super();
			this.position = position;
			this.state = state;
			this.zifengAllAppIcon = zifengAllAppIcon;
			this.iv = iv;
		}
	}
}
