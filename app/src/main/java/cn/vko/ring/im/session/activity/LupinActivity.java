package cn.vko.ring.im.session.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.alibaba.fastjson.JSONObject;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.OnClick;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.SystemBarTintManager;
import cn.shikh.utils.okhttp.OkHttpUtils;
import cn.shikh.utils.okhttp.callback.StringCallback;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.model.ItemDialogModel;
import cn.vko.ring.common.widget.dialog.CommonDialog;
import cn.vko.ring.common.widget.pop.LupinPop;
import cn.vko.ring.im.session.activity.lupin.BitmapCompressUtils;
import cn.vko.ring.im.session.activity.lupin.DrawingView;
import cn.vko.ring.im.session.activity.lupin.LineEditText;
import cn.vko.ring.im.session.activity.lupin.LupinInfo;
import cn.vko.ring.im.session.activity.lupin.RecordService;
import cn.vko.ring.mine.model.MsgInfo;
import cn.vko.ring.utils.FileUtil;
import okhttp3.Call;

public class LupinActivity extends BaseActivity implements View.OnClickListener {
    public static final String LUPIN_VID = "lupin_vid";
    public static final String LUPIN_BT = "lupin_bt";
    public static final String LUPIN_TP = "lupin_tp";
    public static final String LUPIN_URL = "lupin_url";
    public static final String LUPIN_TASK_ID = "lupin_taskId";

    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    public static final String EXTRA_VIDEO_CONTENT = "EXTRA_VIDEO_CONTENT";
    private static int REQUEST_CROP_PICTURE = 1 << 2;
    protected Activity mContext;
    public SystemBarTintManager tintManager;//状态栏

    private int count = 0;

    ImageView ivBack;
    ImageView menuZK;
    ImageView menuSQ;
    TextView tvTitle;
    TextView tvRight;
    LinearLayout linearColor;
    LinearLayout linearMenu;
    LinearLayout linearBT;
    LineEditText editBT;
    Button buttonBT;
    ImageButton buttonQX;

    private String groupId;
    private ProgressBar lupin_progress;
    private LinearLayout progress_linear;
    private String biaoti;
    private LupinPop pop;
    private Drawable dewUp,dewDown;
    private Chronometer lupin_chronometer;
    private RelativeLayout lupin_rinear_time,lupin_head;

    CommonDialog dialog ;
    private File f, outFile;

    public static final String SESSSION_ID="session_id";
    //custom drawing view
    private DrawingView drawView;
    //buttons
    private TextView drawBtn, eraseBtn, newBtn, saveBtn, opacityBtn;
    private ImageView currPaint;
    //sizes
    private float smallBrush, mediumBrush, largeBrush,bigBrush;

    //录屏功能
    private static final int RECORD_REQUEST_CODE  = 101;
    private static final int STORAGE_REQUEST_CODE = 102;
    private static final int AUDIO_REQUEST_CODE   = 103;

    private MediaProjectionManager projectionManager;
    private int currentapiVersion;
    private MediaProjection mediaProjection;
    private RecordService recordService;
    private ImageView startBtn;
    private TextView startPhoto,tv_agin;
    private VideoView videoView;
    private ImageView videoview_begin;
    private RelativeLayout lupin_relative;
    boolean isColor=true;//是否显示颜色栏
    boolean isMenu=true;//是否显示菜单栏

    @SuppressLint("HandlerLeak")
    private Handler handle = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Thread.currentThread().interrupt();
                    progress_linear.setVisibility(View.GONE);
//                    Toast.makeText(LupinActivity.this, "���سɹ�����", Toast.LENGTH_SHORT).show();
                    break;

                case 1:
//				if(!Thread.currentThread().isInterrupted()){
                    lupin_progress.setProgress(count);
//				}
                    break;

                default:
                    break;
            }
        }

    };



    public static void startActivityForResult(Activity activity, int resultCode,String sessionId) {
        Intent intent = new Intent();
        intent.setClass(activity, LupinActivity.class);
        intent.putExtra(SESSSION_ID,sessionId);
        activity.startActivityForResult(intent, resultCode);
    }

    @Override
    public int setContentViewResId() {
        return R.layout.activity_lupin1;

    }

    @Override
    public void initView() {
        // 创建状态栏的管理实例
        tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 设置一个状态栏资源
        tintManager.setStatusBarTintResource(R.color.black);

        setGuideResId(R.drawable.lupin_two);// 添加引导页
        setGuideResId1(R.drawable.lupin_one);// 添加引导页
        //get drawing view
        drawView = (DrawingView)findViewById(R.id.drawing);
        tvRight= (TextView) findViewById(R.id.tv_right);
        tvTitle= (TextView) findViewById(R.id.tv_title);
        ivBack= (ImageView) findViewById(R.id.iv_back);
        linearMenu= (LinearLayout) findViewById(R.id.lupin_linear_menu);
        linearColor= (LinearLayout) findViewById(R.id.lupin_linear_color);
        linearColor.setVisibility(View.GONE);
        linearMenu.setVisibility(View.GONE);
        menuZK= (ImageView) findViewById(R.id.iv_lupin_zhan);
        menuSQ= (ImageView) findViewById(R.id.iv_lupin_shou);
        menuSQ.setVisibility(View.GONE);
        lupin_chronometer= (Chronometer) findViewById(R.id.lupin_chronometer);
        lupin_rinear_time= (RelativeLayout) findViewById(R.id.lupin_rinear_time);
        lupin_head= (RelativeLayout) findViewById(R.id.lupin_head);
        lupin_rinear_time.setVisibility(View.GONE);
        //输入轻课小标题
        linearBT= (LinearLayout) findViewById(R.id.lupin_linear_little_biaoti);
        editBT= (LineEditText) findViewById(R.id.lupin_little_biaoti_et);
        buttonBT= (Button) findViewById(R.id.lupin_little_biaoti_bt);
        buttonQX= (ImageButton) findViewById(R.id.lupin_little_biaoti_qx);
        linearBT.setVisibility(View.GONE);
        lupin_progress= (ProgressBar) findViewById(R.id.lupin_progress);
        progress_linear= (LinearLayout) findViewById(R.id.progress_linear);


        tvTitle.setText("轻课录制");
        tvRight.setVisibility(View.VISIBLE);
        //get the palette and first color button
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageView) paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        //sizes from dimensions
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);
        bigBrush = getResources().getInteger(R.integer.color_size);


        //draw button
        drawBtn = (TextView)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        //set initial size
        drawView.setBrushSize(mediumBrush);

        //erase button
        eraseBtn = (TextView)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        //new button
        newBtn = (TextView)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        //save button
//        saveBtn = (ImageButton)findViewById(R.id.save_btn);
//        saveBtn.setOnClickListener(this);

        //opacity
//        opacityBtn = (TextView)findViewById(R.id.opacity_btn);
//        opacityBtn.setOnClickListener(this);

        currentapiVersion=android.os.Build.VERSION.SDK_INT;
        Log.e("=======当前版本号",currentapiVersion+"");

        if (currentapiVersion >= 21){
            //录屏代码
            projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        }else {
            showToast("该功能基于Android 5.0版本以上");
        }


        startBtn = (ImageView) findViewById(R.id.start_record);
        tv_agin = (TextView) findViewById(R.id.tv_agin);
        startPhoto = (TextView) findViewById(R.id.select_photo);
        videoView = (VideoView) findViewById(R.id.videoview_lupin);
        videoview_begin = (ImageView) findViewById(R.id.lupin_videoview_begin);
        lupin_relative = (RelativeLayout) findViewById(R.id.lupin_relative);

//        videoView.setVideoURI(Uri.parse(path));


        startBtn.setEnabled(false);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentapiVersion >= 21){
                    if (recordService.isRunning()) {
                        recordService.stopRecord();
                        startBtn.setImageDrawable(getResources().getDrawable(R.drawable.lupin_start));
                        lupin_rinear_time.setVisibility(View.GONE);
//                        lupin_head.setVisibility(View.VISIBLE);
                        tvRight.setVisibility(View.VISIBLE);
                        // 将计时器清零
                        lupin_chronometer.setBase(SystemClock.elapsedRealtime());
                        //停止
                        lupin_chronometer.stop();
                        startBtn.setVisibility(View.INVISIBLE);
                        startPhoto.setVisibility(View.INVISIBLE);
                        tv_agin.setVisibility(View.INVISIBLE);

                        goPlayQKDemo();
                    } else {
                        Intent captureIntent = projectionManager.createScreenCaptureIntent();
                        startActivityForResult(captureIntent, RECORD_REQUEST_CODE);
                    }
                }else {
                    showToast("该功能基于Android 5.0版本以上");
                }

            }
        });

//        startPhoto.setEnabled(false);
//        startPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(LupinActivity.this,"传图片",Toast.LENGTH_SHORT).show();
//            }
//        });


        if (ContextCompat.checkSelfPermission(LupinActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(LupinActivity.this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.RECORD_AUDIO}, AUDIO_REQUEST_CODE);
        }

        Intent intent = new Intent(this, RecordService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);

    }

    private void goPlayQKDemo() {
        drawView.setVisibility(View.GONE);
        lupin_relative.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.lupin_videoview_begin)
    public void goPlayVideo(){
        String path  = Environment.getExternalStorageDirectory() + "/ScreenRecord/QKDemo.mp4";
        Log.e("======path",path);
        videoView.setVideoPath(path);
        videoview_begin.setVisibility(View.GONE);
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview_begin.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.lupin_color_select)
    public void selectColor(){
        if (isColor){
            linearColor.setVisibility(View.VISIBLE);
            isColor=false;
        }else {
            linearColor.setVisibility(View.GONE);
            isColor=true;
        }
    }
    //展开右侧菜单
    @OnClick(R.id.iv_lupin_zhan)
    public void zhanMenu(){
//        isMenu=true;
            linearMenu.setVisibility(View.VISIBLE);
            menuSQ.setVisibility(View.VISIBLE);
    }
    //展开右侧菜单
    @OnClick(R.id.iv_lupin_shou)
    public void shouMenu(){
//        isMenu=true;
            linearMenu.setVisibility(View.GONE);
            menuSQ.setVisibility(View.GONE);
    }
    //重录和上传
    public void toggle(boolean isOpen) {
        isOpen = !isOpen;
        if (isOpen) {
            switchDrawable(tvRight, dewUp);
        } else {
            switchDrawable(tvRight, dewDown);
        }
    }

    public void switchDrawable(TextView tv, Drawable drawable) {
        if (drawable == null) {
            return;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
        tv.setCompoundDrawables(null, null, drawable, null);// 画在右边
    }


    @OnClick(R.id.tv_agin)
    void toLupinAgin(){
        drawView.setVisibility(View.VISIBLE);
        lupin_relative.setVisibility(View.GONE);
        startBtn.setVisibility(View.VISIBLE);
        startPhoto.setVisibility(View.VISIBLE);
        tv_agin.setVisibility(View.VISIBLE);

        if (recordService.isRunning()) {
            recordService.stopRecord();
            startBtn.setImageDrawable(getResources().getDrawable(R.drawable.lupin_start));
//            lupin_rinear_time.setVisibility(View.GONE);
//            lupin_head.setVisibility(View.VISIBLE);
            // 将计时器清零
            lupin_chronometer.setBase(SystemClock.elapsedRealtime());
            //停止
            lupin_chronometer.stop();
            drawView.startNew();
            Intent captureIntent = projectionManager.createScreenCaptureIntent();
            startActivityForResult(captureIntent, RECORD_REQUEST_CODE);
//            startBtn.setVisibility(View.INVISIBLE);
//            startPhoto.setVisibility(View.INVISIBLE);
//            goPlayQKDemo();
        } else {
            Intent captureIntent = projectionManager.createScreenCaptureIntent();
            startActivityForResult(captureIntent, RECORD_REQUEST_CODE);

        }
    }

    @OnClick(R.id.tv_right)
    void onFilterDteail(){

        linearBT.setVisibility(View.VISIBLE);
        editBT.setFocusable(true);
        editBT.setFocusableInTouchMode(true);



    }

    @OnClick(R.id.lupin_little_biaoti_bt)
    void getBT(){
        biaoti=editBT.getText().toString().trim();
        if (biaoti.isEmpty()||biaoti.equals("")){
            showToast("标题不能为空");
            return;
        }else {
            linearBT.setVisibility(View.GONE);
        }
        editBT.setFocusable(false);
        editBT.setFocusableInTouchMode(false);
        progress_linear.setVisibility(View.VISIBLE);
        String path  = Environment.getExternalStorageDirectory() + "/ScreenRecord/QKDemo.mp4";
        File file = new File(path);
        upLoadLupin(file);
    }
    @OnClick(R.id.lupin_little_biaoti_qx)
    void qxBT(){
        linearBT.setVisibility(View.GONE);
        editBT.setFocusable(false);
        editBT.setFocusableInTouchMode(false);
    }


    // 上传视频
    private void upLoadLupin(final File file) {
        if (!file.exists()) {
            progress_linear.setVisibility(View.GONE);
            showToast("没有视频，赶紧去录一个吧...");
            return;
        }
//        rectangleProgressBarInit();
        final Map<String, String> map = new HashMap<String, String>();
        map.put("token", VkoContext.getInstance(this).getUser().getToken());
        map.put("fName", biaoti);
        map.put("groupId", groupId);
        Log.e("-----------群id", groupId+"~~~");
//        File file = BitmapUtils.scalBitmap(f.getAbsolutePath(),10*1024,FileUtil.getCacheDir());
        System.out.print("token ="+VkoContext.getInstance(this).getUser().getToken() +" path="+file.getAbsolutePath());
        OkHttpUtils.post().params(map).addFile("file",file.getName(),file).url(ConstantUrl.VKOIP
                + "/lightCourse/upload").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(LupinActivity.this, "上传失败",
                        Toast.LENGTH_SHORT).show();
                progress_linear.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String result) {
                if (!TextUtils.isEmpty(result)) {
                    Log.e("-----------result", result);
                    LupinInfo parseObject = JSONObject.parseObject(result, LupinInfo.class);
                    LupinInfo.Data data=parseObject.getData();
                    String isOk=data.getMsg();
                    String vid=data.getVid();
                    String courseType=data.getCourseType();
                    String url=data.getUrl();
                    String taskId=data.getTaskId();
//                    String url=data.get
//                    Log.e("-----------isOk", isOk);
                    Log.e("-----------url", url+"~~~");
                    Log.e("-----------url", url+"~~~");
//                    Log.e("-----------vid", courseType);

                    // 问题在此parseObject.isData() = false
                    String path  = Environment.getExternalStorageDirectory() + "/ScreenRecord/QKDemo.mp4";
                    File file = new File(path);
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_VIDEO_PATH, path);
                    intent.putExtra(LUPIN_VID, vid);
                    intent.putExtra(LUPIN_BT, biaoti);
                    intent.putExtra(LUPIN_TP, courseType);
                    intent.putExtra(LUPIN_URL, url);
                    intent.putExtra(LUPIN_TASK_ID, taskId);
                    setResult(Activity.RESULT_OK, intent);
                    finish();

//                    Log.e("-----------", result + parseObject.getData());
//                    if (parseObject != null && parseObject.isData()) {
//                    }
                } else {
                    Toast.makeText(LupinActivity.this, "上传失败",
                            Toast.LENGTH_SHORT).show();
                    progress_linear.setVisibility(View.GONE);
                }
            }
        });
    }




    private void rectangleProgressBarInit() {
        lupin_progress.setMax(100);
        lupin_progress.setProgress(0);
        Thread myThread = new Thread(new Runnable() {

            @Override
            public void run() {
                for(int i=0;i<20;i++){
                    count = (i + 1) * 5;
                    SystemClock.sleep(500);
                    if(i == 19){
                        handle.sendEmptyMessage(0);
                        break;
                    }else{
                        handle.sendEmptyMessage(1);
                    }
                }
            }
        });
        myThread.start();
    }



    @Override
    public void initData() {
        groupId = getIntent().getStringExtra(SESSSION_ID);
    }


    //录屏代码
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            recordService.setMediaProject(mediaProjection);
            recordService.startRecord();
            lupin_rinear_time.setVisibility(View.VISIBLE);
//            lupin_head.setVisibility(View.GONE);
            tvRight.setVisibility(View.GONE);
            startBtn.setImageDrawable(getResources().getDrawable(R.drawable.lupin_end));
            // 将计时器清零
            lupin_chronometer.setBase(SystemClock.elapsedRealtime());
            //开始计时
            lupin_chronometer.start();

        }
        if (resultCode == RESULT_OK) {
            if (requestCode == 10 || requestCode == 20) {
                Log.e("===========", "执行了onActivityResult");
                startCropView(data, requestCode);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_REQUEST_CODE || requestCode == AUDIO_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
            }
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RecordService.RecordBinder binder = (RecordService.RecordBinder) service;
            recordService = binder.getRecordService();
            recordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
            startBtn.setEnabled(true);
            startBtn.setImageDrawable(getResources().getDrawable(recordService.isRunning() ? R.drawable.lupin_end : R.drawable.lupin_start));
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {}
    };


    /*
    * 照片涂鸦代码
    * */
    //user clicked paint
    public void paintClicked(View view){
        //use chosen color

        //set erase false
        drawView.setErase(false);
        drawView.setPaintAlpha(100);
        drawView.setBrushSize(drawView.getLastBrushSize());

        if(view!=currPaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            //update ui
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
        linearColor.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view){

        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
//            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            //listen for clicks on size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            //show and wait for user interaction
            brushDialog.show();
        }
        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
//            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            //size buttons
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
//                    drawView.setColor("#FFFFFFFF");
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
//                    drawView.setColor("#FFFFFFFF");
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
//                    drawView.setColor("#FFFFFFFF");
                    drawView.setBrushSize(bigBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.new_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
//            newDialog.setTitle("");
            newDialog.setMessage("清空当前绘画？");
            newDialog.setPositiveButton("是", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("否", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        else if(view.getId()==R.id.save_btn){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    drawView.setDrawingCacheEnabled(true);
                    //attempt to save
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString()+".png", "drawing");
                    //feedback
                    if(imgSaved!=null){
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else{
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
        else if(view.getId()==R.id.opacity_btn){
            //launch opacity chooser
            final Dialog seekDialog = new Dialog(this);
            seekDialog.setTitle("Opacity level:");
            seekDialog.setContentView(R.layout.opacity_chooser);
            //get ui elements
            final TextView seekTxt = (TextView)seekDialog.findViewById(R.id.opq_txt);
            final SeekBar seekOpq = (SeekBar)seekDialog.findViewById(R.id.opacity_seek);
            //set max
            seekOpq.setMax(100);
            //show current level
            int currLevel = drawView.getPaintAlpha();
            seekTxt.setText(currLevel+"%");
            seekOpq.setProgress(currLevel);
            //update as user interacts
            seekOpq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekTxt.setText(Integer.toString(progress)+"%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}

            });
            //listen for clicks on ok
            Button opqBtn = (Button)seekDialog.findViewById(R.id.opq_ok);
            opqBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setPaintAlpha(seekOpq.getProgress());
                    seekDialog.dismiss();
                }
            });
            //show dialog
            seekDialog.show();
        }
    }

    @OnClick(R.id.select_photo)
    public void goSelectPhoto(){
        showDialogPhoto();
    }

    private void showDialogPhoto() {
        dialog = new CommonDialog.Builder(this)
                .items(new ItemDialogModel("拍照"))
                .items(new ItemDialogModel("从相册选择"))
                .items(new ItemDialogModel("取消")).setOnLvItemClickListener(new CommonDialog.Builder.OnLvItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (position) {
                            case 0:
                                takePhoto();

                                break;
                            case 1:
                                getPhotoPicture();
                                break;
                        }
                        dialog.dismiss();
                    }
                }).build();

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
    // 调用图库,选择头像
    protected void getPhotoPicture() {
        // TODO Auto-generated method stub
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 10);
    }

    // 调用相机拍头像
    protected void takePhoto() {
        outFile = new File(FileUtil.getCameraDir(), "head.jpg");
        StartActivityUtil.startCameraForResult(this, 20, outFile);
    }

    private void startCropView(Intent data, int resultCode) {
        if (resultCode == 10){

		Uri imageUri =data.getData();
		Log.e("======imageUri",imageUri +"");

//            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(0, 0,
//                    imageUri);
//            // 设置截图框的颜色
//            cropImage.setOutlineColor(0xFF378dcc).setSquareCrop(true);
//            // 设置截图框的形状
//            // cropImage.setCircleCrop(true);
//                if (data != null) {
//                    cropImage.setSourceImage(data.getData());
//                    startActivityForResult(cropImage.getIntent(this),
//                            REQUEST_CROP_PICTURE);
//                }
		String imagePath="";

        String[] projection={MediaStore.Images.Media.DATA};
        Cursor cursor=managedQuery(imageUri, projection, null, null, null);
        if(cursor!=null){
            int colomn_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            imagePath=cursor.getString(colomn_index);
        }
        Log.e("======imagePath",imagePath +"");
//            File file = new File(imagePath);
//            if (!file.exists()) {
//                Toast.makeText(LupinActivity.this, "文件不存在", Toast.LENGTH_LONG).show();
//            }
//            if (file.length() > 3.5 * 1024 * 1024) {
//                Log.e("======文件大小",file.length() +"");
//                Toast.makeText(LupinActivity.this, "文件太大了", Toast.LENGTH_LONG).show();
//                return;
//            }


            Bitmap bit= BitmapFactory.decodeFile(imagePath);
            bit = BitmapCompressUtils.imageZoom(bit, 310.00);
            Drawable drawable1 =new BitmapDrawable(bit);

            Drawable drawable=Drawable.createFromPath(imagePath);

        drawView.setBackground(drawable1);
        Toast.makeText(LupinActivity.this, imagePath, Toast.LENGTH_LONG).show();

        }else if (resultCode==20){
            f = new File(FileUtil.getCameraDir(), "head.jpg");
            Uri croppedImage = Uri.fromFile(f);

            Log.e("======croppedImage",croppedImage +"");

//            String imagePath="";
            String imagePath=croppedImage.getPath();

            Bitmap bit= BitmapFactory.decodeFile(imagePath);
            bit = BitmapCompressUtils.imageZoom(bit, 310.00);
            Drawable drawable1 =new BitmapDrawable(bit);

            Drawable drawable=Drawable.createFromPath(imagePath);

            drawView.setBackground(drawable1);

//            File file = new File(imagePath);
//            if (!file.exists()) {
//                Toast.makeText(LupinActivity.this, "文件不存在", Toast.LENGTH_LONG).show();
//            }
//            if (file.length() > 3.5 * 1024 * 1024) {
//                Log.e("======文件大小",file.length() +"");
//                Toast.makeText(LupinActivity.this, "文件太大了", Toast.LENGTH_LONG).show();
//                return;
//            }

//            Drawable drawable=Drawable.createFromPath(imagePath);
//            drawView.setBackground(drawable);
            Toast.makeText(LupinActivity.this, imagePath, Toast.LENGTH_LONG).show();

            //        CropImageIntentBuilder cropImage = new CropImageIntentBuilder(400, 400,
//                croppedImage);
//        // 设置截图框的颜色
//        cropImage.setOutlineColor(0xFF378dcc).setSquareCrop(true);
//        // 设置截图框的形状
//        // cropImage.setCircleCrop(true);
//        if (resultCode == 10) {
//            if (data != null) {
//                cropImage.setSourceImage(data.getData());
//                startActivityForResult(cropImage.getIntent(this),
//                        REQUEST_CROP_PICTURE);
//            }
//        } else {
//            cropImage.setSourceImage(Uri.fromFile(outFile));
//            startActivityForResult(cropImage.getIntent(this),
//                    REQUEST_CROP_PICTURE);
//        }
        }else {
            Toast.makeText(LupinActivity.this, "error", Toast.LENGTH_LONG).show();
        }
//        try {
//            WallpaperManager manager=WallpaperManager.getInstance(this);
//            manager.setBitmap(bit);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }吧

    }

    @OnClick(R.id.iv_back)
    public void goBack() {
        finish();
    }

}
