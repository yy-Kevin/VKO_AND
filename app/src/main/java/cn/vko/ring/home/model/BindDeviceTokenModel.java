package cn.vko.ring.home.model;

import cn.vko.ring.common.base.BaseResponseInfo;

public class BindDeviceTokenModel extends BaseResponseInfo {
	private BindResult data;

	public static class BindResult{
		private boolean isband;
		private boolean unband;
		public boolean isIsband() {
			return isband;
		}
		public void setIsband(boolean isband) {
			this.isband = isband;
		}
		public boolean isUnband() {
			return unband;
		}
		public void setUnband(boolean unband) {
			this.unband = unband;
		}
	
		
	}

	public BindResult getData() {
		return data;
	}

	public void setData(BindResult data) {
		this.data = data;
	}
	

}
