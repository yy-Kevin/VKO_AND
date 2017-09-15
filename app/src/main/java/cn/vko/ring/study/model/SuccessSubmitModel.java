/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: SuccessSubmitModel.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-18 下午6:14:54 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.io.Serializable;

import cn.vko.ring.common.base.BaseResponseInfo;

/** 
 * @ClassName: SuccessSubmitModel 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-18 下午6:14:54  
 */
public class SuccessSubmitModel extends BaseResponseInfo{
	private SubmitResult data;
	
	
	public SubmitResult getData() {
		return data;
	}


	public void setData(SubmitResult data) {
		this.data = data;
	}


	public class SubmitResult implements Serializable{
		private String  content;
		private String success;
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getSuccess() {
			return success;
		}
		public void setSuccess(String success) {
			this.success = success;
		}
		
		
	}
	
}
