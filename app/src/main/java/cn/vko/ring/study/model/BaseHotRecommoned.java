package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

public class BaseHotRecommoned implements Serializable{
	private String code;
	private String msg;
	private String stime;
	private List<HotRecommoned> data;
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
	public List<HotRecommoned> getData() {
		return data;
	}
	public void setData(List<HotRecommoned> data) {
		this.data = data;
	}
	
	
	
}

