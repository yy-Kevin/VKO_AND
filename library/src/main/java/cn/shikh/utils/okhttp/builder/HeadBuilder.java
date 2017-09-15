package cn.shikh.utils.okhttp.builder;

import cn.shikh.utils.okhttp.OkHttpUtils;
import cn.shikh.utils.okhttp.request.OtherRequest;
import cn.shikh.utils.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}
