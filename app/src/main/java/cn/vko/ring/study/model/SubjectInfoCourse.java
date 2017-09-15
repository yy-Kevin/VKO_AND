package cn.vko.ring.study.model;

import java.io.Serializable;

import android.text.TextUtils;

public class SubjectInfoCourse implements Serializable {
	public static final String SUBJECT_INFO = "SubjectInfoCourse";
	private String id; // 科目id
	private String bookId;// 书的id
	private String version;// 版本id
	private String vname;// 版本名字
	private String name;// 科目名字
	private String currType;// 同步41，综合43
	private String type;//更换教材版本
	/*是否通知刷新首页学科数据*/
	private boolean isRefreshHomeSubject;

	public boolean isRefreshHomeSubject() {
		return isRefreshHomeSubject;
	}
	public void setRefreshHomeSubject(boolean isRefreshHomeSubject) {
		this.isRefreshHomeSubject = isRefreshHomeSubject;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCurrType() {
		return currType;
	}
	public void setCurrType(String currType) {
		this.currType = currType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVname() {
		return vname;
	}
	public void setVname(String vname) {
		this.vname = vname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isGotoSync() {
		// 默认跳转同步
		return TextUtils.isEmpty(currType) ? true : currType.equals("41");
	}
}
