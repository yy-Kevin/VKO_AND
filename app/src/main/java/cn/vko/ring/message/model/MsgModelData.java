/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: MsgModelData.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.message.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-7 下午2:14:01 
 * @version: V1.0   
 */
package cn.vko.ring.message.model;

import cn.vko.ring.home.model.BaseResultInfo;

/**
 * @ClassName: MsgModelData
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-7 下午2:14:01
 */
public class MsgModelData extends BaseResultInfo {
	private BaseMsgModel data;

	public BaseMsgModel getData() {
		return data;
	}
	public void setData(BaseMsgModel data) {
		this.data = data;
	}
}
