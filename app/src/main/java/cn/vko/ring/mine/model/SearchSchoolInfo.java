/*******************************************************************************
 * Copyright (c) 2015 by Luxury Corporation all right reserved.
 * 2015-9-11 
 * 
 *******************************************************************************/ 
package cn.vko.ring.mine.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.home.model.Pager;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 
 * 编写日期:	2015-9-11
 * 作者:	宋宁宁
 * 
 * 历史记录
 *    修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class SearchSchoolInfo implements Serializable{

	public int code;
	public String msg;
	public long stime;
	public Data data;
	public class Data implements Serializable{
		public Pager pager;
		public List<School> school;
		
		public class School implements Serializable{
			public String proId;   //省id
			public String cityId;  //市id
			public String schoolId;//学校id
			public String sName;   //学校名称
			public String areaId;  //区id
		}
	}
}
