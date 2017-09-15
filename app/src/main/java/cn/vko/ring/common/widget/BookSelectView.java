/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: BookSelectView.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.common.widget 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-27 下午4:26:10 
 * @version: V1.0   
 */
package cn.vko.ring.common.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import cn.shikh.utils.ViewUtils;
import cn.vko.ring.R;
import cn.vko.ring.common.listener.IBookClickListener;
import cn.vko.ring.common.model.Book;
import cn.vko.ring.common.model.BookData;
import cn.vko.ring.common.model.BookModel;
import cn.vko.ring.study.model.ParamData;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * @ClassName: BookSelectView
 * @Description: 版本 及 书本的选择
 * @author: JiaRH
 * @date: 2015-11-27 下午4:26:10
 */
public class BookSelectView {
	private int RIGHT_PANDING = 12;
	private int LEFT_P_PANDING = 15;
	private int BOTTOM_P_PANDING = 20;
	private LinearLayout mContainer;
	private LinearLayout mVersionContainer;
	private LinearLayout mBookContainer;
	private LinearLayout viewVersionContainer;
	private LinearLayout viewBookContainer;
	private BookData data;
	private Activity context;
	private IBookClickListener bookClickListener;
	private List<BookView> versionBookList;
	private List<BookView> vBookList;
	private boolean isOpen = false;
	private View viewBg;
	private int density;
	ParamData paramData;
	private BookModel tempBookModel;
	private Drawable dewUp, dewDown;
	
	private TextView tvTitle2;
	public BookSelectView(LinearLayout mContainer, BookData data, Activity context, ParamData paramData) {
		super();
		this.mContainer = mContainer;
		this.data = data;
		this.context = context;
		this.paramData = paramData;		
		initDrawable();
		initView();
	}
	private void initDrawable() {
		dewDown = context.getResources().getDrawable(R.drawable.title_drawabledown);
		dewUp = context.getResources().getDrawable(R.drawable.title_drawableup);
	}
	
	public void toggle(TextView tvTitle2) {
		this.tvTitle2=tvTitle2;
		isOpen = !isOpen;
		if (isOpen) {
			showView();
			switchDrawable(tvTitle2, dewUp);
		} else {
			closeView();
			switchDrawable(tvTitle2, dewDown);
		}
	}
	
	
	public void switchDrawable(TextView tv, Drawable drawable) {
		if (drawable == null) {
			return;
		}
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
		tv.setCompoundDrawables(null, null, drawable, null);// 画在右边
	}
	
	
	public void showView() {
		isOpen = true;
		ObjectAnimator oo = ObjectAnimator.ofFloat(mContainer, "translationY", -mContainer.getHeight(), 0);
		oo.setDuration(300);
		oo.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO 在动画初始化完成，开始动画时再设置显示隐藏，避免第一次点击闪一下
				mContainer.setVisibility(View.VISIBLE);
				viewBg.setVisibility(View.VISIBLE);
			}
			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onAnimationEnd(Animator arg0) {
			}
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
			}
		});
		oo.start();
	}
	public void closeView() {
		isOpen = false;
		ObjectAnimator oo = ObjectAnimator.ofFloat(mContainer, "translationY", 0, -mContainer.getHeight());
		oo.setDuration(300);
		oo.start();
		viewBg.setVisibility(View.INVISIBLE);
		switchDrawable(tvTitle2, dewDown);
	}
	/**
	 * @Title: initView
	 * @Description: TODO
	 * @return: void
	 */
	private void initView() {
		density = (int) ViewUtils.getScreenDensity(context);
		RIGHT_PANDING = RIGHT_PANDING * density;
		LEFT_P_PANDING = LEFT_P_PANDING * density;
		BOTTOM_P_PANDING = BOTTOM_P_PANDING * density;
		mVersionContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_book_select_view, null,
				false);
		viewVersionContainer = (LinearLayout) mVersionContainer.findViewById(R.id.book_container);
		mBookContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_book_select_view, null,
				false);
		mVersionContainer.setPadding(LEFT_P_PANDING, BOTTOM_P_PANDING, 0, BOTTOM_P_PANDING);
		mBookContainer.setPadding(LEFT_P_PANDING, 0, 0, BOTTOM_P_PANDING);
		viewBookContainer = (LinearLayout) mBookContainer.findViewById(R.id.book_container);
		versionBookList = new ArrayList<BookView>();
		vBookList = new ArrayList<BookView>();
		addVersionView();
	}
	public void addVersionView() {
		if (data == null || data.getData().size() == 0) {
			return;
		}
		viewVersionContainer.removeAllViews();
		TextView title = (TextView) mVersionContainer.findViewById(R.id.book_or_version_name);
		title.setText("选择版本");
		for (final BookModel version : data.getData()) {
			final BookView bv = new BookView(context);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.rightMargin = RIGHT_PANDING;
			params.bottomMargin = RIGHT_PANDING;
			bv.setLayoutParams(params);
			bv.setText(version.getVersionName());
			if (isSetVersion(version)) {
				version.setSelected(true);
				bv.setChecked(true);
				tempBookModel = version;
			}
			bv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					addBookVersionAndBook(version, bv);
				}
			});
			versionBookList.add(bv);
			viewVersionContainer.addView(bv);
		}
		mContainer.removeAllViews();
		mContainer.addView(mVersionContainer);
		if (isSetVersion(tempBookModel)) {
			addBookView(tempBookModel);
		}
	}
	private boolean isSetVersion(final BookModel version) {
		if (version == null || TextUtils.isEmpty(paramData.getBookVersionId())
				|| TextUtils.isEmpty(version.getVersionId())) {
			return false;
		}
		return version.getVersionId().equals(paramData.getBookVersionId());
	}
	private void addBookVersionAndBook(final BookModel version, final BookView bv) {
		addBookView(version);
		resetBv(versionBookList);
		bv.setChecked(true);
	}
	private void resetBv(List<BookView> bvs) {
		for (BookView bv : bvs) {
			bv.setChecked(false);
		}
	}
	public void addBookView(BookModel bookModel) {
		if (bookModel == null || bookModel.getBookList().size() == 0) {
			return;
		}
		TextView title = (TextView) mBookContainer.findViewById(R.id.book_or_version_name);
		title.setText("选择书本");
		viewBookContainer.removeAllViews();
		for (final Book book : bookModel.getBookList()) {
			final BookView bv = new BookView(context);	
			
			ParamData pd = new ParamData.Builder(context).setBookVersionId(bookModel.getVersionId()).build();
			book.setParamData(pd);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.rightMargin = RIGHT_PANDING;
			params.bottomMargin = RIGHT_PANDING;
			bv.setLayoutParams(params);
			bv.setText(book.getBookname());
			if (book.getBookid().equals(paramData.getBookId())) {
				book.setSelected(true);
				bv.setChecked(book.isSelected());
			}
			bv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					resetBv(vBookList);
					bv.setChecked(true);
					if (bookClickListener != null) {
						bookClickListener.onBookSelected(book);
					}
					closeView();
				}
			});
			vBookList.add(bv);
			viewBookContainer.addView(bv);
		}
		int count = mContainer.getChildCount();
		if (count == 2) {
			mContainer.removeViewAt(1);
		}
		mContainer.addView(mBookContainer);
	}
	public void setBookClickListener(IBookClickListener bookClickListener) {
		this.bookClickListener = bookClickListener;
	}
	/**
	 * @Title: setViewBg
	 * @Description: 透明背景点击设置
	 * @param viewBg
	 * @return: void
	 */
	public void setViewBg(View viewBg) {
		this.viewBg = viewBg;
		viewBg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closeView();
			}
		});
	}
	
}
