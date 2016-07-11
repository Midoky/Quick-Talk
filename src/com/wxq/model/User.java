package com.wxq.model;

import java.io.Serializable;


public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1699305551861438544L;
	private String userName;
	private String userPsw;	
	private String state = "0";
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPsw() {
		return userPsw;
	}
	public void setUserPsw(String userPsw) {
		this.userPsw = userPsw;
	}
}
