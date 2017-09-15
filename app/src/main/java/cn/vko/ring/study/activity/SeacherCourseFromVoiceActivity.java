package cn.vko.ring.study.activity;

import android.content.ComponentName;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speech.VoiceRecognitionService;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.crop.camera.gallery.VideoList;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.widget.ContainsEmojiEditText;
import cn.vko.ring.common.widget.NoScrollViewPager;
import cn.vko.ring.common.widget.SimpleViewPagerIndicator;
import cn.vko.ring.common.widget.SimpleViewPagerIndicator.IScrollPageChangeListener;
import cn.vko.ring.home.adapter.TabFragmentPagerAdapter;
import cn.vko.ring.study.fragment.CourseContentFragment;
import cn.vko.ring.study.fragment.SearchTestFragment;
import cn.vko.ring.study.model.HotRecommoned;
import cn.vko.ring.study.presenter.MyPhotoSeacherPersoner;
import cn.vko.ring.study.presenter.MyPopWindowpersoner;
import cn.vko.ring.study.presenter.MyRecommonedHostorypersoner;
import cn.vko.ring.study.presenter.MyVoiceSearchPersoner;

//import com.baidu.speech.VoiceRecognitionService;

/**
 * 课程搜索
 * @author shikh
 * @time 2016/5/10 9:35
 */

public class SeacherCourseFromVoiceActivity extends BaseActivity implements IScrollPageChangeListener, MyRecommonedHostorypersoner.IHostorySeacherListener, MyVoiceSearchPersoner.ISeacherDataListener {
	private String[] titles = new String[] { "课程内容", "搜题内容" };
	@Bind(R.id.id_stickynavlayout_viewpager)
	public NoScrollViewPager mViewPager;
	@Bind(R.id.layout_indicator)
	public SimpleViewPagerIndicator mIndicator;
	@Bind(R.id.layout_recommend)
	public LinearLayout layout_recommend;
	@Bind(R.id.iv_seacher_to)
	public ImageView iv_seacher_to;
	@Bind(R.id.tv_search)
	public TextView tv_search;
	@Bind(R.id.tv_voice_search)
	public RelativeLayout tv_Voice_search;
	@Bind(R.id.et_seacher)
	public ContainsEmojiEditText et_seacher;

	@Bind(R.id.layout_hot_search)
	public LinearLayout layout_hot_search;
	@Bind(R.id.layout_seacher_result)
	public LinearLayout layout_seacher_result;
	
	private TabFragmentPagerAdapter mAdapter;
	private List<Fragment> fragments;
//	private SeacherContentFragment seacherFragment;
	private SearchTestFragment seacherFragment;
	private CourseContentFragment courseFragment;
	private List<VideoList> videolist;
	private SpeechRecognizer speechRecognizer;
	private String search;
	@OnClick(R.id.iv_back)
	public void goBack() {
		finish();
	}

	@OnClick(R.id.iv_seacher_to)
	public void onIvSearchClicked() {
		// 拍照搜题
		new MyPhotoSeacherPersoner(this);

	}
	@OnClick(R.id.tv_search)
	public void searchDataByString(){
		search = et_seacher.getText().toString().trim();
		if (TextUtils.isEmpty(search)) {
			Toast.makeText(this, "搜索不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		refreshData(search);
	}
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_seacher_course_from_video;
	}

	@Override
	public void initView() {
		mIndicator.setiScrollpageChangeListener(this);
		mIndicator.setTitles(titles, mViewPager);
		iv_seacher_to.setVisibility(View.VISIBLE);

		fragments = new ArrayList<Fragment>();
		courseFragment = new CourseContentFragment();
		seacherFragment = new SearchTestFragment();
		fragments.add(courseFragment);
		fragments.add(seacherFragment);
		mViewPager.setClipChildren(false);
		mViewPager.setOffscreenPageLimit(2);

		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(),fragments);
		mViewPager.setAdapter(mAdapter);
	}

	@Override
	public void initData() {
		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this,new ComponentName(this, VoiceRecognitionService.class));
		mViewPager.setCurrentItem(0);
		search = et_seacher.getText().toString().trim();
		refreshData(search);
		initRequest();
	}

	private void initRequest() {
		// 搜索历史
		new MyRecommonedHostorypersoner(layout_recommend, this)
				.setiHostorySeacherListener(this);
		/*
		 * // 语音搜索 new MyVoiceSearchPersoner(this, speechRecognizer,
		 * layout_video_speech, layout_video_luxyin,
		 * et_seacher).setlSeacherListener(this);
		 */
		// 语音搜索
		new MyVoiceSearchPersoner(this, speechRecognizer, tv_Voice_search,
				et_seacher).setlSeacherListener(this);
		new MyPopWindowpersoner(this, iv_seacher_to);

	}

	private void initViewPage(){
		if(layout_hot_search.isShown()){
			layout_hot_search.setVisibility(View.GONE);
			layout_recommend.removeAllViews();
		}
	}
	private void refreshData(String search) {
		initViewPage();
		this.search = search;
		switch (mViewPager.getCurrentItem()) {
		case 0:
			if (courseFragment != null) {
				courseFragment.setlistVideo(search);
			}
			break;
		case 1:
			if (seacherFragment != null) {
				seacherFragment.setlistVideo(search);
			}
			break;
		}

	}

	@Override
	public void toChangePage(int position) {
		switch (position) {
		case 0:
			if (courseFragment != null) {
				courseFragment.setlistVideo(search);
			}
			break;
		case 1:
			if (seacherFragment != null) {
				seacherFragment.setlistVideo(search);
			}
			break;
		}
	}

	@Override
	public void hostorySeacher(HotRecommoned hotRecommoned) {
		if (hotRecommoned != null) {
			if (!TextUtils.isEmpty(hotRecommoned.getKeyWord())) {
				search = hotRecommoned.getKeyWord() + "";
				et_seacher.setText(search);
				layout_hot_search.setVisibility(View.GONE);
				if (seacherFragment != null) {
					seacherFragment.setlistVideo(search);
					refreshData(search);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		speechRecognizer.destroy();
		super.onDestroy();
	}

	@Override
	public void seacherText() {
		speechRecognizer.stopListening();
		// layout_video_luxyin.setVisibility(View.GONE);
		searchDataByString();
	}

	@Override
	protected void onPause() {
		super.onPause();
			hideSoftInput(this,et_seacher,true);

	}

}
