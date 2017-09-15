package cn.vko.ring.classgroup.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.im.session.extension.CourseMsg;

/**
 * desc:
 * Created by jiarh on 16/5/12 10:54.
 */
public class CourseListAdapter extends BaseListAdapter<CourseMsg> {


    public CourseListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected ViewHolder createViewHolder(View root) {
        return new MyViewHolder(root);
    }

    @Override
    protected void fillView(View root, CourseMsg item, BaseListAdapter.ViewHolder holder, int position) {

        if (item==null)return;

        MyViewHolder h = (MyViewHolder) holder;
        h.courseTitle.setText(item.getCourseName());
        h.courseSummary.setText(item.getCourseDesc());
        if (item.isChecked()){
            h.checkIv.setImageResource(R.drawable.checkbox_checked);
        }else{
            h.checkIv.setImageResource(R.drawable.checkbox_normal);
        }

    }

    public void upDateItem(ListView listView,int position,CourseMsg courseMsg){
        View view = listView.getChildAt(position- listView.getFirstVisiblePosition());
        MyViewHolder h = (MyViewHolder) view.getTag();
        if (courseMsg.isChecked())
        h.checkIv.setImageResource(R.drawable.checkbox_checked);
        else
            h.checkIv.setImageResource(R.drawable.checkbox_normal);
    }

    @Override
    protected int getItemViewId() {
        return R.layout.course_list_item_view;
    }

   public class MyViewHolder extends BaseListAdapter.ViewHolder {

        public MyViewHolder(View view) {
            super(view);
        }


        @Bind(R.id.course_tag_icon)
        ImageView courseTagIcon;
        @Bind(R.id.tag_course_lay)
        LinearLayout tagCourseLay;
        @Bind(R.id.courseImg)
        ImageView courseImg;
        @Bind(R.id.courseTitle)
        TextView courseTitle;
        @Bind(R.id.courseSummary)
        TextView courseSummary;
        @Bind(R.id.checkIv)
        ImageView checkIv;

    }
}
