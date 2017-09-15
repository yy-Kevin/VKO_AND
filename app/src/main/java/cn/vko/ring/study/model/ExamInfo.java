/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ExamInfo.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-19 下午2:05:53 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;


/** 
 * @ClassName: ExamInfo 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-19 下午2:05:53  
 */
public class ExamInfo implements Serializable{
	private SecInfo secInfo;
	private List<EvalDetail> eval;
	private boolean isdone;
	private String gId;
	private  List<ExamContentDetail> exam;
	public static final String EXAMINFO ="ExamInfo";
	
	
	

	public String getGId() {
		return gId;
	}
	public void setGId(String gId) {
		this.gId = gId;
	}
	public List<EvalDetail> getEval() {
		return eval;
	}
	public void setEval(List<EvalDetail> eval) {
		this.eval = eval;
	}
	public SecInfo getSecInfo() {
		return secInfo;
	}
	public void setSecInfo(SecInfo secInfo) {
		this.secInfo = secInfo;
	}
	public boolean isIsdone() {
		return isdone;
	}
	public void setIsdone(boolean isdone) {
		this.isdone = isdone;
	}
	public List<ExamContentDetail> getExam() {
		return exam;
	}
	public void setExam(List<ExamContentDetail> exam) {
		this.exam = exam;
	}
	




}
