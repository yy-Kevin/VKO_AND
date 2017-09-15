package cn.vko.ring.mine.model;

import java.io.Serializable;

import cn.vko.ring.home.model.BaseResultInfo;

public class BaseSocreInfo extends BaseResultInfo {
	private SocreInfo data;
	
	
	public SocreInfo getData() {
		return data;
	}

	public void setData(SocreInfo data) {
		this.data = data;
	}

	public class SocreInfo implements Serializable{
		private String  userId;
		private String  score;
		private String  id;
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public String getScore() {
			return score;
		}
		public void setScore(String score) {
			this.score = score;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
		
	}
}
