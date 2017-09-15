/**   
 * Copyright © 2016 cn.vko.com. All rights reserved.
 * 
 * @Title: ItemDialogModel.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.common.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2016-1-7 上午11:28:41 
 * @version: V1.0   
 */
package cn.vko.ring.common.model;

import java.io.Serializable;

import android.graphics.Color;

/** 
 * @ClassName: ItemDialogModel 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2016-1-7 上午11:28:41  
 */
public class ItemDialogModel implements Serializable{
	private String content;
	/** 设置颜色*/
	private  int color=Color.parseColor("#0079ff");
	private int textSize=20;
	
	public ItemDialogModel(String content) {
		super();
		this.content = content;
	}
	public int getTextSize() {
		return textSize;
	}
	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
}
