/**   
 * Copyright © 2016 cn.vko.com. All rights reserved.
 * 
 * @Title: CancleCollectEvent.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.mine.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2016-1-4 下午3:03:37 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import cn.vko.ring.Constants;

/**
 * @ClassName: CancleCollectEvent
 * @Description: TODO
 * @author: JiaRH
 * @date: 2016-1-4 下午3:03:37
 */
public class CancleCollectEvent {
	
	public CancleCollectEvent(){
	}
	public CancleCollectEvent(String id){
		this.id = id;
	}
	
	private String id;
	/** type=1 删除收藏使用*/
	private int type ;



	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
