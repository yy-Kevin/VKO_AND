package cn.vko.ring.circle.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.Bind;
import cn.shikh.utils.ViewUtils;
import cn.vko.ring.R;
import cn.vko.ring.circle.model.ImageInfo;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.widget.RoundAngleImageView;

public class ImageItemAdapter extends BaseListAdapter<ImageInfo> {

	private int w;

	public int getW() {

		return w;
	}

	public ImageItemAdapter(Context ctx) {

		super(ctx);
		// TODO Auto-generated constructor stub
		w = ViewUtils.getScreenWidth(ctx) / 3-10;
	}

	@Override
	protected ViewHolder createViewHolder(View root) {

		MyViewHolder holder = new MyViewHolder(root);
		return holder;
	}

	@Override
	protected void fillView(View root, ImageInfo item, ViewHolder holder,
			final int position) {
		// TODO Auto-generated method stub
		if (item == null)
			return;
		MyViewHolder h = (MyViewHolder) holder;
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, w);
		h.ivThrumb.setLayoutParams(lp);
		h.ivThrumb.setRoundWidth(4, 4);
		h.ivDel.setVisibility(View.GONE);
		loadImageView(h.ivThrumb, item.getUrl(), R.drawable.icon_default, w, w);
	}

	@Override
	protected int getItemViewId() {

		// TODO Auto-generated method stub
		return R.layout.layout_image_view;
	}

	class MyViewHolder extends ViewHolder {

		/**
		 * @param view
		 */
		public MyViewHolder(View view) {

			super(view);
			// TODO Auto-generated constructor stub
		}

		@Bind(R.id.iv_thumb)
		public RoundAngleImageView ivThrumb;
		@Bind(R.id.iv_del)
		public ImageView ivDel;
	}

}
