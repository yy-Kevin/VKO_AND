package cn.vko.ring.classgroup.activity;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import java.net.URL;

import butterknife.OnClick;
import cn.shikh.utils.SystemBarTintManager;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseActivity;

public class LocalVideoActivity extends BaseActivity {
    private String playUrl;

    private VideoView videoView;
    private ImageView video_local_begin;
    private ImageView iv_back;

    public SystemBarTintManager tintManager;

    @Override
    public int setContentViewResId() {
        return R.layout.activity_local_video;
    }

    @Override
    public void initView() {
        // 创建状态栏的管理实例
        tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 设置一个状态栏资源
        tintManager.setStatusBarTintResource(R.color.black);

        video_local_begin= (ImageView) findViewById(R.id.video_local_begin);
        iv_back= (ImageView) findViewById(R.id.iv_back);
        videoView= (VideoView) findViewById(R.id.video_local_view);
    }

    @Override
    public void initData() {
        playUrl=getIntent().getExtras().getString("PLAYURL");

    }

    @OnClick(R.id.video_local_begin)
    public void goPlayVideo(){
//        videoView.setVideoPath(path);
        Log.e("====palyUrl",playUrl);
        videoView.setVideoURI(Uri.parse(playUrl));
        video_local_begin.setVisibility(View.GONE);
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                video_local_begin.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void sayBack(){
        finish();
    }
}
