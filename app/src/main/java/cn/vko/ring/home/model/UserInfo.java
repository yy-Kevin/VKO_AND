package cn.vko.ring.home.model;

import java.io.Serializable;

import cn.vko.ring.circle.model.MemberInfo;
import cn.vko.ring.im.common.util.sys.SystemUtil;

public class UserInfo implements Serializable {
	private String schoolId;
	private String cityId;
	private String provinceId;
	private String areaId;
	private String cityName; // 城市名称
	private String areaName; // 区名称
	private String proinceName;// 省名称
	private String name;// 真实姓名
	private String nick;// 昵称、用户名
	private String mobile;// 手机号
	private String grade;// 年级
	private String gradeId;
	private String bface;// 用户头像-大
	private String sface;// 用户头像-小
	private String school;// 学校
	private String learn;// 学段
	private int gender;// 性别：0-男，1-女，空-保密
	private String udid;// 用户详情编号，修改个人信息时传递 
	private String token;
	private String deviceToken;
	private int clasz;
	private String msg;
	private String userId;
	private String inviteKey;
	private String sourceCityId;


	/** 是否教师 */
	private String isTeacher;
	private int isSchoolUser=1;// 是否是学校用户
	private String imToken;


	public String getSourceCityId() {
		return sourceCityId;
	}

	public void setSourceCityId(String sourceCityId) {
		this.sourceCityId = sourceCityId;
	}
	public String getImToken() {
		return imToken;
	}

	public void setImToken(String imToken) {
		this.imToken = imToken;
	}

	public int getIsSchoolUser() {
		return isSchoolUser;
	}

	public void setIsSchoolUser(int isSchoolUser) {
		this.isSchoolUser = isSchoolUser;
	}

	private String expire;//会员到时间
	private int level;//会员登录
	private int scord; //会员积分（V币）
	
	private boolean first;//第三方登录时用 
	private int firstLoginVb;//>0 表示 第一次注册登录后获取Vb 
	private boolean signInState;
	private long signInTime;//签到时间

	public String getIsTeacher() {
		return isTeacher;
	}

	public void setIsTeacher(String isTeacher) {
		this.isTeacher = isTeacher;
	}

	public long getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(long signInTime) {
		this.signInTime = signInTime;
	}

	public boolean isSignInState() {
		return signInState;
	}

	public void setSignInState(boolean signInState) {
		this.signInState = signInState;
	}

	public int getFirstLoginVb() {
		return firstLoginVb;
	}

	public void setFirstLoginVb(int firstLoginVb) {
		this.firstLoginVb = firstLoginVb;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getScord() {
		return scord;
	}

	public void setScord(int scord) {
		this.scord = scord;
	}

	/**
	 * @return ssssssss
	 */
	public String getInviteKey() {
		return inviteKey;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	/**
	 * @param inviteKey
	 *            the inviteKey to set
	 */
	public void setInviteKey(String inviteKey) {
		this.inviteKey = inviteKey;
	}

	public String getUserId() {

		return userId;
	}

	public void setUserId(String userId) {

		this.userId = userId;
	}

	/**
	 * @return ssssssss
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return ssssssss
	 */
	public int getClasz() {
		return clasz;
	}

	/**
	 * @param clasz
	 *            the clasz to set
	 */
	public void setClasz(int clasz) {
		this.clasz = clasz;
	}

	public String getGradeId() {

		return gradeId;
	}

	public void setGradeId(String gradeId) {

		this.gradeId = gradeId;
	}

	// public String getGradeName() {
	// return gradeName;
	// }
	//
	// public void setGradeName(String gradeName) {
	// this.gradeName = gradeName;
	// }

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getProinceName() {
		return proinceName;
	}

	public void setProinceName(String proinceName) {
		this.proinceName = proinceName;
	}

	public String getLearn() {
		return learn;
	}

	public void setLearn(String learn) {
		this.learn = learn;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getBface() {
		return bface;
	}

	public void setBface(String bface) {
		this.bface = bface;
	}

	public String getSface() {
		return sface;
	}

	public void setSface(String sface) {
		this.sface = sface;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public boolean isBindDeviceToken() {
		return deviceToken != null && !"".equals(deviceToken);
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		UserInfo u = (UserInfo) o;
		return u.getUdid().equals(getUdid());
	}

	public MemberInfo getMemberInfo() {
		MemberInfo info = new MemberInfo(userId);
		// info.setGradeName(gradeName);
		info.setHeadImg(sface);
		info.setName(name);
		info.setSchoolName(school);
		return info;
	}

	@Override
	public String toString() {
		return "UserInfo [schoolId=" + schoolId + ", cityId=" + cityId
				+ ", provinceId=" + provinceId + ", areaId=" + areaId
				+ ", cityName=" + cityName + ", areaName=" + areaName
				+ ", proinceName=" + proinceName + ", name=" + name + ", nick="
				+ nick + ", mobile=" + mobile + ", grade=" + grade
				+ ", gradeId=" + gradeId + ", bface=" + bface + ", sface="
				+ sface + ", school=" + school + ", learn=" + learn
				+ ", gender=" + gender + ", udid=" + udid + ", token=" + token
				+ ", deviceToken=" + deviceToken + ", clasz=" + clasz
				+ ", msg=" + msg + ", userId=" + userId + ", inviteKey="
				+ inviteKey + ", first=" + first + "]";
	}

}
