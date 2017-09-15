/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ExamDatasModel.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-21 下午3:05:07 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import cn.vko.ring.common.base.BaseResponseInfo;

/** 
 * @ClassName: ExamDatasModel 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-21 下午3:05:07  
 */
public class ExamDatasModel extends BaseResponseInfo{
	private ExamDatas data;

	public ExamDatas getData() {
		return data;
	}

	public void setData(ExamDatas data) {
		this.data = data;
	}
	
}
