/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: IDeviceTokenBindListener.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.home.listener 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-22 下午3:39:18 
 * @version: V1.0   
 */
package cn.vko.ring.home.listener;

/** 
 * @ClassName: IDeviceTokenBindListener 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-22 下午3:39:18  
 */
public interface IDeviceTokenBindListener {
	/**
	 * 
	 * @Title: unBind 
	 * @Description: 解绑
	 * @param isSuccess
	 * @return: void
	 */
	void unBind(boolean isSuccess);
}
