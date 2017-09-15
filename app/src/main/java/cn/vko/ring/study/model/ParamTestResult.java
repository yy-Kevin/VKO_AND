/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ParamTestResult.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-8-13 下午4:07:46 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.io.Serializable;

/**
 * @ClassName: ParamTestResult
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-8-13 下午4:07:46
 */
public class ParamTestResult implements Serializable {
	public static final String PARAM_TEST_RESULT = "ParamTestResult";
	private boolean isComeTest;
	private int index;
	private String trackId;
	private String headUrl;
	private String title;
	private String subjectId;
	
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isComeTest() {
		return isComeTest;
	}
	public void setComeTest(boolean isComeTest) {
		this.isComeTest = isComeTest;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getTrackId() {
		return trackId;
	}
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}
	
}
