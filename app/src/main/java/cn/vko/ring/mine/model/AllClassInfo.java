/*******************************************************************************
 * Copyright (c) 2015 by Luxury Corporation all right reserved.
 * 2015-8-18 
 * 
 *******************************************************************************/
package cn.vko.ring.mine.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 
 * 编写日期:	2015-8-18
 * 作者:	宋宁宁
 * 
 * 历史记录
 *    修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class AllClassInfo extends BaseResponseInfo {
	public Data data;

	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}

	public class Data implements Serializable {
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

		public class Course implements Serializable {
			private String id;
			private String name;
			private String subjectId;
			
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
	}
}
