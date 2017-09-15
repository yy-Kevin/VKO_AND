/*******************************************************************************
 * Copyright (c) 2015 by Luxury Corporation all right reserved.
 * 2015-8-17 
 * 
 *******************************************************************************/ 
package cn.vko.ring.mine.model;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 
 * 编写日期:	2015-8-17
 * 作者:	宋宁宁
 * 
 * 历史记录
 *    修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class ErrNotepadInfo implements Serializable{

	public int code;
	public String msg;
	public String stime;

	public List<Data> data;

	public class Data implements Serializable {
		public int id;
		public String cnt;
	}

	/**
	 * @return ssssssss
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return ssssssss
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return ssssssss
	 */
	public String getStime() {
		return stime;
	}

	/**
	 * @param stime the stime to set
	 */
	public void setStime(String stime) {
		this.stime = stime;
	}

	/**
	 * @return ssssssss
	 */
	public List<Data> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<Data> data) {
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ErrNotepadInfo [code=" + code + ", msg=" + msg + ", stime="
				+ stime + ", data=" + data + "]";
	}
	
}
