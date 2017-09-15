/*
 *CourseTreeAdapter.java [V 1.0.0]
 *classes : cn.vko.ring.mine.adapter.CourseTreeAdapter
 *宣义阳 Create at 2015-8-6 上午10:40:15
 */
package cn.vko.ring.mine.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.vko.ring.R;
import cn.vko.ring.common.widget.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import cn.vko.ring.mine.model.SubTitleInfo.DataCourse.Course;
import cn.vko.ring.mine.model.TreeCourseInfo;

/**
 * cn.vko.ring.mine.adapter.CourseTreeAdapter
 * @author 宣义阳 
 * create at 2015-8-6 上午10:40:15
 */
public class CourseTreeAdapter extends AnimatedExpandableListAdapter {

	private List<Course> mKnowModels ;
	private Context mContext;
	private LayoutInflater inflater;
	public CourseTreeAdapter(List<Course> mKnowModels, Context mContext){
		this.mKnowModels = mKnowModels;
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mKnowModels.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return mKnowModels.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		
		return mKnowModels.get(groupPosition).getSection().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ParentViewHolder parentViewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.exter_item_parent, parent, false);
			parentViewHolder = new ParentViewHolder();
			parentViewHolder.knowTitle = (TextView) convertView.findViewById(R.id.tv_parent_name);
			parentViewHolder.parentIcon = (ImageView) convertView.findViewById(R.id.tree_parent_icon);
			convertView.setTag(parentViewHolder);
		} else {
			parentViewHolder = (ParentViewHolder) convertView.getTag();
		}
		if (isExpanded) {
			parentViewHolder.parentIcon.setBackgroundResource(R.drawable.class_main_b_close);
		}else{
			parentViewHolder.parentIcon.setBackgroundResource(R.drawable.class_main_b_unfold);
			
		}
		parentViewHolder.knowTitle.setText(mKnowModels.get(groupPosition).getName());
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		
		return true;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ItemViewHolder itemViewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.exter_item_son, parent, false);
			itemViewHolder = new ItemViewHolder();
			itemViewHolder.knowTitle = (TextView) convertView.findViewById(R.id.tv_son_name);
			itemViewHolder.itemIcon = (ImageView) convertView.findViewById(R.id.tree_icon);
			convertView.setTag(itemViewHolder);
		} else {
			itemViewHolder = (ItemViewHolder) convertView.getTag();
		}
		itemViewHolder.knowTitle
				.setText(mKnowModels.get(groupPosition).getSection().get(childPosition).getSectionName());
		itemViewHolder.itemIcon.setVisibility(View.INVISIBLE);
		return convertView;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		
		return mKnowModels.get(groupPosition).getSection().size();
	}
	 class ParentViewHolder {
			TextView knowTitle;
			ImageView parentIcon;
		}

		 class ItemViewHolder {
			TextView knowTitle;
			ImageView itemIcon;
		}
}
