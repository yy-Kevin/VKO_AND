/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ErrorExamSubmitResult.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.study.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-22 上午11:23:49 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import cn.vko.ring.home.model.BaseResultInfo;

/** 
 * @ClassName: ErrorExamSubmitResult 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-22 上午11:23:49  
 */
public class ErrorExamSubmitResult extends BaseResultInfo{
	
	private ErrorSubResult data;
	
	public ErrorSubResult getData() {
		return data;
	}

	public void setData(ErrorSubResult data) {
		this.data = data;
	}

	public static class ErrorSubResult{
		private String content;
		private String status;
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		
	}
}
