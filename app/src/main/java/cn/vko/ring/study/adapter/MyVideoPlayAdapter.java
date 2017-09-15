package cn.vko.ring.study.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.vko.ring.R;
import cn.vko.ring.R.color;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.study.model.UnitVideo;
import cn.vko.ring.study.model.VideoSet;

public class MyVideoPlayAdapter extends BaseListAdapter<VideoSet> {


	public String videoId;
	private Context context;
		
	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public MyVideoPlayAdapter(Context ctx) {
		super(ctx);
		this.context=ctx;
	}

	@Override
	protected ViewHolder createViewHolder(
			View root) {
		MyViewHolder holder = new MyViewHolder(root);
		holder.tv_paly_numbering = (TextView) root
				.findViewById(R.id.tv_paly_numbering);
		return holder;
	}


	@Override
	protected void fillView(View root, VideoSet item,
			ViewHolder holder,
			int position) {
		final MyViewHolder h = (MyViewHolder) holder;
		if (item == null) {
			return;
		}		
		if (videoId !=null && videoId.equals(item.getVideoId())) {
			h.tv_paly_numbering.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.select_video_color_choose));
			h.tv_paly_numbering.setTextColor(context.getResources().getColor(color.white));
		}else if(videoId == null && position == 0){
			h.tv_paly_numbering.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.select_video_color_choose));
			h.tv_paly_numbering.setTextColor(context.getResources().getColor(color.white));
		}else{
			h.tv_paly_numbering.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.select_video));
			h.tv_paly_numbering.setTextColor(context.getResources().getColor(color.text_black_color));
		}
		if(position<9){
			h.tv_paly_numbering.setText("0"+(position + 1) + "");
		}else{
			h.tv_paly_numbering.setText(position + 1 + "");
		}
		
		
	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item__unit_video;
	}

	class MyViewHolder extends ViewHolder {
		TextView tv_paly_numbering;

		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}

	}
}
