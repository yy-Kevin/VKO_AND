package cn.vko.ring.classgroup.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.okhttp.OkHttpUtils;
import cn.shikh.utils.okhttp.builder.PostFormBuilder;
import cn.shikh.utils.okhttp.callback.StringCallback;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.model.AudioInfo;
import cn.vko.ring.classgroup.model.UpDataResult;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.IClickAlertListener;
import cn.vko.ring.common.listener.IRecordAudioListener;
import cn.vko.ring.common.widget.ImageLinearLayout;
import cn.vko.ring.common.widget.VoiceLinearLyout;
import cn.vko.ring.common.widget.dialog.ChosePicDialog;
import cn.vko.ring.common.widget.dialog.LoadingDialog;
import cn.vko.ring.common.widget.dialog.RecordAudioDialog;
import cn.vko.ring.utils.EditTextUtils;
import cn.vko.ring.utils.FileUtil;
import cn.vko.ring.utils.GalleryUtil;
import okhttp3.Call;

/**
 * 创建任务
 * Created by shikh on 2016/5/18.
 */
public class CreateTaskActivty extends BaseActivity{
    @Bind(R.id.et_topic)
    public EditText etTopic;
    @Bind(R.id.et_title)
    public EditText etTitle;
    @Bind(R.id.layout_images)
    public ImageLinearLayout imageLayout;
    @Bind(R.id.iv_add_image)
    public ImageView ivAddImage;
    @Bind(R.id.iv_pub_pic)
    public ImageView ivPubPic;
    @Bind(R.id.iv_pub_sound)
    public ImageView ivPubSound;

    @Bind(R.id.iv_sound_del)
    public ImageView ivSoundDel;
    @Bind(R.id.voice_view)
    public VoiceLinearLyout voiceView;
    @Bind(R.id.layout_voice)
    public FrameLayout layoutVoice;
    @Bind(R.id.layout_buttom)
    public LinearLayout buttomLayout;
    @Bind(R.id.tv_num_filter)
    public TextView mTextView;
    private ChosePicDialog mDialog;
    private RecordAudioDialog rDialog;
    private File picFile;
    private String groupId;
    private String title;
    private boolean isSubmit;
    private LoadingDialog progressDialog;

    public static final String SESSSION_ID="session_id";
    public static final String TASK_BT = "task_bt";
    public static final String TASK_URL = "task_url";

    @Override
    public int setContentViewResId() {
        // TODO Auto-generated method stub
        return R.layout.activity_public_topic;
    }

    public static void startActivityForResult(Activity activity, int resultCode, String sessionId) {
        Intent intent = new Intent();
        intent.setClass(activity, CreateTaskActivty.class);
        intent.putExtra(SESSSION_ID,sessionId);
        activity.startActivityForResult(intent, resultCode);
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        imageLayout.setOnIChildViewUpdateListener(new ImageLinearLayout.IChildViewUpdateListener() {

                    @Override
                    public void onUpdate(List<String> urls) {
                        // TODO Auto-generated method stub
                        if (urls != null && urls.size() > 0) {
                            imageLayout.setVisibility(View.VISIBLE);
                            ivPubPic.setVisibility(View.GONE);
                            ivAddImage.setVisibility(View.VISIBLE);
//                            buttomLayout.setVisibility(View.GONE);
                        } else {
                            imageLayout.setVisibility(View.GONE);
                            ivPubPic.setVisibility(View.VISIBLE);
                            ivAddImage.setVisibility(View.GONE);
//                            buttomLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
        new EditTextUtils(etTopic, 500, mTextView);
//        new EditTextUtils(etTitle, 100, mTextView);
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
        groupId = getIntent().getExtras().getString("GROUPID");
        Log.e("-------groupId上",groupId+"");
        if (groupId==null||groupId.isEmpty()){
            groupId = getIntent().getStringExtra(SESSSION_ID);
        }
        Log.e("-------groupId下",groupId+"");
    }

    @OnClick(R.id.iv_pub_sound)
    void sayRecordAudio() {
        if (rDialog == null) {
            rDialog = new RecordAudioDialog(this);
            rDialog.setCanceledOnTouchOutside(true);
            rDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    rDialog.recoverView();
                }
            });
            rDialog.setListener(new IRecordAudioListener<AudioInfo>() {

                @Override
                public void onDone(AudioInfo t) {
                    // TODO Auto-generated method stub
                    layoutVoice.setVisibility(View.VISIBLE);
                    ivPubSound.setVisibility(View.GONE);
//                    buttomLayout.setVisibility(View.GONE);
                    voiceView.initView(t);
                }

                @Override
                public void onCancel() {
                    // TODO Auto-generated method stub
                }
            });
        }
        rDialog.show();
    }

    @OnClick({ R.id.iv_pub_pic, R.id.iv_add_image })
    void sayChosePic(View v) {
        if (mDialog == null) {
            mDialog = new ChosePicDialog(this);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.setOnClickAlertListener(new IClickAlertListener() {

                @Override
                public void onClick(int type) {
                    // TODO Auto-generated method stub
                    if (type == 1) {
                        takeCamear();
                    } else {
                        getPhotoPicture();
                    }
                }
            });
        }
        mDialog.show();
    }

    @OnClick(R.id.iv_sound_del)
    void sayDelAudio() {
        layoutVoice.setVisibility(View.GONE);
        ivPubSound.setVisibility(View.VISIBLE);
        voiceView.setAudio(null);
//        buttomLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_cancel)
    void sayCncel() {
        finish();
        overridePendingTransition(0, R.anim.bottom_out);
    }

    @OnClick(R.id.tv_submit)
    void saySubmit() {// 发表
        if(!VkoContext.getInstance(this).doLoginCheckToSkip(this)){
            if(isSubmit) return;
            title = etTitle.getText().toString();
            String content = etTopic.getText().toString();
            List<String> urls = imageLayout.getUrls();
            AudioInfo audio = voiceView.getAudio();
            if(TextUtils.isEmpty(title)){
                Toast.makeText(this,"请填充任务标题",Toast.LENGTH_SHORT).show();
                return;
            }
            if(title.length() >100){
                Toast.makeText(this,"标题不能超过100个字符",Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(content) && audio == null
                    && (urls == null || urls.size() == 0)) {
                Toast.makeText(this,"请填充发表内容",Toast.LENGTH_SHORT).show();
                return;
            }
            Log.e("=====content",content);
            Log.e("=====title",title);
            Log.e("=====urls",urls+"");
            Log.e("=====audio",audio+"");
            submitGroupTask(content,title, urls, audio);
        }

    }

    private void submitGroupTask(String content,String title, final List<String> urls, final AudioInfo audio) {
        // TODO Auto-generated method stub
        showProgress();
        isSubmit = true;
//        Map<String,File> paramFile = new HashMap<>();
        Map<String,String> params = new HashMap<>();
        params.put("description", content);
        params.put("targetId", groupId);
        params.put("title", title);
        params.put("token", VkoContext.getInstance(this).getToken());
        int size = (urls !=null?urls.size():0)+(audio == null? 0:1);
        if(size == 0){
            params.put("len", "0");
            Log.e("=====走size0回调了","callback");
            OkHttpUtils.post().params(params).url(ConstantUrl.VK_GROUP_SUBMIT_TASK).build().execute(new uploadStringCallback());
//            OkHttpUtils.post().params(params).url(ConstantUrl.VK_GROUP_SUBMIT_TASK);
//            Intent intent = new Intent();
//            intent.putExtra(TASK_BT, title);
//            setResult(Activity.RESULT_OK, intent);
//            finish();
        }else{
            Log.e("=====走size回调了","callback");
            PostFormBuilder postbuilder =  OkHttpUtils.post().params(params).url(ConstantUrl.VK_GROUP_SUBMIT_TASK);
            if(urls != null && urls.size()>0){
                for(int i = 0;i<urls.size();i++){
                    File file = new File(urls.get(i));
                    postbuilder.addFile("file",file.getName(),file);
                    postbuilder.addFileParams("len","0");
                }
            }
            if(audio != null && audio.getUrl()!= null){
                File file = new File(audio.getUrl());
                postbuilder.addFile("file",file.getName(),file);
                postbuilder.addFileParams("len",audio.getLen()+"");
            }
            postbuilder.build().execute(new uploadStringCallback());
        }
    }

    public class uploadStringCallback extends StringCallback {

        @Override
        public void onError(Call call, Exception e) {
            stopProgress();
            isSubmit = false;
        }

        @Override
        public void onResponse(String response) {
            stopProgress();
            isSubmit = false;
            if(!TextUtils.isEmpty(response)){
                try{
                    UpDataResult data =  com.alibaba.fastjson.JSONObject.parseObject(response,UpDataResult.class);
                    if(data != null && data.getData() != null){
                        String url=data.getData().get(0).getUrl();
                        Log.e("----------url",url);
                        EventBus.getDefault().post(data.getData().get(0));
                        Intent intent = new Intent();
                        intent.putExtra(TASK_BT, title);
                        intent.putExtra(TASK_URL, url);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }catch (Exception e){
                }

            }

        }
    };
    // 调用图库,选择头像
    protected void getPhotoPicture() {
        // TODO Auto-generated method stub
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 10);
    }

    private String getPicFilePath() {
        return  "vko_"+ System.currentTimeMillis() + ".jpg";
    }

    // 调用相机拍头像
    protected void takeCamear() {
        picFile = new File(FileUtil.getCameraDir(), getPicFilePath());
        StartActivityUtil.startCameraForResult(this, 20, picFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 10 && data != null) {
                try{
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    if(cursor != null){
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        imageLayout.addImageAndDelView(picturePath, 9);
                    }else{
                        String picturePath =  GalleryUtil.getPath(this,selectedImage);
                        imageLayout.addImageAndDelView(picturePath, 9);
                    }

                }catch (Exception e){

                }

            } else if (requestCode == 20) {
                imageLayout.addImageAndDelView(picFile.getAbsolutePath(), 9);
            }
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        hideSoftInput(this, etTopic, true);
    }

    public void showProgress() {
        if (progressDialog == null && !isFinishing()) {
            progressDialog = new LoadingDialog(this);
            progressDialog.setCancelable(true);
        }
        if (progressDialog != null) {
            try {
                progressDialog.show();
            } catch (Exception e) {

            }
        }
    }

    public void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {

            }

        }
    }

}
