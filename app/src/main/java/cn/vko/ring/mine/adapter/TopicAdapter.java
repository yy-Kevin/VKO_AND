package cn.vko.ring.mine.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import cn.vko.ring.R;
import cn.vko.ring.mine.model.CancleCollectEvent;
import cn.vko.ring.mine.model.FavoriteTopicInfo;
import cn.vko.ring.utils.ImageUtils;

public class TopicAdapter extends BaseAdapter {
	private List<FavoriteTopicInfo> data;
	private LayoutInflater layoutInflater;
	public List<FavoriteTopicInfo> selectedList;
	private ViewHolder viewHolder = null;
	public HashMap<Integer, Integer> visiblecheck;// 用来记录每个checkBox是否显示
	public boolean flag;// 是否为编辑状态

	public TopicAdapter(Context context, List<FavoriteTopicInfo> data,
			boolean flag) {
		this.data = data;
		this.flag = flag;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		selectedList = new ArrayList<FavoriteTopicInfo>();
		visiblecheck = new HashMap<Integer, Integer>();
		
	}


	public void setData(List<FavoriteTopicInfo> data) {
		this.data = data;
		setVisable(flag);
	}


	public void setVisable(boolean flag) {
		if (data!=null&&data.size()==0) {
			return;
		}
		if (flag) {
			for (int i = 0; i < data.size(); i++) {
				visiblecheck.put(i, CheckBox.VISIBLE);
			}
		} else {
			for (int i = 0; i < data.size(); i++) {
				visiblecheck.put(i, CheckBox.GONE);
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
			convertView = layoutInflater.inflate(R.layout.item_favorite_topic,
					parent, false);
			viewHolder = new ViewHolder();
			viewHolder.ivHead = (ImageView) convertView
					.findViewById(R.id.iv_head);
			viewHolder.tvName = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewHolder.tvGroup = (TextView) convertView
					.findViewById(R.id.tv_group);
			viewHolder.tvSummary = (TextView) convertView
					.findViewById(R.id.tv_summary);
			viewHolder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_time);
			viewHolder.cb = (CheckBox) convertView.findViewById(R.id.checkbox);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.cb.setVisibility(visiblecheck.get(position) == CheckBox.VISIBLE?View.VISIBLE:View.GONE);
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
		ImageUtils.loadPerviewImage(
				data.get(position).getHeadImg(), 100, 100, viewHolder.ivHead);
		viewHolder.tvName.setText(data.get(position).getUserName());
		viewHolder.tvGroup.setText("来自: " + data.get(position).getGroupName());
		viewHolder.tvSummary.setText(data.get(position).getSummary());
		viewHolder.tvTime.setText(data.get(position).getCrTime());
		return convertView;
	}

	class ViewHolder {
		ImageView ivHead;
		TextView tvName;
		TextView tvGroup;
		TextView tvSummary;
		TextView tvTime;
		CheckBox cb;
	}

}
