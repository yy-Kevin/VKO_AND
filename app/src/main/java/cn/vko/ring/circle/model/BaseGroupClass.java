package cn.vko.ring.circle.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.home.model.BaseResultInfo;
import cn.vko.ring.home.model.Pager;

public class BaseGroupClass extends BaseResultInfo {
	
	private ClassListInfo data;
	
	public ClassListInfo getData() {
		return data;
	}

	public void setData(ClassListInfo data) {
		this.data = data;
	}

	public class ClassListInfo implements Serializable{
		private Pager pager;
		
		private  List<GroupClassInfo> data;

		public Pager getPager() {
			return pager;
		}

		public void setPager(Pager pager) {
			this.pager = pager;
		}

		public List<GroupClassInfo> getData() {
			return data;
		}

		public void setData(List<GroupClassInfo> data) {
			this.data = data;
		}
		
		
	}
}
