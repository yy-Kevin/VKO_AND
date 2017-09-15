package cn.vko.ring.circle.model;

import java.io.Serializable;

public class MemberInfo implements Serializable {
	private String name;
	private String userId;
	private String schoolName;
	private String gradeName;
	private String headImg;
	private int privilleges;

	public MemberInfo(){};
	public MemberInfo(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public int getPrivilleges() {
		return privilleges;
	}

	public void setPrivilleges(int privilleges) {
		this.privilleges = privilleges;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		MemberInfo g = (MemberInfo) o;
		return g.getUserId().equals(getUserId());
	}

}
