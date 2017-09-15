/*******************************************************************************
 * Copyright (c) 2015 by Luxury Corporation all right reserved.
 * 2015-8-13 
 * 
 *******************************************************************************/
package cn.vko.ring.mine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.mine.model.NotedInfo;

/**
 */
public class NotepadListAdapter extends BaseListAdapter<NotedInfo.Data> {

	/**
	 * @param ctx
	 */
	private int type;

	public NotepadListAdapter(Context ctx, int type) {
		super(ctx);
		// TODO Auto-generated constructor stub
		this.type = type;
	}

	@Override
	protected ViewHolder createViewHolder(View root) {
		MyViewHolder mHolder = new MyViewHolder(root);
		// mHolder.tvNum = (TextView) root.findViewById(R.id.tv_num);
		return mHolder;
	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_errornoted;
	}

	@Override
	protected void fillView(View root, NotedInfo.Data item, ViewHolder holder,
			int position) {
		MyViewHolder mHolder = (MyViewHolder) holder;
		if (type == 1) {
			if (item.name != null && item.name.length() > 13) {
				String simpleName = item.name.substring(0, 10);
				mHolder.tvNoted.setText(simpleName + "...");
			} else {
				mHolder.tvNoted.setText(item.name);
			}
		} else {
			if (item.name != null && item.name.length() > 10) {
				String simpleName = item.name.substring(0, 8);
				mHolder.tvNoted.setText(simpleName + "...");
			} else {
				mHolder.tvNoted.setText(item.name);
			}
		}
		mHolder.tvNum.setText("共有" + item.countEb + "道错题");
	}

	class MyViewHolder extends ViewHolder {
		/**
		 * @param view
		 */
		public MyViewHolder(View view) {
			super(view);

		}

		@Bind(R.id.tv_notepad)
		public TextView tvNoted;
		@Bind(R.id.tv_notepad_num)
		public TextView tvNum;
	}

	public void clear() {
		if (list != null) {
			list.clear();
		}
	}

}
