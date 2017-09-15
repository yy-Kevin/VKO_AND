package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.home.model.Pager;

public class CourseVideoSeacher implements Serializable {
	private List<VideoList> videos;
	private Pager pager;

	public List<VideoList> getVideos() {
		return videos;
	}

	public void setVideos(List<VideoList> videos) {
		this.videos = videos;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

}
