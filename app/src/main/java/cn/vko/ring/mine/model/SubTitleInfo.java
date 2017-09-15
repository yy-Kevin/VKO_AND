/*
 *SubTitleInfo.java [V 1.0.0]
 *classes : cn.vko.ring.mine.model.SubTitleInfo
 *宣义阳 Create at 2015-8-7 上午9:56:39
 */
package cn.vko.ring.mine.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;

/**
 * cn.vko.ring.mine.model.SubTitleInfo
 * @author 宣义阳 create at 2015-8-7 上午9:56:39
 */
public class SubTitleInfo extends BaseResponseInfo {
	private DataCourse data;
	
	public DataCourse getData() {
		return data;
	}
	public void setData(DataCourse data) {
		this.data = data;
	}

	public class DataCourse implements Serializable {
		private List<Course> course;
		private Pager pager;
		
		public List<Course> getCourse() {
			return course;
		}
		public void setCourse(List<Course> course) {
			this.course = course;
		}

		public Pager getPager() {
			return pager;
		}
		public void setPager(Pager pager) {
			this.pager = pager;
		}
		
		public class Pager implements Serializable {
			private int pageTotal;
			private int pageNo;

			public int getPageTotal() {
				return pageTotal;
			}
			public void setPageTotal(int pageTotal) {
				this.pageTotal = pageTotal;
			}
			public int getPageNo() {
				return pageNo;
			}
			public void setPageNo(int pageNo) {
				this.pageNo = pageNo;
			}
		}

		public class Course implements Serializable {
			private String id;
			private String name;
			private String subjectId;
			private String goodsId;
			private List<Section> section;
			
			public String getGoodsId() {
				return goodsId;
			}
			public void setGoodsId(String goodsId) {
				this.goodsId = goodsId;
			}
			public String getSubjectId() {
				return subjectId;
			}
			public void setSubjectId(String subjectId) {
				this.subjectId = subjectId;
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
			public List<Section> getSection() {
				return section;
			}
			public void setSection(List<Section> section) {
				this.section = section;
			}

			public class Section implements Serializable {
				private String sectionId;
				private String sectionName;

				public String getSectionId() {
					return sectionId;
				}
				public void setSectionId(String sectionId) {
					this.sectionId = sectionId;
				}
				public String getSectionName() {
					return sectionName;
				}
				public void setSectionName(String sectionName) {
					this.sectionName = sectionName;
				}
			}
		}
	}

	
}
