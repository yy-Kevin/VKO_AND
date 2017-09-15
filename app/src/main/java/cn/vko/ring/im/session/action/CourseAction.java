package cn.vko.ring.im.session.action;

import android.content.Intent;
import android.util.Log;

import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nim.uikit.session.constant.RequestCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import cn.vko.ring.R;
import cn.vko.ring.im.session.activity.SendCourseActivity;
import cn.vko.ring.im.session.extension.CourseAttachment;
import cn.vko.ring.im.session.extension.CourseDataParam;
import cn.vko.ring.im.session.extension.CustomAttachmentType;


/**
 * desc:
 * Created by jiarh on 16/5/3 10:15.
 */
public class CourseAction extends BaseAction {

    private static final String TAG = "CourseAction";
    public CourseAction() {
        super(R.drawable.im_course, R.string.text_course);
    }

    @Override
    public void onClick() {

       SendCourseActivity.startActivityForResult(getActivity(),makeRequestCode(RequestCode.GET_COURSE),getSessionId());

//        CourseAttachment attachment = new CourseAttachment();
//        IMMessage message=null;
//        if (getContainer() != null && getContainer().sessionType == SessionTypeEnum.ChatRoom) {
//            message = ChatRoomMessageBuilder.createChatRoomCustomMessage(getAccount(), attachment);
//        } else {
////            message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), attachment.getValue().getDesc(), attachment);
//        }
//
//        sendMessage(message);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.GET_COURSE) {
            CourseDataParam courseDataParam= (CourseDataParam) data.getSerializableExtra(SendCourseActivity.SELECT_COURSE);
            for (int i = 0;i<courseDataParam.getData().size();i++){
                CourseAttachment attachment = new CourseAttachment(CustomAttachmentType.COURSE,courseDataParam.getData().get(i));
                IMMessage message = MessageBuilder.createCustomMessage(getAccount(),getSessionType(),attachment);
                Log.e(TAG,message.getSessionId()+"00");
                sendMessage(message);
            }

        }
    }



}
