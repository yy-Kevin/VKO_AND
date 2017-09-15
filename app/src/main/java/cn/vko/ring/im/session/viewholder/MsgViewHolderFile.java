package cn.vko.ring.im.session.viewholder;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.common.util.file.AttachmentStore;
import com.netease.nim.uikit.common.util.file.FileUtil;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import cn.shikh.utils.AndroidFileUtil;
import cn.vko.ring.R;
import cn.vko.ring.im.file.FileIcons;


/**
 * Created by zhoujianghua on 2015/8/6.
 */
public class MsgViewHolderFile extends MsgViewHolderBase implements View.OnClickListener {


    private ImageView fileIcon;
    private TextView fileNameLabel;
    private TextView fileStatusLabel;
    private ProgressBar progressBar;

    private FileAttachment msgAttachment;

    private static final String TAG = "MsgViewHolderFile";
    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_file;
    }

    @Override
    protected void inflateContentView() {
        view.setOnClickListener(this);

        fileIcon = (ImageView) view.findViewById(R.id.message_item_file_icon_image);
        fileNameLabel = (TextView)view.findViewById(R.id.message_item_file_name_label);
        fileStatusLabel = (TextView)view.findViewById(R.id.message_item_file_status_label);
        progressBar = (ProgressBar) view.findViewById(R.id.message_item_file_transfer_progress_bar);

        fileIcon.setOnClickListener(this);
        fileNameLabel.setOnClickListener(this);
    }

    @Override
    protected void bindContentView() {
        msgAttachment = (FileAttachment) message.getAttachment();
        String path = msgAttachment.getPath();
        initDisplay();

        Log.d(TAG, "maspath: " + path+"");
        Log.d(TAG, "masurl: " + msgAttachment.getUrl()+"");
        if (!TextUtils.isEmpty(path)) {
            loadImageView();
        } else {
            AttachStatusEnum status = message.getAttachStatus();
            switch (status) {
            case def:
                updateFileStatusLabel();
                break;
            case transferring:
                fileStatusLabel.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                int percent = (int) (getAdapter().getProgress (message) * 100);
                progressBar.setProgress(percent);
                break;
            case transferred:
            case fail:
                updateFileStatusLabel();
                break;
            }
        }
    }

    private void loadImageView() {
        fileStatusLabel.setVisibility(View.VISIBLE);
        // 文件长度
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtil.formatFileSize(msgAttachment.getSize()));
        fileStatusLabel.setText(sb.toString());

        progressBar.setVisibility(View.GONE);
    }

    private void initDisplay() {
        int iconResId = FileIcons.smallIcon(msgAttachment.getDisplayName());
        fileIcon.setImageResource(iconResId);
        fileNameLabel.setText(msgAttachment.getDisplayName());
    }

    private boolean isOriginImageHasDownloaded(final IMMessage message) {
        if (message.getAttachStatus() == AttachStatusEnum.transferred ) {
            return true;
        }
        return false;
    }

    private void updateFileStatusLabel() {
        fileStatusLabel.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        // 文件长度
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtil.formatFileSize(msgAttachment.getSize()));
        sb.append("  ");
        // 下载状态
        String path = msgAttachment.getPathForSave();
        if (AttachmentStore.isFileExist(path)) {
            sb.append(context.getString(R.string.file_transfer_state_downloaded));
        } else {
            sb.append(context.getString(R.string.file_transfer_state_undownload));
        }
        fileStatusLabel.setText(sb.toString());
    }

    @Override
    protected int leftBackground() {
        return R.drawable.nim_message_left_white_bg;
    }

    @Override
    protected int rightBackground() {
        return R.drawable.nim_message_right_blue_bg;
    }

    @Override
    public void onClick(View v) {

        FileAttachment msgAttachment = (FileAttachment) message.getAttachment();
        String path = msgAttachment.getPath();
        Log.e(TAG,path);

        try {
            if (!TextUtils.isEmpty(path))
            context.startActivity(AndroidFileUtil.openFile(path));
        } catch (Exception e) {

            Log.d(TAG, "oException " + "e = [" + e.toString() + "]");
            Toast.makeText(context,"暂不支持此类型打开",Toast.LENGTH_SHORT).show();
        }
    }
}
