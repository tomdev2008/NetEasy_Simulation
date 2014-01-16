package com.simulation.neteasy.domain;

import java.io.Serializable;

/**
 * 
 * @author 紫枫
 *
 */
public class City implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7210649986791168221L;
	private String name;
	private String code;

	public City() {
		super();
		// TODO Auto-generated constructor stub
	}

	public City(String name, String code) {
		super();
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "City [name=" + name + ", code=" + code + "]";
	}

}
