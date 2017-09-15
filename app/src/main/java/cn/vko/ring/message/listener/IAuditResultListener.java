/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: IAuditResultListener.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.message.listener 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-7 上午10:28:22 
 * @version: V1.0   
 */
package cn.vko.ring.message.listener;

import cn.vko.ring.message.model.AutoResultModel;

/** 
 * @ClassName: IAuditResultListener 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-7 上午10:28:22  
 */
public interface IAuditResultListener {
	void auditResult(AutoResultModel autoResultModel);
}
