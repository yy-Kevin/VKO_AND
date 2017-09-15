package cn.vko.ring.study.model;

import java.io.Serializable;

public class PagerCount implements Serializable{
	private int pageTotal;
	private int pageNo;
	
	public int getPageTotal() {
		return pageTotal;
	}
	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	
}

