package com.simulation.neteasy.domain;

import java.io.Serializable;

import android.graphics.drawable.Drawable;
/**
 * 
 * @author 紫枫
 *
 */
public class Comment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2227162017121440999L;
	private int id;
	private String text;
	private String email;
	private String userFlag;
	private String head;
	private String time;
	private int userId;
	private int newsId;
	private Drawable drawable;

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public Comment(int userId, int newsId, String text, String time,
			String userFlag) {
		super();
		this.text = text;
		this.userFlag = userFlag;
		this.time = time;
		this.userId = userId;
		this.newsId = newsId;
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Comment(int id, String text, String email, String userFlag,
			String head, String time) {
		super();
		this.id = id;
		this.text = text;
		this.email = email;
		this.userFlag = userFlag;
		this.head = head;
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserFlag() {
		return userFlag;
	}

	public void setUserFlag(String userFlag) {
		this.userFlag = userFlag;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", text=" + text + ", email=" + email
				+ ", userFlag=" + userFlag + ", head=" + head + ", time="
				+ time + "]";
	}

}
