package com.simulation.neteasy.domain;

import java.io.Serializable;
import java.util.Arrays;

import android.graphics.Bitmap;
/**
 * 
 * @author 紫枫
 *
 */
public class NewsImage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5504100066696813465L;
	private int id;
	private byte[] imageByte;
	private String title;
	private int newsId;
	private String uri;
	private Bitmap drawable;

	
	public NewsImage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NewsImage(byte[] imageByte, String title, int newsId, String uri) {
		super();
		this.imageByte = imageByte;
		this.title = title;
		this.newsId = newsId;
		this.uri = uri;
	}

	public NewsImage(int id, byte[] imageByte, String title, int newsId,
			String uri, Bitmap drawable) {
		super();
		this.id = id;
		this.imageByte = imageByte;
		this.title = title;
		this.newsId = newsId;
		this.uri = uri;
		this.drawable = drawable;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getImageByte() {
		return imageByte;
	}

	public void setImageByte(byte[] imageByte) {
		this.imageByte = imageByte;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNewsId() {
		return newsId;
	}

	public void setNewsId(int newsId) {
		this.newsId = newsId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Bitmap getDrawable() {
		return drawable;
	}

	public void setDrawable(Bitmap drawable) {
		this.drawable = drawable;
	}

	@Override
	public String toString() {
		return "NewsImage [id=" + id + ", imageByte="
				+ Arrays.toString(imageByte) + ", title=" + title + ", newsId="
				+ newsId + ", uri=" + uri + ", drawable=" + drawable + "]";
	}

}
