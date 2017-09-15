package cn.vko.ring.circle.model;

import cn.vko.ring.home.model.BaseResultInfo;

public class SubmitResultInfo extends BaseResultInfo {

	private ResultInfo data;

	public ResultInfo getData() {
		return data;
	}

	public void setData(ResultInfo data) {
		this.data = data;
	}

	public class ResultInfo {

		private String groupId;
		private String replyId;
		private String articleId;

		public String getReplyId() {
			return replyId;
		}

		public void setReplyId(String replyId) {
			this.replyId = replyId;
		}

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public String getArticleId() {
			return articleId;
		}

		public void setArticleId(String articleId) {
			this.articleId = articleId;
		}

	}
}
