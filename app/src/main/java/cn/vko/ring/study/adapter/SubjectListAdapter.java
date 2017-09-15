package cn.vko.ring.study.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.study.model.SubjectInfoCourse;
import cn.vko.ring.study.widget.ProgressBarView;

/**
 * Created by shikh on 2016/6/7.
 */
public class SubjectListAdapter extends BaseListAdapter<SubjectInfoCourse> {
    public Map<String,Integer> subjectMap = new HashMap<>();
    public SubjectListAdapter(Context ctx) {
        super(ctx);
        initMapView();
    }
    private void initMapView() {
        subjectMap.put("语文",R.drawable.subject_china);
        subjectMap.put("数学",R.drawable.subject_matheatics);
        subjectMap.put("英语",R.drawable.subject_endshish);
        subjectMap.put("物理",R.drawable.subject_physical);
        subjectMap.put("化学",R.drawable.subject_chemistry);
        subjectMap.put("生物",R.drawable.subject_biological);
        subjectMap.put("历史",R.drawable.subject_hostory);
        subjectMap.put("地理",R.drawable.subject_geograhy);
        subjectMap.put("政治",R.drawable.subject_political);
        subjectMap.put("专题",R.drawable.subject_special);
    }

    @Override
    protected ViewHolder createViewHolder(View root) {
        MyViewHolder holder = new MyViewHolder(root);
        return holder;
    }

    @Override
    protected void fillView(View root, SubjectInfoCourse info, ViewHolder holder, int position) {
        MyViewHolder h = (MyViewHolder) holder;
        if(info == null) return;
        // 科目名字
        if (!TextUtils.isEmpty(info.getName())) {
            h.tv_subject_name.setText(info.getName());
            h.ivSubject.setImageResource(subjectMap.get(info.getName()));
            h.tv_subject_name.setTextColor(ctx.getResources().getColor(R.color.text_show_color));
        }
        // 版本名字
        if (!TextUtils.isEmpty(info.getVname())) {
            h.tvVersionName.setVisibility(View.VISIBLE);
            h.tvVersionName.setText(info.getVname());
        } else {
           h.tvVersionName.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected int getItemViewId() {
        return R.layout.item_subject_select;
    }

    class MyViewHolder extends ViewHolder {
        @Bind(R.id.iv_subject_image)
       public ImageView ivSubject ;
        @Bind(R.id.tv_subject_name)
        public  TextView tv_subject_name ;
        @Bind(R.id.tv_verision_name)
        public TextView tvVersionName ;
        public MyViewHolder(View view) {
            super(view);

        }

    }
}
