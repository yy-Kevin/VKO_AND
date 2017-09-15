package cn.vko.ring.study.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.shikh.utils.ImageCacheUtils;
import cn.shikh.utils.okhttp.callback.BitmapCallback;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.RoundAngleImageView;
import cn.vko.ring.study.model.Praise;
import cn.vko.ring.study.model.VideoCommentInfo;
import okhttp3.Call;

/**
 * Created by shikh on 2016/6/6.
 */
public class CourseCommentListAdapter extends BaseAdapter {
    protected List<VideoCommentInfo> list;
    private View emptyView;
    private boolean isEmpty;
    protected Context ctx;

    private Animation animation;
    private VolleyUtils<Praise> volleyPraise;

    public CourseCommentListAdapter(Context context){
        this.ctx = context;
        emptyView = View.inflate(context, R.layout.layout_empty_view, null);
        volleyPraise = new VolleyUtils<Praise>(ctx,Praise.class);
        animation = AnimationUtils.loadAnimation(ctx,
                R.anim.applaud_animation);
    }
    public void add(VideoCommentInfo t) {
        if (null == list) {
            list = new ArrayList<VideoCommentInfo>(0);
        }
        if(!list.contains(t)){
            list.add(t);
        }
    }
    public void add(VideoCommentInfo t, int index) {
        if (null == list) {
            list = new ArrayList<VideoCommentInfo>(0);
        }
        list.add(index, t);
    }
    public void add(List<VideoCommentInfo> t) {
        if (null == list) {
            list = new ArrayList<VideoCommentInfo>(0);
        }

        list.addAll(t);
    }
    public void clear() {
        if (null != list) {
            this.list.clear();
        }
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if (isEmpty){
            return 1;
        }
        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list != null && position > -1 && position < this.getCount()) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public VideoCommentInfo getListItem(int position) {
        if (position > -1 && list != null && position < list.size()) {
            return list.get(position);
        }
        return null;
    }
    @Override
    public View getView(int i, View root, ViewGroup viewGroup) {
        if (isEmpty) {
            emptyView.setLayoutParams(new AbsListView.LayoutParams(viewGroup.getWidth(), viewGroup.getHeight()));
            return emptyView;
        }
        root = View.inflate(ctx, R.layout.item_comments, null);

        final MyViewHolder h = new MyViewHolder();
        h.tv_comments = (TextView) root.findViewById(R.id.tv_comments);
        h.iv_student_photo = (RoundAngleImageView) root
                .findViewById(R.id.iv_student_photo);
        h.tv_date = (TextView) root.findViewById(R.id.tv_date);
        h.tv_student_name = (TextView) root.findViewById(R.id.tv_student_name);
        h.iv_video_like = (ImageView) root.findViewById(R.id.iv_video_like);
        h.iv_video_like_num = (TextView) root
                .findViewById(R.id.iv_video_like_num);
        h.tv_like_animation=(TextView) root.findViewById(R.id.tv_like_animation);
        h.layoutLike = (RelativeLayout) root.findViewById(R.id.layout_like);

        final VideoCommentInfo item = this.getListItem(i);
        if(item == null) return root;
        h.tv_comments.setText(item.getComment()+"");
        h.tv_date.setText(item.getCrTime());
        h.tv_student_name.setText(item.getUserName()+"");
        h.iv_video_like_num.setText(item.getUpCount()+"");
        if (item.isFlag()) {
            h.iv_video_like.setImageResource(R.drawable.class_video_s_praised);
        } else {
            h.iv_video_like.setImageResource(R.drawable.class_video_s_praise);
        }

//        loadImageView(h.iv_student_photo, item.getUsface(), 0, 0, 0);
        ImageCacheUtils.getInstance().loadImage(item.getUsface(), new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(Bitmap response) {
                if(response != null){
                    h.iv_student_photo.setImageBitmap(response);
                }
            }
        });

        h.layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!item.isFlag()) {
                    h.requestZan(item);
                } else {
                    Toast.makeText(ctx, "你已经点过赞了", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }



    class MyViewHolder {
        RoundAngleImageView iv_student_photo;
        TextView tv_student_name;
        TextView tv_date;
        TextView tv_comments;
        ImageView iv_video_like;
        TextView iv_video_like_num;
        TextView tv_like_animation;
        RelativeLayout layoutLike;

        public void requestZan(final VideoCommentInfo item) {
            String commenturl = ConstantUrl.VKOIP.concat("/course/commentZan");
            Log.e("地址", commenturl);
            Uri.Builder builder = volleyPraise.getBuilder(commenturl);
            // builder.appendQueryParameter("commentId", item.getCommentId() + "");
            builder.appendQueryParameter("commentId", item.getCommentId()+"");
            builder.appendQueryParameter("opt", 1 + "");
            builder.appendQueryParameter("token", VkoContext.getInstance(ctx).getUser().getToken());
            Log.e("点赞地址", builder.toString());
            volleyPraise.setUiDataListener(new UIDataListener<Praise>(){
                @Override
                public void onDataChanged(Praise response) {
                    if (response != null) {
                        if (!TextUtils.isEmpty(response.getData())) {
                            if ("-1".equals(response.getData())) {
                                Toast.makeText(ctx, "点赞失败", Toast.LENGTH_SHORT).show();
                            }else{
                                iv_video_like.setImageResource(R.drawable.class_video_s_praised);
                                item.setUpCount(item.getUpCount()+ 1);
                                iv_video_like_num.setText(item.getUpCount() + "");
                                System.out.print("num="+item.getUpCount());
                                DisplayAnimation(tv_like_animation);
                                item.setFlag(true);
                            }

                        }
                    }
                }

                @Override
                public void onErrorHappened(String errorCode, String errorMessage) {

                }
            });
            volleyPraise.sendGETRequest(false,builder);
        }
    }

    public void DisplayAnimation(final TextView textView) {
        textView.setVisibility(View.VISIBLE);
        textView.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                textView.setVisibility(View.GONE);
            }
        }, 1000);
    }


    public boolean judgeHasEmpty() {
        if (list == null || list.size() == 0) {
            isEmpty = true;
        } else {
            isEmpty = false;
        }
        return isEmpty ;

    }
}
