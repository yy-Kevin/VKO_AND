package cn.vko.ring;

import android.app.Activity;
import android.content.Context;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.home.activity.LoginActivity;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.utils.ACache;

public class VkoContext {

	private static VkoContext self;
	private UserInfo user;
	private Context ctx;

	public UserInfo getUser() {
		if (user == null) {
			user = (UserInfo) ACache.get(ctx,"user").getAsObject("USER");
		}
		return user;
	}

	private VkoContext (Context context){
		this.ctx = context;
	}
	public void setUser(UserInfo user) {
		this.user = user;
		ACache.get(ctx,"user").put("USER",user);
	}


	public static VkoContext getInstance(Context context) {
		if (self == null) {
			self = new VkoContext(context);
		}
		return self;
	}

	public String getUserId() {
		return getUser() != null ? getUser().getUserId() : null;
	}

	public String getToken() {
		return getUser() != null ? getUser().getToken() : null;
	}

	public String getGradeId() {
		if(getUser() != null){
			return getUser().getGradeId() == null ? "-1" : getUser().getGradeId();
		}
		return "-1";
	}

	public boolean isLogin() {
		return getUser() != null;
	}

	public boolean doLoginCheckToSkip(Context act) {
		if (!isLogin()) {
			StartActivityUtil.startActivity(act, LoginActivity.class);
			try {
				((Activity) ctx).overridePendingTransition(R.anim.bottom_in, 0);
			} catch (Exception e) {

			}
			return true;
		}
		return false;
	}
}
