package cn.vko.ring.mine.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import cn.shikh.utils.DateUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.model.BaseSignInfo;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.UserInfo;

import com.android.volley.VolleyError;

public class SignInPresenter {

	private Context ctx;
	private ImageView ivSingIn;
	private boolean isSignIn;
	private TextView tvJifen;
	private VolleyUtils<BaseSignInfo> volley;

	public SignInPresenter(Context ctx, ImageView ivSingIn,TextView tvJifen) {
		this.ctx = ctx;
		this.ivSingIn = ivSingIn;
		this.tvJifen = tvJifen;
	}

	public void initView() {
		if (VkoContext.getInstance(ctx).isLogin()) {
			if (VkoContext.getInstance(ctx).getUser().getSignInTime() == 0) {// 刚登录
				switchSignInView(VkoContext.getInstance(ctx).getUser()
						.isSignInState());
			} else {
				switchSignInView(DateUtils.isToday(VkoContext.getInstance(ctx)
						.getUser().getSignInTime()));
			}
		} else {
			switchSignInView(false);
		}

	}

	private void switchSignInView(boolean hasSignIn) {
		if (hasSignIn) {
			isSignIn = true;
			ivSingIn.setImageResource(R.drawable.icon_sign);
		} else {
			ivSingIn.setImageResource(R.drawable.icon_unsing);

		}
	}

	public void signIn() {
		if (!VkoContext.getInstance(ctx).doLoginCheckToSkip(ctx) && !isSignIn) {
			if (volley == null) {
				volley = new VolleyUtils<BaseSignInfo>(ctx,BaseSignInfo.class);
			}
			Uri.Builder builder = volley.getBuilder(ConstantUrl.VK_CIRCLE_SIGN);
			builder.appendQueryParameter("token", VkoContext.getInstance(ctx).getToken());
			volley.sendGETRequest(true,builder);
			volley.setUiDataListener(new UIDataListener<BaseSignInfo>() {
				@Override
				public void onDataChanged(BaseSignInfo response) {
					if (response != null && response.getCode() == 0) {
						switchSignInView(true);
						UserInfo u = VkoContext.getInstance(ctx).getUser();
						u.setSignInTime(System.currentTimeMillis());
						u.setScord(u.getScord()+2);
						VkoContext.getInstance(ctx).setUser(u);
						tvJifen.setText(u.getScord() + "V币");
					}
				}

				@Override
				public void onErrorHappened(String errorCode, String errorMessage) {

				}
			});

		}
	}
}
