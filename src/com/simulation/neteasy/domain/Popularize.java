package com.simulation.neteasy.domain;

import java.io.Serializable;

import android.graphics.drawable.Drawable;
/**
 * 
 * @author 紫枫
 *
 */
public class Popularize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5711668666126562669L;
	private int id;
	private String title;
	private String img;
	private Drawable imgDrawable;
	private byte[] imgByte;

	public Drawable getImgDrawable() {
		return imgDrawable;
	}

	public void setImgDrawable(Drawable imgDrawable) {
		this.imgDrawable = imgDrawable;
	}

	public byte[] getImgByte() {
		return imgByte;
	}

	public void setImgByte(byte[] imgByte) {
		this.imgByte = imgByte;
	}

	public Popularize() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Popularize(int id, String title, String img) {
		super();
		this.id = id;
		this.title = title;
		this.img = img;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "Popularize [id=" + id + ", title=" + title + ", img=" + img
				+ "]";
	}

}
