package cn.vko.ring.mine.adapter;

import butterknife.Bind;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.study.model.BookInfo;


/**
 * @author shikh
 *
 */
public class BookAdapter extends BaseListAdapter<BookInfo> {

	private int index = -1;
	private BookInfo book;
	
	
	public BookInfo getBook() {
	
		return book;
	}
	
	public void setSelectIndex(int index){
		this.index = index;
	}
	/**
	 * @param ctx
	 */
	public BookAdapter(Context ctx) {
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
	protected void fillView(View root, BookInfo item, ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		MyViewHolder h = (MyViewHolder) holder;
		if(item == null) return;
		String name = item.getBookname();
		if(name != null && name.contains(" ")){
			int end = name.length();
			if(name.contains("（")){
				end = name.indexOf("（");
			}
			name = name.substring(name.indexOf(" "), end);
		}
		h.tvName.setText(name);
		loadImageView(h.ivCover, item.getImgurl(), R.drawable.class_bookfacenoimage, 200, 150);
		if(position == index){
			h.bookLayout.setBackgroundResource(R.drawable.shape_book_bg);
		}else{
			h.bookLayout.setBackgroundResource(R.color.transparent);
		}
		if(item.getState()== 0){
			book = item;
			h.mCb.setVisibility(View.VISIBLE);
		}else{
			h.mCb.setVisibility(View.GONE);
		}
//		h.mCb.setVisibility(item.getState()== 0? View.VISIBLE:View.GONE);
		
	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_book_list;
	}
	
	public class MyViewHolder extends ViewHolder {

		/**
		 * @param view
		 */
		public MyViewHolder(View view) {

			super(view);

			// TODO Auto-generated constructor stub
		}

		@Bind(R.id.tv_bookname)
		public TextView tvName;
		@Bind(R.id.iv_cover)
		public ImageView ivCover;
		@Bind(R.id.checkbox)
		public ImageView mCb;
		@Bind(R.id.book_layout)
		public RelativeLayout bookLayout;

	}

}
