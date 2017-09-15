/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: MessageInfo.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.mine.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-7-22 下午2:07:14 
 * @version: V1.0   
 */
package cn.vko.ring.mine.model;

import java.io.Serializable;


/** 
 * 推送消息
 * @ClassName: MessageInfo 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-7-22 下午2:07:14  
 */
public class MessageInfo implements Serializable {
	private String title;
	private String content;
	private String id;
	private String imaUrl;
	private String linkUrl;
	private String ctime;
	private String showTitle;
	
	
	public String getShowTitle() {
		return showTitle;
	}

	public void setShowTitle(String showTitle) {
		this.showTitle = showTitle;
	}

	public String getCtime() {
		return ctime;
	}
	
	public void setCtime(String ctime) {
	
		this.ctime = ctime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	
	public String getImaUrl() {
	
		return imaUrl;
	}
	
	public void setImaUrl(String imaUrl) {
	
		this.imaUrl = imaUrl;
	}
	
	public String getLinkUrl() {
	
		return linkUrl;
	}
	
	public void setLinkUrl(String linkUrl) {
	
		this.linkUrl = linkUrl;
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		MessageInfo m = (MessageInfo) o;
		return m.getId().equals(getId());
	}
	
}
