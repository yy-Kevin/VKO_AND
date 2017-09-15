package cn.vko.ring.mine.activity;

import android.net.Uri.Builder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.adapter.MyErrorAdapter;
import cn.vko.ring.mine.model.NotedInfo;
import cn.vko.ring.mine.model.NotedInfo.Data;
import cn.vko.ring.mine.presenter.EVolleyUtils;

public class ErrNotepadDetailsActivity extends BaseActivity {

	// @Bind(R.id.err_notepad_details_webview)
	// public WebView eWebView;
	@Bind(R.id.err_notepad_viewpager)
	public ViewPager eViewPager;
	@Bind(R.id.tv_content)
	public TextView eTitle;
	@Bind(R.id.tv_num)
	public TextView eNum;
//	private VolleyUtils<ErrNotepadInfo> volleyUtils;
	private EVolleyUtils mEvolleyUtils;

	private NotedInfo.Data notedInfo;
	private String courseType;
	private List<WebView> webList = new ArrayList<WebView>();
	private static final String TAG = "ErrDetailsActivity";
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_err_notepad_details;
	}

	@Override
	public void initView() {

	}

	@Override
	public void initData() {
//		volleyUtils = new VolleyUtils<ErrNotepadInfo>(this,ErrNotepadInfo.class);
		mEvolleyUtils = new EVolleyUtils(this,null);
		String url = ConstantUrl.VKOIP + "/user/errDetail";
		UserInfo user = VkoContext.getInstance(this).getUser();
		if (user == null) {
			return;
		}
		notedInfo = (Data) getIntent().getSerializableExtra("subjectData");
		courseType = getIntent().getStringExtra("courseType");

		if (notedInfo.name != null && notedInfo.name.length() > 10) {
			eTitle.setText(notedInfo.name.substring(0, 8) + "...");
		} else {
			eTitle.setText(notedInfo.name);
		}
		eNum.setText(1 + "/" + notedInfo.countEb);
		if (notedInfo != null) {
			Builder builder = mEvolleyUtils.getBuilder(url);
			builder.appendQueryParameter("subjectId", notedInfo.subjectId);
			builder.appendQueryParameter("learnId", user.getLearn());
			builder.appendQueryParameter("courseType", courseType);
			builder.appendQueryParameter("token", user.getToken());
			if (courseType.equals("43")) {
				builder.appendQueryParameter("knowledgeId",
						notedInfo.knowledgeId);
			} else if (courseType.equals("41")) {
				builder.appendQueryParameter("courseId", notedInfo.courseId);
			}

			mEvolleyUtils.sendGETRequest(true,builder);
			mEvolleyUtils.setUiDataListener(new UIDataListener<String>() {
				@Override
				public void onDataChanged(String data) {
					try {
						JSONObject oo = new JSONObject(data);
						JSONArray array = oo.getJSONArray("data");

						if (array==null)return;
						for (int i = 0; i < array.length(); i++) {
						JSONObject jsonObject = array.getJSONObject(i);
							String contentData = jsonObject.optString("cnt");
							Log.e(TAG, "onDataChanged: "+contentData);
							WebView eWebView = new WebView(ErrNotepadDetailsActivity.this);
//							eWebView.setOverScrollMode(R.style.common_bar_style);
							eWebView.getSettings().setJavaScriptEnabled(true);
							eWebView.loadDataWithBaseURL(null,contentData, "text/html", "utf-8", null);
							webList.add(eWebView);
							MyErrorAdapter adapter = new MyErrorAdapter(webList);
							eViewPager.setAdapter(adapter);
							eViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

								@Override
								public void onPageSelected(
										int arg0) {
									// TODO Auto-generated
									eNum.setText(arg0 + 1 + "/"
											+ notedInfo.countEb);
								}

								@Override
								public void onPageScrolled(int arg0, float arg1, int arg2) {
								}

								@Override
								public void onPageScrollStateChanged(int arg0) {
								}
							});

					}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onErrorHappened(String errorCode, String errorMessage) {

				}
			});
//			volleyUtils.sendGETRequest(false, builder);
//			volleyUtils.setUiDataListener(new UIDataListener<ErrNotepadInfo>() {
//				@Override
//				public void onDataChanged(ErrNotepadInfo response) {
//					for (int i = 0; i < response.data.size(); i++) {
//						if (response.code == 0 && response.data != null
//								&& response.data.get(i) != null) {
//							WebView eWebView = new WebView(ErrNotepadDetailsActivity.this);
//							eWebView.setOverScrollMode(R.style.common_bar_style);
//							eWebView.getSettings().setJavaScriptEnabled(true);
//							eWebView.loadDataWithBaseURL(null, response.data.get(i).cnt.toString(), "text/html", "utf-8", null);
//							webList.add(eWebView);
//							MyErrorAdapter adapter = new MyErrorAdapter(webList);
//							eViewPager.setAdapter(adapter);
//							eViewPager.addOnPageChangeListener(new OnPageChangeListener() {
//
//								@Override
//								public void onPageSelected(
//										int arg0) {
//									// TODO Auto-generated
//									eNum.setText(arg0 + 1 + "/"
//											+ notedInfo.countEb);
//								}
//
//								@Override
//								public void onPageScrolled(int arg0, float arg1, int arg2) {
//								}
//
//								@Override
//								public void onPageScrollStateChanged(int arg0) {
//								}
//							});
//						}
//					}
//				}
//
//				@Override
//				public void onErrorHappened(String errorCode, String errorMessage) {
//
//				}
//			});


		}

	}

	@OnClick(R.id.iv_left)
	public void setBack() {
		finish();
	}

	@OnClick(R.id.delete_icon)
	public void setDelete() {
		Toast.makeText(this, "待开发。。。", Toast.LENGTH_SHORT).show();
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
}
