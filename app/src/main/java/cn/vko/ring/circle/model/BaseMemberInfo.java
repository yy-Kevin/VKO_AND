package cn.vko.ring.circle.model;

import java.util.List;


import cn.vko.ring.home.model.BaseResultInfo;
import cn.vko.ring.home.model.Pager;

public class BaseMemberInfo extends BaseResultInfo{

	private BaseMemberListInfo data;
	
	public BaseMemberListInfo getData() {
		return data;
	}

	public void setData(BaseMemberListInfo data) {
		this.data = data;
	}

	public class BaseMemberListInfo{
		private List<MemberInfo> data;
		private Pager pager;
		public List<MemberInfo> getData() {
			return data;
		}
		public void setData(List<MemberInfo> data) {
			this.data = data;
		}
		public Pager getPager() {
			return pager;
		}
		public void setPager(Pager pager) {
			this.pager = pager;
		}
		
		
	}
	
	
}
