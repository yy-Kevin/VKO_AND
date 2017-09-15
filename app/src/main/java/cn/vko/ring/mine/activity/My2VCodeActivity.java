package cn.vko.ring.mine.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.mine.model.MemberInfo;
import cn.vko.ring.mine.model.MyCodeInfo;
import cn.vko.ring.utils.Cord2VUtil;

public class My2VCodeActivity extends BaseActivity {
	@Bind(R.id.iv_code)
	public ImageView ivCode;
	@Bind(R.id.tv_time)
	public TextView tvTime;
	@Bind(R.id.tv_title)
	public TextView tvTitle;

	private VolleyUtils<MyCodeInfo> volleyUtils;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_my2vcode;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText("我的二维码");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		volleyUtils = new VolleyUtils<>(this,MyCodeInfo.class);
		Uri.Builder builder = volleyUtils.getBuilder(ConstantUrl.VK_MY_CODE);
		builder.appendQueryParameter("type","1");
		builder.appendQueryParameter("token", VkoContext.getInstance(this).getToken());
		volleyUtils.sendGETRequest(true,builder);
		volleyUtils.setUiDataListener(new UIDataListener<MyCodeInfo>() {
			@Override
			public void onDataChanged(MyCodeInfo info) {
				if(info != null && info.data != null){
					initCode(info.data.getContent());
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
//
	}

	private void initCode(String code){

		try {
			// Bitmap bm = Cord2VUtil.Create2DCode(this, "群帐号", 500);
//			Bitmap logo = BitmapFactory.decodeResource(super.getResources(),
//					R.drawable.ic_logo);
//			Bitmap bm = Cord2VUtil.createCode(code, logo,
//					BarcodeFormat.QR_CODE, 500);
			Bitmap bm = Cord2VUtil.Create2DCode(this,code,500);
			ivCode.setImageBitmap(bm);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		finish();
	}

}
