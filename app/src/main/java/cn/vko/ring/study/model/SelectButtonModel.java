/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: SelectButton.java 
 * @Prject: AndroidTestDemo
 * @Package: com.jrh.views.testtopview 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-7-28 上午9:42:32 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import android.text.TextUtils;


/**
 * @ClassName: SelectButton
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-7-28 上午9:42:32
 */
public class SelectButtonModel {
	private int currentIndex=0;
	private String selectedAnswer="";
	private STATE state;
	private boolean isRight;
	private boolean isSingleSelect=true;

	public SelectButtonModel(int currentIndex, STATE state,boolean isSingle) {
		super();
		this.currentIndex = currentIndex;
		this.state = state;
		this.isSingleSelect = isSingle;
	
	}
	public SelectButtonModel(boolean isRight) {
		super();
		this.isRight = isRight;
		setState(STATE.UNFOURSE);
	}
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	public STATE getState() {
		return state;
	}
	public void setState(STATE state) {
		this.state = state;
	}
	public String getSelectedAnswer() {
		return selectedAnswer;
	}
	public String setSelectedAnswer(String ans) {
		if (isSingleSelect) {
			this.selectedAnswer = ans;
		}else{
			 if (!TextUtils.isEmpty(ans)) {
		           if (TextUtils.isEmpty(selectedAnswer)){
		        	   //第一次选答案
		        	   selectedAnswer+=ans;
		           }else{
		        	   //第二次
		                if(selectedAnswer.contains(ans)){
		                	selectedAnswer=selectedAnswer.replace(ans,"");
		                }else{
		                	selectedAnswer+=ans;
		                }
		            }
		        }
		}
		 if (TextUtils.isEmpty(selectedAnswer)) {
			 setState(STATE.FOURSED);
		}else{
			setState(STATE.SELECTED);
		}
		 return selectedAnswer;
	}
	public boolean isRight() {
		return isRight;
	}
	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}
	public boolean isSelected() {
		return !TextUtils.isEmpty(selectedAnswer);
	}
	public boolean isSingleSelect() {
		return isSingleSelect;
	}
	public void setSingleSelect(boolean isSingleSelect) {
		this.isSingleSelect = isSingleSelect;
	}
	
}
