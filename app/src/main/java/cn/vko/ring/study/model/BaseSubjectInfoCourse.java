package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.home.model.BaseResultInfo;

public class BaseSubjectInfoCourse extends BaseResultInfo{
	
	
	private List<SubjectInfoCourse> data;


	public List<SubjectInfoCourse> getData() {
		return data;
	}

	public void setData(List<SubjectInfoCourse> data) {
		this.data = data;
	}
	
	
}


