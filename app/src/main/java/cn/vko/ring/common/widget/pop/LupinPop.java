package cn.vko.ring.common.widget.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.vko.ring.R;
import cn.vko.ring.common.base.BasePopuWindow;

/**
 * Created by A on 2016/12/6.
 */
public class LupinPop extends BasePopuWindow implements View.OnClickListener {

    private TextView tvAgin, updown;
    private View.OnClickListener listener;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public LupinPop(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

    }

    @Override
    public void setUpViews(View contentView) {
        // TODO Auto-generated method stub
        updown = (TextView) contentView.findViewById(R.id.tv_updown);
        tvAgin = (TextView) contentView.findViewById(R.id.tv_agin);
    }

    @Override
    public void setUpListener() {
        // TODO Auto-generated method stub
        updown.setOnClickListener(this);
        tvAgin.setOnClickListener(this);
    }



    @Override
    public int getAnimationStyle() {
        // TODO Auto-generated method stub
        return R.style.zoomAnimation;
    }

    @Override
    public int getResView() {
        // TODO Auto-generated method stub
        return R.layout.pop_lupin;
    }

    @Override
    public boolean updateView(View contentView) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }
}
