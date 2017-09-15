package cn.vko.ring.study.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.RoundAngleImageView;
import cn.vko.ring.study.model.Praise;
import cn.vko.ring.study.model.VideoCommentInfo;

public class VideoCommentListAdapter extends BaseListAdapter<VideoCommentInfo> {
	private Context ctx;
	private Animation animation;
	private VolleyUtils<Praise> volleyPraise;
	public VideoCommentListAdapter(Context ctx) {
		super(ctx);
		this.ctx = ctx;
		volleyPraise = new VolleyUtils<Praise>(ctx,Praise.class);
		animation = AnimationUtils.loadAnimation(ctx,
				R.anim.applaud_animation);
	}

	@Override
	protected ViewHolder createViewHolder(View root) {
		MyViewHolder h = new MyViewHolder(root);
		h.tv_comments = (TextView) root.findViewById(R.id.tv_comments);
		h.iv_student_photo = (RoundAngleImageView) root
				.findViewById(R.id.iv_student_photo);
		h.tv_date = (TextView) root.findViewById(R.id.tv_date);
		h.tv_student_name = (TextView) root.findViewById(R.id.tv_student_name);
		h.iv_video_like = (ImageView) root.findViewById(R.id.iv_video_like);
		h.iv_video_like_num = (TextView) root
				.findViewById(R.id.iv_video_like_num);
		h.tv_like_animation=(TextView) root.findViewById(R.id.tv_like_animation);
		h.layoutLike = (RelativeLayout) root.findViewById(R.id.layout_like);
		return h;
	}

	@Override
	protected void fillView(View root, final VideoCommentInfo item, ViewHolder holder, int position) {
		if (item == null) {
			return;
		}
		final MyViewHolder h = (MyViewHolder) holder;
		h.tv_comments.setText(item.getComment()+"");
		h.tv_date.setText(item.getCrTime());
		h.tv_student_name.setText(item.getUserName()+"");
		h.iv_video_like_num.setText(item.getUpCount()+"");
		if (item.isFlag()) {
			h.iv_video_like.setImageResource(R.drawable.class_video_s_praised);
		} else {
			h.iv_video_like.setImageResource(R.drawable.class_video_s_praise);
		}

//		new MyReviewZanpersoner(ctx,item ,h.iv_video_like, h.iv_video_like_num,h.tv_like_animation);
		loadImageView(h.iv_student_photo, item.getUsface(), 0, 0, 0);
		// h.iv_video.setImageBitmap(loadImageView(h.iv_video, item.get(), 0, 0,
		// 0));
		h.layoutLike.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!item.isFlag()) {
					h.requestZan(item);
				} else {
					Toast.makeText(ctx, "你已经点过赞了", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_comments;
	}

	class MyViewHolder extends ViewHolder {
		RoundAngleImageView iv_student_photo;
		TextView tv_student_name;
		TextView tv_date;
		TextView tv_comments;
		ImageView iv_video_like;
		TextView iv_video_like_num;
		TextView tv_like_animation;
		RelativeLayout layoutLike;
		public MyViewHolder(View view) {
			super(view);

		}
		public void requestZan(final VideoCommentInfo item) {
			String commenturl = ConstantUrl.VKOIP.concat("/course/commentZan");
			Log.e("地址", commenturl);
			Uri.Builder builder = volleyPraise.getBuilder(commenturl);
			// builder.appendQueryParameter("commentId", item.getCommentId() + "");
			builder.appendQueryParameter("commentId", item.getCommentId()+"");
			builder.appendQueryParameter("opt", 1 + "");
			builder.appendQueryParameter("token", VkoContext.getInstance(ctx).getUser().getToken());
			Log.e("点赞地址", builder.toString());
			volleyPraise.setUiDataListener(new UIDataListener<Praise>(){
				@Override
				public void onDataChanged(Praise response) {
					if (response != null) {
						if (!TextUtils.isEmpty(response.getData())) {
							if ("-1".equals(response.getData())) {
								Toast.makeText(ctx, "点赞失败", Toast.LENGTH_SHORT).show();
							}else{
								iv_video_like.setImageResource(R.drawable.class_video_s_praised);
								item.setUpCount(item.getUpCount()+ 1);
								iv_video_like_num.setText(item.getUpCount() + "");
								System.out.print("num="+item.getUpCount());
								DisplayAnimation(tv_like_animation);
								item.setFlag(true);
							}

						}
					}
				}

				@Override
				public void onErrorHappened(String errorCode, String errorMessage) {

				}
			});
			volleyPraise.sendGETRequest(false,builder);
		}
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
}


