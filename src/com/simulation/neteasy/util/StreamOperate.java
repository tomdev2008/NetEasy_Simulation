package com.simulation.neteasy.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import com.simulation.neteasy.domain.City;

public class StreamOperate {

	/**
	 * 将流转成byte数组
	 * 
	 * @param is
	 * @return 紫枫
	 */
	public static byte[] parseInputStream(InputStream is) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			int leng;
			while ((leng = is.read(data)) != -1) {
				bos.write(data, 0, leng);
			}
			byte[] result = bos.toByteArray();
			bos.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean writeCitysXml(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlSerializer serializer = factory.newSerializer();
			serializer.setOutput(fos, "UTF-8");
			serializer.startDocument("utf-8", true);
			serializer.startTag("", "citys");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("长沙");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101250101");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("邵阳");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101250901");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("常德");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101250601");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("郴州");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101250501");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("吉首");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101251501");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("株洲");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101250301");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("娄底");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101250801");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("湘潭");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101250201");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("益阳");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101250701");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("永州");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101251401");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("岳阳");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101251001");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("衡阳");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101250401");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("怀化");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101251201");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("韶山");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101250202");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.startTag("", "city");
			serializer.startTag("", "name");
			serializer.text("张家界");
			serializer.endTag("", "name");
			serializer.startTag("", "code");
			serializer.text("101251101");
			serializer.endTag("", "code");
			serializer.endTag("", "city");

			serializer.endTag("", "citys");
			serializer.endDocument();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static List<City> getCitys(File file) {
		List<City> list = new ArrayList<City>();
		City city = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			FileInputStream fis = new FileInputStream(file);
			parser.setInput(fis, "utf-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tag_name = null;
				switch (eventType) {
				case XmlPullParser.START_TAG:
					tag_name = parser.getName();
					if (tag_name.equals("city")) {
						city = new City();
					} else if (tag_name.equals("name")) {
						String name = parser.nextText();
						city.setName(name);
					} else if (tag_name.equals("code")) {
						String code = parser.nextText();
						city.setCode(code);
					} 
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("city")) {
						list.add(city);
					}
					break;
				default:
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
