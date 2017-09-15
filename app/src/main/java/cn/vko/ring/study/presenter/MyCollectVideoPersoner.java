package cn.vko.ring.study.presenter;

import com.android.volley.VolleyError;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.study.model.BaseCollect;
import cn.vko.ring.study.model.BaseUnitData;
import cn.vko.ring.study.model.CancleCollectEvent;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class MyCollectVideoPersoner implements OnClickListener,UIDataListener<BaseCollect> {
	private Context context;
	private ImageView iv_collect;
	private VolleyUtils<BaseCollect> volley;
	public BaseUnitData tUnitData;	
	public MyCollectVideoPersoner(Context context, BaseUnitData tUnitData,ImageView iv_collect) {
		super();
		this.context = context;
		this.tUnitData = tUnitData;		
		this.iv_collect=iv_collect;
		volley=new VolleyUtils<BaseCollect>(context,BaseCollect.class);
		iv_collect.setOnClickListener(this);
		initView();
		
	}
   
	private void initView() {
		
		if(tUnitData.getVideo() != null && tUnitData.getVideo().getIsStore()){
			iv_collect.setImageResource(R.drawable.collect_video_success);			
		}else{
			iv_collect.setImageResource(R.drawable.collect_video);
		}
		
	}

	@Override
	public void onClick(View v) {
		if(tUnitData.getVideo() == null) return;
		if(tUnitData.getVideo().getIsStore()){			
			//取消收藏
			cancelStoreVideo();
		}else{
			//添加收藏
			initData();		
		}		
	}
	
	private void initData() {		
		String commenturl = ConstantUrl.VKOIP.concat("/store/storeVideo");
		Uri.Builder builder = volley.getBuilder(commenturl);
		builder.appendQueryParameter("token", VkoContext.getInstance(context).getToken());		
		builder.appendQueryParameter("videoId", tUnitData.getVideo().getVideoId()+"");
		Log.e("video收藏地址", builder.toString());
		volley.setUiDataListener(this);
		volley.sendGETRequest(false,builder);
	}
	
	
	//取消收藏
	private void cancelStoreVideo() {		
		String commenturl = ConstantUrl.VKOIP.concat("/store/cancelStoreVideo");
		Uri.Builder builder = volley.getBuilder(commenturl);
		builder.appendQueryParameter("token", VkoContext.getInstance(context).getToken());		
		builder.appendQueryParameter("videoId", tUnitData.getVideo().getVideoId()+"");
		Log.e("取消video收藏地址", builder.toString());
		volley.sendGETRequest(false,builder);
		volley.setUiDataListener(new UIDataListener<BaseCollect>() {
			@Override
			public void onDataChanged(BaseCollect response) {
				if(response.getData()){
					Toast.makeText(context, "取消收藏", Toast.LENGTH_SHORT).show();
					iv_collect.setImageResource(R.drawable.collect_video);
					tUnitData.getVideo().setIsStore(false);
					CancleCollectEvent event = new CancleCollectEvent();
					event.setId(tUnitData.getVideo().getVideoId());
					EventBus.getDefault().post(event);
				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {

			}
		});
	}

	@Override
	public void onDataChanged(BaseCollect response) {
		if(response!=null){
			if(response.getData()){
				Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
				iv_collect.setImageResource(R.drawable.collect_video_success);
				tUnitData.getVideo().setIsStore(true);
			}
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}
}
