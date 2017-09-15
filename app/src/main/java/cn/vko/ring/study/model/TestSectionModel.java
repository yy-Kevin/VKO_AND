/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: TestSectionModel.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.study.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-26 下午2:13:45 
 * @version: V1.0   
 */
package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.home.model.BaseResultInfo;

/** 
 * 测试 章节列表
 * @ClassName: TestSectionModel 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-26 下午2:13:45  
 */
public class TestSectionModel extends BaseResultInfo{
	private List<TestSection> data;
	
	public List<TestSection> getData() {
		return data;
	}

	public void setData(List<TestSection> data) {
		this.data = data;
	}
}
