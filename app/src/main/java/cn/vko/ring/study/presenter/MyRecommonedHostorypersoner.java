package cn.vko.ring.study.presenter;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.VolleyError;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.study.model.BaseHotRecommoned;
import cn.vko.ring.study.model.HotRecommoned;
import cn.vko.ring.study.widget.MyViewGroup;

public class MyRecommonedHostorypersoner implements UIDataListener<BaseHotRecommoned> {
	private LinearLayout layout_recommend;
	public Context context;
	MyViewGroup fl;
	private VolleyUtils<BaseHotRecommoned> volley;
	
	public MyRecommonedHostorypersoner(LinearLayout layout_recommend,
			Context context) {
		super();
		this.layout_recommend = layout_recommend;
		this.context = context;
		volley = new VolleyUtils<BaseHotRecommoned>(context,BaseHotRecommoned.class);
		initRequest();		
	}
	 
	private void initRequest() {
		String vuri = ConstantUrl.VKOIP.concat("/s/topSearch");
		Uri.Builder builder = volley.getBuilder(vuri);	
		
		if(VkoContext.getInstance(context).getUser()!=null){
			builder.appendQueryParameter("learnId", VkoContext.getInstance(context).getUser().getLearn()+ "");
		}else{
			builder.appendQueryParameter("learnId", 51+ "");
		}			
		builder.appendQueryParameter("pageIndex", 1 + "");
		builder.appendQueryParameter("pageSize", 10 + "");
		volley.setUiDataListener(this);
		volley.sendGETRequest(true,builder);
	}

	private void initData(List<HotRecommoned> list) {
	//	int horizontalSpacing = 30;
		layout_recommend.removeAllViews();
		fl = new MyViewGroup(context);

		for (int i = 0; i < list.size(); i++) {
			View view = LayoutInflater.from(context).inflate(R.layout.item_recommoned, null);
			view.setLeft(3);
			TextView tv = (TextView) view.findViewById(R.id.tv_recommoned);
			tv.setPadding(30, 10, 20, 10);
			tv.setTextColor(context.getResources().getColor(
					R.color.text_system_color));
			tv.setBackgroundResource(R.drawable.hostory_bookbg);			
			tv.setText(list.get(i).getKeyWord());				
			final HotRecommoned hotRecommoned=list.get(i);
			tv.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					if (iHostorySeacherListener != null) {
						iHostorySeacherListener.hostorySeacher(hotRecommoned);
					}
				}
			});
			fl.addView(view);		
			Log.e("---------", i + "");

		}
		layout_recommend.setOrientation(LinearLayout.HORIZONTAL);
		layout_recommend.setGravity(Gravity.CENTER);
		layout_recommend.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);	
		
		layout_recommend.addView(fl);

	}



	public interface IHostorySeacherListener {	
		void hostorySeacher(HotRecommoned hotRecommoned);
	}

	public IHostorySeacherListener iHostorySeacherListener;

	public void setiHostorySeacherListener(
			IHostorySeacherListener iHostorySeacherListener) {
		this.iHostorySeacherListener = iHostorySeacherListener;
	}


	@Override
	public void onDataChanged(BaseHotRecommoned response) {
		if(response!=null){
			List<HotRecommoned> listRecommoned=response.getData();
			if(listRecommoned!=null){
				initData(listRecommoned);
			}
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}

	

}
