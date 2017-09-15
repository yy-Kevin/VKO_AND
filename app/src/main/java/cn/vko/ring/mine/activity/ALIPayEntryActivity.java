package cn.vko.ring.mine.activity;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.mine.presenter.OrderInfoPresenter;

/**
 * 支付宝支付结果页
 * @author Administrator
 *
 */
public class ALIPayEntryActivity extends BaseActivity {
	@Bind(R.id.tv_result)
	public TextView tvResult;
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.iv_pay_result)
	public ImageView ivPay;
	private String result,orderId;
//	private boolean isFinish,isTime;
	private int openMember;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
//			isTime = true;
//			if(result != null &&"9000".equals(result)){
//				if(openMember == 1){
//					EventBus.getDefault().post(Constants.OPEN_MEMBER);
//				}else{
//					EventBus.getDefault().post(Constants.PAYMENT_COMPLETE);
//				}
//				
//			}
//			if(isFinish){
				finish();
//			}
		};
	};
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.layout_pay_result;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText("支付结果");
		result = getIntent().getExtras().getString("resultStatus");
		orderId = getIntent().getExtras().getString("ORDERID");
		openMember = getIntent().getExtras().getInt("OPEN_MEMBER");
		if(result != null &&"9000".equals(result)){
			tvResult.setText("支付成功");
//			mHandler.sendEmptyMessageDelayed(0, 2000);
			new OrderInfoPresenter(this, orderId, openMember);
		}else {
			ivPay.setImageResource(R.drawable.icon_pay_fail);
			tvResult.setText("支付失败");
			mHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}
	@OnClick(R.id.iv_back)
	void sayBack(){
//		isFinish = true;
//		if(isTime){
//		}
		finish();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
//		isFinish = true;
//		if(isTime){
//		}
		finish();
	}
}
