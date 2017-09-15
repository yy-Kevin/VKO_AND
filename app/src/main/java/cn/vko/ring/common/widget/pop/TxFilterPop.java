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

public class TxFilterPop extends BasePopuWindow implements OnClickListener{

	private TextView tvAll, tvSell,tvTx;
	private OnClickListener listener;

	public void setListener(OnClickListener listener) {
		this.listener = listener;
	}

	public TxFilterPop(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void setUpViews(View contentView) {
		// TODO Auto-generated method stub
		tvAll = (TextView) contentView.findViewById(R.id.tv_all);
		tvSell = (TextView) contentView.findViewById(R.id.tv_sell);
		tvTx = (TextView) contentView.findViewById(R.id.tv_tx);
	}

	@Override
	public void setUpListener() {
		// TODO Auto-generated method stub
		tvAll.setOnClickListener(this);
		tvSell.setOnClickListener(this);
		tvTx.setOnClickListener(this);
	}



	@Override
	public int getAnimationStyle() {
		// TODO Auto-generated method stub
		return R.style.zoomAnimation;
	}

	@Override
	public int getResView() {
		// TODO Auto-generated method stub
		return R.layout.pop_tx_filter;
	}

	@Override
	public boolean updateView(View contentView) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View view) {
		if(listener != null){
			listener.onClick(view);
		}
	}
}
