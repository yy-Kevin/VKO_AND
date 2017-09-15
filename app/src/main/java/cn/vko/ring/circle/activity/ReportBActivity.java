package cn.vko.ring.circle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;

public class ReportBActivity extends BaseActivity {

	@Bind(R.id.iv_back)
	public ImageView ivBack;
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_right)
	public TextView tvText;
	@Bind(R.id.but_submit)
	public Button btnSubmit;
	@Bind(R.id.et_content)
	public EditText etContent;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_reportb;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		String content = getIntent().getExtras().getString("CONTENT");
		tvTitle.setText(R.string.answer_report_text);
		tvText.setVisibility(View.VISIBLE);
		if(!TextUtils.isEmpty(content)){
			etContent.setText(content);
		}
	}

	@OnClick({R.id.but_submit,R.id.iv_back})
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == ivBack) {
			finish();
			hideSoftInput(this, etContent, true);
		} else if (v == btnSubmit) {
			String contnet = etContent.getText().toString();
			Intent data = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("CONTENT", contnet);
			data.putExtras(bundle);
			setResult(20, data);
			hideSoftInput(this, etContent, true);
			finish();
		}

	}

}
