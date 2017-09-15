package cn.vko.ring.mine.model;

import java.io.Serializable;



public class VersionUpdateInfo implements Serializable{

	public int code;
	public String msg;
	public String stime;
	public Data data;
	public class Data{
		public int ver;
		public String vers;
		public String url;
		public String info;
	}
}
