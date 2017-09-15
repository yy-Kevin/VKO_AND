/*
 *UserAppearanceInfo.java [V 1.0.0]
 *classes : cn.vko.ring.mine.model.UserAppearanceInfo
 *宣义阳 Create at 2015-7-27 上午11:47:45
 */
package cn.vko.ring.home.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;

/**
 * cn.vko.ring.mine.model.UserAppearanceInfo
 * @author 宣义阳 create at 2015-7-27 上午11:47:45
 */
public class UserAppearanceInfo extends BaseResponseInfo {
	public List<Data> data;

	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}

	public class Data implements Serializable {
		public String id;
		public String name;
		public String pic;

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
		public String getPic() {
			return pic;
		}
		public void setPic(String pic) {
			this.pic = pic;
		}
	}
}
