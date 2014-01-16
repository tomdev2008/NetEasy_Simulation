package com.simulation.neteasy.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Environment;

import com.simulation.neteasy.constant.XmlConstant;
import com.simulation.neteasy.domain.City;
/**
 * 
 * @author 紫枫
 *
 */
public class InitCity {

	@SuppressWarnings("unchecked")
	public static List<String> sort() {
		// 城市信息集合
		File cityFile = new File(Environment.getExternalStorageDirectory(),
				XmlConstant.CITY_XML_path);
		List<City> citys = StreamOperate.getCitys(cityFile);
		//城市名称集合
		List<String> cityList = new ArrayList<String>();
		// 抽取城市名称
		for (City city : citys) {
			cityList.add(city.getName());
		}
		// 排序好的城市名称集合
		ChineseCharComp comp = new ChineseCharComp();
		Collections.sort(cityList, comp);
		return cityList;
	}
}
