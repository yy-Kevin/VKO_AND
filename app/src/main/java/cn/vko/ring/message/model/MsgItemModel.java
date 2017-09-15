/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: MsgItemModel.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.message.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-1 上午10:19:23 
 * @version: V1.0   
 */
package cn.vko.ring.message.model;

import android.text.TextUtils;

/**
 * @ClassName: MsgItemModel
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-1 上午10:19:23
 */
public class MsgItemModel extends BaseMsgModel {
	private int MSG_TYPE = 0;
	private String gotoo;
	private String ctime;
	private String imgUrl;
	private String applyerId;
	private String applyerName;
	private String groupId;
	private String groupName;
	private String msg;
	private String content;// 消息内容
	private String id;
	private String title;// 消息标题
	private String linkUrl; // 消息连接地址
	private boolean isNew;
	private int handle;
	private String type;
	private String questionId;
	
	
	
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getHandle() {
		return handle;
	}
	public void setHandle(int handle) {
		this.handle = handle;
	}
	public int getMSG_TYPE() {
		return MSG_TYPE;
	}
	public void setMSG_TYPE(int mSG_TYPE) {
		MSG_TYPE = mSG_TYPE;
	}
	public String getGotoo() {
		return gotoo;
	}
	public void setGotoo(String gotoo) {
		this.gotoo = gotoo;
	}
	public String getCtime() {
		
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getApplyerId() {
		return applyerId;
	}
	public void setApplyerId(String applyerId) {
		this.applyerId = applyerId;
	}
	public String getApplyerName() {
		return applyerName;
	}
	public void setApplyerName(String applyerName) {
		this.applyerName = applyerName;
	}
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
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
}
