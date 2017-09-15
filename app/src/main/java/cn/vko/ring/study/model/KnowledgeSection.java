package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 同步
 * @author Administrator
 *
 */
public class KnowledgeSection implements Serializable{
	
	private String goodsId;
	private String chapterId;
	private int trackRate;
	private String star;
	private String chapterName;	
	private String bookId;
	private int lockState;//章是否解锁 1已解锁 2锁
	private int videoCnt;
	private SubjectInfoCourse subjectInfo;
			
	public SubjectInfoCourse getSubjectInfo() {
		return subjectInfo;
	}

	public void setSubjectInfo(SubjectInfoCourse subjectInfo) {
		this.subjectInfo = subjectInfo;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	

	public int getTrackRate() {
		return trackRate;
	}

	public void setTrackRate(int trackRate) {
		this.trackRate = trackRate;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public int getLockState() {
		return lockState;
	}

	public void setLockState(int lockState) {
		this.lockState = lockState;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public int getVideoCnt() {
		return videoCnt;
	}

	public void setVideoCnt(int videoCnt) {
		this.videoCnt = videoCnt;
	}

	
		
}

