/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: TestSection.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.study.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-26 下午2:49:55 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.io.Serializable;

/** 
 * @ClassName: TestSection 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-26 下午2:49:55  
 */
public class TestSection implements Serializable{
	private String courseId;//节id
	private String courseName;
	private String star;
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	
}
