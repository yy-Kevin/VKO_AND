package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;
import cn.vko.ring.home.model.Pager;

public class BaseTopicInfo extends BaseResponseInfo {

	private BaseTopic data;

	
	public BaseTopic getData() {
	
		return data;
	}

	
	public void setData(BaseTopic data) {
	
		this.data = data;
	}
	
	

	
}
