package cn.vko.ring.study.model;

import java.io.Serializable;


public class CourceInfo implements Serializable {
	private String id;
	private String name;
	private String teacher;
	private String school;
	private String play;
	private String goodsId;
	private String objId;
	private String duration;
	private String tVideo;
	private String tsface;
	private String stateTips;
	private String state;
	private String strTime;
	private long startTime;
	private long endTime;


	public long getEndTime() {

		return endTime;
	}


	public void setEndTime(long endTime) {

		this.endTime = endTime;
	}

	public long getStartTime() {

		return startTime;
	}


	public void setStartTime(long startTime) {

		this.startTime = startTime;
	}

	public String getStrTime() {

		return strTime;
	}


	public void setStrTime(String strTime) {

		this.strTime = strTime;
	}

	public String getState() {

		return state;
	}


	public void setState(String state) {

		this.state = state;
	}

	public String getStateTips() {
	
		return stateTips;
	}

	
	public void setStateTips(String stateTips) {
	
		this.stateTips = stateTips;
	}

	public String getTVideo() {

		return tVideo;
	}


	public void setTVideo(String tVideo) {

		this.tVideo = tVideo;
	}

	
	public String getTsface() {
	
		return tsface;
	}

	
	public void setTsface(String tsface) {
	
		this.tsface = tsface;
	}

	public String getDuration() {
	
		return duration;
	}
	
	public void setDuration(String duration) {
	
		this.duration = duration;
	}
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getPlay() {
		return play;
	}
	public void setPlay(String play) {
		this.play = play;
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
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		CourceInfo c = (CourceInfo) o;
		return c.getId().equals(getId());
	}
	
	
//	public IntegraSectionCourse getIntegraSectionCourse(String goodsid){
//		IntegraSectionCourse i = new IntegraSectionCourse();
//		i.setDuration(getDuration());
//		i.setGoodsId(goodsid);
//		i.setTName(getTeacher());
//		i.setVideoId(getId());
//		i.setVideoName(getName());
//		i.setVideoUrl(getPlay());
//		i.setTsface(getTsface());
//		i.setTVideo(getTVideo());
//		return i;
//	}
	
	public VideoAttributes getVideoAttributes(){
		VideoAttributes video = new VideoAttributes();
		video.setVideoId(getId());
		video.setVideoName(getName());
		video.setGoodsId(getGoodsId());
//		video.setBookId(bookId);
//		video.setSectionId(sectionId);
//		video.setCourseType(courseType)
		return video;
	}
}
