/*
 *RegisteActivityNext.java [V 1.0.0]
 *classes : cn.vko.ring.mine.views.RegisteActivityNext
 *宣义阳 Create at 2015-7-24 下午3:49:39
 */
package cn.vko.ring.home.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.DateUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.home.adapter.MyBindSchoolAdapter;
import cn.vko.ring.home.model.RevisePersonMsgInfo;
import cn.vko.ring.home.model.SearchSchoolInfo;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.home.presenter.LocationHelper;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;


/**
 * cn.vko.ring.mine.views.RegisteActivityNext
 * 
 * @author 宣义阳 create at 2015-7-24 下午3:49:39
 */
public class RegisteTwoActivity extends BaseActivity implements
		OnCheckedChangeListener, OnItemClickListener, XListView.IXListViewListener {

	@Bind(R.id.tv_city)
	public TextView tvLocation;
	@Bind(R.id.tv_title)
	public TextView tvContent;
	@Bind(R.id.tv_right)
	public TextView tvSave;
	@Bind(R.id.tv_school)
	public TextView tvSchool;
	@Bind(R.id.edit_bind_school)
	public EditText editBindSchool;
	@Bind(R.id.iv_back)
	public ImageView ivLeft;

	@Bind(R.id.refresh_lv)
	public XListView lvSchool;

	private RadioGroup rgButton;
	private String grade; // 年级
	private MyLocationListener myLocationListener; // 位置监听
	private boolean isFirst = true;
	private String city;
	private boolean mIsShow;
	private MyBindSchoolAdapter adapter;
	private String search;
	private UserInfo user;
	private Intent intent = new Intent();
	private SearchSchoolInfo.Data.School school;
	private String schoolName;
	private boolean isChecked = false;
	private int page = 1;
	private VolleyUtils<SearchSchoolInfo> volleyUtils;

	private LocationHelper locationHelper;
	@Override
	public int setContentViewResId() {
		return R.layout.activity_register_two;
	}

	@Override
	public void initView() {
		locationHelper = new LocationHelper(this);
		myLocationListener = new MyLocationListener();
		locationHelper.tenXunService(myLocationListener);
		
		rgButton = (RadioGroup) this.findViewById(R.id.rg_button);
		ivLeft.setVisibility(View.INVISIBLE);
		user = VkoContext.getInstance(this).getUser();
		if (user == null) {
			return;
		}
		grade = user.getGradeId();
		rgButton.setOnCheckedChangeListener(this);
		editBindSchool.addTextChangedListener(watcher);
	}

	@Override
	public void initData() {
		adapter = new MyBindSchoolAdapter(RegisteTwoActivity.this);
		tvContent.setText(R.string.select_class);
		tvSave.setVisibility(View.VISIBLE);
		tvSave.setText(R.string.save);
		lvSchool.setPullRefreshEnable(false);
		lvSchool.setPullLoadEnable(false);
		lvSchool.setXListViewListener(this);
		adapter.judgeHasEmpty();
		lvSchool.setAdapter(adapter);
		lvSchool.setOnItemClickListener(this);
		volleyUtils = new VolleyUtils<SearchSchoolInfo>(this,SearchSchoolInfo.class);
	}

	@OnClick(R.id.tv_right)
	public void saveOnClick() {
		modifyUserInfoTask(intent);

	}

	TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterTextChanged(Editable s) {
			search = s.toString();
			if (!TextUtils.isEmpty(search.trim())) {
				doSearch(search, 1);
			} else {
				lvSchool.setPullLoadEnable(false);
				adapter.clear();
				adapter.notifyDataSetChanged();
			}
		}
	};

	/**
	 * 方法说明：修改用户信息
	 * 
	 */
	private void modifyUserInfoTask(Intent intent) {
		// TODO Auto-generated method stub
		String str = tvSchool.getText().toString().trim();
		if (isChecked) {
			if (!str.isEmpty()) {
				// getGradeTask(intent);
				getSelectSchoolTask(intent);
			} else {
				Toast.makeText(this, "请输入学校名称", Toast.LENGTH_SHORT).show();
				return;
			}
		} else {
			Toast.makeText(this, "请选择年级", Toast.LENGTH_SHORT).show();
			return;
		}
	}

	@OnClick(R.id.search_btn)
	public void searchBtnOnClick() {
		schoolName = editBindSchool.getText().toString().trim();
		doSearch(schoolName, 1);
	}

	private void doSearch(String search, int page) {
		if (!TextUtils.isEmpty(search) && search != null) {
			searchSchoolRequest(search, page);
		} else {
			Toast.makeText(this, "请输入学校名称", Toast.LENGTH_SHORT).show();
		}
	}

	@OnClick(R.id.iv_back)
	public void ivImgClick() {
		if (user != null && user.getSchoolId() != null
				&& Integer.parseInt(user.getGradeId()) >= 0) {
			finish();
		} else {
			// 提示完善信息
			Toast.makeText(this, "请完善信息并保存哦！", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected boolean isEnableSwipe() {
		// TODO Auto-generated method stub
		return false;
	}





	@Override
	protected void onDestroy() {
		super.onDestroy();
//		// 移除位置监听器
//		TencentLocationManager locationManager = TencentLocationManager
//				.getInstance(this);
//		locationManager.removeUpdates(myLocationListener);
		locationHelper.removeListener(myLocationListener);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// Intent intent = new Intent();
		isChecked = true;
		switch (checkedId) {
		case R.id.tv_juniorone:
			intent.putExtra("grade", "初一");
			intent.putExtra("gradeId", "0");
			intent.putExtra("learn", "52");
			break;
		case R.id.tv_juniortwo:
			intent.putExtra("grade", "初二");
			intent.putExtra("gradeId", "1");
			intent.putExtra("learn", "52");
			break;
		case R.id.tv_juniorthree:
			intent.putExtra("grade", "初三");
			intent.putExtra("gradeId", "2");
			intent.putExtra("learn", "52");
			break;
		case R.id.tv_highone:
			intent.putExtra("grade", "高一");
			intent.putExtra("gradeId", "3");
			intent.putExtra("learn", "51");
			break;
		case R.id.tv_hightwo:
			intent.putExtra("grade", "高二");
			intent.putExtra("gradeId", "4");
			intent.putExtra("learn", "51");
			break;
		case R.id.tv_highthree:
			intent.putExtra("grade", "高三");
			intent.putExtra("gradeId", "5");
			intent.putExtra("learn", "51");
			break;
		}
	}
	private class MyLocationListener implements TencentLocationListener {
		@Override
		public void onLocationChanged(TencentLocation location, int error,
									  String reason) {
			if (TencentLocation.ERROR_OK == error) {
				// 定位成功
				city = location.getCity();
				String province = location.getProvince();
				String district = location.getDistrict();
				tvLocation.setText(city);
				if (isFirst) {
					editBindSchool.setText(city);
					isFirst = false;
			locationHelper.removeListener(myLocationListener);
				}
			} else {
				// 定位失败
				Toast.makeText(RegisteTwoActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onStatusUpdate(String name, int status, String desc) {

		}
	}
	public void searchSchoolRequest(String name, int pageIndex) {
		String url = ConstantUrl.VKOIP + "/s/school";
		if (!name.isEmpty()) {
			Map<String,String> params = new HashMap<String,String>();
			params.put("key", name);
			params.put("pageIndex", Integer.toString(pageIndex));
			params.put("pageSize", "10");
			volleyUtils.sendPostRequest(false,url,params);
			volleyUtils.setUiDataListener(new UIDataListener<SearchSchoolInfo>() {
				@Override
				public void onDataChanged(SearchSchoolInfo response) {
					if (response.code == 0) {
						SearchSchoolInfo.Data base = response.data;
						if (base != null && base.school != null
								&& base.school.size() > 0) {
							page = base.pager.getPageNo();
							if (page == 1) {
								adapter.clear();
							}
							adapter.add(base.school);
							int totalPage = base.pager.getPageTotal();
							if (page >= totalPage) {
								lvSchool.setPullLoadEnable(false);
							} else {
								lvSchool.setPullLoadEnable(true);
							}
						}
					}
					adapter.judgeHasEmpty();
					adapter.notifyDataSetChanged();
					stopUp();
				}
				@Override
				public void onErrorHappened(String errorCode, String errorMessage) {

				}
			});
		} else {
			adapter.clear();
			adapter.notifyDataSetChanged();
		}
	}

	public void stopUp() {
		lvSchool.stopLoadMore();
		lvSchool.stopRefresh();
		lvSchool.setRefreshTime(DateUtils.getCurDateStr());
	}

	// @OnClick(R.id.tv_regist)
	// public void registNextClick() {
	// if (grade != -1) {
	// user.setGrade(Integer.toString(grade));
	//
	// }
	// if (!city.equals("")) {
	// user.setCityName(city);
	// }
	// Intent intent = new Intent(RegisteActivityNext.this, HomeActivity.class);
	// startActivity(intent);
	// finish();
	// }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg1 != null) {
			if (!adapter.isEmpty()) {
				school = adapter.getListItem(arg2 - 1);
				if (school != null && school.sName != null) {
					tvSchool.setText(school.sName);
					adapter.clear();
					adapter.notifyDataSetChanged();
					if (intent != null) {
						intent.putExtra("school", school.sName);
						intent.putExtra("schoolId", school.schoolId);
						intent.putExtra("proId", school.proId);
						intent.putExtra("cityId", school.cityId);
						intent.putExtra("areaId", school.areaId);
					}
				}
			}
			// VkoContext.getInstance(RegisteActivityNext.this)
		}
	}

	private void getSelectSchoolTask(final Intent intent) {
		String url = ConstantUrl.VKOIP + "/user/mod";
		VolleyUtils<RevisePersonMsgInfo> volleyUtils = new VolleyUtils<RevisePersonMsgInfo>(this,RevisePersonMsgInfo.class
		);
		Map<String,String> params = new HashMap<String,String>();
		params.put("udid", user.getUdid());
		params.put("token", user.getToken());
		params.put("grade", intent.getStringExtra("gradeId"));
		params.put("provinceId",
				intent.getStringExtra("proId"));
		params.put("cityId", intent.getStringExtra("cityId"));
		params.put("areaId", intent.getStringExtra("areaId"));
		params.put("schoolName",
				intent.getStringExtra("school"));
		params.put("schoolId",
				intent.getStringExtra("schoolId"));
		volleyUtils.sendPostRequest(true,url,params);
		volleyUtils.setUiDataListener(new UIDataListener<RevisePersonMsgInfo>() {
			@Override
			public void onDataChanged(RevisePersonMsgInfo response) {
				if (response != null) {
					Log.i("==========", "response不为空");
					if ("0".equals(response.getCode())
							&& response.isData()) {
						user.setSchool(intent.getStringExtra("school"));
						user.setSchoolId(intent
								.getStringExtra("schoolId"));
						user.setProvinceId(intent
								.getStringExtra("proId"));
						user.setCityId(intent.getStringExtra("cityId"));
						user.setAreaId(intent.getStringExtra("areaId"));
						user.setGradeId(intent
								.getStringExtra("gradeId"));
						user.setGrade(intent.getStringExtra("grade"));
						user.setLearn(intent.getStringExtra("learn"));
						VkoContext.getInstance(RegisteTwoActivity.this).setUser(user);
						EventBus.getDefault().post(Constants.LOGIN_REFRESH);
						EventBus.getDefault().post(Constants.SUBJECT_REFRESH);
						StartActivityUtil.startActivity(RegisteTwoActivity.this, MainActivity.class);
						finish();
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		doSearch(search, 1);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		doSearch(search, page + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int,
	 * android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 如果按返回键时，没有完善信息或者保存的话，不能返回
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (user != null && !TextUtils.isEmpty(user.getSchoolId())
					&& Integer.parseInt(user.getGradeId()) >= 0) {
				StartActivityUtil.startActivity(this, MainActivity.class);
				finish();
			} else {
				// 提示完善信息
				Toast.makeText(this, "请完善信息并保存哦！", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
