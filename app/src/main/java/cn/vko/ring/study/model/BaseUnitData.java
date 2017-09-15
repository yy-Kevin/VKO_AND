package cn.vko.ring.study.model;

import java.io.Serializable;

public class BaseUnitData implements Serializable{
	
	private boolean auth;
	
	private Zan zan;
	
	private UnitVideo video;

	private Boolean collectVideo;
	
	
	public Boolean getCollectVideo() {
		return collectVideo;
	}

	public void setCollectVideo(Boolean collectVideo) {
		this.collectVideo = collectVideo;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	public Zan getZan() {
		return zan;
	}

	public void setZan(Zan zan) {
		this.zan = zan;
	}

	public UnitVideo getVideo() {
		return video;
	}

	public void setVideo(UnitVideo video) {
		this.video = video;
	};
	
	
}
