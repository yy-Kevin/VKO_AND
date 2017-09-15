/**   
 * Copyright © 2016 cn.vko.com. All rights reserved.
 * 
 * @Title: CommonDialog.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.common.widget.dialog 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2016-1-7 上午10:51:05 
 * @version: V1.0   
 */
package cn.vko.ring.common.widget.dialog;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.model.ItemDialogModel;
import cn.vko.ring.common.widget.dialog.CommonDialog.Builder.OnButtonClicked;
import cn.vko.ring.common.widget.dialog.CommonDialog.Builder.OnLvItemClickListener;

/**
 * @ClassName: CommonDialog
 * @Description: TODO
 * @author: JiaRH
 * @date: 2016-1-7 上午10:51:05
 */
public class CommonDialog extends CommonDialogBase {
	private Builder builder;
	@Bind(R.id.title)
	TextView tvTitle;
	@Bind(R.id.content)
	TextView tvContent;
	@Bind(R.id.tv_bottom_left)
	Button tvBottomLeft;
	@Bind(R.id.tv_bottom_right)
	Button tvBottomRight;
	@Bind(R.id.common_dialog_lv)
	ListView lvCommonDialog;
	@Bind(R.id.bottom_line)
	View ivBottomLine;
	@Bind(R.id.bottom_mid_line)
	View ivBottomMidLine;
	private DialogAdapter mAdapter;
	private OnButtonClicked onButtonClicked;
	private OnLvItemClickListener  onLvItemClickListener;
	private List<ItemDialogModel> itemModels = new ArrayList<ItemDialogModel>();

	public static class Builder {
		private Context context;
		private String title;
		private String content;
		private String cancleText;
		private String sureText;
		private List<ItemDialogModel> items = new ArrayList<ItemDialogModel>();
		private boolean isShowCancle;
		private boolean isShowSure;
		private boolean isShowContent;
		private boolean isShowTitle;
		private boolean isShowBottom;
		private boolean isShowListView;
		private OnButtonClicked onButtonClicked;
		private OnLvItemClickListener  onLvItemClickListener;
		
		public Builder(Context context) {
			super();
			this.context = context;
		}
		public CommonDialog build() {
			return new CommonDialog(context, R.style.MaskDialog,this);
		}
		public Builder setTitleText(String title) {
			this.title = title;
			if (!TextUtils.isEmpty(title)) {
				isShowTitle = true;
			} else {
				isShowTitle = false;
			}
			return this;
		}
		public Builder setContentText(String content) {
			this.content = content;
			if (!TextUtils.isEmpty(content) && !isShowListView) {
				isShowContent = true;
			} else {
				isShowContent = false;
			}
			return this;
		}
		public Builder setCancleText(String cancleText) {
			this.cancleText = cancleText;
			if (!TextUtils.isEmpty(cancleText)) {
				isShowCancle = true;
				isShowBottom = true;
			} else {
				isShowCancle = false;
			}
			return this;
		}
		public Builder setSureText(String sureText) {
			this.sureText = sureText;
			if (!TextUtils.isEmpty(sureText)) {
				isShowSure = true;
				isShowBottom = true;
			} else {
				isShowSure = false;
			}
			return this;
		}
		public Builder setItems(List<ItemDialogModel> items) {
			this.items = items;
			if (items.size() > 0 && !isShowContent) {
				isShowListView = true;
			} else {
				isShowListView = false;
			}
			return this;
		}
		public Builder items(ItemDialogModel item) {
			items.add(item);
			if (items.size() > 0 && !isShowContent) {
				isShowListView = true;
			} else {
				isShowListView = false;
			}
			return this;
		}
		public boolean isShowBottom() {
			if (isShowCancle || isShowSure) {
				isShowBottom = true;
			} else {
				isShowBottom = false;
			}
			return isShowBottom;
		}
		
		public interface OnButtonClicked {
			void onSureButtonClick(CommonDialog commonDialog);
			void onCancleButtonClick(CommonDialog commonDialog);
		}

		public Builder setOnButtonClicked(OnButtonClicked onButtonClicked) {
			this.onButtonClicked = onButtonClicked;
			return this;
		}

		public interface OnLvItemClickListener {
			void onItemClick(View view, int position);
		}

		public Builder setOnLvItemClickListener(OnLvItemClickListener onLvItemClickListener) {
			this.onLvItemClickListener = onLvItemClickListener;
			return this;
		}
	}

	/**
	 * @Description:TODO
	 */
	public CommonDialog(Context context,int theme, Builder builder) {
		super(context, theme,builder);
		this.builder = builder;
		this.onButtonClicked=builder.onButtonClicked;
		this.onLvItemClickListener=builder.onLvItemClickListener;
	
	}

	@Override
	public void dealViews(View rootView) {
	
		if (builder.isShowTitle) {
			tvTitle.setVisibility(View.VISIBLE);
			tvTitle.setText(builder.title);
		} else {
			tvTitle.setVisibility(View.GONE);
		}
		if (builder.isShowContent) {
			tvContent.setVisibility(View.VISIBLE);
			tvContent.setText(builder.content);
		} else {
			tvContent.setVisibility(View.GONE);
		}
		if (builder.isShowCancle) {
			tvBottomLeft.setVisibility(View.VISIBLE);
			tvBottomLeft.setBackgroundResource(R.drawable.left_bottom_btn_selector);
			tvBottomLeft.setText(builder.cancleText);
		} else {
			tvBottomLeft.setVisibility(View.GONE);
		}
		if (builder.isShowSure) {
			tvBottomRight.setVisibility(View.VISIBLE);
			tvBottomRight.setBackgroundResource(R.drawable.right_bottom_btn_selector);
			tvBottomRight.setText(builder.sureText);
		} else {
			tvBottomRight.setVisibility(View.GONE);
		}
		if (builder.isShowListView) {
			lvCommonDialog.setVisibility(View.VISIBLE);
		} else {
			lvCommonDialog.setVisibility(View.GONE);
		}
		if (builder.isShowBottom()) {
			ivBottomLine.setVisibility(View.VISIBLE);
		} else {
			ivBottomLine.setVisibility(View.GONE);
		}
		if (builder.isShowCancle && builder.isShowSure) {
			ivBottomMidLine.setVisibility(View.VISIBLE);
		} else {
			ivBottomMidLine.setVisibility(View.GONE);
		}
		if (builder.isShowCancle&&!builder.isShowSure) {
		
			tvBottomLeft.setBackgroundResource(R.drawable.mid_bottom_btn_selector);
		}
		if (!builder.isShowCancle&&builder.isShowSure) {
			
			tvBottomRight.setBackgroundResource(R.drawable.mid_bottom_btn_selector);
		}
		itemModels = builder.items;
		mAdapter = new DialogAdapter(context);
		mAdapter.setList(itemModels);
		lvCommonDialog.setAdapter(mAdapter);
		
	}

	@OnClick(R.id.tv_bottom_left)
	public void onCancleClick() {
		if (onButtonClicked != null) {
			onButtonClicked.onCancleButtonClick(this);
		}else{
			this.dismiss();
		}
	}
	@OnClick( R.id.tv_bottom_right )
	public void onSureClick() {
		if (onButtonClicked != null) {
			onButtonClicked.onSureButtonClick(this);
		}else{
			this.dismiss();
		}
	}
	

	class DialogAdapter extends BaseListAdapter<ItemDialogModel> {
		/**
		 * @Description:TODO
		 */
		public DialogAdapter(Context ctx) {
			super(ctx);
			// TODO Auto-generated constructor stub
		}
		/*
		 * @Description: TODO
		 */
		@Override
		protected ViewHolder createViewHolder(View root) {
			// TODO Auto-generated method stub
			return new MyViewHolder(root);
		}
		/*
		 * @Description: TODO
		 */
		@Override
		protected void fillView(View root, ItemDialogModel item,
				ViewHolder holder, int position) {
			if (item == null) {
				return;
			}
			MyViewHolder h = (MyViewHolder) holder;
			h.tvItemView.setText(item.getContent());
			h.tvItemView.setTextSize(item.getTextSize());
			h.tvItemView.setTextColor(item.getColor());
		}
		/*
		 * @Description: TODO
		 */
		@Override
		protected int getItemViewId() {
			// TODO Auto-generated method stub
			return R.layout.layout_textview;
		}

		class MyViewHolder extends ViewHolder {
			/**
			 * @Description:TODO
			 */
			public MyViewHolder(View view) {
				super(view);
				// TODO Auto-generated constructor stub
			}

			@Bind(R.id.tv_item_textview)
			TextView tvItemView;
		}
	}




	/*  
	 * 
	 * @Description: TODO
	 *  
	 */
	@Override
	public Object getCommonBuilder(Object t) {
		// TODO Auto-generated method stub
		return builder=(Builder) t;
	}

	/*  
	 * 
	 * @Description: TODO
	 *  
	 */
	@Override
	public void dealListener() {
		lvCommonDialog.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (onLvItemClickListener!=null) {
			onLvItemClickListener.onItemClick(view, position);
			}
			}
		});
		
	}


}
