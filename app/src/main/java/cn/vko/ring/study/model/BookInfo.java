/*
 *BookInfo.java [V 1.0.0]
 *classes : cn.vko.ring.mine.model.BookInfo
 *宣义阳 Create at 2015-7-29 下午4:39:50
 */
package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

/**
 * cn.vko.ring.mine.model.BookInfo
 * 
 * @author 宣义阳 create at 2015-7-29 下午4:39:50
 */
public class BookInfo implements Serializable {

	private String bookid;
	private String bookname;
	private String imgurl;
	private int state;
	private String bvName;
	private String versionId;
	private String subjectId;
	private String learn;

	public String getBvName() {

		return bvName;
	}

	public void setBvName(String bvName) {

		this.bvName = bvName;
	}

	public String getLearn() {

		return learn;
	}

	public void setLearn(String learn) {

		this.learn = learn;
	}

	public String getVersionId() {

		return versionId;
	}

	public void setVersionId(String versionId) {

		this.versionId = versionId;
	}

	public String getSubjectId() {

		return subjectId;
	}

	public void setSubjectId(String subjectId) {

		this.subjectId = subjectId;
	}

	public String getBookid() {

		return bookid;
	}

	public void setBookid(String bookid) {

		this.bookid = bookid;
	}

	public String getBookname() {

		return bookname;
	}

	public void setBookname(String bookname) {

		this.bookname = bookname;
	}

	public String getImgurl() {

		return imgurl;
	}

	public void setImgurl(String imgurl) {

		this.imgurl = imgurl;
	}

	public int getState() {

		return state;
	}

	public void setState(int state) {

		this.state = state;
	}

	@Override
	public boolean equals(Object o) {

		// TODO Auto-generated method stub
		BookInfo b = (BookInfo) o;
		return b.getBookid().equals(getBookid());
	}

}
