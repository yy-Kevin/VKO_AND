package cn.vko.ring.study.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.shikh.utils.ViewUtils;
import cn.vko.ring.R;

/**
 * n条波的叠加
 * Created by JiaRH on 2015/11/23.
 */
public class WaveMoveView extends FrameLayout {

    private List<ShipWaveView> shipWaveViews;
    private ShipWaveView swv0,swv1,swv2;
//    private BoatWavePathView boatView;
	private int density = 3;
    public WaveMoveView(Context context) {
        super(context);
        init(context);
    }

    public WaveMoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public WaveMoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
    	density = (int) ViewUtils.getScreenDensity(context);
        shipWaveViews = new ArrayList<ShipWaveView>();
        swv0 = new ShipWaveView(context);
        swv1 = new ShipWaveView(context);
        swv2 = new ShipWaveView(context);

//        boatView = new BoatWavePathView(context);
        int sp = density>1?3:2;
        int sspp = sp;
        if (density==2) {
			sspp=4;
		}
        swv0.setSpeed(3/sspp*density);
        swv1.setSpeed(5/sspp*density);
        swv2.setSpeed(7/sspp*density);

        swv0.setColor(Color.parseColor("#8800a9ff"));
        swv1.setColor(Color.parseColor("#8800a9ff"));
        swv2.setColor(getResources().getColor(R.color.study_top_bg));

        swv0.setWAVE_HEIGHT(40/sp*density);
        swv1.setWAVE_HEIGHT(35/sp*density);
        swv2.setWAVE_HEIGHT(50/sp*density);

        swv0.setmWaveWidth(550/sp*density);
        swv1.setmWaveWidth(600/sp*density);
        swv2.setmWaveWidth(700/sp*density);


//        shipWaveViews.add(swv0);
//        shipWaveViews.add(swv1);
        shipWaveViews.add(swv2);
       for(ShipWaveView swv : shipWaveViews){
           this.addView(swv);
//           swv.startMove();
       }
//        this.addView(boatView);
    }
    
    public void resetAni(){
//    	boatView.resetAni();
    }
    public void stopAni(){
//    	boatView.stopAni();
    }
}
