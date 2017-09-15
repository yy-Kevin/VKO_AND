package cn.vko.ring.study.model;

import java.io.Serializable;

public class TonBuCourseSection implements Serializable {
	private String chapterId;
	private String objId;
	private int trackRate;
	private String videoId;
	private String videoName;

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
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
