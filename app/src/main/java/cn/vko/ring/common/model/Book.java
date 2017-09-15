/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: Book.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.common.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-27 下午4:30:28 
 * @version: V1.0   
 */
package cn.vko.ring.common.model;

import java.io.Serializable;

import cn.vko.ring.study.model.ParamData;

/**
 * @ClassName: Book
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-27 下午4:30:28
 */
public class Book implements Serializable {
	private String bookname;
	private String bookid;
	private String imgurl;
	private boolean selected;
	private ParamData paramData;

	public ParamData getParamData() {
		return paramData;
	}
	public void setParamData(ParamData paramData) {
		this.paramData = paramData;
	}
	public String getBookname() {
		return bookname;
	}
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	public String getBookid() {
		return bookid;
	}
	public void setBookid(String bookid) {
		this.bookid = bookid;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
