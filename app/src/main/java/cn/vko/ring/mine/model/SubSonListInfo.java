/*
 *SubSonListInfo.java [V 1.0.0]
 *classes : cn.vko.ring.mine.model.SubSonListInfo
 *宣义阳 Create at 2015-8-6 下午3:36:53
 */
package cn.vko.ring.mine.model;

import java.io.Serializable;
import java.util.List;

/**
 * cn.vko.ring.mine.model.SubSonListInfo
 * @author 宣义阳 
 * create at 2015-8-6 下午3:36:53
 */
public class SubSonListInfo implements Serializable {
	public int code;
	public String msg;
	public long stime;
	
	public List<Data> data;
	public class Data implements Serializable{
		public String bookId;
		public String bookName;
		public String goodsId;
		public String sectionId;
		public List<Video> video;
		public class Video implements Serializable{
			public String duration;
			public String teacherId;
			public String teacherName;
			public String teacherUrl;
			public String track;
			public String trackRate;
			public String videoId;
			public String videoName;
			public String videoUrl;
		}
	}
}
