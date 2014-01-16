package com.simulation.neteasy.domain;

import java.io.Serializable;
import java.util.Arrays;
/**
 * 
 * @author 紫枫
 *
 */
public class NewsToutiao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8827716486168571394L;
	private int id;
	private String title;
	private String text;
	private String time;
	private String address;
	private int comment;
	private String type;
	private byte[] newsImage;

	public NewsToutiao() {
		super();
	}

	public NewsToutiao(int id, String title, String text, String time,
			String address, int comment, String type) {
		super();
		this.id = id;
		this.title = title;
		this.text = text;
		this.time = time;
		this.address = address;
		this.comment = comment;
		this.type = type;
	}

	public NewsToutiao(String title, String text, String time, String address,
			int comment, String type, byte[] newsImage) {
		super();
		this.title = title;
		this.text = text;
		this.time = time;
		this.address = address;
		this.comment = comment;
		this.type = type;
		this.newsImage = newsImage;
	}

	public NewsToutiao(int id, String title, String text, String time,
			String address, int comment, String type, byte[] newsImage) {
		super();
		this.id = id;
		this.title = title;
		this.text = text;
		this.time = time;
		this.address = address;
		this.comment = comment;
		this.type = type;
		this.newsImage = newsImage;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getComment() {
		return comment;
	}

	public void setComment(int comment) {
		this.comment = comment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getNewsImage() {
		return newsImage;
	}

	public void setNewsImage(byte[] newsImage) {
		this.newsImage = newsImage;
	}

	@Override
	public String toString() {
		return "NewsToutiao [id=" + id + ", title=" + title + ", text=" + text
				+ ", time=" + time + ", address=" + address + ", comment="
				+ comment + ", type=" + type + ", newsImage="
				+ Arrays.toString(newsImage) + "]";
	}

}
