package cn.vko.ring.im.session.viewholder;

import android.util.Log;
import android.widget.TextView;

import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;

import cn.vko.ring.im.session.extension.RemindAttachment;

/**
 * desc:
 * Created by jiarh on 16/5/19 15:56.
 */
public class MsgViewHolderRemind  extends MsgViewHolderBase{
    protected TextView notificationTextView;
    RemindAttachment attachment;
    @Override
    protected int getContentResId() {
        return com.netease.nim.uikit.R.layout.nim_message_item_notification;
    }

    @Override
    protected void inflateContentView() {
        notificationTextView = (TextView) view.findViewById(com.netease.nim.uikit.R.id.message_item_notification_label);
    }

    @Override
    protected boolean isShowHeadImage() {
        return false;
    }


    @Override
    protected boolean isMiddleItem() {
        return true;
    }

    @Override
    protected int leftBackground() {
        return com.netease.nim.uikit.R.color.transparent;
    }

    @Override
    protected void bindContentView() {
        attachment = (RemindAttachment) message.getAttachment();
        if (attachment!=null&&attachment.getCourse()!=null){
            Log.e("任务", "bindContentView: "+ attachment.getCourse().getTitle());
            notificationTextView.setText(attachment.getCourse().getTitle());

        }

    }
}
