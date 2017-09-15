package cn.vko.ring.local.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.home.model.Pager;

public class LocalVideoModel implements Serializable {
	private int code;
	private String msg;
	private long stime;
	private DataEntity data;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getStime() {
		return stime;
	}

	public void setStime(long stime) {
		this.stime = stime;
	}

	public DataEntity getData() {
		return data;
	}

	public void setData(DataEntity data) {
		this.data = data;
	}

	public static class DataEntity implements Serializable{
		public LocalCourseModel.DataEntity.GoodsListEntity getGoods() {
			return goods;
		}

		public void setGoods(LocalCourseModel.DataEntity.GoodsListEntity goods) {
			this.goods = goods;
		}

		private LocalCourseModel.DataEntity.GoodsListEntity goods;
		private List<VideoListEntity> videoList;
		private Pager pager;
		


		public Pager getPager() {
			return pager;
		}
		public void setPager(Pager pager) {
			this.pager = pager;
		}
		public List<VideoListEntity> getVideoList() {
			return videoList;
		}
		public void setVideoList(List<VideoListEntity> videoList) {
			this.videoList = videoList;
		}
		
		
		
	}
	
	public static class VideoListEntity implements Serializable{
		private String duration;
		private String videoId;
		private String videoName;
		public String getDuration() {
			return duration;
		}
		public void setDuration(String duration) {
			this.duration = duration;
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
}
