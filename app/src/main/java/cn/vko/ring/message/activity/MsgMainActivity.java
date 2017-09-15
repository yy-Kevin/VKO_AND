package cn.vko.ring.message.activity;


import android.text.TextUtils;
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
 * 消息主页
 * 
 * @author JiaRH
 * 
 */
public class MsgMainActivity extends BaseActivity {
	@Bind(R.id.iv_back)
	public ImageView ivBack;
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_error)
	public TextView tvError;
	@Bind(R.id.msglistview)
	XListView msgListView;
	MsgListPresenter listPresenter;
	@Bind(R.id.empty)
	View empty;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_message;
	}

	@Override
	public void initView() {
		EventBus.getDefault().register(this);
		ivBack.setVisibility(View.VISIBLE);
		tvTitle.setText("消息");
		tvError.setText(R.string._error_down_refresh);
	}

	@OnClick(R.id.iv_back)
	public void goBack() {
		finish();
	}

	@Override
	public void initData() {
		listPresenter = new MsgListPresenter(this, msgListView, this, 1, empty);

	}
	@Subscribe
	public void onEventMainThread(String event) {
		if (!TextUtils.isEmpty(event)
				&& event.equals(Constants.REFRESH_MSG_LIST)) {
			if (listPresenter != null) {
				listPresenter.refreshData();
			} else {
				initData();
			}
		}
	}

	/*
	 * 
	 * @Description: TODO
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
