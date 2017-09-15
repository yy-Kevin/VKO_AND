package cn.vko.ring.local.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.common.listener.IOnClickItemListener;
import cn.vko.ring.common.widget.dialog.UserVbDialog;
import cn.vko.ring.home.model.BtnInfo;
import cn.vko.ring.mine.activity.MembersCenterActivity;
import cn.vko.ring.mine.activity.PlaceOrderActivity;
import cn.vko.ring.study.model.VideoAttributes;

/**
 * Created by A on 2016/12/8.
 */
public class PayPeiyPresenter {
    private UserVbDialog dialog;
    private Activity ctx;
//    private VideoAttributes vab;
    private String goodsId;
    private double price;
    public void getData(String goodsId,double price){
        this.goodsId=goodsId;
        this.price=price;
        showHintLockDialog(goodsId,price);
    }


    public PayPeiyPresenter(Activity ctx){
        this.ctx = ctx;
    }

//    public void initData(VideoAttributes vab){
//        this.vab = vab;
//        showHintLockDialog();
//    }
    protected void showHintLockDialog(final String goodsId, final double price) {
        // TODO Auto-generated method stub
        if (dialog == null) {
            dialog = new UserVbDialog(ctx);
            dialog.setHeadImage(R.drawable.head_recharge);
            dialog.setOnClickItemListener(new IOnClickItemListener<String>() {

                @Override
                public void onItemClick(int position, String t, View v) {
                    // TODO Auto-generated method stub
                    if(position == 2){
                        dialog.dismiss();
                    }else if(position == 0){
                        Bundle bundle = new Bundle();
                        bundle.putInt("HAVESCORE",0);
                        bundle.putDouble("NEEDSCORE", price*10);
//                        bundle.putDouble("NEEDSCORE", 0.1);
                        bundle.putInt("TYPE", 4);
                        bundle.putString("SUBJECT", "购买培优课");
                        bundle.putString("GOODSID", goodsId);
                        StartActivityUtil.startActivity(ctx,PlaceOrderActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        dialog.dismiss();

                    }else if(position == 1){
                        StartActivityUtil.startActivity(ctx, MembersCenterActivity.class);
                        dialog.dismiss();
                    }
                }
            });
        }
        dialog.setContentText("你需要花费" + price + "元购买本课程");
        dialog.setBtnInfo(new BtnInfo("购买", R.color.white,
                R.drawable.selector_light_blue_button), new BtnInfo("开通会员", R.color.white,
                R.drawable.selector_red_button),new BtnInfo("取消", R.color.white, R.drawable.shape_gray_btn));
        if(!dialog.isShowing()){
            dialog.show();
        }
    }
}
