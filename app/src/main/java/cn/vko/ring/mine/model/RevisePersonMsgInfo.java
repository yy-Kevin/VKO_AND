package cn.vko.ring.mine.model;

import java.io.Serializable;

public class RevisePersonMsgInfo implements Serializable{

	private String code;
	private String msg;
	private String stime;
	private boolean data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public boolean isData() {
		return data;
	}
	public void setData(boolean data) {
		this.data = data;
	}
	
	
}
