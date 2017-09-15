package cn.vko.ring.classgroup.presenter;

import android.content.Context;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import cn.vko.ring.ConstantUrl;
import cn.vko.ring.VkoContext;
import cn.vko.ring.classgroup.model.CourseListData;
import cn.vko.ring.classgroup.view.CourseListView;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.volley.VolleyUtils;

/**
 * desc:
 * Created by jiarh on 16/5/12 11:49.
 */
public class CourseListPresenterImp implements CourseListPrsenter {
    private Context mContext;
    private CourseListView courseListView;
    private VolleyUtils<CourseListData> volleyUtils;

    public CourseListPresenterImp(CourseListView courseListView, Context mContext) {
        this.courseListView = courseListView;
        this.mContext = mContext;
    }

    @Override
    public void getCourseList(String objType,int pageIndex,int pageSize) {
        Map<String,String> params = new HashMap<String,String>();
        if(volleyUtils == null){
            volleyUtils = new VolleyUtils<CourseListData>(mContext,CourseListData.class);
        }
        Uri.Builder b = volleyUtils.getBuilder(ConstantUrl.VK_COURSELIST);
        b.appendQueryParameter("token", VkoContext.getInstance(mContext).getToken());
        b.appendQueryParameter("objType", objType);
        b.appendQueryParameter("pageIndex", pageIndex+"");
        b.appendQueryParameter("pageSize", pageSize+"");
        volleyUtils.setUiDataListener(new UIDataListener<CourseListData>() {
            @Override
            public void onDataChanged(CourseListData data) {
                if (data!=null&&data.getData()!=null&&data.getData().getCourseList()!=null)
                {
                   courseListView.getShowData(data.getData().getPager(),data.getData().getCourseList());
                }else {
                    courseListView.getShowData(null,null);
                }
            }

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {

                courseListView.getShowData(null,null);
            }
        });
        volleyUtils.sendGETRequest(true,b);
    }
}
