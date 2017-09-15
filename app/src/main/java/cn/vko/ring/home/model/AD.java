package cn.vko.ring.home.model;

import java.io.Serializable;

import android.text.TextUtils;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.common.base.BaseResponseInfo;


public class AD extends BaseResponseInfo{
	
	public static final String AD_ID="ID";
	public static final String AD_HAS_LOOK="has_look";
	public static final String AD_START_TIME="start_time";
	public static final String AD_END_TIME="end_time";
	public static final String AD_PIC_URL="pic_url";
	public static final String AD_AC_URL="act_url";
	public static final String AD_NOTE="ad_note";
	public static final String AD_NAME="ad_name";
	
	private ADactivity data;
	
	public ADactivity getData() {
		return data;
	}
	
	public void setData(ADactivity data) {
		this.data = data;
	}
	public class ADactivity implements Serializable{
		private ADitem activity;

		public ADitem getActivity() {
			return activity;
		}

		public void setActivity(ADitem activity) {
			this.activity = activity;
		}
		public class ADitem implements Serializable{
			private String name;
			private long startTime;
			private long endTime;
			private String deleted;
			private String actUrl;
			private String sn;
			private String actId;
			private String note;
			private String actBgImg;
			
			
			public String getActBgImg() {
				return actBgImg;
			}
			public void setActBgImg(String actBgImg) {
				this.actBgImg = actBgImg;
			}
			public String getNote() {
				return note;
			}
			public void setNote(String note) {
				this.note = note;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}

			public long getStartTime() {
				return startTime;
			}
			public void setStartTime(long startTime) {
				this.startTime = startTime;
			}
			public long getEndTime() {
				return endTime;
			}
			public void setEndTime(long endTime) {
				this.endTime = endTime;
			}
			public String getDeleted() {
				return deleted;
			}
			public void setDeleted(String deleted) {
				this.deleted = deleted;
			}
			public String getActUrl() {
				return actUrl;
			}
			public void setActUrl(String actUrl) {
				this.actUrl = actUrl;
			}
			public String getSn() {
				return sn;
			}
			public void setSn(String sn) {
				this.sn = sn;
			}
			public String getActId() {
				return actId;
			}
			public void setActId(String actId) {
				this.actId = actId;
			}
		
	
		}
	}

}