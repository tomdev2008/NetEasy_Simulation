package com.simulation.neteasy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.simulation.neteasy.adapter.CityAdapter;
import com.simulation.neteasy.util.HanziToPinyin;
import com.simulation.neteasy.util.InitCity;
import com.zifeng.wangyi.R;

/**
 * 
 * @author 紫枫
 *
 */
public class CityActivity extends Activity {

	private List<String> zifengSort;
	private HanziToPinyin zifengPinyin;
	//正常状态
	private List<String> zifengList;
	//输入框存在内容状态
	private EditText zifengActvCityHitn;
	private int zifengLength;
	
	private ListView zifengCity;
	private CityAdapter cityAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city);
		
		zifengPinyin = HanziToPinyin.getInstance();
		zifengCity = (ListView) findViewById(R.id.lv_city);
		zifengSort = InitCity.sort();
		zifengList = getList();
		cityAdapter = new CityAdapter(zifengList,this);
		zifengCity.setAdapter(cityAdapter);
		zifengActvCityHitn = (EditText) findViewById(R.id.actv_city_hitn);
		zifengActvCityHitn.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				zifengLength = s.length();
//				hitnList.clear();
				if (zifengLength != 0) {
					for (int i = 0; i < zifengList.size(); i++) {
						String city = zifengList.get(i);
						if ( city.length() >= zifengLength &&(s.toString()).equals(city.substring(0, zifengLength))) {
							zifengCity.setSelection(i);
						}
					}
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		zifengCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (zifengList.get(position).length() > 1) {
					Intent data = new Intent();
					data.putExtra("cityName", zifengList.get(position));
					setResult(200, data );
					finish();
				}
			}
		});
	}
	
	//得到插入首字母的城市集合
	public List<String> getList(){
		List<String> list = new ArrayList<String>();
		//遍历得到的城市集合
		for (String string : zifengSort) {
			//得到首字拼音
			String frist = zifengPinyin.get(string).get(0).target.charAt(0) + "";
			//如果list为0 就添加这个城市和他的首字字母
			if (list.size() == 0) {
				list.add(frist);
				list.add(string);
			} else {
				//否则判断 是否当前这个城市的首字字母存在在集合
				String item = zifengPinyin.get(list.get(list.size() - 1)).get(0).target.charAt(0) + "";
				//存在就直接添加这个城市
				if (frist.equals(item)) {
					list.add(string);
					//不然还要加他的首字字母
				} else {
					list.add(frist);
					list.add(string);
				} 
			}
		}
		return list;
	}

	public void back(View v) {
		finish();
		overridePendingTransition(0, R.anim.out_news_text);
	}
}
