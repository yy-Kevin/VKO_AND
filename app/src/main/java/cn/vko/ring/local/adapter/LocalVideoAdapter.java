package cn.vko.ring.local.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.local.model.LocalVideoModel.VideoListEntity;

public class LocalVideoAdapter extends BaseListAdapter<VideoListEntity> {

	public LocalVideoAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ViewHolder createViewHolder(View root) {
		// TODO Auto-generated method stub
		MyViewHolder h = new MyViewHolder(root);
		return h;
	}

	@Override
	protected void fillView(View root, VideoListEntity item,ViewHolder holder,int position) {
		// TODO Auto-generated method stub
		final MyViewHolder h = (MyViewHolder) holder;
		if (item != null) {
			h.tvName.setText(item.getVideoName());
			h.tvLong.setText(item.getDuration());
		}
	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_local_video;
	}
	class MyViewHolder extends ViewHolder {
		/**
		 * @param view
		 */
		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}
		@Bind(R.id.tv_name)
		public TextView tvName;
		@Bind(R.id.tv_long)
		public TextView tvLong;
		@Bind(R.id.iv_d)
		public ImageView ivShow;
		
	}
	

}
