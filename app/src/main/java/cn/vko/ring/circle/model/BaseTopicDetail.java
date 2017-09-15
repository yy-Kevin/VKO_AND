package cn.vko.ring.circle.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.home.model.BaseResultInfo;
import cn.vko.ring.home.model.Pager;

public class BaseTopicDetail extends BaseResultInfo {
	private BsseTopicDetailInfo data;
	
	
	public BsseTopicDetailInfo getData() {
		return data;
	}


	public void setData(BsseTopicDetailInfo data) {
		this.data = data;
	}


	public class BsseTopicDetailInfo{
		private Pager pager;
		private TopicInfo article;
		private List<ReplyInfo> reply;
		
		public Pager getPager() {
			return pager;
		}
		public void setPager(Pager pager) {
			this.pager = pager;
		}
		public TopicInfo getArticle() {
			return article;
		}
		public void setArticle(TopicInfo article) {
			this.article = article;
		}
		public List<ReplyInfo> getReply() {
			return reply;
		}
		public void setReply(List<ReplyInfo> reply) {
			this.reply = reply;
		}
		
		
		
	}
}
