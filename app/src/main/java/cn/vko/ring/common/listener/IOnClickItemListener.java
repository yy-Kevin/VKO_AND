package cn.vko.ring.common.listener;

import android.view.View;

public interface IOnClickItemListener<T> {
	void onItemClick(int position, T t, View v);
}
