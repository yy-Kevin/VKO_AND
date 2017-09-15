package cn.vko.ring.study.listener;

public interface ILockSuccessListener<T> {
	void onLock(T t);
}
