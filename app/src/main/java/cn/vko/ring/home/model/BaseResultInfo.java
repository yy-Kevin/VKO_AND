package cn.vko.ring.home.model;

import java.io.Serializable;

public class BaseResultInfo implements Serializable {

	private int code;
	private String msg;
	private String stime;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
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
	
}
