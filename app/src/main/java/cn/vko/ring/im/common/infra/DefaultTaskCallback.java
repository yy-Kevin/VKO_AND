package cn.vko.ring.im.common.infra;

public interface DefaultTaskCallback {
    public void onFinish(String key, int result, Object attachment);
}