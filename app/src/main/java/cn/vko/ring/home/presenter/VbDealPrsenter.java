/**   
 * Copyright © 2016 cn.vko.com. All rights reserved.
 * 
 * @Title: VbDealPrsenter.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.home.presenter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2016-1-13 下午5:31:02 
 * @version: V1.0   
 */
package cn.vko.ring.home.presenter;

import com.android.volley.VolleyError;

import android.content.Context;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.BaseResultInfo;

/**
 * @ClassName: VbDealPrsenter
 * @Description: 根据不同的类型 调用获取VB接口
 * @author: JiaRH
 * @date: 2016-1-13 下午5:31:02
 */
public class VbDealPrsenter implements UIDataListener<BaseResultInfo> {
	/* 分享视频 */
	public static final int SHARE_VIDEO = 0x01;
	/* 分享话题 */
	public static final int SHARE_ARTICLE = 0x03;
	/* 邀请好友 */
	public static final int SHARE_FRIENDS = 0x02;
	/* 邀请好友 */
	public static final int LOOK_VIDEO = 0x04;
	
	private VolleyUtils<BaseResultInfo> mVolleyUtils;
	private Context context ;

	public VbDealPrsenter(Context context) {
		super();
		this.context = context;
		if (mVolleyUtils == null) {
			mVolleyUtils = new VolleyUtils<BaseResultInfo>(context,BaseResultInfo.class);
			mVolleyUtils.setUiDataListener(this);

		}
	}
	/**
	 * @Title: doRequest
	 * @Description: 请求
	 * @param shareType
	 * @return: void
	 */
	public void doRequest(int shareType) {
		switch (shareType) {
			case SHARE_VIDEO:
				mVolleyUtils.sendGETRequest(true,mVolleyUtils.getBuilder(ConstantUrl.VK_VB_SHARE)
						.appendQueryParameter("token", VkoContext.getInstance(context).getToken()).appendQueryParameter("objType", 1+""));
				break;
			case SHARE_ARTICLE:
				mVolleyUtils.sendGETRequest(true,mVolleyUtils.getBuilder(ConstantUrl.VK_VB_SHARE)
						.appendQueryParameter("token", VkoContext.getInstance(context).getToken()).appendQueryParameter("objType", 2+""));
				break;
			case SHARE_FRIENDS:
				mVolleyUtils.sendGETRequest(true,mVolleyUtils.getBuilder(ConstantUrl.VK_VB_ASKFRIEND).appendQueryParameter("token", VkoContext.getInstance(context).getToken()));
				break;
			default:
				break;
		}

	}

	@Override
	public void onDataChanged(BaseResultInfo data) {

	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}
