/*
 *TopicCourseInfo.java [V 1.0.0]
 *classes : cn.vko.ring.mine.model.TopicCourseInfo
 *宣义阳 Create at 2015-8-7 上午10:31:30
 */
package cn.vko.ring.mine.model;

import java.io.Serializable;
import java.util.List;

/**
 * cn.vko.ring.mine.model.TopicCourseInfo
 * @author 宣义阳 
 * create at 2015-8-7 上午10:31:30
 */
public class TopicCourseInfo implements Serializable {

	public int code;
	public String msg;
	public long stime;
	
	public List<Data> data;
	public class Data implements Serializable{
		public String id;
		public String imgUrl;
		public String name;
		public String totalCourse;
	}
}
