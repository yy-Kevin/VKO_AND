package cn.vko.ring.mine.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.mine.model.FavoriteVideoInfo;

public class CollectVideo implements Serializable {
	private Pager pager;
	private List<FavoriteVideoInfo> videos;

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public List<FavoriteVideoInfo> getVideos() {
		return videos;
	}

	public void setVideos(List<FavoriteVideoInfo> videos) {
		this.videos = videos;
	}

	public class Pager implements Serializable {
		int pageTotal;
		int pageNo;
		public int getPageTotal() {
			return pageTotal;
		}
		public void setPageTotal(int pageTotal) {
			this.pageTotal = pageTotal;
		}
		public int getPageNo() {
			return pageNo;
		}
		public void setPageNo(int pageNo) {
			this.pageNo = pageNo;
		}
		
	}

}
