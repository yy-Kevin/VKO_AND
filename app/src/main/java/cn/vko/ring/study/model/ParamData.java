/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ParamData.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.study.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-27 上午11:14:58 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import android.content.Context;
import android.text.TextUtils;
import cn.vko.ring.VkoContext;
import cn.vko.ring.home.model.UserInfo;

/**
 * @ClassName: ParamData
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-27 上午11:14:58
 */
public class ParamData {
	private String token;
	private String gradeId;
	private String learnId;
	/* 学科id */
	private String subjectId;
	/* 书的id */
	private String bookId;
	/* 书名 */
	private String bookName;
	/* 教材版本Id */
	private String bookVersionId;
	/* 教材版本名字 */
	private String bookVersionName;
	/* 章的id */
	private String sectionId;
	/* 一级知识点Id */
	private String k1Id;
	/* 二级知识点Id */
	private String k2Id;
	private Context context;
	private int fromType;//1 我的课程
	
	

	
	
	public static class Builder {
		private String token;
		private String gradeId;
		private String learnId;
		private String subjectId;
		private String bookId;
		private String sectionId;
		private String k1Id;
		private String k2Id;
		private String bookName;
		private String bookVersionId;
		private String bookVersionName;
		private Context context;
		private int fromType;//1 我的课程

		/**
		 * @Description:TODO
		 */
		public Builder(Context context) {
			this.context = context;
			setToken(null);
			setGradeId(null);
			setLearnId(null);
		}
		
		public Builder setGradeId(String gradeId) {
			if (TextUtils.isEmpty(gradeId)&&getUser()!=null) {
				this.gradeId =  getUser().getGradeId();
				return this;
			}
			this.gradeId = gradeId;
			return this;
		}

		public Builder setLearnId(String learnId) {
			if (TextUtils.isEmpty(learnId)&&getUser()!=null) {
				this.learnId =  getUser().getLearn();
				return this;
			}
			this.learnId = learnId;
			return this;
		}

		public UserInfo getUser(){
			return VkoContext.getInstance(context).getUser();
		}
		public Builder setToken(String token) {
			if (TextUtils.isEmpty(token)&&getUser()!=null) {
				this.token =  getUser().getToken();
				return this;
			}
			this.token = token;
			return this;
		}
		public Builder setSubjectId(String subjectId) {
			this.subjectId = subjectId;
			return this;
		}
		public Builder setBookId(String bookId) {
			this.bookId = bookId;
			return this;
		}
		public Builder setSectionId(String sectionId) {
			this.sectionId = sectionId;
			return this;
		}
		public Builder setK1Id(String k1Id) {
			this.k1Id = k1Id;
			return this;
		}
		public Builder setK2Id(String k2Id) {
			this.k2Id = k2Id;
			return this;
		}
		public Builder setBookName(String bookName) {
			this.bookName = bookName;
			return this;
		}
		public Builder setFromType(int fromType) {
			this.fromType = fromType;
			return this;
		}
		public Builder setBookVersionId(String bookVersionId) {
			this.bookVersionId = bookVersionId;
			return this;
		}
		public Builder setBookVersionName(String bookVersionName) {
			this.bookVersionName = bookVersionName;
			return this;
		}
		public ParamData build() {
			return new ParamData(this);
		}
	}

	/**
	 * @Description:TODO
	 */
	public ParamData(Builder b) {
		// TODO Auto-generated constructor stub
		this.token = b.token;
		this.subjectId = b.subjectId;
		this.sectionId = b.sectionId;
		this.bookId = b.bookId;
		this.k1Id = b.k1Id;
		this.k2Id = b.k2Id;
		this.context= b.context;
		this.bookName=b.bookName;
		this.bookVersionId=b.bookVersionId;
		this.bookVersionName=b.bookVersionName;
		this.gradeId = b.gradeId;
		this.learnId=b.learnId;
		this.fromType = b.fromType;
	}
	
	public String getGradeId() {
		return gradeId;
	}

	public String getLearnId() {
		return learnId;
	}

	public String getToken() {
		return token;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public String getBookId() {
		return bookId;
	}
	public String getSectionId() {
		return sectionId;
	}
	public Context getContext() {
		return context;
	}
	public String getK1Id() {
		return k1Id;
	}
	public String getK2Id() {
		return k2Id;
	}
	
	public String getBookName() {
		return bookName;
	}
	public String getBookVersionId() {
		return bookVersionId;
	}
	public String getBookVersionName() {
		return bookVersionName;
	}
	public int getFromType() {
		return fromType;
	}

	/**
	 * @Title: isSync
	 * @Description: 是否同步列表
	 * @return
	 * @return: boolean
	 */
	public boolean isSync() {
		return TextUtils.isEmpty(k1Id);
	}
}
