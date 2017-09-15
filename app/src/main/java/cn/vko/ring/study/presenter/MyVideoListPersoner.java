package cn.vko.ring.study.presenter;

import java.io.Serializable;
import java.util.List;

import com.android.volley.VolleyError;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.XGridView;

import cn.vko.ring.study.adapter.MyVideoPlayAdapter;
import cn.vko.ring.study.model.BaseVideoList;
import cn.vko.ring.study.model.VideoAttributes;
import cn.vko.ring.study.model.VideoSet;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MyVideoListPersoner implements UIDataListener<BaseVideoList>,OnItemClickListener{
	private Context context;
	private VolleyUtils<BaseVideoList> volley;
	public XGridView  mGridView;
	public MyVideoPlayAdapter myVideoAdapter;
	private String objId,courseType,sectionId,videoId;

	public void setSwitch(boolean aSwitch) {
		isSwitch = aSwitch;
	}

	private boolean isSwitch;
	public MyVideoListPersoner(Context context,String objId,String courseType,String sectionId,String videoId,XGridView mGridView) {
		super();
		this.context = context;
		this.objId = objId;
		this.courseType = courseType;
		this.sectionId = sectionId;
		this.videoId = videoId;
		if(TextUtils.isEmpty(objId)) return;
		this.mGridView=mGridView;
		volley=new VolleyUtils<BaseVideoList>(context,BaseVideoList.class);
		initAdapter();
		initData();
	}

	private void initAdapter() {
		myVideoAdapter = new MyVideoPlayAdapter(context);
		mGridView.setAdapter(myVideoAdapter);
		mGridView.setOnItemClickListener(this);
		myVideoAdapter.setVideoId(videoId);
	}


	private void initData() {
		String commenturl = ConstantUrl.VKOIP.concat("/play/videoIds");
		final Uri.Builder builder = volley.getBuilder(commenturl);			
		builder.appendQueryParameter("objId", objId);
		if(courseType != null && courseType.equals("41")){
			builder.appendQueryParameter("sectionId", sectionId);
		}
		builder.appendQueryParameter("courseType", courseType);
		Log.e("视频选集地址", builder.toString());
		volley.setUiDataListener(this);
		volley.sendGETRequest(false,builder);
	}


	@Override
	public void onDataChanged(BaseVideoList response) {
		if(response.getData()!=null){
			myVideoAdapter.clear();
			myVideoAdapter.add(response.getData());
			myVideoAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}

	public interface IVideoListListener{				
		void getVideoList(VideoSet videoSet);
	}	
	private IVideoListListener iVideolistener;
	
	public void setiVideolistener(IVideoListListener iVideolistener) {
		this.iVideolistener = iVideolistener;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(!isSwitch)return;
		VideoSet videoSet=myVideoAdapter.getListItem(position);
		myVideoAdapter.setVideoId(videoSet.getVideoId());
		view.findViewById(R.id.tv_paly_numbering).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.select_video_color_choose));
		myVideoAdapter.notifyDataSetChanged();	
		if(iVideolistener!=null){
			iVideolistener.getVideoList(videoSet);
		}
	}
	
	
}
