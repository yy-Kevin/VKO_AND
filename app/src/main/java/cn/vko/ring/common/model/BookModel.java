/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: BookModel.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.common.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-27 下午4:28:42 
 * @version: V1.0   
 */
package cn.vko.ring.common.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.home.model.BaseResultInfo;

/** 
 * @ClassName: BookModel 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-27 下午4:28:42  
 */
public class BookModel implements Serializable{
	private String versionId;
	private String versionName;
	private boolean selected;
	private List<Book> bookList;
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public List<Book> getBookList() {
		return bookList;
	}
	public void setBookList(List<Book> bookList) {
		this.bookList = bookList;
	}
	
}
