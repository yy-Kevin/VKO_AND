package cn.vko.ring.mine.fragment;

import java.util.LinkedList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.mine.adapter.MyVideoFinishAdapter;
import cn.vko.ring.study.activity.CourseVideoViewActivity;
import cn.vko.ring.study.db.DBservice;
import cn.vko.ring.study.model.DownloadInfo;
import cn.vko.ring.study.model.VideoAttributes;

public class MyFinishedDownLoadFragment extends BaseFragment implements
		OnItemClickListener, MyVideoFinishAdapter.IDeleteListener {
	public String TAG="MyFinishedDownLoadFragment";
	@Bind(R.id.xl_xlistview)
	public ListView xl_xlistview;

	private DBservice service;
	private LinkedList<DownloadInfo> list;

	private MyVideoFinishAdapter myLoadAdapter;
	
	@Bind(R.id.download_empty)
	public View empty;

	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.activity_video_finish_download;
	}

	@Override
	public void initView(View root) {

		initListener();
		service = new DBservice(atx);
		myLoadAdapter = new MyVideoFinishAdapter(atx);
		xl_xlistview.setAdapter(myLoadAdapter);
		myLoadAdapter.setDeletelistener(this);
		getfinishData();
	}

	private void getfinishData() {
		if(empty == null) return;
		list = service.getCompleteFiles();
		if (list != null) {
			if (!list.isEmpty()) {
				myLoadAdapter.clear();
				myLoadAdapter.add(list);
				myLoadAdapter.notifyDataSetChanged();
				empty.setVisibility(View.GONE);
				xl_xlistview.setVisibility(View.VISIBLE);
				return;
			}
		}
		empty.setVisibility(View.VISIBLE);
		xl_xlistview.setVisibility(View.GONE);
	}

	private void initListener() {
		xl_xlistview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		DownloadInfo info = myLoadAdapter.getListItem(position);
		Log.e(TAG, "goodsid: "+info.getGoodid());
		Log.e(TAG, "title: "+info.getTitle());
		Log.e(TAG, "videoid: "+info.getVideoid());
		Log.e(TAG, "VId: "+info.getVid());
		VideoAttributes vab = new VideoAttributes();
		// vab.setBookId(knowsection.getBookId());
		vab.setGoodsId(info.getGoodid());
		vab.setVideoName(info.getTitle());
		vab.setVideoId(info.getVideoid());
		vab.setEncryptionid(info.getVid());
		// vab.setSectionId(knowsection.getChapterId());
		// 同步视频
		// vab.setCourseType("41");
		Intent intent = new Intent(atx, CourseVideoViewActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("video", vab);
		bundle.putInt("TYPE", -2);
		intent.putExtras(bundle);
		atx.startActivity(intent);
	}

	public void setRefreshData() {
		getfinishData();
	}

	@Override
	public void deletedowninfo() {
		getfinishData();

	}

	@OnClick(R.id.download_empty)
	void sayRefresh() {
		getfinishData();
	}

}
