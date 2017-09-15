package cn.vko.ring.study.model;

import java.io.Serializable;

public class KnowledgePointK1 implements Serializable{
	private String id;//  一级知识点id
	private String name;
	private String subjectId;
	
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

