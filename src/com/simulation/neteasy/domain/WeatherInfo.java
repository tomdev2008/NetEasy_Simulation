package com.simulation.neteasy.domain;

import java.io.Serializable;
/**
 * 
 * @author 紫枫
 *
 */
public class WeatherInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -280203335298560062L;
	private String city;
	private String cityid;
	private String temp1;
	private String temp2;
	private String weather;
	private String ptime;
	private String image1;
	private String image2;

	public WeatherInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WeatherInfo(String city, String cityid, String temp1, String temp2,
			String weather, String ptime, String image1, String image2) {
		super();
		this.city = city;
		this.cityid = cityid;
		this.temp1 = temp1;
		this.temp2 = temp2;
		this.weather = weather;
		this.ptime = ptime;
		this.image1 = image1;
		this.image2 = image2;
	}

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public WeatherInfo(String city, String temp1, String temp2, String weather,
			String ptime) {
		super();
		this.city = city;
		this.temp1 = temp1;
		this.temp2 = temp2;
		this.weather = weather;
		this.ptime = ptime;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getPtime() {
		return ptime;
	}

	public void setPtime(String ptime) {
		this.ptime = ptime;
	}

	@Override
	public String toString() {
		return "WeatherInfo [city=" + city + ", cityid=" + cityid + ", temp1="
				+ temp1 + ", temp2=" + temp2 + ", weather=" + weather
				+ ", ptime=" + ptime + ", image1=" + image1 + ", image2="
				+ image2 + "]";
	}

}
