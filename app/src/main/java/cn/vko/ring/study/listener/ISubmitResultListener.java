/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: ISubmitResultListener.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.study.listener 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-21 下午5:57:50 
 * @version: V1.0   
 */
package cn.vko.ring.study.listener;

import cn.vko.ring.study.model.ErrorExamSubmitResult;

/** 
 * @ClassName: ISubmitResultListener 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-21 下午5:57:50  
 */
public interface ISubmitResultListener {
	void onSubmitResult(ErrorExamSubmitResult errorRes);
	void onFailSubmit();
}
