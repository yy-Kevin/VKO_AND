package cn.vko.ring.common.widget.pop;

import butterknife.Bind;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.easefun.polyvsdk.BitRateEnum;

import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;


/**
 * @author shikh
 *
 */
public class LevelListAdapter extends BaseListAdapter<BitRateEnum> {

	public int index;
	public void setIndex(int index){
		this.index = index;
	}
	/**
	 * @param ctx
	 */
	public LevelListAdapter(Context ctx) {
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
	protected void fillView(View root, BitRateEnum item,ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		MyViewHolder h = (MyViewHolder) holder;
		h.tvName.setBackgroundDrawable(null);
		h.tvName.setText(item.getName());
		h.tvName.setPadding(10,8, 10, 8);
		if(position == index){
			h.tvName.setTextColor(ctx.getResources().getColor(R.color.blue));
		}else{
			h.tvName.setTextColor(ctx.getResources().getColor(R.color.white));
		}
	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_index_view;
	}
	
	class MyViewHolder extends ViewHolder{

		/**
		 * @param view
		 */
		public MyViewHolder(View view) {

			super(view);
			
			// TODO Auto-generated constructor stub
		}
		@Bind(R.id.tv_index)
		public TextView tvName;
		
	}

}
