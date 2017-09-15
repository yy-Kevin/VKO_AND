/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: AutoResultModel.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.message.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-4 下午5:29:59 
 * @version: V1.0   
 */
package cn.vko.ring.message.model;

import cn.vko.ring.home.model.BaseResultInfo;

/** 
 * @ClassName: AutoResultModel 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-4 下午5:29:59  
 */
public class AutoResultModel extends BaseResultInfo{
	private boolean data;

	public boolean getData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}
	
}
