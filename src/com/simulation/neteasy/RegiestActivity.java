package com.simulation.neteasy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.simulation.neteasy.api.UserApi;
import com.simulation.neteasy.constant.HandMsgConstant;
import com.simulation.neteasy.domain.User;
import com.simulation.neteasy.inter.OnNewsListener;
import com.zifeng.wangyi.R;

/**
 * 
 * @author 紫枫
 *
 */
public class RegiestActivity extends Activity {

	private WebView zifengRegiest;
	private LinearLayout zifengRegiestByEmail;
	
	private String zifengResponse;
	
	private CheckBox zifengAgree;
	private EditText zifengRegiestEmail;
	private EditText zifengRegiestPassword;
	private ProgressBar zifengRegiestLoading;
	
	private Handler zifengHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case HandMsgConstant.REGIEST_UI_CHANGGE:
				zifengRegiest.setVisibility(View.GONE);
				zifengRegiestByEmail.setVisibility(View.VISIBLE);
				break;
			case HandMsgConstant.REGIEST_SUCCESS:
				Toast.makeText(RegiestActivity.this, zifengResponse, 0).show();
				if (zifengResponse.equals("注册成功")) {
					RegiestActivity.this.finish();
					overridePendingTransition(0, R.anim.out_news_text);
				}
				break;
			default:
				break;
			}
		};
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regiest);
		// web选择注册方式
		zifengRegiest = (WebView) findViewById(R.id.wv_regiest);
		// 注册
		zifengRegiestByEmail = (LinearLayout) findViewById(R.id.ll_regiest_by_email);
		// 同意条款
		zifengAgree = (CheckBox) findViewById(R.id.cb_agree);
		zifengAgree.setText(Html
				.fromHtml("<font color='black'>我同意</font><font color='blue'>'服务条款'</font><font color='black'>和</font><font color='blue'>'网络游戏用户隐私权保护和个人信息利用政策'</font>"));
		// 邮箱
		zifengRegiestEmail = (EditText) findViewById(R.id.et_regiest_email);
		// 密码
		zifengRegiestPassword = (EditText) findViewById(R.id.et_regiest_password);
		// 注册进度条
		zifengRegiestLoading = (ProgressBar) findViewById(R.id.pb_regiest_loading);

		zifengRegiest.loadUrl("http://10.0.2.2:8080/wangyiWeb/news.html");//随便代替的
		zifengRegiest.getSettings().setJavaScriptEnabled(true);
		zifengRegiest.addJavascriptInterface(new Regiest(), "obj");
	}

	class Regiest {
		public void login() {
			RegiestActivity.this.finish();
			overridePendingTransition(0, R.anim.out_news_text);
		}

		public void regiestByPhone() {
			Toast.makeText(RegiestActivity.this, "暂不开放", 0).show();
		}

		public void regiestByEmail() {
			Message msg = new Message();
			msg.arg1 = HandMsgConstant.REGIEST_UI_CHANGGE;
			zifengHandler.sendMessage(msg);
		}
	}

	public void back(View v) {
		finish();
		overridePendingTransition(0, R.anim.out_news_text);
	}

	public boolean checkForm(String content) {
		String pattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(content);
		return matcher.matches();
	}

	public void regiest(View v) {
		boolean checked = zifengAgree.isChecked();
		String email = zifengRegiestEmail.getText().toString();
		String password = zifengRegiestPassword.getText().toString();
		if (!checked) {
			Toast.makeText(this, "请同意服务条款!", 0).show();
		} else if (!checkForm(email)) {
			Toast.makeText(this, "请输入正确格式邮箱!", 0).show();
		} else if (password.length() < 6 || password.length() > 16) {
			Toast.makeText(this, "请输入正确格式密码!", 0).show();
		} else {
			zifengRegiestLoading.setVisibility(View.VISIBLE);
			UserApi uApi = new UserApi();
			User u = new User();
			u.setEmail(email);
			u.setPassword(password);
			uApi.regiest(u, new MyOnListener());
		}
	}
	
	private class MyOnListener implements OnNewsListener {

		@Override
		public void onComplete(String result) {
			zifengResponse = result;
			Message msg = new Message();
			msg.arg1 = HandMsgConstant.REGIEST_SUCCESS;
			zifengHandler.sendMessage(msg);
		}

		@Override
		public void onError(String error) {
			Toast.makeText(RegiestActivity.this, error, 0).show();
		}

		@Override
		public void onException(Exception exception) {
			exception.printStackTrace();
		}

	}
}
