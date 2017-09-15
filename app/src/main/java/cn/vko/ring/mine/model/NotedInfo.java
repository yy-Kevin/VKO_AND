package cn.vko.ring.mine.model;

import java.io.Serializable;
import java.util.List;

public class NotedInfo implements Serializable {
	public int code;
	public String msg;
	public String stime;

	public List<Data> data;
	public class Data implements Serializable {
		public String id;
		public int objType;
		public int countEb;
		public String name;
		public String subjectId;
		public String objId;
		public String examId;
		public String knowledgeId;
		public String courseId;
	}
}
