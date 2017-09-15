/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: BaseMsgModel.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.message.model 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-1 上午10:09:12 
 * @version: V1.0   
 */
package cn.vko.ring.message.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.home.model.BaseResultInfo;

/**
 * @ClassName: BaseMsgModel
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-1 上午10:09:12
 */
public class BaseMsgModel implements Serializable {
	/* 系统消息，消息首页list条目 */
	public static final int MSG_SYS_ICON = 1;
	/* 消息文本，或链接 */
	public static final int MSG_LINK_TEXT = 2;
	/* 加群审核 */
	public static final int MSG_GROUP_AUDIT = 3;
	/* 群相关 以及和问题相关需要跳转*/
	public static final int MSG_GROUP_INFO =  4;
	
	private List<MsgItemModel> msglist;


	public List<MsgItemModel> getMsglist() {
		return msglist;
	}
	public void setMsglist(List<MsgItemModel> msglist) {
		this.msglist = msglist;
	}
}
