package cn.vko.ring.study.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.study.model.CourceInfo;
import cn.vko.ring.study.model.TopicInfo;
import cn.vko.ring.study.presenter.MyDownLoadCommons;

/**
 * Created by A on 2016/12/13.培优详情-课程列表适配器
 */
public class PeiyCourseListAdapter extends BaseListAdapter<CourceInfo> {
    private int checkedIndex = -1;
    private TopicInfo info;
    private MyDownLoadCommons myDownLoadCommons;


    public void setInfo(TopicInfo info) {

        this.info = info;
    }
    /**
     * @param ctx
     */
    public PeiyCourseListAdapter(Context ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
//		this.info = info;
    }
    @Override
    protected ViewHolder createViewHolder(View root) {
        // TODO Auto-generated method stub
        MyViewHolder h = new MyViewHolder(root);
        return h;
    }
    @Override
    protected void fillView(View root, final CourceInfo item, ViewHolder holder, final int position) {
        // TODO Auto-generated method stub
        final MyViewHolder h = (MyViewHolder) holder;
        if (item != null) {
            h.tvName.setText(item.getName());
            h.tvTime.setText(item.getStrTime());
            h.tvLong.setText(item.getDuration());
            h.tvState.setText(item.getStateTips());
            h.tvTeacher.setVisibility(View.VISIBLE);
            h.tvTeacher.setText(item.getTeacher());
            h.ivShow.setTag(item);
//			if (checkedIndex == position) {
//				h.layoutButtom.setVisibility(View.VISIBLE);
//				h.ivShow.setImageResource(R.drawable.class_main_b_listclose);
//			} else {
//				h.layoutButtom.setVisibility(View.GONE);
//				h.ivShow.setImageResource(R.drawable.class_main_b_listunfold);
//			}
        }
    }
    @Override
    protected int getItemViewId() {
        // TODO Auto-generated method stub
        return R.layout.item_peiy_video;
//        return R.layout.item_local_video;
    }

    class MyViewHolder extends BaseListAdapter.ViewHolder {
        /**
         * @param view
         */
        public MyViewHolder(View view) {
            super(view);
            // TODO Auto-generated constructor stub
        }

        @Bind(R.id.tv_pylist_time)
        public TextView tvTime;
        @Bind(R.id.tv_name)
        public TextView tvName;
        @Bind(R.id.tv_long)
        public TextView tvLong;
        @Bind(R.id.tv_teacher)
        public TextView tvTeacher;
        @Bind(R.id.tv_state)
        public TextView tvState;
        @Bind(R.id.iv_d)
        public ImageView ivShow;
    }
}