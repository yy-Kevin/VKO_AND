package cn.vko.ring.circle.activity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.utils.Cord2VUtil;

public class Group2VCodeActivity extends BaseActivity {
	@Bind(R.id.iv_code)
	public ImageView ivCode;
	@Bind(R.id.tv_time)
	public TextView tvTime;
	@Bind(R.id.tv_title)
	public TextView tvTitle;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_my2vcode;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText("群二维码");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		String code = getIntent().getExtras().getString("CODE");
		try {
			// Bitmap bm = Cord2VUtil.Create2DCode(this, "群帐号", 500);
			Bitmap logo = BitmapFactory.decodeResource(super.getResources(),
					R.drawable.ic_logo);
			Bitmap bm = Cord2VUtil.createCode(code, logo,
					BarcodeFormat.QR_CODE, 500);
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
