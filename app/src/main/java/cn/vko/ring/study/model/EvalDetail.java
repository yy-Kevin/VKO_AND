/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: EvalDetail.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-19 下午4:23:11 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

/** 
 * @ClassName: EvalDetail 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-19 下午4:23:11  
 */
public class EvalDetail implements Serializable{
	
	private String 	trackId;
	private String 	starNum;
	private String 	k1Name;
	private String 	crTime;
	private String 	k1;
	private List<Ans> ans;


	public String getTrackId() {
		return trackId;
	}
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}
	public String getStarNum() {
		return starNum;
	}
	public void setStarNum(String starNum) {
		this.starNum = starNum;
	}
	public String getK1Name() {
		return k1Name;
	}
	public void setK1Name(String k1Name) {
		this.k1Name = k1Name;
	}
	public String getCrTime() {
		return crTime;
	}
	public void setCrTime(String crTime) {
		this.crTime = crTime;
	}
	public String getK1() {
		return k1;
	}
	public void setK1(String k1) {
		this.k1 = k1;
	}
	public List<Ans> getAns() {
		return ans;
	}
	public void setAns(List<Ans> ans) {
		this.ans = ans;
	}
	}
	