package cn.vko.ring.study.presenter;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cn.shikh.utils.DateUtils;
import cn.shikh.utils.NetUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.ContainsEmojiEditText;
import cn.vko.ring.study.model.VideoCommentInfo;
import cn.vko.ring.study.model.VideoReviews;

public class VideoCommentPrenenter implements OnClickListener{
	private ContainsEmojiEditText et_text;
	private ImageView iv_send;
	private String vid;
	private Context context;
	private IRefreshCommentListener ifs;

	public VideoCommentPrenenter(ContainsEmojiEditText et_text, ImageView iv_send, String vid,
			Context context) {
		super();
		this.et_text = et_text;
		this.iv_send = iv_send;
		this.vid = vid;
		this.context = context;
		initListener();
	}

	public void updateVideoId(String vid){
		this.vid = vid;
	}
	public void setOnRefreshCommentListener(IRefreshCommentListener ifs) {
		this.ifs = ifs;
	}

	public interface IRefreshCommentListener {
		public void onRefresh(VideoCommentInfo comm);
	}

	private void initListener() {
		iv_send.setOnClickListener(this);
	}

	private String getEditString() {
		return et_text.getText().toString().trim();
	}

	@Override
	public void onClick(View v) {
		if (!NetUtils.isCheckNetAvailable(context)) {
			Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
			return;
		} else {
			Log.e("MyVideoReviews", "针对当前视频的评论");			
			if (getEditString().trim().isEmpty()) {
				Toast.makeText(context, "文字不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			submitCommentTask(getEditString() + "");
		}
	}

	private void submitCommentTask(final String edit) {
		if(vid == null) return;
		String commenturl = ConstantUrl.VKOIP.concat("/course/videoComment");
		VolleyUtils<VideoReviews> volley = new VolleyUtils<VideoReviews>(context,VideoReviews.class);
		Map<String,String> params = new HashMap<String,String>();
		params.put("token", VkoContext.getInstance(context)
				.getUser().getToken()
				+ "");
		params.put("userName", VkoContext
				.getInstance(context).getUser().getName()
				+ "");
		params.put("cnt",edit );
		params.put("videoId", vid + "");// "18500193685"
		volley.sendPostRequest(true,commenturl,params);
		volley.setUiDataListener(new UIDataListener<VideoReviews>() {
			@Override
			public void onDataChanged(VideoReviews response) {
				if (response != null) {
					if (response.getCode().equals("0")) {
						if (ifs != null) {
							VideoCommentInfo comm = new VideoCommentInfo();
							comm.setCommentId(response.getData());
							comm.setComment(edit);
							comm.setCrTime(DateUtils.currentData());
							comm.setVideoId(vid);
							comm.setUserId(VkoContext.getInstance(context).getUserId());
							comm.setUserName(VkoContext.getInstance(context).getUser().getName());
							comm.setUsface(VkoContext.getInstance(context).getUser().getBface());
							ifs.onRefresh(comm);
							et_text.setText("");
						}
					} else {
					}
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});

	}

}
