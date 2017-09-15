package cn.vko.ring.home.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.vko.ring.R;
import cn.vko.ring.home.model.Recommond;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareGvAdapter extends BaseAdapter {

	private Context mContext;
	private List<Recommond> mList = new ArrayList<Recommond>();

	public ShareGvAdapter(Context ctx, List<Recommond> mList) {
		this.mContext = ctx;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	MyViewHolder hodler;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			hodler =  new MyViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_share_gv, null);
			hodler.icon = (ImageView) convertView.findViewById(R.id.share_icon);
			hodler.title = (TextView) convertView.findViewById(R.id.share_title);
		convertView.setTag(hodler);
		}else{
			hodler =(MyViewHolder) convertView.getTag();
		}
		hodler.icon.setImageResource(mList.get(position).getImg());
		hodler.title.setText(mList.get(position).getTitle());
		return convertView;
	}

}

class MyViewHolder {
	ImageView icon;
	TextView title;
}
