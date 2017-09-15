package cn.vko.ring.im.session.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.session.constant.Extras;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;

import cn.shikh.utils.ImageCacheUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.okhttp.callback.BitmapCallback;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.classgroup.activity.GroupQinkeVideoActivity;
import cn.vko.ring.classgroup.activity.GroupVideoViewActivity;
import cn.vko.ring.classgroup.activity.LocalVideoActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.im.session.activity.lupin.LupinInfo;
import cn.vko.ring.im.session.extension.CourseAttachment;
import okhttp3.Call;


/**
 * desc:
 * Created by jiarh on 16/5/3 15:24.
 */
public class MsgViewHolderCourse extends MsgViewHolderBase implements View.OnClickListener {

    private ImageView courseIcon, courseTagIcon;
    private TextView courseName, courseType;
    private TextView courseSummary;
    private RelativeLayout couseButton;
    private CourseAttachment attachment;
    private static final String TAG = "MsgViewHolderCourse";

    private VolleyUtils<LupinInfo> volley;
    private Activity act;

    @Override
    protected int getContentResId() {
        return R.layout.chat_course;
    }

    @Override
    protected void inflateContentView() {

        courseIcon = (ImageView) view.findViewById(R.id.courseImg);
        courseName = (TextView) view.findViewById(R.id.courseTitle);
        courseTagIcon = (ImageView) view.findViewById(R.id.course_tag_icon);
        courseType = (TextView) view.findViewById(R.id.course_type);
        courseSummary = (TextView) view.findViewById(R.id.courseSummary);
        couseButton = (RelativeLayout) view.findViewById(R.id.courseButton);
        couseButton.setOnClickListener(this);

        volley = new VolleyUtils<LupinInfo>(context,LupinInfo.class);

    }

    private void getIsPlay() {
        Log.e("====走这里了","getisPlay");
        String url = ConstantUrl.VKOIP.concat("/lightCourse/getVideo");
        Uri.Builder builder = volley.getBuilder(url);
        builder.appendQueryParameter("token", VkoContext.getInstance(context).getToken() + "");
        builder.appendQueryParameter("videoId", attachment.getCourse().getObjId());
        builder.appendQueryParameter("taskId", attachment.getCourse().getTaskId());

        String groupId =  ((Activity)context).getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);

        builder.appendQueryParameter("targetId", groupId);
        volley.sendGETRequest(true,builder);

        volley.setUiDataListener(new UIDataListener<LupinInfo>() {
            @Override
            public void onDataChanged(LupinInfo info) {
                if (info != null) {
                    if (info.getData() != null) {
                        String playUrl=info.getData().getPlayUrl();
                        Log.e("====视频状态",info.getData().getVideoState());
                        Log.e("====区分轻课课程courseType",info.getData().getCourseType());

                        if (info.getData().getVideoState().equals("2")){
                            if (info.getData().getCourseType().equals("48")){
                                Bundle b = new Bundle();
                                b.putString("ID", attachment.getCourse().getObjId());
                                b.putInt("TYPE", attachment.getCourse().getType());
                                String groupId =  ((Activity)context).getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
                                b.putString("TASKID",attachment.getCourse().getTaskId() );
                                b.putString("GROUPID", groupId);
                                b.putString("TEACHERID", attachment.getCourse().getTeacherId());
                                StartActivityUtil.startActivity(context, GroupQinkeVideoActivity.class, b, Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            }else {
                                Bundle b = new Bundle();
                                b.putString("ID", attachment.getCourse().getObjId());
                                b.putInt("TYPE", attachment.getCourse().getType());
                                String groupId =  ((Activity)context).getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
                                b.putString("TASKID",attachment.getCourse().getTaskId() );
                                b.putString("GROUPID", groupId);
                                b.putString("TEACHERID", attachment.getCourse().getTeacherId());
                                StartActivityUtil.startActivity(context, GroupVideoViewActivity.class, b, Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            }
                        }else {
                            Bundle b = new Bundle();
                            b.putString("PLAYURL", info.getData().getPlayUrl());
                            StartActivityUtil.startActivity(context, LocalVideoActivity.class, b, Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        }
                    }
                }
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {
//                mAdapter.postNotifyDataSetChanged();
            }
        });
    }

    @Override
    protected void bindContentView() {
        attachment = (CourseAttachment) message.getAttachment();
        if (attachment != null && attachment.getCourse() != null) {
            Log.d(TAG, "bindContentView() called with: " + "");
            courseName.setText(attachment.getCourse().getTitle());
            courseSummary.setText(attachment.getCourse().getObjInfo());

            switch (attachment.getCourse().getType()) {

                case 3:
                    courseTagIcon.setVisibility(View.GONE);
                    courseType.setText(getTe() + "任务");
                    break;
                default:
                    courseType.setText(getTe() +"课程预习");
                    break;

            }

            if(!TextUtils.isEmpty(attachment.getCourse().getCourseCover()))
            ImageCacheUtils.getInstance().loadImage(attachment.getCourse().getCourseCover(), new BitmapCallback() {
                @Override
                public void onError(Call call, Exception e) {

                }

                @Override
                public void onResponse(Bitmap response) {
courseIcon.setImageBitmap(response);
                }
            });
        }



    }

    @NonNull
    private String getTe() {
        String tName=attachment.getCourse().getTeacherName();
        Log.d(TAG, "getTe() called with: " + tName+"");
        return TextUtils.isEmpty(tName)?"":attachment.getCourse().getTeacherName() + "老师 ";
    }

    @Override
    protected int leftBackground() {
        return R.drawable.white_rect;
    }

    @Override
    protected int rightBackground() {
        return R.drawable.blue_rect;
    }

    @Override
    public void onClick(View v) {


        if(attachment.getCourse() != null){
//            Log.e("=====TYPE",attachment.getCourse().getType()+"");
//            Log.e("=====getTaskId",attachment.getCourse().getTaskId()+"");
//            Log.e("=====getUrl",attachment.getCourse().getUrl()+"");
//            Log.e("=====courseType",attachment.getCourse().getCourseType()+"");

            if (attachment.getCourse().getType()==0){
                getIsPlay();
            }else {
                Bundle b = new Bundle();
                b.putString("ID", attachment.getCourse().getObjId());

                b.putInt("TYPE", attachment.getCourse().getType());
                String groupId =  ((Activity)context).getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
                b.putString("TASKID",attachment.getCourse().getTaskId() );
                b.putString("GROUPID", groupId);
                b.putString("TEACHERID", attachment.getCourse().getTeacherId());
                StartActivityUtil.startActivity(context, GroupVideoViewActivity.class, b, Intent.FLAG_ACTIVITY_SINGLE_TOP);


            }

            }

    }
}
