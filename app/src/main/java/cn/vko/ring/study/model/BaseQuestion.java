package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;

public class BaseQuestion implements Serializable {
	
	private List<QuestionInfo> examList;

	private String id;

	public List<QuestionInfo> getExamList() {
		return examList;
	}

	public void setExamList(List<QuestionInfo> examList) {
		this.examList = examList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
}
