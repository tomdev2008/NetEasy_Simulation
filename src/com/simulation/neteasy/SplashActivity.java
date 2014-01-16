package com.simulation.neteasy;

import java.io.File;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.simulation.neteasy.constant.XmlConstant;
import com.simulation.neteasy.util.NotifiUtil;
import com.simulation.neteasy.util.StreamOperate;
import com.zifeng.wangyi.R;

/**
 * 
 * @author 紫枫
 *
 */
public class SplashActivity extends Activity {

	private SharedPreferences zifengSharedPreferences;
	private boolean zifengFirst;
	private Uri zifengUri;
	private boolean zifengCreate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		NotifiUtil.sendNotifi(this, "网易新闻");
		zifengSharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		View ll_splash = findViewById(R.id.ll_splash);
		AlphaAnimation animation = new AlphaAnimation(0, 1);
		animation.setAnimationListener(new MyAnimation());
		animation.setDuration(3000);
		System.out.println(animation + "     " + ll_splash);
		ll_splash.setAnimation(animation);
		zifengFirst = zifengSharedPreferences.getBoolean("isFirst", true);
		zifengCreate = zifengSharedPreferences.getBoolean("isCreate", true);
		createShortcut();
		File file = new File(Environment.getExternalStorageDirectory(),
				XmlConstant.WANGYI);
		File city = new File(Environment.getExternalStorageDirectory(),
				XmlConstant.CITY_XML_path);
		if (!file.exists()) {
			file.mkdir();
		}
		if (!city.exists()) {
			try {
				city.createNewFile();
				StreamOperate.writeCitysXml(city);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	void createShortcut() {
		if (zifengCreate) {
			if (!isExist()) {
				Parcelable icon = Intent.ShortcutIconResource.fromContext(this,
						R.drawable.icon);
				Intent intent = new Intent(
						"com.android.launcher.action.INSTALL_SHORTCUT");
				intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "网易");
				intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
				Intent mIntent = new Intent(Intent.ACTION_MAIN);
				mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				ComponentName component = new ComponentName(this,
						SplashActivity.class);
				mIntent.setComponent(component);
				intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, mIntent);
				sendBroadcast(intent);
				zifengSharedPreferences.edit().putBoolean("isCreate", false);
			}
		}
	}

	public boolean isExist() {
		if (android.os.Build.VERSION.SDK_INT >= 8) {
			zifengUri = Uri
					.parse("content://com.android.launcher2.settings/favorites");
		} else {
			zifengUri = Uri
					.parse("content://com.android.launcher.settings/favorites");
		}
		Log.i("SplashActivity", "URI:" + zifengUri);
		Cursor cursor = getContentResolver().query(zifengUri,
				new String[] { "title" }, "title = ?", new String[] { "网易" },
				null);
		System.out.println(cursor);
		boolean isExit = cursor == null ? false : true;
		System.out.println(isExit);
		if (isExit) {
			isExit = cursor.moveToNext();
			cursor.close();
		}
		return isExit;
	}

	class MyAnimation implements AnimationListener {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			Log.i("Abstruct:", "Splash页面:得到" + zifengFirst);
			if (zifengFirst) {
				Intent intent = new Intent(SplashActivity.this,
						AbstructActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(SplashActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
			finish();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}
}