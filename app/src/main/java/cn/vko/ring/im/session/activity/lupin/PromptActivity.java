package cn.vko.ring.im.session.activity.lupin;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.vko.ring.R;

/**
 * Created by A on 2016/12/29.
 */
public class PromptActivity extends Activity {

    private int guideResourceId = 0;// 引导页图片资源id
    private int guideResourceId1 = 1;// 引导页图片资源id
    private PromptSharedPreferences psp;
    @Override
    protected void onStart() {
        super.onStart();
        addGuideImage();// 添加引导页
        addGuideImage1();// 添加引导页
    }

    //显示引导图片
    public void addGuideImage() {
        psp = new PromptSharedPreferences();
        View view = getWindow().getDecorView().findViewById(
                R.id.my_content_view);// 查找通过setContentView上的根布局
        if (view == null)
            return;
        if (psp.takeSharedPreferences(this)) {
            // 有过功能引导，就跳出
            return;
        }
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof FrameLayout) {
            final FrameLayout frameLayout = (FrameLayout) viewParent;
            if (guideResourceId != 0) {
                // 设置了引导图片
                final ImageView guideImage = new ImageView(this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.FILL_PARENT);
                guideImage.setLayoutParams(params);
                guideImage.setScaleType(ImageView.ScaleType.FIT_XY);
                guideImage.setImageResource(guideResourceId);
                guideImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除引导图片
                        frameLayout.removeView(guideImage);
                        //保存记录
                        psp.saveSharedPreferences(PromptActivity.this, "启动了");
                    }
                });

                frameLayout.addView(guideImage);// 添加引导图片

            }
        }
    }

    //显示引导图片
    public void addGuideImage1() {
        psp = new PromptSharedPreferences();
        View view = getWindow().getDecorView().findViewById(
                R.id.my_content_view);// 查找通过setContentView上的根布局
        if (view == null)
            return;
        if (psp.takeSharedPreferences(this)) {
            // 有过功能引导，就跳出
            return;
        }
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof FrameLayout) {
            final FrameLayout frameLayout = (FrameLayout) viewParent;
            if (guideResourceId1 != 0) {
                // 设置了引导图片
                final ImageView guideImage = new ImageView(this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.FILL_PARENT);
                guideImage.setLayoutParams(params);
                guideImage.setScaleType(ImageView.ScaleType.FIT_XY);
                guideImage.setImageResource(guideResourceId1);
                guideImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除引导图片
                        frameLayout.removeView(guideImage);
                        //保存记录
                        psp.saveSharedPreferences(PromptActivity.this, "启动了");
                    }
                });

                frameLayout.addView(guideImage);// 添加引导图片

            }
        }
    }

    //获得图片id
    protected void setGuideResId(int resId) {
        this.guideResourceId = resId;
    }
    protected void setGuideResId1(int resId1) {
        this.guideResourceId1 = resId1;
    }


}