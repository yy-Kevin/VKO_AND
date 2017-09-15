package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

public class TonBuCourse implements Serializable{
	private String code;
	private String msg;
	private String stime;
	 
	private List<TonBuCourseSection> data;

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

	public List<TonBuCourseSection> getData() {
		return data;
	}

	public void setData(List<TonBuCourseSection> data) {
		this.data = data;
	}
	
	
}

