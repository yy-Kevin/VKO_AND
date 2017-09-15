package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;


public class TopicInfo implements Serializable {
	private String id;// 专题编号
	private String name;// 名称
	private String pic;// 封面地址
	private double sellPrice;// 价格
	private int classTotal;// 课程节数
	private List<Teacher> teacher;// 主讲教师
	private String intro;// 课程简介
	private String isbuy;// 是否购买 0 已购买 1未购买
	private String title;// 副标题

	private String subjectId;
	private String objId;
	private String topicId;

	
	/**
	 * @return ssssssss
	 */
	public double getSellPrice() {
		return sellPrice;
	}



	/**
	 * @param sellPrice the price to set
	 */
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}



	public List<Teacher> getTeacher() {
	
		return teacher;
	}


	
	public void setTeacher(List<Teacher> teacher) {
	
		this.teacher = teacher;
	}



	public String getIsbuy() {

		return isbuy;
	}


	public void setIsbuy(String isbuy) {
	
		this.isbuy = isbuy;
	}



	public String getTopicId() {

		return topicId;
	}

	public void setTopicId(String topicId) {

		this.topicId = topicId;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getPic() {

		return pic;
	}

	public void setPic(String pic) {

		this.pic = pic;
	}



	public int getClassTotal() {

		return classTotal;
	}

	public void setClassTotal(int classTotal) {

		this.classTotal = classTotal;
	}

	public String getIntro() {

		return intro;
	}

	public void setIntro(String intro) {

		this.intro = intro;
	}

	public String getTitle() {

		return title;
	}

	public void setTitle(String title) {

		this.title = title;
	}

	public String getSubjectId() {

		return subjectId;
	}

	public void setSubjectId(String subjectId) {

		this.subjectId = subjectId;
	}

	public String getObjId() {

		return objId;
	}

	public void setObjId(String objId) {

		this.objId = objId;
	}

	public class Teacher implements Serializable{

		private String tname;
		private String tsface;

		public String getTname() {

			return tname;
		}

		public void setTname(String tname) {

			this.tname = tname;
		}

		public String getTsface() {

			return tsface;
		}

		public void setTsface(String tsface) {

			this.tsface = tsface;
		}

	}

	@Override
	public boolean equals(Object o) {

		// TODO Auto-generated method stub
		TopicInfo s = (TopicInfo) o;
		return s.getId().equals(getId());
	}

}
