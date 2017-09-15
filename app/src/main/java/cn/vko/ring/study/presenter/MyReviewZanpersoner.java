package cn.vko.ring.study.presenter;


import com.android.volley.VolleyError;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.study.model.VideoCommentInfo;
import cn.vko.ring.study.model.Praise;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyReviewZanpersoner implements OnClickListener,
		UIDataListener<Praise> {
	public VideoCommentInfo item;
	public Context context;
	public ImageView iv_video_like;
	public TextView iv_video_like_num;
	public TextView tv_like_animation;
	private VolleyUtils<Praise> volleyPraise;
	private Animation animation;

	public MyReviewZanpersoner(Context context, VideoCommentInfo item,
			ImageView iv_video_like, TextView iv_video_like_num,
			TextView tv_like_animation) {
		super();
		this.context = context;
		this.item = item;
		this.iv_video_like = iv_video_like;
		this.iv_video_like_num = iv_video_like_num;
		this.tv_like_animation = tv_like_animation;
		initListener();
	}
	

	public void DisplayAnimation(final TextView textView) {
		textView.setVisibility(View.VISIBLE);
		textView.startAnimation(animation);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				textView.setVisibility(View.GONE);
			}
		}, 1000);
	}

	private void initListener() {
		animation = AnimationUtils.loadAnimation(context,
				R.anim.applaud_animation);

		iv_video_like.setOnClickListener(this);

		initsetBackgrouce();
		initVolley();

	}

	private void initsetBackgrouce() {
		if (item.isFlag()) {
			iv_video_like.setImageResource(R.drawable.class_video_s_praised);
		} else {
			iv_video_like.setImageResource(R.drawable.class_video_s_praise);
		}
	}

	private void initVolley() {
		volleyPraise = new VolleyUtils<Praise>(context,Praise.class);

	}

	@Override
	public void onClick(View v) {
		if (!item.isFlag()) {					
			requestZan();
		} else {
			Toast.makeText(context, "你已经点过赞了", Toast.LENGTH_SHORT).show();
		}
	}

	private void requestZan() {
		String commenturl = ConstantUrl.VKOIP.concat("/course/commentZan");
		Log.e("地址", commenturl);
		Uri.Builder builder = volleyPraise.getBuilder(commenturl);
		// builder.appendQueryParameter("commentId", item.getCommentId() + "");
		builder.appendQueryParameter("commentId", item.getCommentId()+"");
		builder.appendQueryParameter("opt", 1 + "");
		builder.appendQueryParameter("token", VkoContext.getInstance(context).getUser().getToken());
		Log.e("点赞地址", builder.toString());
		volleyPraise.setUiDataListener(this);
		volleyPraise.sendGETRequest(false,builder);
	}


	@Override
	public void onDataChanged(Praise response) {
		if (response != null) {
			if (!TextUtils.isEmpty(response.getData())) {
				if ("-1".equals(response.getData())) {
					Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT).show();
				}else{
					DisplayAnimation(tv_like_animation);
					iv_video_like.setImageResource(R.drawable.class_video_s_praised);
					iv_video_like_num.setText((item.getUpCount()+ 1) + "");
					item.setFlag(true);
				}

			}
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}
