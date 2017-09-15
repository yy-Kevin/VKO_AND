package cn.vko.ring.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * Created by shikh on 2016/6/6.
 */
public class LinearLayoutForListView extends LinearLayout {
    private BaseAdapter adapter;
    private OnClickListener onClickListener = null;

    public LinearLayoutForListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    /**
     * 绑定布局
     */
    public void bindLinearLayout() {
        int count = adapter.getCount();
        this.removeAllViews();
        for (int i = 0; i < count; i++) {
            View v = adapter.getView(i, null, null);
            v.setOnClickListener(this.onClickListener);
            addView(v, i);
        }
        Log.v("countTAG", "" + count);
    }
}
