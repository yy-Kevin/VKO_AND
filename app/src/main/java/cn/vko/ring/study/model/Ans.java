/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: Ans.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-19 下午4:21:30 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.io.Serializable;

/** 
 * @ClassName: Ans 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-19 下午4:21:30  
 */
public  class Ans implements Serializable {
	private String id;
	private boolean right;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isRight() {
		return right;
	}
	public void setRight(boolean right) {
		this.right = right;
	}
	
}
