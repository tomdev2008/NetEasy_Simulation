package com.simulation.neteasy;


import com.zifeng.wangyi.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

/**
 * 
 * @author 紫枫
 *
 */
public class ForgetActivity extends Activity {

	private WebView zifengForgetPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget);
		zifengForgetPassword = (WebView) findViewById(R.id.wv_forget_password);
		zifengForgetPassword.loadUrl("file:///android_asset/forget.html");//随便填
		
	}
	
	public void back(View v) {
		finish();
		overridePendingTransition(0, R.anim.out_news_text);
	}
}
