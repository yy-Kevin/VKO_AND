package cn.vko.ring.im.session.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.vko.ring.classgroup.activity.TaskDetailActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.im.session.activity.lupin.LupinInfo;
import cn.vko.ring.im.session.extension.CourseAttachment;
import cn.vko.ring.im.session.extension.TaskAttachment;
import okhttp3.Call;

/**
 * Created by A on 2017/3/23.
 */
public class MsgViewHolderTask extends MsgViewHolderBase implements View.OnClickListener {

    private ImageView courseIcon, courseTagIcon;
    private TextView courseName, courseType;
    private TextView courseSummary;
    private TaskAttachment attachment;
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
        courseIcon.setImageResource(R.drawable.course_task_default2);
        courseName = (TextView) view.findViewById(R.id.courseTitle);
        courseTagIcon = (ImageView) view.findViewById(R.id.course_tag_icon);
        courseType = (TextView) view.findViewById(R.id.course_type);
        courseSummary = (TextView) view.findViewById(R.id.courseSummary);
        courseName.setOnClickListener(this);

        volley = new VolleyUtils<LupinInfo>(context,LupinInfo.class);

    }

    @Override
    protected void bindContentView() {
        attachment = (TaskAttachment) message.getAttachment();
        if (attachment != null && attachment.getCourse() != null) {
            Log.d(TAG, "bindContentView() called with: " + "");
            Log.e(TAG, "bindContentView() called with: " + "");
            courseName.setText(attachment.getCourse().getTitle());
            courseSummary.setText(attachment.getCourse().getObjInfo());
            courseType.setText(attachment.getCourse().getCourseType());
            Log.e("=====courseType",attachment.getCourse().getCourseType()+"");
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
            Log.e("=====TYPE",attachment.getCourse().getType()+"");
            Log.e("=====getTaskId",attachment.getCourse().getTaskId()+"");
            Log.e("=====getUrl",attachment.getCourse().getUrl()+"");

            if (attachment.getCourse().getType()==0){
                Bundle b = new Bundle();

                String groupId =  ((Activity)context).getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
                b.putString("URL",attachment.getCourse().getUrl() );
                b.putString("TITLE", attachment.getCourse().getCourseName());
                StartActivityUtil.startActivity(context, TaskDetailActivity.class, b, Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }else {
                return;
            }

        }

    }
}
