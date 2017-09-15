package cn.vko.ring.circle.model;

import java.util.List;

import cn.vko.ring.home.model.BaseResultInfo;
import cn.vko.ring.home.model.Pager;

public class BaseGroupListInfo extends BaseResultInfo {

	private GroupListInfo data;

	public GroupListInfo getData() {
		return data;
	}

	public void setData(GroupListInfo data) {
		this.data = data;
	}

	public class GroupListInfo {
		private Pager pager;

		private List<GroupInfo> data;

		public Pager getPager() {
			return pager;
		}

		public void setPager(Pager pager) {
			this.pager = pager;
		}

		public List<GroupInfo> getData() {
			return data;
		}

		public void setData(List<GroupInfo> data) {
			this.data = data;
		}

	}

}
