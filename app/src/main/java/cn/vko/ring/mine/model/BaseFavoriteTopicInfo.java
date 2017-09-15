package cn.vko.ring.mine.model;

import java.io.Serializable;

import cn.vko.ring.home.model.BaseResultInfo;

public class BaseFavoriteTopicInfo extends BaseResultInfo {

	private CollectTopic data;
	

	public CollectTopic getData() {
		return data;
	}

	public void setData(CollectTopic data) {
		this.data = data;
	}

}
