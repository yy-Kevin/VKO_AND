package cn.vko.ring.study.model;

import java.io.Serializable;

import cn.vko.ring.common.base.BaseResponseInfo;

public class BaseQuestionInfo extends BaseResponseInfo {
	private BaseQuestion data;

	public BaseQuestion getData() {
		return data;
	}

	public void setData(BaseQuestion data) {
		this.data = data;
	}
	
	
}
