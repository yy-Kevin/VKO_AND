package cn.vko.ring.mine.fragment;

import java.util.LinkedList;

import android.view.View;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.mine.adapter.VideoDownloadAdapter;
import cn.vko.ring.mine.listener.IRefreshDataListener;
import cn.vko.ring.study.db.DBservice;
import cn.vko.ring.study.model.DownloadInfo;

public class MyDownLoadFragment extends BaseFragment{
	@Bind(R.id.xl_xlistview)
	public ListView xl_xlistview;
	private DBservice service;
	private LinkedList<DownloadInfo> list;
	private VideoDownloadAdapter myLoadAdapter;

	@Bind(R.id.download_empty)
	View empty;

	public IRefreshDataListener islistener;

	public void setIslistener(IRefreshDataListener islistener) {
		this.islistener = islistener;
	}

	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.activity_video_download;
	}

	@Override
	public void initView(View root) {
		// ButterKnife.bind(this, root);
	}

	@Override
	public void initData() {
		super.initData();
		service = new DBservice(atx);
		refreshData();
	}


	public void refreshData() {
		if(empty == null) return;
		if (list != null) {
			list.clear();
			list = null;
		}
		list = service.getDownloadFiles();
		if (list != null ) {
			if(!list.isEmpty()){
				myLoadAdapter = new VideoDownloadAdapter(atx, list);
				if (xl_xlistview != null) {
					xl_xlistview.setAdapter(myLoadAdapter);				
				}
				myLoadAdapter.setIrflistenr(islistener);
				empty.setVisibility(View.GONE);
				xl_xlistview.setVisibility(View.VISIBLE);
				return;
			}
		} 
		empty.setVisibility(View.VISIBLE);
		xl_xlistview.setVisibility(View.GONE);
	}

	@OnClick(R.id.download_empty)
	void sayRefresh(){
		refreshData();
	}
}
