package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.home.model.BaseResultInfo;
import cn.vko.ring.home.model.SubjectInfo;

public class KnowledgeRecommond extends BaseResultInfo {
	

	private List<KnowledgePointK1> data;


	public List<KnowledgePointK1> getData() {
		return data;
	}

	public void setData(List<KnowledgePointK1> data) {
		this.data = data;
	}

	

}
