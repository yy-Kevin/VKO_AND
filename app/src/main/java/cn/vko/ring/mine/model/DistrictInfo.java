package cn.vko.ring.mine.model;

import java.io.Serializable;
import java.util.List;

public class DistrictInfo implements Serializable {

	public String code;
	public List<Data> data;
	public class Data implements Serializable{
		public String id;
		public String name;
	}
	public String msg;
	public String stime;
}
