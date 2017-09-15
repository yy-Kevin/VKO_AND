package cn.vko.ring.circle.model;

import cn.vko.ring.home.model.BaseResultInfo;

public class BaseRechargeInfo extends BaseResultInfo {
	private RechargeInfo data;

	public RechargeInfo getData() {
		return data;
	}

	public void setData(RechargeInfo data) {
		this.data = data;
	}

	public class RechargeInfo {
		private boolean flag;
		private int needScore;
		
		
		public int getNeedScore() {
			return needScore;
		}

		public void setNeedScore(int needScore) {
			this.needScore = needScore;
		}

		public boolean isFlag() {
			return flag;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}

	}
}
