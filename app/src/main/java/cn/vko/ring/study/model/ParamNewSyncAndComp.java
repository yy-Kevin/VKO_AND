/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ParamNewSyncAndComp.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-18 下午5:15:10 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.io.Serializable;

import android.text.TextUtils;

/**
 * @ClassName: ParamNewSyncAndComp
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-18 下午5:15:10
 */
public class ParamNewSyncAndComp implements Serializable {
	// knowledgeId=3965&subjectId=25&token=7d7dfd780d2c4b7797feb5bdf874b9b5
	// sync
	// courseId=3031&bookId=110&subjectId=26&token=7d7dfd780d2c4b7797feb5bdf874b9b5&courseType=41&learnId=3
	public static final String PARAMNEWSYNCCOMP = "ParamNewSyncAndComp";
	private String courseId;
	private String bookId;
	private String subjectId;
	private String courseType;
	private String knowledgeId;
	private String courseName;
	private String knowledgeName;
	private String ht;
	private String k1;
	private String sectionId;
	private boolean isSubmitCome;

	/**
	 * 同步
	 * @Description:TODO
	 */
	public ParamNewSyncAndComp(String courseId, String bookId, String subjectId, String courseType, String courseName,
			String ht, String sectionId, boolean isSubmitCome) {
		super();
		this.courseId = courseId;
		this.bookId = bookId;
		this.subjectId = subjectId;
		this.courseType = courseType;
		this.courseName = courseName;
		this.ht = ht;
		this.sectionId = sectionId;
	}
	/**
	 * 综合
	 * @Description:TODO
	 */
	public ParamNewSyncAndComp(String subjectId, String courseType, String knowledgeId, String knowledgeName,
			String ht, String k1, boolean isSubmitCome) {
		super();
		this.subjectId = subjectId;
		this.courseType = courseType;
		this.knowledgeId = knowledgeId;
		this.knowledgeName = knowledgeName;
		this.ht = ht;
		this.k1 = k1;
	}
	public boolean isSubmitCome() {
		return isSubmitCome;
	}
	public void setSubmitCome(boolean isSubmitCome) {
		this.isSubmitCome = isSubmitCome;
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	public String getK1() {
		return k1;
	}
	public void setK1(String k1) {
		this.k1 = k1;
	}
	public String getHt() {
		return ht;
	}
	public void setHt(String ht) {
		this.ht = ht;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getKnowledgeName() {
		return knowledgeName;
	}
	public void setKnowledgeName(String knowledgeName) {
		this.knowledgeName = knowledgeName;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getKnowledgeId() {
		return knowledgeId;
	}
	public void setKnowledgeId(String knowledgeId) {
		this.knowledgeId = knowledgeId;
	}
	public boolean isSync() {
		return TextUtils.isEmpty(courseType) ? false : courseType.equals("41");
	}
}
