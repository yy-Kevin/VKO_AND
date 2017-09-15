/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: SysMsgModel.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.message.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-7 下午2:08:51 
 * @version: V1.0   
 */
package cn.vko.ring.message.model;

import java.io.Serializable;

/** 
 * @ClassName: SysMsgModel 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-7 下午2:08:51  
 */
public class SysMsgModel implements Serializable{

	private String groupId;
	private String groupName;;;
	private String gotoo;
	private String type;
	private String msg;
	private String groupUserIds;
	private String ctime;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGotoo() {
		return gotoo;
	}
	public void setGotoo(String gotoo) {
		this.gotoo = gotoo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getGroupUserIds() {
		return groupUserIds;
	}
	public void setGroupUserIds(String groupUserIds) {
		this.groupUserIds = groupUserIds;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	
	

}
