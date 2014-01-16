package com.simulation.neteasy.domain;

import java.io.Serializable;

import android.graphics.drawable.BitmapDrawable;
/**
 * 
 * @author 紫枫
 *
 */
public class AppInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3258290291981841527L;
	private int id;
	private String name;
	private String version;
	private String size;
	private int level;
	private String uri;
	private BitmapDrawable appIcon;
	private String iconUri;

	public AppInfo(int id, String name, String version, String size, int level,
			String uri, String iconUri) {
		super();
		this.id = id;
		this.name = name;
		this.version = version;
		this.size = size;
		this.level = level;
		this.uri = uri;
		this.iconUri = iconUri;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public AppInfo(int id, String name, String version, String size, int level,
			String uri) {
		super();
		this.id = id;
		this.name = name;
		this.version = version;
		this.size = size;
		this.level = level;
		this.uri = uri;
	}

	public AppInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AppInfo(int id, String name, String version, String size, int level,
			String uri, BitmapDrawable appIcon) {
		super();
		this.id = id;
		this.name = name;
		this.version = version;
		this.size = size;
		this.level = level;
		this.uri = uri;
		this.appIcon = appIcon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public BitmapDrawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(BitmapDrawable appIcon) {
		this.appIcon = appIcon;
	}

	@Override
	public String toString() {
		return "AppInfo [id=" + id + ", name=" + name + ", version=" + version
				+ ", size=" + size + ", level=" + level + ", uri=" + uri
				+ ", appIcon=" + appIcon + "]";
	}

}
