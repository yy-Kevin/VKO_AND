package cn.vko.ring.mine.model;

import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;
import cn.vko.ring.home.model.Pager;

/**
 * @author shikh
 * 
 */
public class BaseMessageInfo extends BaseResponseInfo {

	private BaseMessage data;

	public BaseMessage getData() {

		return data;
	}

	public void setData(BaseMessage data) {

		this.data = data;
	}

	public class BaseMessage {

		private List<MessageInfo> msglist;
		private Pager pager;

		public Pager getPager() {

			return pager;
		}

		public void setPager(Pager pager) {

			this.pager = pager;
		}

		public List<MessageInfo> getMsglist() {

			return msglist;
		}

		public void setMsglist(List<MessageInfo> msglist) {

			this.msglist = msglist;
		}
	}

}
