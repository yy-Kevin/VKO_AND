package cn.vko.ring.study.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.widget.RoundAngleImageView;
import cn.vko.ring.study.model.VideoList;

public class MyVideoCourseSeacherAdapter extends BaseListAdapter<VideoList> {

	public MyVideoCourseSeacherAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ViewHolder createViewHolder(
			View root) {

		MyViewHolder h = new MyViewHolder(root);
		h.iv_video_photo=(ImageView) root.findViewById(R.id.iv_video_photo);
		h.tv_video_name=(TextView) root.findViewById(R.id.tv_video_name);
		h.tv_teacher_name=(TextView) root.findViewById(R.id.tv_teacher_name);
		h.iv_teacher_photo=(RoundAngleImageView) root.findViewById(R.id.iv_teacher_photo);
		h.tv_video_playtime=(TextView) root.findViewById(R.id.tv_video_playtime);
		
		return h;
	}

	@Override
	protected void fillView(View root, VideoList item,
			ViewHolder holder,
			int position) {
		
		final MyViewHolder h = (MyViewHolder) holder;	
		if(item==null){
			return;
		}
		h.tv_video_name.setText(item.getVideoName()+"");
		h.tv_teacher_name.setText(item.getTName()+"");
		h.tv_video_playtime.setText(item.getDuration()+"");	
		
		/* h.iv_video_photo.setImageURI(Uri.parse(item.getImage()));
		 h.iv_video_photo.setScaleType(ScaleType.FIT_XY);*/
		if (item.getImage() != null) {
			loadImageView(h.iv_video_photo, item.getImage(),R.drawable.icon_default, 0, 0);
			 h.iv_video_photo.setScaleType(ScaleType.FIT_XY);
		}
		/*  
		 * LayoutParams p=(LayoutParams) h.iv_video_photo.getLayoutParams();
		 * //p.width=380; p.height=240; h.iv_video_photo.setLayoutParams(p);
		 * h.iv_video_photo.setImageBitmap(loadImageView(h.iv_video_photo,
		 * item.getImage(), 0, 0, 0));
		 */
		//loadImageView(h.iv_video_photo, item.getImage(), R.drawable.class_videonoimage, 0, 0);
		if(item.getTimage()!=null){
			loadImageView(h.iv_teacher_photo, item.getTimage(),
					R.drawable.default_personal_head, 0, 0);	
		}
	}
	
	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_seacher_video;
	}

	class MyViewHolder extends ViewHolder {
		ImageView iv_video_photo;
		TextView tv_video_name;
		TextView tv_teacher_name;
		RoundAngleImageView iv_teacher_photo;
		TextView tv_video_playtime;
		
				
		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}

	}

}
