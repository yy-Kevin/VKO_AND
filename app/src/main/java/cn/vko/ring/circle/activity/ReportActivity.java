package cn.vko.ring.circle.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.model.StringRequestInfo;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends BaseActivity {

	@Bind(R.id.study_layout)
	public RelativeLayout studyLayout;
	@Bind(R.id.sexy_layout)
	public RelativeLayout sexyLayout; 
	@Bind(R.id.advert_layout)
	public RelativeLayout advertLayout;
	@Bind(R.id.politics_layout)
	public RelativeLayout politicsLayout;
	@Bind(R.id.rumor_layout)
	public	RelativeLayout rumorlayout;
	@Bind(R.id.cheat_layout)
	public RelativeLayout cheatLayout;
	@Bind(R.id.iv_study)
	public ImageView ivStudy;
	@Bind(R.id.iv_sexy)
	public ImageView ivSexy;
	@Bind(R.id.iv_advert)
	public ImageView ivAdvert;
	@Bind(R.id.iv_politics)
	public ImageView ivPolitics; 
	@Bind(R.id.iv_rumor)
	public ImageView ivRumor; 
	@Bind(R.id.iv_cheat)
	public ImageView ivCheat;
	@Bind(R.id.tv_supply)
	public TextView tvSupply;

	@Bind(R.id.iv_back)
	public ImageView ivBack;
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	
	private ImageView ivTemp;
	
	@Bind(R.id.but_submit)
	public Button btnSubmit;
	private String content="",rt = "6";
	private int type;
	private String objId;
	private VolleyUtils<StringRequestInfo> volley;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_report;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stu
		ivTemp = ivStudy;
		tvTitle.setText(R.string.answer_report_text);
		volley = new VolleyUtils<StringRequestInfo>(this,StringRequestInfo.class);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		objId = getIntent().getExtras().getString("OBJID");
		type = getIntent().getExtras().getInt("TYPE");
	}
	@OnClick({R.id.study_layout,R.id.iv_back,R.id.sexy_layout,R.id.advert_layout,R.id.politics_layout,R.id.rumor_layout,R.id.cheat_layout,R.id.tv_supply,R.id.but_submit})
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == ivBack) {
			finish();
		} else if (v == studyLayout) {
			switchBitmap(ivStudy);
			rt = "6";
		} else if (v == sexyLayout) {
			switchBitmap(ivSexy);
			rt = "1";
		} else if (v == advertLayout) {
			switchBitmap(ivAdvert);
			rt = "2";
		} else if (v == politicsLayout) {
			switchBitmap(ivPolitics);
			rt = "3";
		} else if (v == rumorlayout) {
			switchBitmap(ivRumor);
			rt = "4";
		} else if (v == cheatLayout) {
			switchBitmap(ivCheat);
			rt = "5";
		}else if(v == tvSupply){
			Bundle bundle = new Bundle();
			bundle.putString("CONTENT", isSupply ? tvSupply.getText().toString():"");
			StartActivityUtil.startForResult(this, ReportBActivity.class, bundle, 100);
		}else if(v == btnSubmit){
			submitData();
		}

	}

	private void submitData() {
		// TODO Auto-generated method stub
		Map<String,String> params = new HashMap<String,String>();
		Uri.Builder builder = volley.getBuilder(ConstantUrl.VK_REPORT);
		params.put("cnt", content);
		params.put("rt", rt);
		params.put("objId", objId);
		params.put("objType", type+"");
		params.put("token", VkoContext.getInstance(this).getUser().getToken());
		volley.sendPostRequest(true,ConstantUrl.VK_REPORT,params);
		volley.setUiDataListener(new UIDataListener<StringRequestInfo>() {
			@Override
			public void onDataChanged(StringRequestInfo t) {
				if(t != null){
//					Toast.makeText(ReportActivity.this, t.getMsg(), Toast.LENGTH_LONG).show();
					if(0 == t.getCode()){
						finish();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	private void switchBitmap(ImageView iv) {
		if (iv == ivTemp)
			return;
		ivTemp.setVisibility(View.GONE);
		iv.setVisibility(View.VISIBLE);
		ivTemp = iv;
	}
	
	boolean isSupply;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100 && data != null){
			content = data.getExtras().getString("CONTENT");
			if(!TextUtils.isEmpty(content)){
				isSupply = true;
				tvSupply.setText(content);
			}
		}
	}

	public void onPostExecute(StringRequestInfo t) {
		// TODO Auto-generated method stub
		if(t != null){
//			Toast.makeText(this, t.getMsg(), 0).show();
			if(t.getCode() == 0){
				finish();
			}
		}
	}

}
