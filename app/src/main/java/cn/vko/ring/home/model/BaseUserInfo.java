package cn.vko.ring.home.model;

import cn.vko.ring.common.base.BaseResponseInfo;

public class BaseUserInfo extends BaseResponseInfo {

	private UserInfo data;

	public UserInfo getData() {
		return data;
	}

	public void setData(UserInfo data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "BaseUserInfo [data=" + data + ", getCode()=" + getCode()
				+ ", getMsg()=" + getMsg() + ", getStime()=" + getStime() + "]";
	}

}
