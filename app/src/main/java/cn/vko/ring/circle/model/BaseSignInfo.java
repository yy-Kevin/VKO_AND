package cn.vko.ring.circle.model;

import java.io.Serializable;

import cn.vko.ring.home.model.BaseResultInfo;

public class BaseSignInfo extends BaseResultInfo {

	private SignInfo data;
	
	public SignInfo getData() {
		return data;
	}

	public void setData(SignInfo data) {
		this.data = data;
	}


	public class SignInfo implements Serializable{
		private int vb;
		boolean signInState;
		public int getVb() {
			return vb;
		}
		public void setVb(int vb) {
			this.vb = vb;
		}
		public boolean isSignInState() {
			return signInState;
		}
		public void setSignInState(boolean signInState) {
			this.signInState = signInState;
		}
		
		
		
	}
}
