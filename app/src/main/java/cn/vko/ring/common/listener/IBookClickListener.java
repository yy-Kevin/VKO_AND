/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: IBookClickListener.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.common.listener 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-27 下午5:42:32 
 * @version: V1.0   
 */
package cn.vko.ring.common.listener;

import cn.vko.ring.common.model.Book;

/** 
 * @ClassName: IBookClickListener 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-27 下午5:42:32  
 */
public interface IBookClickListener {
	void onBookSelected(Book book);
}
