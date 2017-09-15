package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri.Builder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.UserInfo;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;


public class SelectGradeActivity extends BaseActivity implements
		OnClickListener, UIDataListener<String> {

	private ImageView ivLeft;
	private TextView tvTitle;
	// 选择年级相关信息
	private TextView tvJuniOne, tvJuniTwo, tvJuniThree, tvHighOne, tvHighTwo,
			tvHighThree,tvLittleOne,tvLittleTwo,tvLittleThree,tvLittlefour,tvLittlefive,tvLittlesix;
	private Intent intent;
	private Drawable defaultBg;
	private UserInfo userInfo;
	private int temp;
	private VkoConfigure vkoConfigure;
	private VkoContext vkoContext;

	@Override
	public int setContentViewResId() {
		return R.layout.activity_select_grade;
	}

	@Override
	public void initView() {
		ivLeft = (ImageView) this.findViewById(R.id.iv_back);
		tvTitle = (TextView) this.findViewById(R.id.tv_title);
		tvJuniOne = (TextView) this.findViewById(R.id.tv_junione);
		tvJuniTwo = (TextView) this.findViewById(R.id.tv_junitwo);
		tvJuniThree = (TextView) this.findViewById(R.id.tv_junithree);
		tvHighOne = (TextView) this.findViewById(R.id.tv_highone);
		tvHighTwo = (TextView) this.findViewById(R.id.tv_hightwo);
		tvHighThree = (TextView) this.findViewById(R.id.tv_highthree);
		//小学
		tvLittleOne = (TextView) this.findViewById(R.id.tv_littleone);
		tvLittleTwo = (TextView) this.findViewById(R.id.tv_littletwo);
		tvLittleThree = (TextView) this.findViewById(R.id.tv_littlethree);
		tvLittlefour = (TextView) this.findViewById(R.id.tv_littlefour);
		tvLittlefive = (TextView) this.findViewById(R.id.tv_littlefive);
		tvLittlesix = (TextView) this.findViewById(R.id.tv_littlesix);

		ivLeft.setOnClickListener(this);
		tvJuniOne.setOnClickListener(this);
		tvJuniTwo.setOnClickListener(this);
		tvJuniThree.setOnClickListener(this);
		tvHighOne.setOnClickListener(this);
		tvHighTwo.setOnClickListener(this);
		tvHighThree.setOnClickListener(this);

		tvLittleOne.setOnClickListener(this);
		tvLittleTwo.setOnClickListener(this);
		tvLittleThree.setOnClickListener(this);
		tvLittlefour.setOnClickListener(this);
		tvLittlefive.setOnClickListener(this);
		tvLittlesix.setOnClickListener(this);



		defaultBg = getResources().getDrawable(
				R.drawable.my_coursesetting_normel);
		vkoConfigure = VkoConfigure.getConfigure(this);
		vkoContext = VkoContext.getInstance(this);
	}

	@Override
	public void initData() {
		tvTitle.setText("选择年级");
		int gradeId;
		userInfo = VkoContext.getInstance(this).getUser();
		if (userInfo != null) {
			String gradeID = userInfo.getGradeId();
			if (TextUtils.isEmpty(gradeID)) {
				return;
			}
			gradeId = Integer.parseInt(gradeID);
		} else {
			gradeId = VkoConfigure.getConfigure(this).getInt("gradeId", -1);
		}
		setDefaultBg(gradeId);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		intent = new Intent();
		switch (v.getId()) {
		case R.id.tv_junione:
			intent.putExtra("grade", tvJuniOne.getText().toString().trim());
			intent.putExtra("gradeId", "0");
			temp = 0;
			break;
		case R.id.tv_junitwo:
			intent.putExtra("grade", tvJuniTwo.getText().toString().trim());
			intent.putExtra("gradeId", "1");
			temp = 1;
			break;
		case R.id.tv_junithree:
			intent.putExtra("grade", tvJuniThree.getText().toString().trim());
			intent.putExtra("gradeId", "2");
			temp = 2;
			break;
		case R.id.tv_highone:
			intent.putExtra("grade", tvHighOne.getText().toString().trim());
			intent.putExtra("gradeId", "3");
			temp = 3;
			break;
		case R.id.tv_hightwo:
			intent.putExtra("grade", tvHighTwo.getText().toString().trim());
			intent.putExtra("gradeId", "4");
			temp = 4;
			break;
		case R.id.tv_highthree:
			intent.putExtra("grade", tvHighThree.getText().toString().trim());
			intent.putExtra("gradeId", "5");
			temp = 5;
			break;
		case R.id.tv_littleone:
			intent.putExtra("grade", tvLittleOne.getText().toString().trim());
			intent.putExtra("gradeId", "6");
			temp = 6;
			break;
		case R.id.tv_littletwo:
			intent.putExtra("grade", tvLittleTwo.getText().toString().trim());
			intent.putExtra("gradeId", "7");
			temp = 7;
			break;
		case R.id.tv_littlethree:
			intent.putExtra("grade", tvLittleThree.getText().toString().trim());
			intent.putExtra("gradeId", "8");
			temp = 8;
			break;
		case R.id.tv_littlefour:
			intent.putExtra("grade", tvLittlefour.getText().toString().trim());
			intent.putExtra("gradeId", "9");
			temp = 9;
			break;
		case R.id.tv_littlefive:
			intent.putExtra("grade", tvLittlefive.getText().toString().trim());
			intent.putExtra("gradeId", "10");
			temp = 10;
			break;
		case R.id.tv_littlesix:
			intent.putExtra("grade", tvLittlesix.getText().toString().trim());
			intent.putExtra("gradeId", "11");
			temp = 11;
			break;
		case R.id.iv_back:
			this.finish();
			break;
		default:
			temp = -1;
			break;

		}
		// setResult(5,intent);
		// finish();

		if (userInfo != null) {
			getGradeTask(intent);
		}
	}

	public void setDefaultBg(int gradeId) {
		TextView tempTv = null;
		switch (gradeId) {
		case 0:
			tempTv = tvJuniOne;
			break;
		case 1:
			tempTv = tvJuniTwo;
			break;
		case 2:
			tempTv = tvJuniThree;
			break;
		case 3:
			tempTv = tvHighOne;
			break;
		case 4:
			tempTv = tvHighTwo;
			break;
		case 5:
			tempTv = tvHighThree;
			break;
		case 6:
			tempTv = tvLittleOne;
			break;
		case 7:
			tempTv = tvLittleTwo;
			break;
		case 8:
			tempTv = tvLittleThree;
			break;
		case 9:
			tempTv = tvLittlefour;
			break;
		case 10:
			tempTv = tvLittlefive;
			break;
		case 11:
			tempTv = tvLittlesix;
			break;
		default:
			break;
		}
		if (tempTv != null) {
			switchDrawable(tempTv, defaultBg);
		}
	}

	public void switchDrawable(TextView tv, Drawable drawable) {
		if (drawable == null) {
			return;
		}
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		tv.setCompoundDrawables(null, null, drawable, null);// 画在右边
		// tvSubject.setBackgroundResource(R.drawable.subject_bagblue);
	}

	private void getGradeTask(Intent i) {
		String url = ConstantUrl.VKOIP + "/index/saveGrade";
		VolleyUtils<String> volleyUtils = new VolleyUtils<String>(this,String.class);
//		Builder builder = volleyUtils.getBuilder(url);
		Map<String,String> params = new HashMap<String,String>();
		if (TextUtils.isEmpty( i.getStringExtra("gradeId"))) {
			return;
		}
		params.put("gradeId", i.getStringExtra("gradeId"));
		params.put("token",VkoContext.getInstance(this).getToken());
		volleyUtils.setUiDataListener(this);
		volleyUtils.sendPostRequest(true,url,params);
//		volleyUtils.requestHttpPost(builder, this, String.class);
		userInfo.setGradeId(i.getStringExtra("gradeId"));
		userInfo.setGrade(i.getStringExtra("grade"));
	}



	private void notifySubjectUpdate() {
		EventBus.getDefault().post(Constants.SUBJECT_REFRESH);
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// EventCountAction.onActivityResumCount(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// EventCountAction.onActivityPauseCount(this);
	}

	@Override
	public void onDataChanged(String t) {
		if (t != null) {
			JSONObject jsonObject = JSONObject.parseObject(t);

			String code = jsonObject.getString("code");
			if ("0".equals(code)) {
				setResult(101, intent);
				notifySubjectUpdate();
				finish();
			}
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {
		notifySubjectUpdate();
	}
}
