package cn.vko.ring.local.model;

import java.io.Serializable;
import java.util.List;

/**
 * desc: Created by jiarh on 16/3/15 10:35.
 */
public class LocalCourseModel implements Serializable{

	/**
	 * code : 0 msg : stime : 1458009060135 data :
	 * {"goodsList":[{"totalCourse":0
	 * ,"sellPrice":80,"goodsId":"1662","tagdesc":"寒假提高"
	 * ,"description":"本课程相关： 1）test1； 2）test2；"
	 * ,"month":1,"objId":"18951","goodsName"
	 * :"测试本地课程1"}],"gradeId":"0","subjectId":"22","cityNo":"4101"}
	 */

	private int code;
	private String msg;
	private long stime;
	/**
	 * goodsList :
	 * [{"totalCourse":0,"sellPrice":80,"goodsId":"1662","tagdesc":"寒假提高"
	 * ,"description"
	 * :"本课程相关： 1）test1； 2）test2；","month":1,"objId":"18951","goodsName"
	 * :"测试本地课程1"}] gradeId : 0 subjectId : 22 cityNo : 4101
	 */

	private DataEntity data;

	public void setCode(int code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setStime(long stime) {
		this.stime = stime;
	}

	public void setData(DataEntity data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public long getStime() {
		return stime;
	}

	public DataEntity getData() {
		return data;
	}

	public static class DataEntity implements Serializable{
		private String gradeId;
		private String subjectId;
		private String cityNo;

		/**
		 * totalCourse : 0 sellPrice : 80 goodsId : 1662 tagdesc : 寒假提高
		 * description : 本课程相关： 1）test1； 2）test2； month : 1 objId : 18951
		 * goodsName : 测试本地课程1
		 */

		private List<GoodsListEntity> courseList;

		public void setGradeId(String gradeId) {
			this.gradeId = gradeId;
		}

		public void setSubjectId(String subjectId) {
			this.subjectId = subjectId;
		}

		public void setCityNo(String cityNo) {
			this.cityNo = cityNo;
		}

		public List<GoodsListEntity> getCourseList() {
			return courseList;
		}

		public void setCourseList(List<GoodsListEntity> courseList) {
			this.courseList = courseList;
		}

		public String getGradeId() {
			return gradeId;
		}

		public String getSubjectId() {
			return subjectId;
		}

		public String getCityNo() {
			return cityNo;
		}



		public static class GoodsListEntity implements Serializable{
			private int totalCourse;
			private double sellPrice;
			private String goodsId;
			private String tagdesc;
			private String description;
			private int month;
			private String objId;
			private String goodsName;
			private int buyCount;
			private String tcName;
			private String tcSubject;
			private String tcSchool;
			private String goodsDesc;
			private String tcFace;
			private String teacherId;
			private boolean isBuy;
			private String cover;
			private String color;
			

			public boolean isBuy() {
				return isBuy;
			}

			public void setBuy(boolean isBuy) {
				this.isBuy = isBuy;
			}

			public String getCover() {
				return cover;
			}

			public void setCover(String cover) {
				this.cover = cover;
			}

			public String getColor() {
				return color;
			}

			public void setColor(String color) {
				this.color = color;
			}

			public int getBuyCount() {
				return buyCount;
			}

			public void setBuyCount(int buyCount) {
				this.buyCount = buyCount;
			}

			public String getTcName() {
				return tcName;
			}

			public void setTcName(String tcName) {
				this.tcName = tcName;
			}

			public String getTcSubject() {
				return tcSubject;
			}

			public void setTcSubject(String tcSubject) {
				this.tcSubject = tcSubject;
			}

			public String getTcSchool() {
				return tcSchool;
			}

			public void setTcSchool(String tcSchool) {
				this.tcSchool = tcSchool;
			}

			public String getGoodsDesc() {
				return goodsDesc;
			}

			public void setGoodsDesc(String goodsDesc) {
				this.goodsDesc = goodsDesc;
			}

			public String getTcFace() {
				return tcFace;
			}

			public void setTcFace(String tcFace) {
				this.tcFace = tcFace;
			}

			public String getTeacherId() {
				return teacherId;
			}

			public void setTeacherId(String teacherId) {
				this.teacherId = teacherId;
			}

			public void setTotalCourse(int totalCourse) {
				this.totalCourse = totalCourse;
			}

			public void setSellPrice(double sellPrice) {
				this.sellPrice = sellPrice;
			}

			public void setGoodsId(String goodsId) {
				this.goodsId = goodsId;
			}

			public void setTagdesc(String tagdesc) {
				this.tagdesc = tagdesc;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public void setMonth(int month) {
				this.month = month;
			}

			public void setObjId(String objId) {
				this.objId = objId;
			}

			public void setGoodsName(String goodsName) {
				this.goodsName = goodsName;
			}

			public int getTotalCourse() {
				return totalCourse;
			}

			public double getSellPrice() {
				return sellPrice;
			}

			public String getGoodsId() {
				return goodsId;
			}

			public String getTagdesc() {
				return tagdesc;
			}

			public String getDescription() {
				return description;
			}

			public int getMonth() {
				return month;
			}

			public String getObjId() {
				return objId;
			}

			public String getGoodsName() {
				return goodsName;
			}
		}
	}
}
