package cn.vko.ring.study.model;

import java.io.Serializable;

public class Zan implements Serializable{
	/*isPraise: true,   //我是否点过赞
	praiseCnt: "4",	//点赞总数
	isStep: false		//我是否点过踩
	stepCnt: "3"		//点踩总数
*/
	private Boolean  isPraise;
	private int praiseCnt;
	private Boolean isStep;
	private String stepCnt;
	public Boolean getIsPraise() {
		return isPraise;
	}
	public void setIsPraise(Boolean isPraise) {
		this.isPraise = isPraise;
	}	
	
	public int getPraiseCnt() {
		return praiseCnt;
	}
	public void setPraiseCnt(int praiseCnt) {
		this.praiseCnt = praiseCnt;
	}
	public Boolean getIsStep() {
		return isStep;
	}
	public void setIsStep(Boolean isStep) {
		this.isStep = isStep;
	}
	public String getStepCnt() {
		return stepCnt;
	}
	public void setStepCnt(String stepCnt) {
		this.stepCnt = stepCnt;
	}
	
}

