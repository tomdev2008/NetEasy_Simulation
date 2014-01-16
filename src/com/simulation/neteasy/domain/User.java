package com.simulation.neteasy.domain;

import java.io.Serializable;

import android.graphics.drawable.Drawable;
/**
 * 
 * @author 紫枫
 *
 */
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8719966917792624506L;
	private int _id;
	private String email;
	private String password;
	private String address;
	private String flag;
	private Drawable headImage;
	private byte[] img;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(int _id, String email, String password, String address,
			String flag, byte[] img) {
		super();
		this._id = _id;
		this.email = email;
		this.password = password;
		this.address = address;
		this.flag = flag;
		this.img = img;
	}

	public User(int _id, String email, String password, String address,
			String flag, Drawable headImage) {
		super();
		this._id = _id;
		this.email = email;
		this.password = password;
		this.address = address;
		this.flag = flag;
		this.headImage = headImage;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Drawable getHeadImage() {
		return headImage;
	}

	public void setHeadImage(Drawable headImage) {
		this.headImage = headImage;
	}

	@Override
	public String toString() {
		return "User [_id=" + _id + ", email=" + email + ", password="
				+ password + ", address=" + address + ", flag=" + flag
				+ ", headImage=" + headImage + "]";
	}

}
