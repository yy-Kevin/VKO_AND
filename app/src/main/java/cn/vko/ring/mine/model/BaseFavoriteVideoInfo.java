package cn.vko.ring.mine.model;

import java.io.Serializable;

import cn.vko.ring.home.model.BaseResultInfo;

public class BaseFavoriteVideoInfo extends BaseResultInfo {

	private CollectVideo data;

	public CollectVideo getData() {
		return data;
	}

	public void setData(CollectVideo data) {
		this.data = data;
	}

}
