package cn.vko.ring.mine.model;

import java.io.Serializable;

import cn.vko.ring.common.base.BaseResponseInfo;
import cn.vko.ring.home.model.UserInfo;

public class BaseUserInfo extends BaseResponseInfo {
	
	private UserInfo data;

	public UserInfo getData() {
		return data;
	}

	public void setData(UserInfo data) {
		this.data = data;
	}
	
}
