/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ExamDatas.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-19 下午5:19:46 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;

/** 
 * @ClassName: ExamDatas 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-19 下午5:19:46  
 */
public class ExamDatas extends BaseResponseInfo{
	
	private String gId;
	
	public String getGId() {
		return gId;
	}
	public void setGId(String gId) {
		this.gId = gId;
	}

	private List<ExamContentDetail> exam;

	public List<ExamContentDetail> getExam() {
		return exam;
	}

	public void setExam(List<ExamContentDetail> exam) {
		this.exam = exam;
	}
	
}
