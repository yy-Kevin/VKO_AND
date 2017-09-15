package cn.vko.ring.im.session.extension;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public class CustomAttachParser implements MsgAttachmentParser {

    /**
     * msgType=0:课程；4:测试 ；...(添加新类型继续扩);100:tips;
     * if(msgType==0){
     *    type: 0 单个课程； 1 包 ；2 作业 ；3 自定义任务
     * }
     */
    private static final String KEY_TYPE = "msgType";
    private static final String KEY_DATA = "data";
    private static final String TAG = "CustomAttachParser";

    @Override
    public MsgAttachment parse(final String json) {
        Log.d(TAG, "parse() called with: " + "json = " + json + "");
        CustomAttachment attachment = null;
        try {
            JSONObject object = JSONObject.parseObject(json);
            int type = Integer.parseInt(object.getString(KEY_TYPE));
            JSONObject data = object.getJSONObject(KEY_DATA);
            Log.e(TAG, "parse: "+ type);
            switch (type) {
                case CustomAttachmentType.Guess:
//                    attachment = new GuessAttachment();
                    break;
                case CustomAttachmentType.SnapChat:
//                    return new SnapChatAttachment(data);
                case CustomAttachmentType.Sticker:
                    attachment = new StickerAttachment();
                    break;
                case CustomAttachmentType.RTS:
//                    attachment = new RTSAttachment();
                    break;
                case CustomAttachmentType.COURSE:
                    attachment = new CourseAttachment();
                    break;
                case CustomAttachmentType.TASK:
                    attachment = new TaskAttachment();
                    break;
                case CustomAttachmentType.NEWTEST:
                    attachment = new NewTestAttachment();
                    break;
                case CustomAttachmentType.REMIND:
                    Log.e(TAG, "走ReMIND " );
                    attachment = new RemindAttachment();
                    break;
                case CustomAttachmentType.TEST:
//                    attachment = new TestAttachment();
                    break;
                default:
                    attachment = new DefaultCustomAttachment();
                    break;
            }

            if (attachment != null) {
                attachment.fromJson(data);
            }
        } catch (Exception e) {

            Log.d(TAG, "parse() called with: " + "e = [" + e + "]");
        }

        return attachment;
    }

    public static String packData(int type, JSONObject data) {
        JSONObject object = new JSONObject();
        object.put(KEY_TYPE, type);
        if (data != null) {
            object.put(KEY_DATA, data);
        }

        return object.toJSONString();
    }
}
