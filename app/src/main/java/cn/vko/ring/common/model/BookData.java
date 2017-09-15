/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: BookData.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.common.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-27 下午4:33:07 
 * @version: V1.0   
 */
package cn.vko.ring.common.model;

import java.util.List;

import cn.vko.ring.VkoContext;
import cn.vko.ring.home.model.BaseResultInfo;

/** 
 * @ClassName: BookData 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-27 下午4:33:07  
 */
public class BookData extends BaseResultInfo{
	
	private List<BookModel> data;

	public List<BookModel> getData() {
		return data;
	}

	public void setData(List<BookModel> data) {
		this.data = data;
	}

	
	
}
