package cn.vko.ring.mine.adapter;

import java.util.List;

import android.view.ViewGroup;
import android.webkit.WebView;

public class MyErrorAdapter extends BasePagerAdapter<WebView> {

	private List<WebView> list;
	public MyErrorAdapter(List<WebView> list) {
		super(list);
		this.list = list;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(list.get(position));
		return list.get(position);
	}
	
	

}
