package cn.vko.ring.study.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.shikh.utils.NetUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.okhttp.OkHttpUtils;
import cn.shikh.utils.okhttp.callback.StringCallback;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.activity.PhotoGalleryActivity;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.NumViewPagerIndicator;
import cn.vko.ring.mine.model.BaseResultInfo;
import cn.vko.ring.study.adapter.ExamPageAdaper;
import cn.vko.ring.study.model.BaseQuestDetailInfo;
import cn.vko.ring.study.model.BaseQuestionInfo;
import cn.vko.ring.study.model.PhotoAttr;
import cn.vko.ring.study.model.QuestionInfo;
import cn.vko.ring.utils.ACache;
import cn.vko.ring.utils.ImageUtils;
import okhttp3.Call;

public class SearchQuestionActivity extends BaseActivity {

	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.id_stickynavlayout_indicator)
	public NumViewPagerIndicator mIndicator;
	@Bind(R.id.id_stickynavlayout_viewpager)
	public ViewPager mViewPage;
	@Bind(R.id.iv_search)
	public ImageView ivSearch;
	@Bind(R.id.layout_error)
	public RelativeLayout errorLayout;
	@Bind(R.id.layout_footer)
	public LinearLayout footLayout;
	@Bind(R.id.wv_detail)
	public WebView wvDetail;
	@Bind(R.id.tv_remake)
	public TextView tvRemake;
	@Bind(R.id.tv_hint)
	public TextView tvHint;
	@Bind(R.id.tv_camera)
	public TextView tvSkill;
	
	@Bind(R.id.tv_adopt)
	List<View> adopts;
	@Bind(R.id.tv_remake)
	List<View> remakes;

	private File file;
	private String imgUrl, id;
	private List<WebView> webList = new ArrayList<WebView>();
	private List<QuestionInfo> dataList;
	private ExamPageAdaper mAdaper;
	private String from;
	private VolleyUtils<BaseResultInfo> http;
	private int item;
	private ACache aCache;
	private boolean isNetError;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_searchproblem;
	}

	@Override
	public void initView() {
		aCache=ACache.get(this);
		PhotoAttr pAttr=new PhotoAttr();
		from = getIntent().getExtras().getString("FROM");		
		pAttr.setFrom(from);
		
		if (from.equals("ITEM")) {
			tvTitle.setText("搜题详情");
			id = getIntent().getExtras().getString("ID");
			item = getIntent().getExtras().getInt("ITEM");
			footLayout.setVisibility(View.GONE);
			pAttr.setId(id);
			pAttr.setItem(item);
			
		} else if (from.equals("SEARCH")) {
			tvTitle.setText("搜题结果");
		}
		imgUrl = getIntent().getExtras().getString("URL");
		pAttr.setUrl(imgUrl);	
		aCache.put("PhotoAttr", pAttr);
		ImageUtils.loadPerviewImage(imgUrl, 200, 200, ivSearch);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		http = new VolleyUtils<BaseResultInfo>(this,BaseResultInfo.class);
		if (from.equals("SEARCH")) {
			file = new File(imgUrl);
			if (file.exists()) {
				getSearchData();
			}
		} else if (from.equals("ITEM")) {
			getItemDetail();
		}
	}

	private void getItemDetail() {
		// TODO Auto-generated method stub
		VolleyUtils<BaseQuestDetailInfo> httputil = new VolleyUtils<BaseQuestDetailInfo>(this, BaseQuestDetailInfo.class);
		Uri.Builder builder = http.getBuilder(ConstantUrl.VK_SEARCH_DETAIL);
		builder.appendQueryParameter("id", id);
		httputil.sendGETRequest(true, builder);
		httputil.setUiDataListener(new UIDataListener<BaseQuestDetailInfo>() {
			@Override
			public void onDataChanged(BaseQuestDetailInfo response) {
				if (response != null && response.getCode() == 0) {
					if (response.getData() != null) {
						errorLayout.setVisibility(View.GONE);
						if (response.getData().getFlag() == 0) {// 已采纳
							wvDetail.setVisibility(View.VISIBLE);
							mIndicator.setVisibility(View.GONE);
							mViewPage.setVisibility(View.GONE);
							initWebView(response.getData().getExam());
						} else {
							mIndicator.setVisibility(View.VISIBLE);
							mViewPage.setVisibility(View.VISIBLE);
							footLayout.setVisibility(View.VISIBLE);
							tvRemake.setVisibility(View.GONE);
							id = response.getData().getId();
							dataList = response.getData().getExamList();
							initViewPage(dataList);
						}
						return;
					}
				}
				errorLayout.setVisibility(View.VISIBLE);
				mIndicator.setVisibility(View.GONE);
				mViewPage.setVisibility(View.GONE);
				netOrServiceError();
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {
				errorLayout.setVisibility(View.VISIBLE);
				mIndicator.setVisibility(View.GONE);
				mViewPage.setVisibility(View.GONE);
				netOrServiceError();
			}
		});
	}

	protected void netOrServiceError() {
		// TODO Auto-generated method stub
		if (!NetUtils.isCheckNetAvailable(this)) {
			isNetError = true;
			tvHint.setText("网络异常");
			tvSkill.setText("连上网络后点击重试");
		}else{
			isNetError = false; 
			tvHint.setText("哎哟！这道题没搜到");
			tvSkill.setText("查看拍题技巧 >");
		}
	}

	protected void initWebView(QuestionInfo exam) {
		// TODO Auto-generated method stub
		if (exam != null) {
			wvDetail.setBackgroundColor(getResources().getColor(
					R.color.bg_main_view));
			wvDetail.getSettings().setDefaultTextEncodingName("UTF-8");
			wvDetail.getSettings().setBlockNetworkImage(false);
			wvDetail.getSettings().setJavaScriptEnabled(true);
//			wvDetail.setScrollBarStyle(R.style.myWebView);
			wvDetail.loadDataWithBaseURL(null,
					exam.getContent() + exam.getResolve(), "text/html",
					"UTF-8", null);
		}
	}

	private void getSearchData() {		
		http.showProgress();
		Map<String, String> params = new HashMap<String, String>();
		
		if(VkoContext.getInstance(this).getUser()!=null){
			params.put("token ", VkoContext.getInstance(this).getToken());
		}
		OkHttpUtils.post().addFile("file",file.getName(),file).url(ConstantUrl.VK_SEARCH_PROBLEM).params(params).build().execute(new StringCallback(){

			@Override
			public void onError(Call call, Exception e) {
				http.stopProgress();
			}

			@Override
			public void onResponse(String t) {
				http.stopProgress();
				if (SearchQuestionActivity.this.isFinishing())
					return;
				if (!TextUtils.isEmpty(t)) {
					BaseQuestionInfo bq = JSONObject.parseObject(t,
							BaseQuestionInfo.class);
					if (bq != null && bq.getData() != null
							&& bq.getData().getExamList() != null) {
						id = bq.getData().getId();
						dataList = bq.getData().getExamList();
						initViewPage(dataList);
						errorLayout.setVisibility(View.GONE);
						mIndicator.setVisibility(View.VISIBLE);
						mViewPage.setVisibility(View.VISIBLE);
						return;
					}
				}
				errorLayout.setVisibility(View.VISIBLE);
				mIndicator.setVisibility(View.GONE);
				mViewPage.setVisibility(View.GONE);
				netOrServiceError();
			}
		});
	}

	protected void initViewPage(List<QuestionInfo> data) {
		// TODO Auto-generated method stub
		if (data.size() == 0)
			return;
		mIndicator.setEntry(data.size(), mViewPage);
		for (QuestionInfo quest : data) {
			WebView wv = new WebView(this);
			wv.setBackgroundColor(getResources().getColor(R.color.bg_main_view));
			wv.getSettings().setDefaultTextEncodingName("UTF-8");
			wv.getSettings().setBlockNetworkImage(false);
			wv.getSettings().setJavaScriptEnabled(true);
//			wv.setScrollBarStyle(R.style.myWebView);
			wv.loadDataWithBaseURL(null,
					quest.getContent() + quest.getResolve(), "text/html",
					"UTF-8", null);
			webList.add(wv);
		}
		mAdaper = new ExamPageAdaper(webList, this);
		mViewPage.setAdapter(mAdaper);
	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		finish();
	}

	@OnClick(R.id.layout_error)
	void sayReload() {
		if ("ITEM".equals(from)) {
			getItemDetail();
		} else {
			// getSearchData();
		}
	}

	@OnClick(R.id.iv_search)
	void sayImgPreview() {		
		PhotoGalleryActivity.startUrl(this, imgUrl);
		/*Bundle bundle = new Bundle();
		bundle.putSerializable("IMAGEINFO", new ImageInfo(imgUrl));
		ViewUtils.startActivity(this, PictureBrowseActivity.class, bundle,
				Intent.FLAG_ACTIVITY_NEW_TASK);*/
	}

	@OnClick(R.id.tv_camera)
	void saySkill() {
		if ("ITEM".equals(from)) {
			getItemDetail();
		} else {
			if(isNetError){
				getSearchData();
			}else{
				StartActivityUtil.startActivity(this, SkillActivity.class);
			}
		}
	}

	@OnClick(R.id.tv_remake)
	void sayRemake() {
		aCache.remove("PhotoAttr");
		ButterKnife.apply(remakes, ALPHA_FADE);
		Bundle bundle = new Bundle();
		bundle.putString("FROM", "SEARCH");
		StartActivityUtil.startActivity(this, CameraActivity.class, bundle,
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
		File file = new File(imgUrl);
		if (file.exists()) {
			file.delete();
		}
		finish();
	}

	@OnClick(R.id.tv_adopt)
	void sayAdopt() {
		ButterKnife.apply(adopts, ALPHA_FADE);
		if (dataList != null) {
			adoptProblem();
		} else {
			Toast.makeText(this, "无法采纳", Toast.LENGTH_LONG).show();
		}
	}

	private void adoptProblem() {
		// TODO Auto-generated method stub
		VolleyUtils<BaseResultInfo> http = new VolleyUtils<BaseResultInfo>(this,BaseResultInfo.class);
		Uri.Builder builder = http.getBuilder(ConstantUrl.VK_ADOPT_QUESTION);
		builder.appendQueryParameter("id", id);
		builder.appendQueryParameter("examId",
				dataList.get(mViewPage.getCurrentItem()).getId());
		http.sendGETRequest(true,builder);
		http.setUiDataListener(new UIDataListener<BaseResultInfo>() {
			@Override
			public void onDataChanged(BaseResultInfo response) {
				if (response != null && response.getCode() == 0) {
					if (from.equals("ITEM")) {
						Intent intent = new Intent();
						intent.putExtra("ITEM", item);
						setResult(100,intent);
					}
					finish();
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}
}
