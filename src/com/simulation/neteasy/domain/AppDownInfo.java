package com.simulation.neteasy.domain;

import java.io.Serializable;

import android.graphics.Bitmap;
/**
 * 
 * @author 紫枫
 *
 */
public class AppDownInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6133260116158798705L;
	private int _id;
	private int appId;
	private String name;
	private int threadId;
	private int downLength;
	private int state;
	private Bitmap appIcon;
	private int maxLength;

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(long maxLength) {
		this.maxLength = (int) maxLength;
	}

	public Bitmap getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Bitmap appIcon) {
		this.appIcon = appIcon;
	}

	public AppDownInfo(int _id, int appId, String name, int threadId,
			int downLength, int state) {
		super();
		this._id = _id;
		this.appId = appId;
		this.name = name;
		this.threadId = threadId;
		this.downLength = downLength;
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public AppDownInfo(int appId, String name, int threadId, int downLength) {
		super();
		this.appId = appId;
		this.name = name;
		this.threadId = threadId;
		this.downLength = downLength;
	}

	public AppDownInfo(int _id, int appId, String name, int downLength,
			int state) {
		super();
		this._id = _id;
		this.appId = appId;
		this.name = name;
		this.downLength = downLength;
		this.state = state;
	}

	public AppDownInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	public int getDownLength() {
		return downLength;
	}

	public void setDownLength(int downLength) {
		this.downLength = downLength;
	}

	@Override
	public String toString() {
		return "AppDownInfo [_id=" + _id + ", appId=" + appId + ", name="
				+ name + ", threadId=" + threadId + ", downLength="
				+ downLength + ", state=" + state + ", appIcon=" + appIcon
				+ "]";
	}

}
