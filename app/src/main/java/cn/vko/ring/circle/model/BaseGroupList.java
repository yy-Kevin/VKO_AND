package cn.vko.ring.circle.model;

import java.util.List;

import cn.vko.ring.home.model.BaseResultInfo;

public class BaseGroupList extends BaseResultInfo {
	
	private List<GroupInfo> data;

	public List<GroupInfo> getData() {
		return data;
	}

	public void setData(List<GroupInfo> data) {
		this.data = data;
	}
	
	
}
