package cn.vko.ring.common.listener;

/**
 * Created by shikh on 2016/4/27.
 */
public interface UIDataListener<T> {

    public void onDataChanged(T data);
    public void onErrorHappened(String errorCode, String errorMessage);
}
