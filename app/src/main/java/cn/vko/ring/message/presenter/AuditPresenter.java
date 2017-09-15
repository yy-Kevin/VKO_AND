/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: AuditPresenter.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.message.presenter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-4 下午4:48:20 
 * @version: V1.0   
 */
package cn.vko.ring.message.presenter;

import com.android.volley.VolleyError;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.message.listener.IAuditResultListener;
import cn.vko.ring.message.model.AutoResultModel;
import cn.vko.ring.message.model.MsgItemModel;
import cn.vko.ring.mine.model.MsgInfo;
import android.content.Context;
import android.net.Uri;

/**
 * @ClassName: AuditPresenter
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-4 下午4:48:20
 */
public class AuditPresenter implements UIDataListener<AutoResultModel> {
	private Context context;
	private MsgItemModel itemModel;
	private VolleyUtils<AutoResultModel> mVolleyUtils;
	private IAuditResultListener onAuditListener;

	public AuditPresenter(Context context, MsgItemModel itemModel) {
		super();
		this.context = context;
		this.itemModel = itemModel;
		initData();
	}

	/** 
	 * @Title: initData 
	 * @Description: TODO
	 * @return: void
	 */
	private void initData() {
		// TODO Auto-generated method stub
		mVolleyUtils = new VolleyUtils<AutoResultModel>(context,AutoResultModel.class);
	}

	public void doOption(int option) {
		Uri.Builder b = mVolleyUtils.getBuilder(ConstantUrl.VK_AUDIT);
		b.appendQueryParameter("groupId", itemModel.getGroupId());
		b.appendQueryParameter("userId", itemModel.getApplyerId());
		b.appendQueryParameter("userName", itemModel.getApplyerName());
		b.appendQueryParameter("option", option+"");
		b.appendQueryParameter("logId", itemModel.getId());
		mVolleyUtils.sendGETRequest(true,b);
		mVolleyUtils.setUiDataListener(this);
	}


	public void setOnAuditListener(IAuditResultListener onAuditListener) {
		this.onAuditListener = onAuditListener;
	}


	@Override
	public void onDataChanged(AutoResultModel response) {
		if (onAuditListener!=null&&response!=null) {
			onAuditListener.auditResult(response);
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}
