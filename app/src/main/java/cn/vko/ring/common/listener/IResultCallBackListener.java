/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: IResultCallBackListener.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.common.listeners 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-19 上午11:47:21 
 * @version: V1.0   
 */
package cn.vko.ring.common.listener;

/** 
 * @ClassName: IResultCallBackListener 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-19 上午11:47:21  
 */
public interface IResultCallBackListener<T> {
	public void onSuccess(T t);
	public void onFail();
}
