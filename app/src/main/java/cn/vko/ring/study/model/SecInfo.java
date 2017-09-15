/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: SecInfo.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-19 下午4:22:40 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.io.Serializable;

import android.text.TextUtils;

/** 
 * @ClassName: SecInfo 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-19 下午4:22:40  
 */
public class SecInfo implements Serializable {
	private String rate;
	private String starNum;
	private String doneNum;
	private String rightNum;
	public String getRate() {
		return TextUtils.isEmpty(rate)?"0":rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getStarNum() {
		return TextUtils.isEmpty(starNum)?"0":starNum;
	}
	public void setStarNum(String starNum) {
		this.starNum = starNum;
	}
	public String getDoneNum() {
		
		return TextUtils.isEmpty(doneNum)?"0":doneNum;
	}
	public void setDoneNum(String doneNum) {
		this.doneNum = doneNum;
	}
	public String getRightNum() {
		return TextUtils.isEmpty(rightNum)?"0":rightNum;
	}
	public void setRightNum(String rightNum) {
		this.rightNum = rightNum;
	}
	
}