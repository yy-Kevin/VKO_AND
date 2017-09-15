package cn.vko.ring.mine.activity;

import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.widget.xlv.XListView;

public class ActivateRecordActivity extends BaseActivity {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.lv_record)
	public XListView xListView;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_activate_record;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tvTitle.setText("激活记录");
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(false);
	}

	@OnClick(R.id.iv_back)
	public void sayBack() {
		finish();
	}

}
