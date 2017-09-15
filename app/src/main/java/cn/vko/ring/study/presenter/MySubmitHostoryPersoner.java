package cn.vko.ring.study.presenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.study.model.VideoPositionSubmit;
import cn.vko.ring.study.model.VideoRecordInfo;

public class MySubmitHostoryPersoner implements UIDataListener<VideoPositionSubmit> {
	private VideoRecordInfo submitRecord;
	private Context context;
	private VolleyUtils<VideoPositionSubmit> volley;
	private boolean isPause;
	public MySubmitHostoryPersoner(VideoRecordInfo submitRecord, Context context,boolean isPause) {
		super();
		this.submitRecord = submitRecord;
		this.context = context;
		volley=new VolleyUtils<VideoPositionSubmit>(context,VideoPositionSubmit.class);
		this.isPause = isPause;
		initData();
		
	}
	private void initData() {
		String commenturl = ConstantUrl.VKOIP.concat("/course/newTrackV2");	
		Uri.Builder builder = volley.getBuilder(commenturl);
		
		builder.appendQueryParameter("token", VkoContext.getInstance(context).getToken());
		
		builder.appendQueryParameter("sec", submitRecord.getSec() / 1000 + "");
		Log.e("获取当前视频播放时间-秒", "播放的时间" + submitRecord.getSec() / 1000 + "");
		builder.appendQueryParameter("uniKey", new Date().getTime() + "");
		builder.appendQueryParameter("goodsId", submitRecord.getGoodsId()+"");	
		builder.appendQueryParameter("videoId", submitRecord.getVideoId()+"");
		builder.appendQueryParameter("subjectId",submitRecord.getSubjectId());		
		builder.appendQueryParameter("learnId",VkoContext.getInstance(context).getUser().getLearn()+"");
		
		if(submitRecord.getCourseType()!=null){		
			builder.appendQueryParameter("courseType", submitRecord.getCourseType()+"");
		}else{
			return;
		}				
		if(submitRecord.getCourseType().equals("41")){
			builder.appendQueryParameter("bookId", submitRecord.getBookId()+"");
			builder.appendQueryParameter("sectionId", submitRecord.getKnowledgeId()+"");
		}else if(submitRecord.getCourseType().equals("43")){
			builder.appendQueryParameter("knowledgeId", submitRecord.getKnowledgeId()+"");
			builder.appendQueryParameter("objId", submitRecord.getBookId()+"");
		}
		volley.setUiDataListener(this);
		volley.sendGETRequest(false,builder);
		Log.e("提交播放记录地址------------", builder.toString());
//		volley.requestHttpGetNoLoading(builder, this, VideoPositionSubmit.class);
	}



	@Override
	public void onDataChanged(VideoPositionSubmit response) {
		if(response!=null){
			if(response.isData() && isPause){
				// 刷新课程章节界面的数据
				EventBus.getDefault().post(Constants.REFRESH_SYNCTEST_SYNC_DATA_VIDEO);
			}
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}


