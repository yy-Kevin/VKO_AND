package cn.vko.ring.circle.model;

import java.io.Serializable;
import java.util.List;

public class ReplyInfo implements Serializable {
	private String id;
	private String cnt;
	private AudioInfo aud;
	private String crTime;
	private MemberInfo user;
	private List<ImageInfo> imgs;
	private boolean upZan;
	private int upCount;
	private boolean isdel;
	
	
	public boolean isIsdel() {
		return isdel;
	}
	public void setIsdel(boolean isdel) {
		this.isdel = isdel;
	}
	public boolean isUpZan() {
		return upZan;
	}
	public void setUpZan(boolean upZan) {
		this.upZan = upZan;
	}
	public int getUpCount() {
		return upCount;
	}
	public void setUpCount(int upCount) {
		this.upCount = upCount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCnt() {
		return cnt;
	}
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	public AudioInfo getAud() {
		return aud;
	}
	public void setAud(AudioInfo aud) {
		this.aud = aud;
	}
	public String getCrTime() {
		return crTime;
	}
	public void setCrTime(String crTime) {
		this.crTime = crTime;
	}
	public MemberInfo getUser() {
		return user;
	}
	public void setUser(MemberInfo user) {
		this.user = user;
	}
	public List<ImageInfo> getImgs() {
		return imgs;
	}
	public void setImgs(List<ImageInfo> imgs) {
		this.imgs = imgs;
	}
	
	
}
