package cn.vko.ring.local.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;

public class CityAndSubject extends BaseResponseInfo{
	private List<CSdatas> data;
	
	public List<CSdatas> getData() {
		return data;
	}

	public void setData(List<CSdatas> data) {
		this.data = data;
	}

	public static class CSdatas implements Serializable{
		private String provinceId;
		private String procinceName;
		private String cityId;
		private String cityName;
		private List<Subject> subjectList;
		public String getProvinceId() {
			return provinceId;
		}
		public void setProvinceId(String provinceId) {
			this.provinceId = provinceId;
		}
		public String getProcinceName() {
			return procinceName;
		}
		public void setProcinceName(String procinceName) {
			this.procinceName = procinceName;
		}
		public String getCityId() {
			return cityId;
		}
		public void setCityId(String cityId) {
			this.cityId = cityId;
		}
		public String getCityName() {
			return cityName;
		}
		public void setCityName(String cityName) {
			this.cityName = cityName;
		}
		public List<Subject> getSubjectList() {
			return subjectList;
		}
		public void setSubjectList(List<Subject> subjectList) {
			this.subjectList = subjectList;
		}
		
		
	}
	
	public static class Subject implements Serializable{
		private String subjectName;
		private String subjectId;
		public String getSubjectName() {
			return subjectName;
		}
		public void setSubjectName(String subjectName) {
			this.subjectName = subjectName;
		}
		public String getSubjectId() {
			return subjectId;
		}
		public void setSubjectId(String subjectId) {
			this.subjectId = subjectId;
		}
		
		
	}
	

}
