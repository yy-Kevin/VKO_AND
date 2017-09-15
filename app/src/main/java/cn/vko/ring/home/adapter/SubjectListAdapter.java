package cn.vko.ring.home.adapter;

import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.home.model.SubjectInfo;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;


/**
 * @author shikh
 *
 */
public class SubjectListAdapter extends BaseListAdapter<SubjectInfo> {
	
	public SubjectInfo info;
	/**
	 * @param ctx
	 */
	public SubjectListAdapter(Context ctx) {

		super(ctx);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
	
		// TODO Auto-generated method stub
//		return super.getCount();
		return null == list ? 0 : list.size()%3 == 0 ?list.size() : 3-list.size()%3 + list.size() ;
	}
	public void setSelectSub(SubjectInfo info){
		this.info = info;
	}
	@Override
	protected ViewHolder createViewHolder(View root) {

		// TODO Auto-generated method stub
		MyViewHolder h = new MyViewHolder(root);
		return h;
	}

	@Override
	protected void fillView(View root, SubjectInfo item, ViewHolder holder,int position) {
		// TODO Auto-generated method stub
		MyViewHolder h = (MyViewHolder) holder;
		if(item != null){
			h.mRbtn.setText(item.getName());
			h.mRbtn.setChecked(info != null && info.equals(item));
		}else{
			h.mRbtn.setText("");
			h.mRbtn.setChecked(false);
		}
		
	}

	@Override
	protected int getItemViewId() {

		// TODO Auto-generated method stub
		return R.layout.item_subject_list;
	}

	class MyViewHolder extends ViewHolder{
		/**
		 * @param view
		 */
		public MyViewHolder(View view) {

			super(view);
			
			// TODO Auto-generated constructor stub
		}
		@Bind(R.id.rbtn)
		public RadioButton mRbtn;
	
	}
	
	
}
