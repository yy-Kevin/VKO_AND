package cn.vko.ring.common.base;

import java.io.Serializable;


/**
 * @author shikh
 *
 */
public class BaseResponseInfo implements Serializable{

	private int code;
	private String msg;
	private long stime;
	
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
	
	public long getStime() {
	
		return stime;
	}
	
	public void setStime(long stime) {
	
		this.stime = stime;
	}

	
	
//	public T getData() {
//	
//		return data;
//	}
//
//	
//	public void setData(T data) {
//	
//		this.data = data;
//	}
//	
	
	
}
