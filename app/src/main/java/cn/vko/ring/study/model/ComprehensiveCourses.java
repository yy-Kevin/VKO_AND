package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

/**
 * 综合
 * @author Administrator
 *
 */
public class ComprehensiveCourses implements Serializable {
	
	private String id;//er
	private int trackRate;	
	private int star;
	private int lockState;//
	private String name;
	private int videoCnt;
	private SubjectInfoCourse subjectInfo;
	
	private int typeFrom ;//1 从我的课程
	
	public int getTypeFrom() {
		return typeFrom;
	}
	public void setTypeFrom(int typeFrom) {
		this.typeFrom = typeFrom;
	}
	public SubjectInfoCourse getSubjectInfo() {
		return subjectInfo;
	}
	public void setSubjectInfo(SubjectInfoCourse subjectInfo) {
		this.subjectInfo = subjectInfo;
	}
	public int getTrackRate() {
		return trackRate;
	}
	public void setTrackRate(int trackRate) {
		this.trackRate = trackRate;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public int getLockState() {
		return lockState;
	}
	public void setLockState(int lockState) {
		this.lockState = lockState;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVideoCnt() {
		return videoCnt;
	}
	public void setVideoCnt(int videoCnt) {
		this.videoCnt = videoCnt;
	}
		
	
}

