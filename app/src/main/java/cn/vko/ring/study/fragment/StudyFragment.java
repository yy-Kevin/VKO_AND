/**
 * Copyright © 2015 cn.vko.com. All rights reserved.
 *
 * @Title: StudyFragment.java
 * @Prject: SvkoCircle
 * @Package: cn.vko.ring.mine.fragment
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-10-29 下午2:23:27
 * @version: V1.0
 */
package cn.vko.ring.study.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.event.SwitchLearnEvent;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.umeng.EventCountAction;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.SimpleViewPagerIndicator;
import cn.vko.ring.home.model.SubjectInfo;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.im.config.preference.Preferences;
import cn.vko.ring.im.session.SessionHelper;
import cn.vko.ring.im.utils.DemoCache;
import cn.vko.ring.mine.model.BaseExeInfo;
import cn.vko.ring.mine.model.NotedInfo;
import cn.vko.ring.study.activity.CourseDetailActivity;
import cn.vko.ring.study.activity.SeacherCourseFromVoiceActivity;
import cn.vko.ring.study.city.CityActivity;
import cn.vko.ring.study.city.LocationSvc;
import cn.vko.ring.study.model.BaseCityInfo;
import cn.vko.ring.study.model.BaseSubjectInfoCourse;
import cn.vko.ring.study.model.KnowledgePointK1;
import cn.vko.ring.study.model.SubjectInfoCourse;
import cn.vko.ring.study.presenter.CusetemPresenter;
import cn.vko.ring.study.presenter.MySubjectPersoner;
import cn.vko.ring.study.presenter.RecommondKnowledgePresenter;
import cn.vko.ring.utils.WebViewUtil;


/**
 * @ClassName: StudyFragment
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-10-29 下午2:23:27
 */
public class StudyFragment extends BaseFragment implements
		MySubjectPersoner.ISelectSubjectlistener, RecommondKnowledgePresenter.IKnowlegeListener,UIDataListener<BaseSubjectInfoCourse> {

	@Bind(R.id.webview)
	public BridgeWebView mWebView;

	public static String SUBJECT = "SUBJECT";

	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_city)
	public TextView tvCity;
	public SimpleViewPagerIndicator mIndicator;

	private FragmentStatePagerAdapter mAdapter;
	private List<BaseFragment> fragments;

	private VolleyUtils<BaseCityInfo> volleyUtils;


	public UserInfo user;
	String subjectId="-1";
	String gradeId;



	@Override
	public int setContentViewId() {
		return R.layout.fragment_study;
	}
	/*
     * @Description: TODO
     */
	@Override
	public void initView(View root) {
		tvTitle.setText("学习");

		initWebView();

		createJson();
	}

	private void initWebView() {

		user = VkoContext.getInstance(atx).getUser();
		String cityName=user.getCityName();
		if (cityName!=null&&!cityName.isEmpty()){
			cityName=cityName.replace("市","");
			tvCity.setText(cityName);
			requestCity(cityName);
		}

		// 注册广播
//		Log.e("注册广播","=============");
		IntentFilter filter = new IntentFilter();
		filter.addAction("locationAction");
		filter.addAction("CITY");
		getActivity().registerReceiver(new LocationBroadcastReceiver(), filter);

		// 启动服务
		Intent intent = new Intent();
		intent.setClass(getActivity(), LocationSvc.class);
		getActivity().startService(intent);




//		Log.e("=====initWebView","走了");
		mWebView.setWebChromeClient(new WebChromeClient());
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDatabaseEnabled(true);
		mWebView.requestFocusFromTouch();


		new WebViewUtil(getActivity(), mWebView, "StudyFragment");

		//进入
		mWebView.registerHandler("toCourseDetail", new BridgeHandler() {
			@Override
			public void handler(String data, CallBackFunction function) {
//				Log.e("=======学习播放data值>", data);
				if (data != null) {
					try {
						JSONObject json = JSONObject.parseObject(data);
						String url=json.getString("url");

						Bundle b = new Bundle();
						b.putString("URL",url);
						StartActivityUtil.startActivity(getActivity(),CourseDetailActivity.class,b, Intent.FLAG_ACTIVITY_SINGLE_TOP);
					} catch (Exception e) {

					}
				}
			}
		});
	}

	private class LocationBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (!intent.getAction().equals("locationAction"))
			return;
//			Log.e("城市名字","=============");
			String city = intent.getStringExtra("CITY");
			if (city==null||city.isEmpty()){
				Log.e("city为空",city+"==");
				Log.e("用户城市名称",user.getCityName()+"==");
				if (user.getCityName()==null||user.getCityName().isEmpty()){
					city="北京";
				}else {
					city=user.getCityName();
					city=city.replace("市","");
				}
				tvCity.setText(city);
				requestCity(city);
			}else {
				Log.e("city部为空",city+"==");
				tvCity.setText(city);
				requestCity(city);
			}
			getActivity().unregisterReceiver(this);// 不需要时注销
		}
	}

	private void requestCity(String city) {
		String url = ConstantUrl.VKOIP + "/goods/schoolInfo";
		volleyUtils = new VolleyUtils<BaseCityInfo>(atx,BaseCityInfo.class);
		Uri.Builder builder = volleyUtils.getBuilder(url);
		builder.appendQueryParameter("groupName", city);
		volleyUtils.sendGETRequest(true,builder);
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("groupName",city);
//		volleyUtils.sendPostRequest(true,url,params);
		volleyUtils.setUiDataListener(new UIDataListener<BaseCityInfo>() {
			@Override
			public void onDataChanged(BaseCityInfo response) {
				if (response != null && response.getCode() == 0) {

					if (response.getData().getSourceId() != null) {
						String sourceId=response.getData().getSourceId();
						Log.e("==sourceId",sourceId+"==");
						user.setSourceCityId(sourceId);
					} else {
						Log.e("==无分站","======");
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		switch (resultCode) {
			case 1000:
				String city=data.getStringExtra("CITY");
				Log.e("city,,,,",city);
				tvCity.setText(city);
				if (city!=null){
					requestCity(city);
				}
		}
	}

	@OnClick(R.id.iv_search)
	public void onSeacher(){
		Intent intent = new Intent(getActivity(), SeacherCourseFromVoiceActivity.class);
		atx.startActivity(intent);
	}
	@OnClick(R.id.tv_city)
	public void goCity(){
		Intent intent = new Intent(getActivity(), CityActivity.class);
		String cityName = (String) tvCity.getText();
		intent.putExtra("CITY", cityName);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityForResult(intent, 100);
	}



	@Override
	public void initData() {
		mWebView.loadUrl("http://static.vko.cn/m/course/2.0/study.html"+"?"+"gradeId="+VkoContext.getInstance(getContext()).getGradeId());
		super.initData();
	}

	public void onItemClick(int position, SubjectInfo subjectInfo, View v) {
		Log.e("=======走onItemClick>","===走onItem");
		createJson();
	}


	// 生成json
	public String createJson() {
		try {

			JSONObject data = new JSONObject();
			data.put("grade", VkoContext.getInstance(getContext()).getGradeId());

//			Log.e("=======data值>",data.toString());
			onFilterData(data.toString());
			return data.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}


	private void  onFilterData(String data) {
		mWebView.callHandler("getCourseListByGrade", data,
				new CallBackFunction() {
					@Override
					public void onCallBack(String data) {
					}
				});
	}


	/*
     *
     * @Description: TODO
     */
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		Log.e("=======onStop>","====");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		Log.e("=======onCreateView>","====");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
//		Log.e("=======onCreateView>","====");
	}

	/*
             * @Description: TODO
             */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		Log.e("=======onResume>","====");
		mWebView.loadUrl("http://static.vko.cn/m/course/2.0/study.html"+"?"+"gradeId="+VkoContext.getInstance(getContext()).getGradeId());
		createJson();
		EventCountAction.onFragRCount(this.getClass());
		// wmv.resetAni();
	}

	/*
     * @Description: TODO
     */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		Log.e("=======onPause>","====");
		EventCountAction.onFragPCount(this.getClass());
		// wmv.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// wmv.stopAni();
		// }
		// }, 300);
	}

	@Subscribe
	public void onEventMainThread(String msg) {

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onClickKnowleg(KnowledgePointK1 k1) {

	}

	@Override
	public void selectSubject(SubjectInfoCourse info) {

	}

	@Override
	public void onDataChanged(BaseSubjectInfoCourse data) {

	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
	CusetemPresenter cuset;
	@OnClick(R.id.custom)
	public void onCusetemService() {
//		EaseLoginPresenter presenter = EaseLoginPresenter.getInstance();
//		presenter.createAccount(getActivity());
		if(VkoContext.getInstance(atx).isLogin()){
			if(VkoContext.getInstance(atx).getUser().getImToken() != null){
				if(DemoCache.getAccount() == null){
					if(getLoginInfo() != null){
						SessionHelper.startP2PSession(getActivity(),"13886257916838578");
					}else{
						cuset = new CusetemPresenter(getActivity());
					}

				}else{
					SessionHelper.startP2PSession(getActivity(),"13886257916838578");
				}

			}else{
				cuset = new CusetemPresenter(getActivity());
			}
		}else{
			Toast.makeText(atx,"登录后才能使用",Toast.LENGTH_SHORT).show();
		}
	}
	public LoginInfo getLoginInfo() {
		String account = Preferences.getUserAccount();
		String token = Preferences.getUserToken();

		if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
			DemoCache.setAccount(account.toLowerCase());
			return new LoginInfo(account, token);
		} else {
			return null;
		}
	}




}
