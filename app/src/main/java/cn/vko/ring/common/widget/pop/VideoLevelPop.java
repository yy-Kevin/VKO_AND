package cn.vko.ring.common.widget.pop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.easefun.polyvsdk.BitRateEnum;

import cn.vko.ring.R;
import cn.vko.ring.common.base.BasePopuWindow;
import cn.vko.ring.common.listener.IOnClickItemListener;


/**
 * @author shikh
 *
 */
public class VideoLevelPop extends BasePopuWindow {

	private ListView mListView;
	private LevelListAdapter mAdapter;
	private IOnClickItemListener<BitRateEnum> litener;
	
	/**
	 * @param context
	 */
	public VideoLevelPop(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}
	public void setOnClickItemListener(IOnClickItemListener<BitRateEnum> litener){
		this.litener = litener;
	}
	public void setListData(List<BitRateEnum>levels){
		if(levels == null) return;
		if(mAdapter == null){
			mAdapter = new LevelListAdapter(context);
			mListView.setAdapter(mAdapter);
		}
		mAdapter.clear();
		mAdapter.add(levels);
		mAdapter.notifyDataSetChanged();
	}
	@Override
	public void setUpViews(View contentView) {
		// TODO Auto-generated method stub
		mListView = (ListView) contentView.findViewById(R.id.listview);
	}

	@Override
	public void setUpListener() {
		// TODO Auto-generated method stub
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				mAdapter.setIndex(position);
				mAdapter.notifyDataSetChanged();
				if(litener != null){
					litener.onItemClick(position, mAdapter.getListItem(position), view);
				}
			}
		});
	}
	
	

	@Override
	public int getAnimationStyle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResView() {
		// TODO Auto-generated method stub
		return 	R.layout.pop_level_list;
	}

	@Override
	public boolean updateView(View contentView) {
		// TODO Auto-generated method stub
		return false;
	}

}
