package cn.vko.ring.mine.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.mine.fragment.ActivationFalseFragment;

public class ActivationCodeActivity extends BaseActivity {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_right)
	public TextView tvRight;
	private FragmentManager fragmentManager;
	private ActivationFalseFragment falseFragment;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_activation_code;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		falseFragment = new ActivationFalseFragment();
		transaction.add(R.id.content, falseFragment);
		transaction.show(falseFragment);
		transaction.commit();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		tvTitle.setText("激活码");
		tvRight.setVisibility(View.VISIBLE);
		tvRight.setText("激活记录");
	}

	@OnClick(R.id.iv_back)
	public void sayBack() {
		finish();
	}

	@OnClick(R.id.tv_right)
	public void activationRecord() {
		StartActivityUtil.startActivity(ActivationCodeActivity.this,
				ActivateRecordActivity.class);
	}

}
