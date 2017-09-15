package cn.vko.ring.common.widget.pop;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.circle.activity.CreateGroupOneActivity;
import cn.vko.ring.circle.activity.SearchGroupActivity;
import cn.vko.ring.common.base.BasePopuWindow;
import cn.vko.ring.home.activity.ScanActivity;

public class CircleAddPop extends BasePopuWindow {

	private TextView tvSearch, tvCreate,tvScan;

	public CircleAddPop(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void setUpViews(View contentView) {
		// TODO Auto-generated method stub
		tvSearch = (TextView) contentView.findViewById(R.id.tv_search_group);
		tvCreate = (TextView) contentView.findViewById(R.id.tv_create_group);
		tvScan = (TextView) contentView.findViewById(R.id.tv_scan_group);
	}

	@Override
	public void setUpListener() {
		// TODO Auto-generated method stub
		tvSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StartActivityUtil.startActivity(context, SearchGroupActivity.class);
				dismiss();
			}
		});
		tvCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StartActivityUtil.startActivity(context, CreateGroupOneActivity.class);
				dismiss();
			}
		});
		tvScan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StartActivityUtil.startActivity(context, ScanActivity.class);
				dismiss();
			}
		});
	}


	@Override
	public int getAnimationStyle() {
		// TODO Auto-generated method stub
		return R.style.zoomAnimation;
	}

	@Override
	public int getResView() {
		// TODO Auto-generated method stub
		return R.layout.pop_circle_add;
	}

	@Override
	public boolean updateView(View contentView) {
		// TODO Auto-generated method stub
		return false;
	}

}
