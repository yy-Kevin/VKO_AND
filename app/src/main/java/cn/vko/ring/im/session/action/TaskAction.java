package cn.vko.ring.im.session.action;

import android.content.Intent;
import android.util.Log;

import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nim.uikit.session.constant.RequestCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;


import cn.vko.ring.R;
import cn.vko.ring.classgroup.activity.CreateTaskActivty;
import cn.vko.ring.im.session.extension.CourseAttachment;
import cn.vko.ring.im.session.extension.CourseMsg;
import cn.vko.ring.im.session.extension.CustomAttachmentType;
import cn.vko.ring.im.session.extension.TaskAttachment;

/**
 * Created by A on 2017/3/21.
 */
public class TaskAction extends BaseAction {

    private static final String TAG = "CourseAction";
    public TaskAction() {
        super(R.drawable.create_task, R.string.text_task);
    }

    @Override
    public void onClick() {

        CreateTaskActivty.startActivityForResult(getActivity(),makeRequestCode(RequestCode.GET_TASK),getSessionId());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.GET_TASK) {
            String biaoti=data.getStringExtra(CreateTaskActivty.TASK_BT);
            String url=data.getStringExtra(CreateTaskActivty.TASK_URL);
            CourseMsg msg=new CourseMsg();
            msg.setCourseName(biaoti);
            msg.setTaskId("1");
            msg.setUrl(url);
//            msg.setType(1000);
//            Log.e("=====msg-biaoti",biaoti);
//            Log.e("=====msg-url",url);

            TaskAttachment attachment = new TaskAttachment(CustomAttachmentType.TASK,msg);
//            IMMessage message = MessageBuilder.createFileMessage(getAccount(), getSessionType(), file, file.getName());
            IMMessage message=MessageBuilder.createCustomMessage(getAccount(),getSessionType(),attachment);
            sendMessage(message);
        }
    }



}
