package cn.vko.ring.home.presenter;

import java.io.Serializable;

import com.android.volley.VolleyError;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.activity.AdActivity;
import cn.vko.ring.home.model.AD;
import cn.vko.ring.home.model.AD.ADactivity.ADitem;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

public class AdPresenter {
	private Context mContext;
	private VolleyUtils<AD> mVolleyUtil;
	ADCallBack callBack;

	public void setCallBack(ADCallBack callBack) {
		this.callBack = callBack;
	}

	public interface ADCallBack {
		void onAdResult(ADitem item);
	}

	public AdPresenter(Context mContext) {
		super();
		this.mContext = mContext;
		this.mVolleyUtil = new VolleyUtils<AD>(mContext,AD.class);
	}

	public void getAds(final ADCallBack call) {
	
		Uri.Builder b = mVolleyUtil.getBuilder(ConstantUrl.VK_ACTIVITY);
		mVolleyUtil.sendGETRequest(false,b);
		mVolleyUtil.setUiDataListener(new UIDataListener<AD>() {
			@Override
			public void onDataChanged(AD response) {
				if (response != null && response.getData() != null) {
					ADitem aditem = response.getData().getActivity();
					if(aditem==null)return;
					if (isNewAd(aditem)) {

						StartActivityUtil.startActivity(mContext,
								AdActivity.class);
					}
					call.onAdResult(aditem);
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	public boolean isNewAd(ADitem aditem) {

		String oldId = VkoConfigure.getConfigure(mContext).getString(AD.AD_ID);
		if (!TextUtils.isEmpty(oldId)&&!TextUtils.isEmpty(aditem.getActId())) {
			if (!aditem.getActId().equals(oldId)) {
				savead(aditem);
				return true;
			}
		} else if(TextUtils.isEmpty(aditem.getActId())){
			//过期
			clearAdinfo();
			return false;
		}else{
			savead(aditem);
			return true;
		}
	
		return false;
	}

	private void clearAdinfo() {
		VkoConfigure.getConfigure(mContext).put(AD.AD_ID, null);
		VkoConfigure.getConfigure(mContext).put(AD.AD_PIC_URL, null);
		VkoConfigure.getConfigure(mContext).put(AD.AD_AC_URL,
				null);
		VkoConfigure.getConfigure(mContext).put(AD.AD_START_TIME,
				null);
		VkoConfigure.getConfigure(mContext).put(AD.AD_END_TIME,
				null);
		VkoConfigure.getConfigure(mContext).put(AD.AD_NAME,
				null);
		VkoConfigure.getConfigure(mContext).put(AD.AD_NOTE,
				null);
		VkoConfigure.getConfigure(mContext).put(AD.AD_HAS_LOOK, false);
	}

	private void savead(ADitem aditem) {
		VkoConfigure.getConfigure(mContext).put(AD.AD_ID, aditem.getActId());
		VkoConfigure.getConfigure(mContext).put(AD.AD_PIC_URL, aditem.getActBgImg());
		VkoConfigure.getConfigure(mContext).put(AD.AD_AC_URL,
				aditem.getActUrl());
		VkoConfigure.getConfigure(mContext).put(AD.AD_START_TIME,
				aditem.getStartTime() + "");
		VkoConfigure.getConfigure(mContext).put(AD.AD_END_TIME,
				aditem.getEndTime() + "");
		VkoConfigure.getConfigure(mContext).put(AD.AD_NAME,
				aditem.getName() + "");
		VkoConfigure.getConfigure(mContext).put(AD.AD_NOTE,
				aditem.getNote() + "");
		VkoConfigure.getConfigure(mContext).put(AD.AD_HAS_LOOK, false);
	}

}
