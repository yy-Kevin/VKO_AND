/**   
 * Copyright © 2016 cn.vko.com. All rights reserved.
 * 
 * @Title: Pager.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.mine.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2016-1-4 上午10:13:20 
 * @version: V1.0   
 */
package cn.vko.ring.mine.model;

import java.io.Serializable;

/** 
 * @ClassName: Pager 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2016-1-4 上午10:13:20  
 */
public class Pager implements Serializable {
	private int pageNo;
	private int pageTotal;
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageTotal() {
		return pageTotal;
	}
	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}
	
}
