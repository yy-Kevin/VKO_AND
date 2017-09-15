package cn.vko.ring.circle.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.Bind;
import cn.shikh.utils.DateUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.circle.activity.GroupHomeActivity;
import cn.vko.ring.circle.adapter.GroupListAdapter;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.common.widget.xlv.XListView.IXListViewListener;

public class GroupFragment extends BaseFragment implements IXListViewListener,OnItemClickListener{

	@Bind(R.id.xlistview)
	public XListView mListView;
	
	private GroupListAdapter mAdapter;
	private int page = 1;

	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_group_list;
	}

	@Override
	public void initView(View root) {
		// TODO Auto-generated method stub
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		mAdapter = new GroupListAdapter(atx);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		getGroupListTask(1);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
//		getGroupListTask(page+1);
	}
	public void stopUp() {
		mListView.stopLoadMore();
		mListView.stopRefresh();
		mListView.setRefreshTime(DateUtils.getCurDateStr());
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (mAdapter.getList() == null || mAdapter.getList().size() == 0) {
			getGroupListTask(1);
		} else if(position >0){
			StartActivityUtil.startActivity(atx, GroupHomeActivity.class);
		}
	}
	
	private void getGroupListTask(int page) {
		// TODO Auto-generated method stub
//		mAdapter.notifyDataSetChanged();
//		stopUp();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		getGroupListTask(1);
	}	

}
