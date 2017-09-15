package cn.vko.ring.study.model;

import cn.vko.ring.common.base.BaseResponseInfo;

public class BaseQuestDetailInfo extends BaseResponseInfo {
	private BaseQuestDetail data;

	public BaseQuestDetail getData() {
		return data;
	}

	public void setData(BaseQuestDetail data) {
		this.data = data;
	}
	
	
}
