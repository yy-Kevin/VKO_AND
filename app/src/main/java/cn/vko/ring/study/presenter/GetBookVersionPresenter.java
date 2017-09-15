/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: GetBookVersionPresenter.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.study.presenter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-12-1 下午5:10:15 
 * @version: V1.0   
 */
package cn.vko.ring.study.presenter;

import com.android.volley.VolleyError;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.IBookClickListener;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.model.Book;
import cn.vko.ring.common.model.BookData;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.BookSelectView;
import cn.vko.ring.study.model.ParamData;
import cn.vko.ring.utils.ACache;

/**
 * @ClassName: GetBookVersionPresenter
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-12-1 下午5:10:15
 */
public class GetBookVersionPresenter implements UIDataListener<BookData> {
	private Activity context;
	private VolleyUtils<BookData> mVolleyUtils;
	private ParamData paramData;
	private BookSelectView bsv;
	private LinearLayout mContainer;
	IBookClickListener bookClickListener;
	View bookBg;
	private TextView tvTitle;
	public static  String BOOK_DATA="bookData";
	private BookData mBookData;
	private ACache mACache;
	public GetBookVersionPresenter(Activity context, LinearLayout container, ParamData paramData,View bookBg) {
		super();
		this.context = context;
		this.paramData = paramData;
		this.mContainer = container;
		this.bookBg = bookBg;
		mACache = ACache.get(context);
		if (!TextUtils.isEmpty(VkoContext.getInstance(context).getUserId())) {
			BOOK_DATA =BOOK_DATA+VkoContext.getInstance(context).getUserId()+paramData.getBookVersionId();
		}
		initData();
	}
	/**
	 * @Title: initData
	 * @Description: TODO
	 * @return: void
	 */
	private void initData() {
		// TODO Auto-generated method stub
		if (mVolleyUtils == null) {
			mVolleyUtils = new VolleyUtils<BookData>(context,BookData.class);
		}
		mBookData = (BookData) mACache.getAsObject(BOOK_DATA);
		if (mBookData!=null) {
			addBookView(mBookData);
			return;
		}
		refreshData();
	}
	private void refreshData() {
		Uri.Builder b = mVolleyUtils.getBuilder(ConstantUrl.VK_BOOK_VERSION);
		b.appendQueryParameter("subjectId", paramData.getSubjectId());
		b.appendQueryParameter("gradeId", paramData.getGradeId());
		mVolleyUtils.setUiDataListener(this);
		mVolleyUtils.sendGETRequest(true,b);
	}

	private void addBookView(BookData response) {
		bsv = new BookSelectView(mContainer, response, context,paramData);
		bsv.setViewBg(bookBg);
		bsv.setBookClickListener(new IBookClickListener() {
			@Override
			public void onBookSelected(Book book) {
				if (bookClickListener!=null) {										
				bookClickListener.onBookSelected(book);
				clearCache();
				}
			}
		});
	}
	public void setBookClickListener(IBookClickListener bookClickListener) {
		this.bookClickListener = bookClickListener;
	}
	
	public void clearCache(){
		ACache.get(context).remove(BOOK_DATA);
	}
	public void toggleView(TextView tvTitle) {
		this.tvTitle=tvTitle;
		if (bsv!=null) {
			bsv.toggle(tvTitle);
		}
	}

	@Override
	public void onDataChanged(BookData response) {
		if (response!=null&&response.getData()!=null) {
			if (response.getData().size()==0) {
				return;
			}
			mACache.put(BOOK_DATA, response,ACache.TIME_DAY);
			addBookView(response);
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}
