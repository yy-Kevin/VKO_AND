/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: GvIndexItemAdapter.java 
 * @Prject: VkoCircle
 * @Package: cn.vko.ring.test.adapter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-9-17 上午11:44:26 
 * @version: V1.0   
 */
package cn.vko.ring.study.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.study.model.Ans;

/** 
 * @ClassName: GvIndexItemAdapter 
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-9-17 上午11:44:26  
 */
public class GvIndexItemAdapter extends BaseListAdapter<Ans>{

	private Context context;
	/** 
	 * 
	 * @Description:TODO 
	 */
	public GvIndexItemAdapter(Context ctx) {
		super(ctx);
	this.context = ctx;
	}

	/*  
	 * 
	 * @Description: TODO
	 *  
	 */
	@Override
	protected ViewHolder createViewHolder(View root) {
		// TODO Auto-generated method stub
		return new MyHolder(root);
	}

	/*  
	 * 
	 * @Description: TODO
	 *  
	 */
	@Override
	protected void fillView(View root, Ans item, ViewHolder holder, int position) {
		if (item==null) {
			return;
		}
		MyHolder h = (MyHolder) holder;
		h.tvIndex.setText(position+1+"");
		if (item.isRight()) {
			h.tvIndex.setBackgroundResource(R.drawable.green_normal);
			h.tvIndex.setTextColor(context.getResources().getColor(R.color.text_title_color));
		}else{
			h.tvIndex.setBackgroundResource(R.drawable.red_normal);
			h.tvIndex.setTextColor(context.getResources().getColor(R.color.white));
		}
	}

	class MyHolder extends ViewHolder{

		/** 
		 * 
		 * @Description:TODO 
		 */
		public MyHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}
		@Bind(R.id.tv_index)
		TextView tvIndex;
	}
	/*  
	 * 
	 * @Description: TODO
	 *  
	 */
	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_index_view;
	}
}
