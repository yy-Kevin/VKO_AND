package cn.vko.ring.im.session.viewholder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.im.session.extension.NewTestAttachment;
import cn.vko.ring.message.activity.RecommendMsgDetailActivity;
import cn.vko.ring.mine.model.MessageInfo;


/**
 *
 * desc:作业测评
 * Created by jiarh on 16/5/3 15:24.
 */
public class MsgViewHolderTest extends MsgViewHolderBase implements View.OnClickListener {


    private TextView testTitle, userName;
    private TextView doTest;
    private NewTestAttachment attachment;
    private static final String TAG = "MsgViewHolderCourse";

    @Override
    protected int getContentResId()
    {
        return R.layout.chat_test;
    }

    @Override
    protected void inflateContentView() {


        testTitle = (TextView) view.findViewById(R.id.test_title);
        userName = (TextView) view.findViewById(R.id.test_type);
        doTest = (TextView) view.findViewById(R.id.dotest);
       doTest.setOnClickListener(this);

    }

    @Override
    protected void bindContentView() {
        attachment = (NewTestAttachment) message.getAttachment();
        if (attachment != null && attachment.getTest() != null) {
           userName.setText(attachment.getTest().getTeacherName()+"老师，作业测评");
            testTitle.setText(attachment.getTest().getTitle());


        }



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
        Bundle b = new Bundle();

        MessageInfo message = new MessageInfo();
        if(attachment!=null)
        message.setLinkUrl(attachment.getTest().getUrl());
        message.setTitle("测评");
        b.putSerializable("MESSAGE",message);
        StartActivityUtil.startActivity(context, RecommendMsgDetailActivity.class, b, Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }
}
