/*
 * Created on 2005-11-22
 *
 */
package com.test;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 * 2005-11-22
 *
 */
class Customer {

	private String code;
	private String name;
	private String status;

	public Customer() {
		this.code = "";
		this.name = "";
		this.status = "";
	}

	public Customer(String code, String name, String status) {
		this.code = code;
		this.name = name;
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String string) {
		code = string;
	}

	public String getName() {
		return name;
	}

	public void setName(String string) {
		name = string;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String string) {
		status = string;
	}
}
