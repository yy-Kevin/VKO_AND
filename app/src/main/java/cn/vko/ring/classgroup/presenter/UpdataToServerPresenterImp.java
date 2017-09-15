package cn.vko.ring.classgroup.presenter;

import android.content.Context;
import android.net.Uri;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoContext;
import cn.vko.ring.classgroup.model.UpDataResult;
import cn.vko.ring.classgroup.view.UpDataView;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.im.session.extension.CourseMsg;

/**
 * desc:
 * Created by jiarh on 16/5/17 16:25.
 */
public class UpdataToServerPresenterImp {
    private Context mContext;
    private VolleyUtils<UpDataResult> volleyUtils;
    private UpDataView upDataView;

    public UpdataToServerPresenterImp(UpDataView upDataView, Context mContext) {
        this.upDataView = upDataView;
        this.mContext = mContext;
    }

    public void upData(List<CourseMsg> lists, String groupId){
        Map<String,String> params = new HashMap<String,String>();
        if(volleyUtils == null){
            volleyUtils = new VolleyUtils<UpDataResult>(mContext,UpDataResult.class);
        }
        Uri.Builder b = volleyUtils.getBuilder(ConstantUrl.VK_SEND_CLASS);
        b.appendQueryParameter("token", VkoContext.getInstance(mContext).getToken());
        b.appendQueryParameter("targetId", groupId);
        JSONArray group = new JSONArray();
        for (CourseMsg msg :lists){
            JSONObject o = new JSONObject();
            o.put("objId",msg.getObjId());
            o.put("type",msg.getType()+"");
            group.add(o);
        }
        b.appendQueryParameter("groupTaskJson",group.toJSONString());
        volleyUtils.setUiDataListener(new UIDataListener<UpDataResult>() {
            @Override
            public void onDataChanged(UpDataResult data) {

                if (data!=null){
                    upDataView.over(data);
                }
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {
                upDataView.over(null);

            }
        });
        volleyUtils.sendGETRequest(true,b);

    }
}
