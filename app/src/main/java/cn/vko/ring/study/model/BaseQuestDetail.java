package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

public class BaseQuestDetail implements Serializable {
	private String imgUrl;
	private String id;
	private int flag;
	private QuestionInfo exam;
	private List<QuestionInfo> examList;
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public QuestionInfo getExam() {
		return exam;
	}
	public void setExam(QuestionInfo exam) {
		this.exam = exam;
	}
	public List<QuestionInfo> getExamList() {
		return examList;
	}
	public void setExamList(List<QuestionInfo> examList) {
		this.examList = examList;
	}
	
	
	
}
