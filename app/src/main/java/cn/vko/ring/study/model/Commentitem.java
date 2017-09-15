package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

public class Commentitem implements Serializable{
	private PagerCount pager;
	private List<VideoCommentInfo> commentList;
	public PagerCount getPager() {
		return pager;
	}
	public void setPager(PagerCount pager) {
		this.pager = pager;
	}
	public List<VideoCommentInfo> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<VideoCommentInfo> commentList) {
		this.commentList = commentList;
	}
	
	
}


