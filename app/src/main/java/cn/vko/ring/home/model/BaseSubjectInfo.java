package cn.vko.ring.home.model;

import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;


/**
 * @author shikh
 *
 */
public class BaseSubjectInfo extends BaseResponseInfo{

	private List<SubjectInfo> data;

	
	public List<SubjectInfo> getData() {
	
		return data;
	}

	
	public void setData(List<SubjectInfo> data) {
	
		this.data = data;
	}
	
	
}
