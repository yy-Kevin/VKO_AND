/*
 *NotepadActivity.java [V 1.0.0]
 *classes : cn.vko.ring.mine.views.NotepadActivity
 *宣义阳 Create at 2015-7-28 上午10:19:36
 */
package cn.vko.ring.mine.activity;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.IOnClickItemListener;
import cn.vko.ring.common.widget.NoScrollViewPager;
import cn.vko.ring.common.widget.SimpleViewPagerIndicator;
import cn.vko.ring.common.widget.SubjectFilterLayout;
import cn.vko.ring.home.adapter.TabFragmentPagerAdapter;
import cn.vko.ring.home.model.SubjectInfo;
import cn.vko.ring.mine.fragment.AllClassFragment;
import cn.vko.ring.mine.fragment.SyncClassFragment;

/**
 */
public class NotepadActivity extends BaseActivity implements
		IOnClickItemListener<SubjectInfo> {

	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.tv_right)
	public TextView ivSubject;
	@Bind(R.id.gv_subject)
	public SubjectFilterLayout mSubjectLayout;
	@Bind(R.id.id_stickynavlayout_viewpager)
	public NoScrollViewPager nViewPager;
	private TabFragmentPagerAdapter nPagerAdapter;
	private List<Fragment> listNoteFragment;
	private String[] titles = new String[] { "同步课程", "综合课程" };
	@Bind(R.id.layout_indicator)
	public SimpleViewPagerIndicator mIndicator;

	private SyncClassFragment acyncFragment;
	private AllClassFragment allFragamet;

	@Override
	public int setContentViewResId() {
		return R.layout.activity_notepad;
	}

	@Override
	public void initView() {
		ivSubject.setVisibility(View.VISIBLE);
		// ivSubject.setText(R.string.notepad_subject);
		mIndicator.setTitles(titles, nViewPager);
		mSubjectLayout.setInitData(null, this);
		mSubjectLayout.setEvent(ivSubject, VkoContext.getInstance(this).getGradeId(), 1);
		mSubjectLayout.initData(VkoContext.getInstance(this).getGradeId(), 0);
	}

	@Override
	public void initData() {
		tvTitle.setText(R.string.notepad_text);
		listNoteFragment = new ArrayList<Fragment>();
		listNoteFragment.add(acyncFragment = new SyncClassFragment());
		listNoteFragment.add(allFragamet = new AllClassFragment());
		nViewPager.setClipChildren(false);
		nViewPager.setOffscreenPageLimit(2);
		nPagerAdapter = new TabFragmentPagerAdapter(
				getSupportFragmentManager(), listNoteFragment);
		nViewPager.setAdapter(nPagerAdapter);
	}

	// 选择科目之后的回调
	@Override
	public void onItemClick(int position, SubjectInfo t, View v) {
		acyncFragment.updateSubject(t.getSubjectId());
		allFragamet.updateSubject(t.getSubjectId());
	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		finish();
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mSubjectLayout != null) {
			mSubjectLayout.unRegister();
		}
		super.onDestroy();
	}

}
