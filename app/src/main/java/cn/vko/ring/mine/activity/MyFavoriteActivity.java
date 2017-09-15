package cn.vko.ring.mine.activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.NoScrollViewPager;
import cn.vko.ring.common.widget.SimpleViewPagerIndicator;
import cn.vko.ring.common.widget.SimpleViewPagerIndicator.IScrollPageChangeListener;
import cn.vko.ring.home.adapter.TabFragmentPagerAdapter;
import cn.vko.ring.mine.fragment.FavoriteTopicFragment;
import cn.vko.ring.mine.fragment.FavoriteVideoFragment;
import cn.vko.ring.mine.model.CancleCollectEvent;
import cn.vko.ring.mine.model.FavoriteDeleteResult;

/**
 * 我的收藏
 * @author shikh
 * @time 2016/5/12 9:51
 */
public class MyFavoriteActivity extends BaseActivity implements
		UIDataListener<FavoriteDeleteResult> {
	private String[] titles = new String[] { "视频", "话题" };
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_right)
	public TextView tvRight;
	@Bind(R.id.tv_delete)
	public TextView tvDelete;
	@Bind(R.id.id_stickynavlayout_viewpager)
	public NoScrollViewPager mViewPager;
	@Bind(R.id.layout_indicator)
	public SimpleViewPagerIndicator mIndicator;

	private TabFragmentPagerAdapter mAdapter;
	private List<Fragment> fragments;
	private FavoriteVideoFragment videoFragment;
	private FavoriteTopicFragment topicFragment;
	private boolean flag;// 是否为编辑状态
	private int VorT;// 标记当前显示的是哪个Fragment
	private String ids="";
	private VolleyUtils<FavoriteDeleteResult> volleyUtils;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		return R.layout.activity_my_favorite;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mIndicator.setTitles(titles, mViewPager);
		tvTitle.setText("我的收藏");
		tvRight.setVisibility(View.VISIBLE);
		tvRight.setText("编辑");
		volleyUtils = new VolleyUtils<FavoriteDeleteResult>(this,FavoriteDeleteResult.class);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		fragments = new ArrayList<Fragment>();
		videoFragment = new FavoriteVideoFragment();
		topicFragment = new FavoriteTopicFragment();

		fragments.add(videoFragment);
		fragments.add(topicFragment);

		mViewPager.setClipChildren(false);
		mViewPager.setOffscreenPageLimit(2);
		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(),
				fragments);
		mViewPager.setAdapter(mAdapter);
		mIndicator.setiScrollpageChangeListener(new IScrollPageChangeListener() {

					@Override
					public void toChangePage(int position) {
						// TODO Auto-generated method stub
						VorT = position;
						editFinish();
						setCheckBox();
					}
				});
	}

	@OnClick(R.id.iv_back)
	public void sayBack() {
		finish();
	}

	// 编辑
	@OnClick(R.id.tv_right)
	public void tvEdit() {
		if (!flag) {
			flag = true;
			tvRight.setText("完成");
			tvDelete.setVisibility(View.VISIBLE);
			
		} else {
			editFinish();
		}
		setCheckBox();
	}

	// 当点击完成或者切换viewpager的时候调用
	private void editFinish() {
		flag = false;
		tvRight.setText("编辑");
		tvDelete.setVisibility(View.INVISIBLE);
		tvDelete.setTextColor(getResources().getColor(R.color.delete_gray));
		switch (VorT) {
		case 0:
			clearVideo();
			if (videoFragment.videoAdapter != null) {
				videoFragment.videoAdapter.selectedList.clear();
			}
			break;
		case 1:
			clearTopic();
			if (topicFragment.topicAdapter != null) {
				topicFragment.topicAdapter.selectedList.clear();
			}
			break;
		default:
			break;
		}
	}

	// 控制CheckBox的显示和隐藏
	private void setCheckBox() {
		switch (VorT) {
		case 0:
			if (videoFragment.videoAdapter != null) {
				videoFragment.videoAdapter.setVisable(flag);
				
				videoFragment.videoAdapter.notifyDataSetChanged();
				if (flag) {
					videoFragment.stopLoad();
				}else{
					videoFragment.resetLoad();
				}
			}
			break;
		case 1:
			if (topicFragment.topicAdapter != null) {
				topicFragment.topicAdapter.setVisable(flag);
				topicFragment.topicAdapter.notifyDataSetChanged();
				if (flag) {
					topicFragment.stopLoad();
				}else{
					topicFragment.resetLoad();
				}
			}
			break;
		default:
			break;
		}
	}

	// 删除
	@OnClick(R.id.tv_delete)
	public void tvDelete() {
		switch (VorT) {
		case 0:
			// for (int i = 0; i <
			// videoFragment.videoAdapter.selectedList.size(); i++) {
			// videoFragment.videoList
			// .remove(videoFragment.videoAdapter.selectedList.get(i));
			// }
			clearVideo();
			for (int i = 0; i < videoFragment.videoAdapter.selectedList.size(); i++) {
				ids += videoFragment.videoAdapter.selectedList.get(i)
						.getId() + ",";
			}
			if (ids.length() > 0) {
				ids = ids.substring(0, ids.length() - 1);
			}
			// videoFragment.videoAdapter.selectedList.clear();
			// videoFragment.videoAdapter.notifyDataSetChanged();
			break;
		case 1:
			// for (int i = 0; i <
			// topicFragment.topicAdapter.selectedList.size(); i++) {
			// topicFragment.topicList
			// .remove(topicFragment.topicAdapter.selectedList.get(i));
			// }
			clearTopic();
			for (int i = 0; i < topicFragment.topicAdapter.selectedList.size(); i++) {
				ids += topicFragment.topicAdapter.selectedList.get(i).getId()
						+ ",";
			}
			if (ids.length() > 0) {
				ids = ids.substring(0, ids.length() - 1);
			}
			// topicFragment.topicAdapter.selectedList.clear();
			// topicFragment.topicAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
		if (ids.length()>0) {
			tvDelete.setTextColor(getResources().getColor(R.color.delete_gray));
			deleteTask();
		}else{
			Toast.makeText(this,"没有删除的内容",Toast.LENGTH_LONG).show( );
		}
	}

	private void deleteTask() {
		String url = null;
		switch (VorT) {
		case 0:
			url = ConstantUrl.VKOIP.concat("/store/batchDeleteVideos");
			break;
		case 1:
			url = ConstantUrl.VKOIP.concat("/store/batchDeleteArticles");
			break;
		default:
			break;
		}
		Uri.Builder builder = volleyUtils.getBuilder(url);
		builder.appendQueryParameter("token", VkoContext.getInstance(this)
				.getToken());
		builder.appendQueryParameter("ids", ids);
		volleyUtils.setUiDataListener(this);
		volleyUtils.sendGETRequest(true,builder);
	}

	// 清除选中状态
	private void clearVideo() {
		for (int i = 0; i < videoFragment.videoList.size(); i++) {
			videoFragment.videoList.get(i).setChecked(false);
		}
	}

	private void clearTopic() {
		for (int i = 0; i < topicFragment.topicList.size(); i++) {
			topicFragment.topicList.get(i).setChecked(false);
		}
	}
	@Subscribe
	public void onEventMainThread(CancleCollectEvent event) {

		if (event!=null&& event.getType()==1) {

			if (videoFragment.videoAdapter.selectedList.size() > 0
					|| topicFragment.topicAdapter.selectedList.size() > 0) {
				tvDelete.setTextColor(getResources().getColor(
						R.color.delete_red));
			} else {
				tvDelete.setTextColor(getResources().getColor(
						R.color.delete_gray));
			}
		}
		if (event!=null&& event.getType()==0) {
			switch (mViewPager.getCurrentItem()) {
				case 0:
					videoFragment.refreshView(event);
					break;
				case 1:
					topicFragment.refreshView(event);
					break;
				default:
					break;
			}
	
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}


	@Override
	public void onDataChanged(FavoriteDeleteResult response) {
		if (response != null && response.isData()) {
			Toast.makeText(this, "删除成功", Toast.LENGTH_LONG).show();
		}
		switch (VorT) {
			case 0:
				for (int i = 0; i < videoFragment.videoAdapter.selectedList.size(); i++) {
					videoFragment.videoList
							.remove(videoFragment.videoAdapter.selectedList.get(i));
				}
				videoFragment.videoAdapter.notifyDataSetChanged();
				videoFragment.videoAdapter.selectedList.clear();
				if (videoFragment.videoList.size()==0) {
					videoFragment.showErrorView();
					tvRight.performClick();
				}
				break;
			case 1:
				for (int i = 0; i < topicFragment.topicAdapter.selectedList.size(); i++) {
					topicFragment.topicList
							.remove(topicFragment.topicAdapter.selectedList.get(i));
				}
				topicFragment.topicAdapter.notifyDataSetChanged();
				topicFragment.topicAdapter.selectedList.clear();

				if (topicFragment.topicList.size()==0) {
					topicFragment.showErrorView();
					tvRight.performClick();
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}
