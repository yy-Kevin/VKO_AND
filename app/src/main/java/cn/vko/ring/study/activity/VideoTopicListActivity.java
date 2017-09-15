package cn.vko.ring.study.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.study.presenter.TopicListPresenter;

public class VideoTopicListActivity extends BaseActivity {
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.iv_back)
	public ImageView ivback;
	@Bind(R.id.xlistview)
	public XListView mListView;
	private TopicListPresenter presenter;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_topiclist;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText(R.string.text_course_topic);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		presenter =	new TopicListPresenter(this, mListView,1);
	}
	
	@OnClick(R.id.iv_back)
	void sayBack(){
		finish();
	}
	
	

}
