package com.simulation.neteasy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.simulation.neteasy.api.UserApi;
import com.simulation.neteasy.constant.HandMsgConstant;
import com.simulation.neteasy.constant.DBConstant.TABLE.TABLE_LINE;
import com.simulation.neteasy.db.DBUtil;
import com.simulation.neteasy.domain.User;
import com.simulation.neteasy.inter.OnNewsListener;
import com.simulation.neteasy.util.JsonUtil;
import com.zifeng.wangyi.R;

/**
 * 
 * @author 紫枫
 *
 */
public class LoginActivity extends Activity {

	private TextView zifengForgetPassword;
	private EditText zifengEmailLogin;
	private EditText zifengPasswordLogin;
	private ProgressBar zifengLogin;
	
	private String zifengState;
	private DBUtil zifengDBUtil;
	
	private Handler zifengHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case HandMsgConstant.SUCCESS:
				if ("error".equals(zifengState)) {
					Toast.makeText(LoginActivity.this, "用户不存在!", 0).show();
					zifengLogin.setVisibility(View.GONE);
				} else {
					User u = JsonUtil.parseUserJson(zifengState);
					zifengDBUtil.clear();
					zifengDBUtil.clearCollect();
					ContentValues values = new ContentValues();
					System.out.println(u.get_id());
					values.put(TABLE_LINE.ID, u.get_id());
					values.put(TABLE_LINE.EMAIL, u.getEmail());
					values.put(TABLE_LINE.PASSWORD, u.getPassword());
					values.put(TABLE_LINE.ADDRESS, u.getAddress());
					values.put(TABLE_LINE.FLAG, u.getFlag());
					System.out.println("服务器上返回的用户头像：——"+u.getImg());
					values.put(TABLE_LINE.HEADIMAGE, u.getImg());
					zifengDBUtil.save(values);
					zifengDBUtil.close();
					Intent intent = new Intent("com.huaao.login.success");
					LoginActivity.this.sendBroadcast(intent);
					LoginActivity.this.finish();
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
		setContentView(R.layout.login);

		zifengDBUtil = new DBUtil(LoginActivity.this);
		// 忘记密码
		zifengForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
		zifengForgetPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,
						ForgetActivity.class));
			}
		});

		zifengEmailLogin = (EditText) findViewById(R.id.et_email_login);
		zifengPasswordLogin = (EditText) findViewById(R.id.et_password_login);
		zifengLogin = (ProgressBar) findViewById(R.id.pb_login);
	}

	// 注册
	public void regiest(View v) {
		startActivity(new Intent(this, RegiestActivity.class));
	}

	public void back(View v) {
		finish();
		overridePendingTransition(0, R.anim.out_news_text);
	}

	// 验证邮箱
	public boolean checkForm(String content) {
		String pattern = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(content);
		return matcher.matches();
	}

	// 登录
	public void login(View v) {
		String email = zifengEmailLogin.getText().toString();
		String password = zifengPasswordLogin.getText().toString();
		if (!checkForm(email)) {
			Toast.makeText(this, "请输入正确邮箱", 0).show();
		} else if (password == "" || password == null) {
			Toast.makeText(this, "请输入密码", 0).show();
		} else {
			zifengLogin.setVisibility(View.VISIBLE);
			UserApi uApi = new UserApi();
			uApi.login(email, password, new MyListener());
		}
	}

	

	private class MyListener implements OnNewsListener {

		@Override
		public void onComplete(String result) {
			zifengState = result;
			Message msg = new Message();
			msg.arg1 = HandMsgConstant.SUCCESS;
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
}
