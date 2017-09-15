package cn.vko.ring.im.session.activity.lupin;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by A on 2016/12/29.
 */
public class PromptSharedPreferences {

    private SharedPreferences sp;

    // 保存
    public void saveSharedPreferences(Context context, String save) {
        sp = context.getSharedPreferences("prompt", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("state", save);
        editor.commit();// 保存新数据
    }

    // 取出
    public boolean takeSharedPreferences(Context context) {
        String str = "";
        sp = context.getSharedPreferences("prompt", context.MODE_PRIVATE);
        str = sp.getString("state", "");
        if (str.equals("")) {
            return false;
        }else{
            return true;
        }
    }
}
