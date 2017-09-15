package cn.vko.ring.circle.model;

import java.io.Serializable;
import java.util.List;

public class TopicInfo implements Serializable {
	private String id;
	private String cnt;
	private AudioInfo aud;
	private String crTime;
	private MemberInfo user;
	private boolean isdel;
	private boolean isjoin;
	private int replyCnt;
	private GroupInfo group;//来自哪个群
	
	public boolean isIsjoin() {
		return isjoin;
	}
	public void setIsjoin(boolean isjoin) {
		this.isjoin = isjoin;
	}
	public boolean isIsdel() {
		return isdel;
	}
	public void setIsdel(boolean isdel) {
		this.isdel = isdel;
	}
	public int getReplyCnt() {
		return replyCnt;
	}
	public void setReplyCnt(int replyCnt) {
		this.replyCnt = replyCnt;
	}
	public GroupInfo getGroup() {
		return group;
	}
	public void setGroup(GroupInfo group) {
		this.group = group;
	}
	private List<ImageInfo> imgs;
	
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
