/*******************************************************************************
 * Copyright (c) 2015 by Luxury Corporation all right reserved.
 * 2015-8-18 
 * 
 *******************************************************************************/
package cn.vko.ring.mine.adapter;

import java.util.List;

import butterknife.Bind;

import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.mine.model.AllClassInfo.Data.Course;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 
 * 编写日期:	2015-8-18
 * 作者:	宋宁宁
 * 
 * 历史记录
 *    修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class AllClassAdapter extends BaseListAdapter<Course> {
	private List<Course> list;
	/**
	 * @param ctx
	 */
	private Resources res;

	public AllClassAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
		res = ctx.getResources();
	}

	class MyViewHolder extends ViewHolder {
		/**
		 * @param view
		 */
		public MyViewHolder(View view) {
			// TODO Auto-generated constructor stub
			super(view);
		}

		@Bind(R.id.all_class_book_name)
		public TextView name;
	}

	@Override
	protected ViewHolder createViewHolder(View root) {
		MyViewHolder h = new MyViewHolder(root);
		return h;
	}
	@Override
	protected int getItemViewId() {
		return R.layout.item_all_class_list;
	}

	/*  
	 * 
	 * @Description: TODO
	 *  
	 */
	@Override
	protected void fillView(View root, Course item, ViewHolder holder,
			int position) {
		MyViewHolder h = (MyViewHolder) holder;
		if (item == null)
			return;
			h.name.setText(item.getName());
			System.out.println("position===" + position);
		
	}
}
