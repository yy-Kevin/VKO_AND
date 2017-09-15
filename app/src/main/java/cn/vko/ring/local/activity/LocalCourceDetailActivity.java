package cn.vko.ring.local.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.DateUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.CardTagView;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.common.widget.xlv.XListView.IXListViewListener;
import cn.vko.ring.local.adapter.LocalVideoAdapter;
import cn.vko.ring.local.model.LocalCourseModel;
import cn.vko.ring.local.model.LocalVideoModel;
import cn.vko.ring.study.activity.CourseVideoViewActivity;
import cn.vko.ring.study.model.VideoAttributes;


public class LocalCourceDetailActivity extends BaseActivity implements IXListViewListener, OnItemClickListener, UIDataListener<LocalVideoModel> {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.id_stickynavlayout_innerscrollview)
	public XListView mListView;

	@Bind(R.id.tv_teacher_name)
	TextView tcName;
	@Bind(R.id.tv_tc_info)
	TextView tcInfo;
	@Bind(R.id.tag_view)
	CardTagView tagInfo;
	@Bind(R.id.tv_course_title)
	TextView courseTitle;
	@Bind(R.id.tv_course_desc)
	TextView courseDesc;
	@Bind(R.id.tv_course_num)
	TextView tvCourseNum;
	@Bind(R.id.tv_buy_count)
	TextView tvCourseBuyCount;
	@Bind(R.id.tv_price)
	TextView tvPrice;
	@Bind(R.id.iv_tchead)
	ImageView ivFace;

	private LocalCourseModel.DataEntity.GoodsListEntity item;
	private VolleyUtils<LocalVideoModel> mVolleyUtils;
	private int page = 1;
	private LocalVideoAdapter mAdapter;
	private LocalVideoModel.VideoListEntity info;
	private int color = Color.parseColor("#2196F3");
	private String teacherId,goodsId;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_local_detail;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		item = (LocalCourseModel.DataEntity.GoodsListEntity) getIntent().getExtras().get("GOODS");
		String title = getIntent().getExtras().getString("TITLE");
		teacherId = getIntent().getExtras().getString("TEACHERID");
		goodsId = getIntent().getExtras().getString("GOODSID");
		tvTitle.setText(title);
		mVolleyUtils = new VolleyUtils<LocalVideoModel>(this,LocalVideoModel.class);
		initGoodsView();

	}



	private void initGoodsView() {
		if (item != null) {
			tcName.setText(item.getTcName());
			tcInfo.setText(item.getTcSchool() + item.getTcSubject());
			teacherId = item.getTeacherId();
			goodsId = item.getGoodsId();
			if (!TextUtils.isEmpty(item.getTagdesc())) {
				if (!TextUtils.isEmpty(item.getColor())) {
					color = Color.parseColor(item.getColor());
				}
				tagInfo.setTextAndColor(item.getTagdesc(), color);
			}

			courseTitle.setText(item.getGoodsName());
			courseDesc.setText(item.getGoodsDesc());
			tvCourseNum.setText(item.getTotalCourse() + "节");
			tvCourseBuyCount.setText(item.getBuyCount() + "人购买");

			if (item.isBuy()) {
				tvPrice.setText("已购买");
			} else {
				tvPrice.setText("￥" + item.getSellPrice() + "");
			}
			ImageListener imgListener = ImageLoader.getImageListener(ivFace,
					R.drawable.head, R.drawable.head);
			mVolleyUtils.getImageLoader().get(item.getTcFace(), imgListener);
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(false);
		mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);
		
		mAdapter = new LocalVideoAdapter(this);
		mListView.setAdapter(mAdapter);
		getVideoListTask(page,false);
	}

	private void getVideoListTask(int p,boolean isLoading) {
		// TODO Auto-generated method stub
//		if(item == null) return;
		Uri.Builder bbBuilder = mVolleyUtils.getBuilder(ConstantUrl.VK_LOCAL_VIDEO);
		bbBuilder.appendQueryParameter("goodsId", goodsId);
		bbBuilder.appendQueryParameter("userId", teacherId);
		bbBuilder.appendQueryParameter("pageIndex", p + "");
		bbBuilder.appendQueryParameter("pageSize", "10");
		mVolleyUtils.sendGETRequest(isLoading,bbBuilder);
		mVolleyUtils.setUiDataListener(this);

	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		finish();
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		if(mAdapter.judgeHasEmpty()){
			getVideoListTask(1, true);
		}else{
			info = mAdapter.getListItem(position-1);
			if(info != null && item != null){
				VideoAttributes video = new VideoAttributes();
				video.setVideoId(info.getVideoId());
				video.setBookId(item.getObjId());
				video.setVideoName(info.getVideoName());
				video.setCourseType("46");
				video.setGoodsId(item.getGoodsId());
				video.setSellPrice(item.getSellPrice());
				Bundle bundle = new Bundle();
				bundle.putSerializable("video", video);
				bundle.putInt("TYPE", 4);
				StartActivityUtil.startActivity(this, CourseVideoViewActivity.class,bundle,Intent.FLAG_ACTIVITY_SINGLE_TOP);
			}
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		getVideoListTask(1, false);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		getVideoListTask(page+1, false);
	}
	public void stopUp() {
		mListView.stopLoadMore();
		mListView.stopRefresh();
		mListView.setRefreshTime(DateUtils.getCurDateStr());
	}
	@Subscribe
	public void onEventMainThread(String msg) {
		if (!TextUtils.isEmpty(msg) && msg.equals(Constants.PAYLOCALCOURSE)) {
			if(info != null){
				
			}
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onDataChanged(LocalVideoModel response) {
		if (response != null && response.getData() != null) {
			page =  response.getData().getPager().getPageNo();
			if (page == 1) {
				mAdapter.clear();
			}
			if(item == null){
				item = response.getData().getGoods();
				initGoodsView();
			}
			mAdapter.add(response.getData().getVideoList());

			int totalPage = response.getData().getPager().getPageTotal();
			if (page >= totalPage) {
				mListView.setPullLoadEnable(false);
			} else {
				mListView.setPullLoadEnable(true);
			}
		}
		mAdapter.judgeHasEmpty();
		mAdapter.notifyDataSetChanged();
		stopUp();
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {
		if(mAdapter.judgeHasEmpty()){
			mAdapter.notifyDataSetChanged();
		}
		stopUp();
	}
}
