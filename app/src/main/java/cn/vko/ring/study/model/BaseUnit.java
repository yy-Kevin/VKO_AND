package cn.vko.ring.study.model;

import java.io.Serializable;

public class BaseUnit implements Serializable {
	private String code;
	private String msg;
	private String stime;
	private BaseUnitData data;
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
	public BaseUnitData getData() {
		return data;
	}
	public void setData(BaseUnitData data) {
		this.data = data;
	}
	
}
