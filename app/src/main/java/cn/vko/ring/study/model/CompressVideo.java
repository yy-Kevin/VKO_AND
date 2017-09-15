package cn.vko.ring.study.model;

import java.io.Serializable;

public class CompressVideo implements Serializable {
	private String goodsId;
	private String objId;
	private int trackRate;
	private String videoId;
	private String videoName;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public int getTrackRate() {
		return trackRate;
	}

	public void setTrackRate(int trackRate) {
		this.trackRate = trackRate;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

}
