package cn.vko.ring.home.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.vko.ring.R;
import cn.vko.ring.home.listener.IMenuItemClickListener;
import cn.vko.ring.home.model.TabMenuInfo;

public class TabMenuView extends LinearLayout {
	
  public TabMenuView(Context context, AttributeSet attrs) {
    super(context, attrs);
    // TODO Auto-generated constructor stub
    this.context = context;
  }
  
  private List<TabMenuInfo> items;
  private Context context;
  private IMenuItemClickListener listener;
  public void reset(){
    if (items!=null)
      items.clear();
    removeAllViews();
  }
  
  public void addMenuItem(TabMenuInfo item, final int poistion) {
    if (items == null) {
      items = new ArrayList<TabMenuInfo>();
    }
    items.add(item);
    View view = View.inflate(context, R.layout.item_tab_menu, null);
    LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT);
    lp.weight = 1;
   
    TextView textView = (TextView) view.findViewById(R.id.menu_text_m);
    ImageView imageView = (ImageView) view.findViewById(R.id.menu_image_m);
    view.setLayoutParams(lp);
    imageView.setImageResource(item.getCurrImageId());
    // 如果 有文本 设置文本 否则隐藏
    if (item.getTextId() > 0) {
      textView.setVisibility(View.VISIBLE);
      textView.setText(item.getTextId());
      textView.setTextColor(item.getCurrTextColor());
//      view.setPadding(0, 50, 0, 0);
    } else {
      textView.setVisibility(View.GONE);
    }
   
    view.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        if (listener != null) {
          listener.onMenuItemClick(poistion);
        }
      }
    });
    addView(view);
  }

  public void setSelectIndex(int index) {
    if (null != this.items) {
      // System.out.println("index=="+index +" size=="+items.size());
      try {
        for (int i = 0; i < items.size(); i++) {
          items.get(i).setFocused(i == index);
          if (getChildAt(i)!=null){
            ImageView iv = (ImageView) getChildAt(i).findViewById(R.id.menu_image_m);
            iv.setImageResource(items.get(i).getCurrImageId());
            TextView menuTxt = (TextView)getChildAt(i).findViewById(R.id.menu_text_m);
            menuTxt.setTextColor(context.getResources().getColor(items.get(i).getCurrTextColor()));
            if(i == index){
              menuTxt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
            }else{
              menuTxt.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//加粗
            }
          }
        }
      } catch (Resources.NotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  public void clearView(){
    items.clear();
    removeAllViews();
  }
  public void showDot(int position){
	  ImageView iv = (ImageView) getChildAt(position).findViewById(R.id.iv_dot);
	  iv.setVisibility(View.VISIBLE);
  }
  
  public void hideDot(int position){
	  ImageView iv = (ImageView) getChildAt(position).findViewById(R.id.iv_dot);
	  iv.setVisibility(View.GONE);
  }
  public List<TabMenuInfo> getItems() {
	return items;
}
  public void setOnMenuClickListener(IMenuItemClickListener listener) {
    this.listener = listener;
  }
 

}
