package cn.vko.ring.mine.model;

import java.io.Serializable;

public class BaseResultInfo implements Serializable {

	private int code;
	private String msg;
	private long servertime;
	private boolean data;
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
	public long getServertime() {
		return servertime;
	}
	public void setServertime(long servertime) {
		this.servertime = servertime;
	}
	public boolean isData() {
		return data;
	}
	public void setData(boolean data) {
		this.data = data;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BaseResultInfo [code=" + code + ", msg=" + msg
				+ ", servertime=" + servertime + ", data=" + data + "]";
	}
	
	
	
}
