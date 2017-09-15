package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.common.base.BaseActivity;

public class BindCommunicationActivity extends BaseActivity implements
		OnClickListener {

	@Bind(R.id.tv_title)
	public TextView bindTitle;
	public boolean isCommunicationBind;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_bind_communication;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		bindTitle.setText("绑定通讯录");
	}

	@OnClick(R.id.iv_back)
	public void sayBack() {
		finish();
	}

	@OnClick(R.id.bind_btn)
	public void isBind() {
		isCommunicationBind = true;
		VkoConfigure.getConfigure(this).put("iscomm", isCommunicationBind);
		// Intent intent = new Intent();
		// intent.putExtra("iscomm", isCommunicationBind);
		// setResult(0, intent);
		Intent intent = new Intent(this, ContactsActivity.class);
		intent.putExtra("flag", 0);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isCommunicationBind = false;
			VkoConfigure.getConfigure(this).put("iscomm", isCommunicationBind);
			Intent intent = new Intent();
			intent.putExtra("iscomm", isCommunicationBind);
			setResult(0, intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
