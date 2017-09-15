/*
 *MemberInfo.java [V 1.0.0]
 *classes : cn.vko.ring.mine.model.MemberInfo
 *宣义阳 Create at 2015-8-4 下午2:08:06
 */
package cn.vko.ring.mine.model;

import java.io.Serializable;

/**
 * cn.vko.ring.mine.model.MemberInfo
 * @author 宣义阳 
 * create at 2015-8-4 下午2:08:06
 */
public class MemberInfo implements Serializable{

	private int code;
	private String msg;
	private long stime;
	private Data data;
	
	public long getStime() {
		return stime;
	}
	public void setStime(long stime) {
		this.stime = stime;
	}
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
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	
	
}
