package cn.vko.ring.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.vko.ring.R;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.home.model.BaseResultInfo;
import cn.vko.ring.mine.model.CancleCollectEvent;
import cn.vko.ring.mine.model.FavoriteVideoInfo;

public class VideoAdapter extends BaseAdapter {
	private List<FavoriteVideoInfo> data;
	private LayoutInflater layoutInflater;
	public List<FavoriteVideoInfo> selectedList;
	private ViewHolder viewHolder = null;
	public HashMap<Integer, Integer> visiblecheck;// 用来记录每个checkBox是否显示
	public boolean flag;// 是否为编辑状态
	private VolleyUtils<BaseResultInfo> mVolleyUtils;
	RequestQueue mQueue;

	public VideoAdapter(Context context, List<FavoriteVideoInfo> data,
			boolean flag) {
		this.data = data;
		this.flag = flag;
		mVolleyUtils = new VolleyUtils<BaseResultInfo>(context,BaseResultInfo.class);
		 mQueue = Volley.newRequestQueue(context);  
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		selectedList = new ArrayList<FavoriteVideoInfo>();
		visiblecheck = new HashMap<Integer, Integer>();
	
	}

	public void setData(List<FavoriteVideoInfo> data) {
		this.data = data;
		setVisable(flag);
	}

	public void setVisable(boolean flag) {
		if (flag) {
			for (int i = 0; i < data.size(); i++) {
				visiblecheck.put(i, CheckBox.VISIBLE);
			}
		} else {
			for (int i = 0; i < data.size(); i++) {
				visiblecheck.put(i, CheckBox.INVISIBLE);
			}
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_favorite_video,
					parent, false);
			viewHolder = new ViewHolder();
			viewHolder.ivVideo = (ImageView) convertView
					.findViewById(R.id.iv_video);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_title);
			viewHolder.ivHead = (ImageView) convertView
					.findViewById(R.id.iv_head);
			viewHolder.tvName = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.cb = (CheckBox) convertView.findViewById(R.id.checkbox);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.cb.setVisibility(visiblecheck.get(position)== CheckBox.VISIBLE?View.VISIBLE:View.GONE);
		viewHolder.cb.setChecked(data.get(position).isChecked());
		viewHolder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (!buttonView.isPressed())
					return;
				if (isChecked) {
					data.get(position).setChecked(true);
					selectedList.add(data.get(position));
				} else {
					data.get(position).setChecked(false);
					selectedList.remove(data.get(position));
				}
				CancleCollectEvent event = new CancleCollectEvent();
				event.setType(1);
				EventBus.getDefault().post(event);
			}
		});
//		ImageUtils.getInstance().loadPerviewImage(
//				data.get(position).getImage(), 100, 100, viewHolder.ivVideo);
		if (!TextUtils.isEmpty(data.get(position).getImage())) {
			ImageListener listener = ImageLoader.getImageListener(viewHolder.ivVideo,
					R.drawable.tcourse, R.drawable.tcourse);
			
			mVolleyUtils.getImageLoader().get(data.get(position).getImage(), listener,100,100);
			
		} else {
			viewHolder.ivVideo.setImageResource(R.drawable.tcourse);
		}
		
		viewHolder.tvTime.setText(data.get(position).getDuration());
		viewHolder.tvTitle.setText(data.get(position).getVideoName());
		if (!TextUtils.isEmpty(data.get(position).getTimage())) {
			ImageListener listener = ImageLoader.getImageListener(viewHolder.ivHead,
					0,0);
			
			mVolleyUtils.getImageLoader().get(data.get(position).getTimage(), listener,30,30);
			
		} 
		
//		ImageUtils.getInstance().loadPerviewImage(
//				data.get(position).getTimage(), 100, 100, viewHolder.ivHead);
		viewHolder.tvName.setText(data.get(position).getTName());
		return convertView;
	}

	class ViewHolder {
		ImageView ivVideo;
		TextView tvTime;
		TextView tvTitle;
		ImageView ivHead;
		TextView tvName;
		CheckBox cb;
	}

}
