package cn.vko.ring.local.model;

import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;

public class Ads extends BaseResponseInfo {

	private List<AD> data;
	
	public List<AD> getData() {
		return data;
	}

	public void setData(List<AD> data) {
		this.data = data;
	}

	public static class AD{
		private String sn;
		private String adImg;
		private String adUrl;
		private String adName;
		public String getSn() {
			return sn;
		}
		public void setSn(String sn) {
			this.sn = sn;
		}
		public String getAdImg() {
			return adImg;
		}
		public void setAdImg(String adImg) {
			this.adImg = adImg;
		}
		public String getAdUrl() {
			return adUrl;
		}
		public void setAdUrl(String adUrl) {
			this.adUrl = adUrl;
		}
		public String getAdName() {
			return adName;
		}
		public void setAdName(String adName) {
			this.adName = adName;
		}
		
		
	}
}
