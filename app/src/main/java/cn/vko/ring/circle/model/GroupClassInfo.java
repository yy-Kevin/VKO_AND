package cn.vko.ring.circle.model;

import java.io.Serializable;

public class GroupClassInfo implements Serializable {
	private String id;
	private String name;
	private String parentId;
	
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		GroupClassInfo g = (GroupClassInfo) o;
		return g.getId().equals(getId());
	}
}
