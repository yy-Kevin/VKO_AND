package cn.vko.ring.study.model;

public class VideoRecordInfo {
	//同步，综合提交看课记录
	private long sec;
	private String uniKey;
	private String goodsId;
	private String videoId;
	private String subjectId;
	
	private String courseType; //同步 41 综合 43
	private String knowledgeId; //综合 
	private String bookId;      //同步
	
	
	public long getSec() {
		return sec;
	}
	public void setSec(long sec) {
		this.sec = sec;
	}
	public String getUniKey() {
		return uniKey;
	}
	public void setUniKey(String uniKey) {
		this.uniKey = uniKey;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getKnowledgeId() {
		return knowledgeId;
	}
	public void setKnowledgeId(String knowledgeId) {
		this.knowledgeId = knowledgeId;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	
	
	
	
}


