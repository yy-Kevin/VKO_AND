/*
 *TreeCourseInfo.java [V 1.0.0]
 *classes : cn.vko.ring.mine.model.TreeCourseInfo
 *宣义阳 Create at 2015-8-6 上午11:35:46
 */
package cn.vko.ring.mine.model;

import java.io.Serializable;
import java.util.List;

/**
 * cn.vko.ring.mine.model.TreeCourseInfo
 * @author 宣义阳 
 * create at 2015-8-6 上午11:35:46
 */
public class TreeCourseInfo implements Serializable {

	public TreeCourseInfo(String courseTitle){
		super();
		this.courseTitle = courseTitle;
	}
	private String courseTitle;
	private List<TreeCourseInfo> mSonCourse;
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public List<TreeCourseInfo> getmSonCourse() {
		return mSonCourse;
	}
	public void setmSonCourse(List<TreeCourseInfo> mSonCourse) {
		this.mSonCourse = mSonCourse;
	}
	
}
