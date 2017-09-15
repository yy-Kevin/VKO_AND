/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ExamResultAndRecordModel.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-17 上午11:30:00 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;

/** 
 * @ClassName: ExamResultAndRecordModel 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-17 上午11:30:00  
 */
public class ExamResultAndRecordModel extends BaseResponseInfo{
	private ExamInfo data;
	
	public ExamInfo getData() {
		return data;
	}

	public void setData(ExamInfo data) {
		this.data = data;
	}
	
}
