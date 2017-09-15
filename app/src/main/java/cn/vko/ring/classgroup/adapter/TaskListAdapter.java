package cn.vko.ring.classgroup.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.classgroup.model.TaskItemData;
import cn.vko.ring.common.base.BaseListAdapter;

/**
 * desc:
 * Created by jiarh on 16/5/13 11:26.
 */
public class TaskListAdapter extends BaseListAdapter<TaskItemData.TaskItem> {




    public TaskListAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    protected ViewHolder createViewHolder(View root) {
        return new MyViewHolder(root);
    }

    @Override
    protected void fillView(View root, TaskItemData.TaskItem item, ViewHolder holder, int position) {

        if (item == null) return;
        MyViewHolder h = (MyViewHolder) holder;
        h.leaderName.setText(item.getUserName());
        h.courseTitle.setText(item.getTitle());
        h.courseSummary.setText(item.getDescription());
        h.courseImg.setImageResource(R.drawable.course_task_default2);

        switch (item.getType()) {
            case 3:
                h.courseTagIcon.setVisibility(View.GONE);
                h.courseType.setText("任务");
                break;
        }

    }

    @Override
    protected int getItemViewId() {
        return R.layout.task_list_item;
    }

    class MyViewHolder extends ViewHolder {

        public MyViewHolder(View view) {
            super(view);
        }

        @Bind(R.id.head_iv)
        ImageView headIv;
        @Bind(R.id.leader_name)
        TextView leaderName;
        @Bind(R.id.course_tag_icon)
        ImageView courseTagIcon;
        @Bind(R.id.course_type)
        TextView courseType;
        @Bind(R.id.tag_course_lay)
        LinearLayout tagCourseLay;
        @Bind(R.id.courseImg)
        ImageView courseImg;
        @Bind(R.id.courseTitle)
        TextView courseTitle;
        @Bind(R.id.courseSummary)
        TextView courseSummary;
    }
}
