package cn.vko.ring.wxapi;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.mine.activity.ToPayActivity;
import cn.vko.ring.mine.presenter.OrderInfoPresenter;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	private IWXAPI api;
	@Bind(R.id.tv_result)
	public TextView tvResult;
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.iv_pay_result)
	public ImageView ivPay;
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		try{
			System.out.print(req.toString());
		}catch(Exception e){
			
		}
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX&& resp.errCode == 0) {
			tvResult.setText("支付成功");
			String transacion =ToPayActivity.transaction;
			if(!TextUtils.isEmpty(transacion) && transacion.contains("#")){
				String strs[] = transacion.split("#");
				new OrderInfoPresenter(WXPayEntryActivity.this, strs[0], Integer.parseInt(strs[1]));
			}
			EventBus.getDefault().post(Constants.WX_PAYMENT_COMPLETE);
		} else {
			ivPay.setImageResource(R.drawable.icon_pay_fail);
			tvResult.setText("支付失败");
		}
	}

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.layout_pay_result;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText("支付结果");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		api = WXAPIFactory.createWXAPI(this, cn.vko.ring.wxapi.Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}
	
	@OnClick(R.id.iv_back)
	void sayBack(){
		finish();
	}
}