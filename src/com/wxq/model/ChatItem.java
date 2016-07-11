package com.wxq.model;

import java.io.Serializable;

public class ChatItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8132434376968025120L;
	private int id;
	private String user;
	private String content;
	private String roomId;
	private boolean res;
	private String time;
	
	public ChatItem(int id, String user,String content,String roomId,boolean res){
		this.setId(id);
		this.setContent(content);
		this.setRes(res);
		this.setRoomId(roomId);
		this.setUser(user);
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public boolean isRes() {
		return res;
	}
	public void setRes(boolean res) {
		this.res = res;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
}
