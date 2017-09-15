package cn.vko.ring.mine.fragment;

import butterknife.Bind;
import android.os.Bundle;
import android.view.View;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.common.widget.xlv.XListView.IXListViewListener;
import cn.vko.ring.mine.presenter.MineZtprestener;

/**
 * 我的课程 本地课程
 * @author Administrator
 *
 */
public class LocalCourseFragment extends BaseFragment implements IXListViewListener {

	
	@Bind(R.id.xlistview)
	XListView mListView;
	MineZtprestener mineZtprestener;
	int pageIndex;
	int pageSize=10;
	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub

		return R.layout.layout_xlistview;

	}

	@Override
	public void initView(View root) {
		// TODO Auto-generated method stub
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		mineZtprestener = new MineZtprestener(atx, mListView);
		onRefresh();
	}


	public void refreshData(String subject, String cityId){

	}
	
	public void refreshItemData(){
	
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		pageIndex=1;
		mineZtprestener.getDatas(pageIndex, pageSize);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		++pageIndex;
		mineZtprestener.getDatas(pageIndex, pageSize);
	}
}
