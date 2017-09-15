package cn.vko.ring.study.model;

import java.io.Serializable;

public class QuestionInfo implements Serializable {
	
	private String id;
	private String time;
	private String imgUrl;//搜题图片地址
	private String content;//题干
	private String resolve;//解析
	private int flag;// 是否采纳 -1 未采纳 或 被采纳的Id
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getResolve() {
		return resolve;
	}
	public void setResolve(String resolve) {
		this.resolve = resolve;
	}
	
	
}
