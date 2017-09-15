package cn.vko.ring.study.model;

import java.io.Serializable;

public class VideoAttributes implements Serializable{
	private String goodsId;
	private String bookId;  //同步  是 书得Id  综合是objId
	private String videoId;  
	private String subjectId;//同步课的学科Id
	private String sectionId; //同步是章的Id  综合是id	
	private String courseType ; //同步视频 41， 综合视频43 			
	private String encryptionid; //下载过的视频 ，以解密的idH
	private String BookName;
	private String VideoName;
	private double sellPrice;
		
	public double getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getVideoName() {
		return VideoName;
	}
	public void setVideoName(String videoName) {
		VideoName = videoName;
	}
	public String getBookName() {
		return BookName;
	}
	public void setBookName(String bookName) {
		BookName = bookName;
	}
	public String getEncryptionid() {
		return encryptionid;
	}
	public void setEncryptionid(String encryptionid) {
		this.encryptionid = encryptionid;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
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
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	
	
}

