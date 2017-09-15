package cn.vko.ring.mine.activity;

import android.content.Intent;
import android.net.Uri.Builder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.DateUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.common.widget.xlv.XListView.IXListViewListener;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.adapter.MyBindSchoolAdapter;
import cn.vko.ring.mine.model.DistrictInfo;
import cn.vko.ring.mine.model.RevisePersonMsgInfo;
import cn.vko.ring.mine.model.SearchSchoolInfo;
import cn.vko.ring.mine.model.SearchSchoolInfo.Data;
import cn.vko.ring.mine.model.SearchSchoolInfo.Data.School;

public class SelectSchoolActivity extends BaseActivity implements OnItemClickListener,
		UIDataListener<RevisePersonMsgInfo>, IXListViewListener {

	private TextView tvTitle;
//	private ImageView ivLeft;
	private XListView lvSchool; // 对应的listview
	private DistrictInfo districtInfo;
	private Intent intent;
	private MyBindSchoolAdapter adapter;
	private UserInfo userInfo;
	private String schoolName;
	private School school;
	private VolleyUtils<SearchSchoolInfo> volleyUtils;
	@Bind(R.id.edit_bind_school)
	public EditText editBindSchool;
	private int page = 1;
	private String search;

	@Override
	public int setContentViewResId() {
		return R.layout.activity_select_school;
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

	@Override
	public void initView() {
		userInfo = VkoContext.getInstance(this).getUser();
		if (userInfo == null) {
			return;
		}
		intent = new Intent();
		adapter = new MyBindSchoolAdapter(SelectSchoolActivity.this);

		lvSchool = (XListView) this.findViewById(R.id.lv_city);
		lvSchool.setPullRefreshEnable(false);
		lvSchool.setPullLoadEnable(false);
		lvSchool.setXListViewListener(this);
		// lvSchool.setIsAddFootView(false);
		tvTitle = (TextView) this.findViewById(R.id.tv_title);
		lvSchool.setAdapter(adapter);
//		ivLeft.setOnClickListener(this);
		lvSchool.setOnItemClickListener(this);
		editBindSchool.addTextChangedListener(watcher);
		
		lvSchool.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				hideSoftInput(SelectSchoolActivity.this,editBindSchool , true);
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void initData() {
		tvTitle.setText("选择学校");
		volleyUtils = new VolleyUtils<SearchSchoolInfo>(this,SearchSchoolInfo.class);
	}

	@OnClick(R.id.search_btn)
	public void searchBtnOnClick() {
		schoolName = editBindSchool.getText().toString().trim();
		doSearch(schoolName, 1);
	}
	
	@OnClick(R.id.iv_back)
	public void sayBack() {
		finish();
	}

	private void doSearch(String search, int page) {
		if (!TextUtils.isEmpty(search) && search != null) {
			searchSchoolRequest(search, page);
		} else {
			Toast.makeText(this, "请输入学校名称", Toast.LENGTH_SHORT).show();
		}
	}

	public void searchSchoolRequest(String name, int pageIndex) {
		String url = ConstantUrl.VKOIP + "/s/school";
//		Builder builder = volleyUtils.getBuilder(url);
		Map<String,String> params = new HashMap<>();
		if (!name.isEmpty()) {
			params.put("key", name);
			params.put("pageIndex",
					Integer.toString(pageIndex));
			params.put("pageSize", "10");
			volleyUtils.sendPostRequest(false,url,params);
			volleyUtils.setUiDataListener(new UIDataListener<SearchSchoolInfo>() {
				@Override
				public void onDataChanged(SearchSchoolInfo response) {
					if (response.code == 0) {
						Data base = response.data;
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		school = adapter.getListItem(arg2 - 1);
		if (school == null) {
			return;
		}
		if (adapter != null && intent != null) {
			if (school.sName.length() > 15) {
				String str = school.sName.substring(0, 13);
				editBindSchool.setText(str + "...");
			} else {
				editBindSchool.setText(school.sName);
			}
			intent.putExtra("school", school.sName);
			intent.putExtra("schoolId", school.schoolId);
			intent.putExtra("proId", school.proId);
			intent.putExtra("cityId", school.cityId);
			intent.putExtra("areaId", school.areaId);
			getSelectSchoolTask(intent);
			// adapter.clear();
			// adapter.notifyDataSetChanged();
		}
	}

	private void getSelectSchoolTask(Intent i) {
		String url = ConstantUrl.VKOIP + "/user/mod";
		VolleyUtils<RevisePersonMsgInfo> volleyUtils = new VolleyUtils<RevisePersonMsgInfo>(this,RevisePersonMsgInfo.class);
		Map<String,String> params = new HashMap<>();
		Builder builder = volleyUtils.getBuilder(url);
		params.put("udid", userInfo.getUdid());
		params.put("token", userInfo.getToken());
		params.put("provinceId",
				intent.getStringExtra("proId"));
		params.put("cityId", intent.getStringExtra("cityId"));
		params.put("areaId", intent.getStringExtra("areaId"));
		params.put("schoolName",
				intent.getStringExtra("school"));
		params.put("schoolId",
				intent.getStringExtra("schoolId"));
		volleyUtils.setUiDataListener(this);
		volleyUtils.sendPostRequest(true,url,params);
//		volleyUtils.requestHttpPost(builder, this, RevisePersonMsgInfo.class);
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
		hideSoftInput(this,editBindSchool , true);
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

	@Override
	public void onDataChanged(RevisePersonMsgInfo t) {
		if (t != null) {
			if ("0".equals(t.getCode())) {
				userInfo.setSchool(intent.getStringExtra("school"));
				userInfo.setSchoolId(intent.getStringExtra("schoolId"));
				userInfo.setProvinceId(intent.getStringExtra("proId"));
				userInfo.setCityId(intent.getStringExtra("cityId"));
				userInfo.setAreaId(intent.getStringExtra("areaId"));
				VkoContext.getInstance(SelectSchoolActivity.this).setUser(
						userInfo);
				setResult(2, intent);
				finish();
			} else {
				Toast.makeText(this, t.getMsg(), Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}
