package cn.vko.ring.im.session.extension;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

/**
 * desc:测评
 * Created by jiarh on 16/5/3 11:37.
 */
public class NewTestAttachment extends CustomAttachment {


   TestMsg test;
    private static final String TAG = "NewTestAttachment";

    public static String getTAG() {
        return TAG;
    }

    public TestMsg getTest() {
        return test;
    }

    public void setTest(TestMsg test) {
        this.test = test;
    }

    public NewTestAttachment(){
        super(CustomAttachmentType.COURSE);
    }

    public NewTestAttachment(int type, TestMsg test) {
        super(type);
        this.test=test;
    }
   public NewTestAttachment(JSONObject data){
        super(CustomAttachmentType.NEWTEST);
//       parseData(data);
    }

    @Override
    protected void parseData(JSONObject data) {

        Log.d(TAG, "parseData() called with: " + "data = [" + data + "]");
       test = JSONObject.parseObject(data.toJSONString(),TestMsg.class);

    }

    @Override
    protected JSONObject packData() {

      JSONObject data = new JSONObject();
       data.put(TestMsg.TITLE,test.getTitle());
       data.put(TestMsg.TYPE,test.getType());
       data.put(TestMsg.TEACHERNAME,test.getTeacherName());
       data.put(TestMsg.URL,test.getUrl());
        data.put(TestMsg.OBJID,test.getObjId());
        data.put(TestMsg.TASKID,test.getTaskId());

        return data;
    }
}
