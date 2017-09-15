package cn.vko.ring.mine.fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseFragment;

public class ActivationFalseFragment extends BaseFragment {
	private ActivationTrueFragment trueFragment;

	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_activation_false;
	}

	@Override
	public void initView(View root) {
		// TODO Auto-generated method stub

	}

	// 点击激活
	@OnClick(R.id.tv_activate)
	public void activateClick() {
		trueFragment = new ActivationTrueFragment();
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.content, trueFragment);
		transaction.hide(this);
		transaction.show(trueFragment);
		transaction.commit();
	}

}
