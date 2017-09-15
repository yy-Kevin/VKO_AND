package cn.vko.ring.im.session.action;

import android.content.Intent;
import android.util.Log;

import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nim.uikit.session.constant.RequestCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;

import cn.vko.ring.R;
import cn.vko.ring.im.file.browser.FileBrowserActivity;
import cn.vko.ring.im.session.activity.LupinActivity;
import cn.vko.ring.im.session.activity.SendCourseActivity;
import cn.vko.ring.im.session.extension.CourseAttachment;
import cn.vko.ring.im.session.extension.CourseDataParam;
import cn.vko.ring.im.session.extension.CourseMsg;
import cn.vko.ring.im.session.extension.CustomAttachment;
import cn.vko.ring.im.session.extension.CustomAttachmentType;

/**
 * Created by A on 2016/12/22.
 */
public class LuPinAction extends BaseAction {

    private static final String TAG = "CourseAction";
    public LuPinAction() {
        super(R.drawable.im_lupin, R.string.text_lupin);
    }

    @Override
    public void onClick() {

        LupinActivity.startActivityForResult(getActivity(),makeRequestCode(RequestCode.GET_LUPIN),getSessionId());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.GET_LUPIN) {
            String path = data.getStringExtra(LupinActivity.EXTRA_VIDEO_PATH);
            String vid=data.getStringExtra(LupinActivity.LUPIN_VID);
            String biaoti=data.getStringExtra(LupinActivity.LUPIN_BT);
            String courseType=data.getStringExtra(LupinActivity.LUPIN_TP);
            String url=data.getStringExtra(LupinActivity.LUPIN_URL);
            String taskId=data.getStringExtra(LupinActivity.LUPIN_TASK_ID);
            CourseMsg msg=new CourseMsg();
            msg.setCourseName(biaoti);
            msg.setCourseId(vid);
            msg.setCourseType(courseType);
            msg.setUrl(url);
            msg.setTaskId(taskId);
//            msg.setType(1000);
            File file = new File(path);
//            Log.e("=====msg-vid",vid);
//            Log.e("=====msg-biaoti",biaoti);
//            Log.e("=====courseType",courseType);

            CourseAttachment attachment = new CourseAttachment(CustomAttachmentType.COURSE,msg);
//            IMMessage message = MessageBuilder.createFileMessage(getAccount(), getSessionType(), file, file.getName());
            IMMessage message=MessageBuilder.createCustomMessage(getAccount(),getSessionType(),attachment);
            sendMessage(message);
        }
    }
}
