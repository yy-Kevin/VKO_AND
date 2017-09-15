package cn.vko.ring.mine.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.xlv.XListView;
import cn.vko.ring.local.activity.LocalCourceDetailActivity;
import cn.vko.ring.mine.adapter.MineLocalAdaper;
import cn.vko.ring.mine.model.MineLocalCourseModel;
import cn.vko.ring.mine.model.MineLocalCourseModel.DataEntity.GoodsListEntity;

public class MineZtprestener {

	private Context context;
	
	private XListView lv;
	private MineLocalAdaper mAdapter;
	private List<GoodsListEntity> datas;
	private VolleyUtils<MineLocalCourseModel> mVolleyUtils;

	private int mCurrentPosition;

	public MineZtprestener(Context context, XListView lv) {
		super();
		this.context = context;
		this.lv = lv;
		
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mVolleyUtils = new VolleyUtils<MineLocalCourseModel>(context,MineLocalCourseModel.class);
		datas = new ArrayList<GoodsListEntity>();
		mAdapter  = new MineLocalAdaper(context);
		mAdapter.setList(datas);
		lv.setAdapter(mAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (mAdapter.judgeHasEmpty()) {
					getDatas(1,10);
					return;
				}
				mCurrentPosition = position;
				GoodsListEntity goods = mAdapter.getListItem(position-1);
				Bundle bundle = new Bundle();
				bundle.putString("TEACHERID",goods.getTeacherId());
				bundle.putSerializable("GOODSID", goods.getObjId());
				bundle.putString("TITLE", goods.getGoodsName());
				StartActivityUtil.startActivity(context, LocalCourceDetailActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
			}
		});
	}

	public void updatePriceStatePartly(){
		int position  =mCurrentPosition;
        int firstVisiblePosition = lv.getFirstVisiblePosition();
        int lastVisiblePosition = lv.getLastVisiblePosition();
        if(position>=firstVisiblePosition && position<=lastVisiblePosition){
            View view = lv.getChildAt(position - firstVisiblePosition);
            TextView tView  = (TextView) view.findViewById(R.id.tv_price);
            tView.setText("已购买");
         
        }
    }
	public void getDatas(final int pageIndex,int pageSize) {
		// TODO Auto-generated method stub
		
		Uri.Builder bbBuilder = mVolleyUtils.getBuilder(ConstantUrl.VK_MYCOURSE_LIST);
		bbBuilder.appendQueryParameter("courseType", "46");
		if(!VkoContext.getInstance(context).doLoginCheckToSkip(context))
		
		bbBuilder.appendQueryParameter("token", VkoContext.getInstance(context).getToken());
		bbBuilder.appendQueryParameter("pageIndex", pageIndex+"");
		bbBuilder.appendQueryParameter("pageSize", pageSize+"");
		mVolleyUtils.sendGETRequest(true,bbBuilder);
		mVolleyUtils.setUiDataListener(new UIDataListener<MineLocalCourseModel>() {
			@Override
			public void onDataChanged(MineLocalCourseModel response) {
				stopRefresh();
				if(response!=null){
					if (response.getData()!=null) {
						if (response.getData().getCourseList()!=null&&response.getData().getCourseList().size()>0) {
							if (pageIndex==1) {
								datas.clear();
							}
							lv.setPullLoadEnable(true);
							if(response.getData().getPager().getPageNo()==response.getData().getPager().getPageTotal()){
								lv.setPullLoadEnable(false);
							}

							datas.addAll(response.getData().getCourseList());
						}else{
							Toast.makeText(context, "暂无数据",Toast.LENGTH_SHORT).show();
						}
					}
				}
				mAdapter.judgeHasEmpty();
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {
				stopRefresh();
			}
		});

	
	}
	public void stopRefresh(){
		lv.stopRefresh();
		lv.stopLoadMore();

	}
	
}
