/**   
 * Copyright © 2015 cn.vko.com. All rights reserved.
 * 
 * @Title: RecommondKnowledgePresenter.java 
 * @Prject: VkoCircleS
 * @Package: cn.vko.ring.study.presenter 
 * @Description: TODO
 * @author: JiaRH   
 * @date: 2015-11-25 下午2:13:52 
 * @version: V1.0   
 */
package cn.vko.ring.study.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;

import cn.shikh.utils.ViewUtils;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.study.model.KnowledgePointK1;
import cn.vko.ring.study.model.KnowledgeRecommond;
import cn.vko.ring.study.model.ParamData;
import cn.vko.ring.study.widget.FlowLayout;
import cn.vko.ring.study.widget.RefreshView;
import cn.vko.ring.utils.ACache;


/**
 * 推荐知识点处理
 * @ClassName: RecommondKnowledgePresenter
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-25 下午2:13:52
 */
public class RecommondKnowledgePresenter  implements UIDataListener<KnowledgeRecommond>{
	private FlowLayout mFlowLay;
	private Context context;
	RefreshView rv;
	private  int REFRESH_ID = 0;
	private VolleyUtils<KnowledgeRecommond> mVolleyUtils;
	private static final String KNOWLEDGE_RECOM = "KnowledgeRecommond";
	KnowledgeRecommond mKnowledgeRecommond;
	List<KnowledgePointK1> dataK1;
	boolean isFirstAdd = true;
	private ParamData mParamData;
	// private TextView reommonTv;
	public RecommondKnowledgePresenter(FlowLayout mFlowLay, ParamData paramData, Context context) {
		super();
		this.mFlowLay = mFlowLay;
		this.context = context;
		this.mParamData = paramData;
		initData();
	}
	/**
	 * @Title: initData
	 * @Description: TODO
	 * @return: void
	 */
	private void initData() {
		dataK1 = new ArrayList<KnowledgePointK1>();
		if (mVolleyUtils == null) {
			mVolleyUtils = new VolleyUtils<KnowledgeRecommond>(context,KnowledgeRecommond.class);
		}
		refreshData();
	}
	public void clear() {
		if (mFlowLay != null) {
//			mFlowLay.removeAllViews();
		}
	}
	/**
	 * @Title: refreshData
	 * @Description: TODO
	 * @return: void
	 */
	public void refreshData() {
		mKnowledgeRecommond = (KnowledgeRecommond) ACache.get(context).getAsObject(KNOWLEDGE_RECOM);
		if (mKnowledgeRecommond != null && mKnowledgeRecommond.getData().size() > 0) {
			addData(mKnowledgeRecommond.getData());
		}
		Uri.Builder b = mVolleyUtils.getBuilder(ConstantUrl.VK_COURSE_RECOMMONED);
		b.appendQueryParameter("token", mParamData.getToken());
		mVolleyUtils.setUiDataListener(this);
		mVolleyUtils.sendGETRequest(true,b);
	}
	/**
	 * @Title: addData
	 * @Description: TODO
	 * @return: void
	 */
	private void addData(final List<KnowledgePointK1> datas) {
		List<KnowledgePointK1> kp = sort(datas);
		if (mFlowLay == null || kp == null) {
			return;
		}
		if (isFirstAdd) {
			isFirstAdd = false;
			LayoutParams params = mFlowLay.getLayoutParams();
			params.height = ViewUtils.getScreenDensity(context) > 1.5f ? params.height: params.height + 5;
			mFlowLay.setLayoutParams(params);
		}
		MarginLayoutParams p = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int space = (int) context.getResources().getDimension(R.dimen.dimen10);
		p.rightMargin = space;
		p.topMargin = space;
		mFlowLay.removeAllViews();
		for (int i = 0; i < kp.size(); i++) {
			final KnowledgePointK1 k1 = kp.get(i);
			TextView reommonTv = (TextView) View.inflate(context, R.layout.layout_recommon_tv, null);
			reommonTv.setLayoutParams(p);
			reommonTv.setText(k1.getName().substring(0, k1.getName().length() > 10 ? 10 : k1.getName().length()));
			reommonTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					goDetail(k1);
				}
			});
			reommonTv.setTag(FlowLayout.TAG_TV);
			mFlowLay.addView(reommonTv);
		}
		rv = new RefreshView(context);
		rv.setTag(FlowLayout.TAG_REFRESH);
		rv.setId(REFRESH_ID);
		rv.setLayoutParams(p);
		rv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rv.startAni();
				ACache.get(context).remove(KNOWLEDGE_RECOM);
				refreshData();
			}
		});
		mFlowLay.addView(rv);
	}
	private void goDetail(KnowledgePointK1 k1) {
		if (iKnowlistener != null) {
			iKnowlistener.onClickKnowleg(k1);
		}
	}

	@Override
	public void onDataChanged(KnowledgeRecommond response) {
		if (response != null && response.getData() != null) {
			if (response.getData().size() < 0) {
				return;
			}
			ACache.get(context).put(KNOWLEDGE_RECOM, response);
			addData(response.getData());
		}
		stopAni();
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {
		stopAni();
	}

	public interface IKnowlegeListener {
		void onClickKnowleg(KnowledgePointK1 k1);
	}

	private IKnowlegeListener iKnowlistener;

	public void setiKnowlistener(IKnowlegeListener iKnowlistener) {
		this.iKnowlistener = iKnowlistener;
	}

	private void stopAni() {
		if (rv != null) {
			rv.stopAni();
		}
	}

	private List<KnowledgePointK1> sort(List<KnowledgePointK1> datas) {
		Collections.sort(datas, new Comparator<KnowledgePointK1>() {
			@Override
			public int compare(KnowledgePointK1 lhs, KnowledgePointK1 rhs) {
				// TODO Auto-generated method stub
				return lhs.getName().length() - rhs.getName().length();
			}
		});
		return datas;
	}
}
