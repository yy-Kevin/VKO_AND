package cn.vko.ring.circle.model;

import java.io.Serializable;
import java.util.List;

public class GroupInfo implements Serializable {
	
	private String id;//群号
	private String imGroupId;//群ID
	private String name;//群名称
	private String introduction;//
	private String bulletin;//群公告
	private String schoolId;//学校ID
	private String schoolName;//群学校名称
	private String gradeId;//年级ID
	private String gradeName;//群年级名称
	private String crId;// 创建者Id
	private String crName;// 创建者名称
	private String crTime;// 创建时间
	private String headImg;// 群头像
	private int privillege;//群开放权限1-开放 2-需要验证 3-不允许添加
	private String parentType;//一级群组类型 2-班级群 3-兴趣群 4-学习群
	private String type;//二级群组类型
	private String typeName;//二级群组名
	private int artsSciences;//文理科 0-不区分文理科 1-文科 2-理科',
	private int membersLimit;//群人数上限
	private int userCnt;
	private int flag;//是否是群组成员标志位，1 是，0 否 2申请中
	private int role;//我的角色  1 群主 2管理员 3 成员 4 其他
	private boolean isChecked;

	private List<MemberInfo> groupUsers;
	
	
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public List<MemberInfo> getGroupUsers() {
		return groupUsers;
	}
	public void setGroupUsers(List<MemberInfo> groupUsers) {
		this.groupUsers = groupUsers;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getUserCnt() {
		return userCnt;
	}
	public void setUserCnt(int userCnt) {
		this.userCnt = userCnt;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getImGroupId() {
		return imGroupId;
	}
	public void setImGroupId(String imGroupId) {
		this.imGroupId = imGroupId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getBulletin() {
		return bulletin;
	}
	public void setBulletin(String bulletin) {
		this.bulletin = bulletin;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getCrId() {
		return crId;
	}
	public void setCrId(String crId) {
		this.crId = crId;
	}
	public String getCrName() {
		return crName;
	}
	public void setCrName(String crName) {
		this.crName = crName;
	}
	public String getCrTime() {
		return crTime;
	}
	public void setCrTime(String crTime) {
		this.crTime = crTime;
	}
	
	public int getPrivillege() {
		return privillege;
	}
	public void setPrivillege(int privillege) {
		this.privillege = privillege;
	}
	
	public String getParentType() {
		return parentType;
	}
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getArtsSciences() {
		return artsSciences;
	}
	public void setArtsSciences(int artsSciences) {
		this.artsSciences = artsSciences;
	}
	public int getMembersLimit() {
		return membersLimit;
	}
	public void setMembersLimit(int membersLimit) {
		this.membersLimit = membersLimit;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	public GroupClassInfo getGroupClassInfo(){
		GroupClassInfo info = new GroupClassInfo();
		info.setId(type);
		info.setParentId(parentType);
		info.setName(typeName);
		return info;
	}
	
	public void setGroupClassInfo(GroupClassInfo info){
		this.type = info.getId();
		this.typeName = info.getName();
		this.parentType = info.getParentId();
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		GroupInfo g = (GroupInfo) o;
		return g.getId().equals(getId());
	}
	
	
	
}
