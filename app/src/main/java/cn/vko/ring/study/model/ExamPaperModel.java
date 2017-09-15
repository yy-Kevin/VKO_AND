/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ExamPaperModel.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-8-10 下午3:20:09 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: ExamPaperModel
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-8-10 下午3:20:09
 */
public class ExamPaperModel implements Serializable {
	private List<ExamDetail> data;

	public List<ExamDetail> getData() {
		return data;
	}
	public void setData(List<ExamDetail> data) {
		this.data = data;
	}

	public class ExamDetail implements Serializable {
		private String id;
		private String context;
		private String elapse;

		public String getElapse() {
			return elapse;
		}
		public void setElapse(String elapse) {
			this.elapse = elapse;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getContext() {
			return context;
		}
		public void setContext(String context) {
			this.context = context;
		}
	}
}
