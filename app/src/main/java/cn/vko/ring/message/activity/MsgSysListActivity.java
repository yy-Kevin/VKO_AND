/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: MsgSysListActivity.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.message.activity 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-1 下午3:43:06 
 * @version: V1.0   
 */
package cn.vko.ring.message.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.message.presenter.MsgListPresenter;

/**
 * @ClassName: MsgSysListActivity
 * @Description: 系统消息列表
 * @author: JiaRH
 * @date: 2015-12-1 下午3:43:06
 */
public class MsgSysListActivity extends BaseActivity {
	@Bind(R.id.iv_back)
	public ImageView ivBack;
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.msglistview)
	XListView msgListView;
	@Bind(R.id.empty)
	View empty;
	@Bind(R.id.tv_error)
	public TextView tvError;
	MsgListPresenter msgListPresenter;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_msg_sys_list;
	}
	@Override
	public void initView() {
		EventBus.getDefault().register(this);
		tvTitle.setText("消息详情");
		tvError.setText(R.string._error_down_refresh);
	}
	@OnClick(R.id.iv_back)
	void onBackClick() {
		finish();
	}
	@Override
	public void initData() {
		msgListPresenter=new MsgListPresenter(this, msgListView,this,2,empty);
	}
	@Subscribe
	public void onEventMainThread(String msg){
		if (msg.equals(Constants.GROUP_AUDIT_REFRESH)) {
			if (msgListPresenter!=null) {
				msgListPresenter.refreshData();
			}
		}
	}
	/*  
	 * 
	 * @Description: TODO
	 *  
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
