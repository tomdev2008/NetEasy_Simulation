package com.simulation.neteasy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simulation.neteasy.api.WeatherApi;
import com.simulation.neteasy.constant.HandMsgConstant;
import com.simulation.neteasy.constant.XmlConstant;
import com.simulation.neteasy.domain.City;
import com.simulation.neteasy.domain.WeatherInfo;
import com.simulation.neteasy.inter.OnNewsListener;
import com.simulation.neteasy.inter.OnWeatherImgListener;
import com.simulation.neteasy.util.JsonUtil;
import com.simulation.neteasy.util.StreamOperate;
import com.zifeng.wangyi.R;

/**
 * 
 * @author 紫枫
 *
 */
public class WeatherActivity extends Activity {
	private WeatherInfo zifengWeatherInfo;
	private TextView zifengWeatherCity;
	private TextView zifengWeather;
	private ImageView zifengWeatherS;
	private ImageView zifengWeather2;
	private TextView zifengWeatherWeather;
	private TextView zifengWeatherUpdateTime;
	private WeatherApi zifengWApi;
	
	private String zifengCode;
	
	private Drawable zifengImage1;
	private Drawable zifengImage2;
	private List<City> zifengCitys;
	
	private Handler zifengHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case HandMsgConstant.SUCCESS:
				zifengWeather.setText(zifengWeatherInfo.getTemp1() + "/"
						+ zifengWeatherInfo.getTemp2());
				zifengWeatherWeather.setText(zifengWeatherInfo.getWeather());
				zifengWeatherUpdateTime
						.setText(zifengWeatherInfo.getPtime() + "   更新");
				String zifengImage1 = zifengWeatherInfo.getImage1();
				String zifengImage2 = zifengWeatherInfo.getImage2();
				zifengWApi.getWeatherImg(zifengImage1, zifengImage2, new MyOnWeatherImgListener());
				break;
			case HandMsgConstant.IMAGE_LOAD_COMPLETE:
				zifengWeatherS.setBackgroundDrawable(WeatherActivity.this.zifengImage2);
				zifengWeather2.setBackgroundDrawable(WeatherActivity.this.zifengImage1);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather);
		// 得到城市信息集合
		File cityFile = new File(Environment.getExternalStorageDirectory(),
				XmlConstant.CITY_XML_path);
		zifengCitys = StreamOperate.getCitys(cityFile);
		// 城市
		zifengWeatherCity = (TextView) findViewById(R.id.tv_weather_city);
		// 温度
		zifengWeather = (TextView) findViewById(R.id.tv_weather_T);
		// 图片1
		zifengWeatherS = (ImageView) findViewById(R.id.iv_weather);
		// 图片1
		zifengWeather2 = (ImageView) findViewById(R.id.iv_weather2);
		// 天气
		zifengWeatherWeather = (TextView) findViewById(R.id.tv_weather_weather);
		// 更新时间
		zifengWeatherUpdateTime = (TextView) findViewById(R.id.tv_weather_update_time);
		date();
		zifengWApi = new WeatherApi();
		// 默认长沙天气
		zifengWApi.getWeather("101250101", new MyOnWeatherListener());
	}


	// 获得天气图片回调
	private class MyOnWeatherImgListener implements OnWeatherImgListener {
		@Override
		public void onComplete(Drawable img1, Drawable img2) {
			zifengImage1 = img1;
			zifengImage2 = img2;
			Message msg = new Message();
			msg.arg1 = HandMsgConstant.IMAGE_LOAD_COMPLETE;
			zifengHandler.sendMessage(msg);
		}
	}

	// 选择城市
	public void selectCity(View v) {
		Intent mIntent = new Intent(this, CityActivity.class);
		startActivityForResult(mIntent, 100);
		overridePendingTransition(R.anim.otherin, R.anim.hold);
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 请求码 与返回码匹配
		if (requestCode == 100 && resultCode == 200) {
			String cityName = data.getStringExtra("cityName");
			zifengWeatherCity.setText(cityName);
			for (City city : zifengCitys) {
				if (city.getName().equals(cityName)) {
					zifengCode = city.getCode();
					break;
				}
			}
			zifengWApi = new WeatherApi();
			zifengWApi.getWeather(zifengCode, new MyOnWeatherListener());
		}
	}

	// 获得天气信息回调
	private class MyOnWeatherListener implements OnNewsListener {

		@Override
		public void onComplete(String result) {
			zifengWeatherInfo = JsonUtil.parseWeatherJson(result);
			Message msg = new Message();
			msg.arg1 = HandMsgConstant.SUCCESS;
			zifengHandler.sendMessage(msg);
		}

		@Override
		public void onError(String error) {

		}

		@Override
		public void onException(Exception exception) {

		}

	}

	private void date() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = new Date();
		String dateInfo = dateFormat.format(date);
		TextView tv_weather_date = (TextView) findViewById(R.id.tv_weather_date);
		tv_weather_date.setText(dateInfo);
	}

	public void back(View v) {
		finish();
		overridePendingTransition(0, R.anim.out_news_text);
	}
}
