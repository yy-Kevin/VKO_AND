package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Section implements Serializable{
	
	private String code;
	private String msg;
	private String stime;
	
	private SynchronousCourses data;
			
	public SynchronousCourses getData() {
		return data;
	}
	public void setData(SynchronousCourses data) {
		this.data = data;
	}
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
	
	
}


