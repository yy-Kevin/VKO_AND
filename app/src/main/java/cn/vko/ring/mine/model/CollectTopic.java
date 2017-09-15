package cn.vko.ring.mine.model;

import java.io.Serializable;
import java.util.List;

public class CollectTopic implements Serializable {
	private Pager pager;
	private List<FavoriteTopicInfo> article;

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public List<FavoriteTopicInfo> getArticle() {
		return article;
	}

	public void setArticle(List<FavoriteTopicInfo> article) {
		this.article = article;
	}

	public class Pager implements Serializable {
		private int pageTotal;
		private int pageNo;
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
