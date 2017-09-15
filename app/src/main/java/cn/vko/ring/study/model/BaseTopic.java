package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.home.model.Pager;


/**
 * @author shikh
 *
 */
public class BaseTopic implements Serializable{

	public Pager pager;
	public List<TopicInfo> list;
	public Pager getPager() {
		return pager;
	}
	public void setPager(Pager pager) {
		this.pager = pager;
	}
	public List<TopicInfo> getList() {
		return list;
	}
	public void setList(List<TopicInfo> list) {
		this.list = list;
	} 
	
}
