package cn.shikh.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by shikh on 2016/4/28.
 */
public class StartActivityUtil {

    public static void startActivity(Context context, Class clazz) {
        startActivity(context, clazz, Intent.FLAG_ACTIVITY_SINGLE_TOP);

    }

    public static void startActivity(Context context, Class clazz, int falgs) {

        startActivity(context, clazz, null, falgs);

    }

    public static void startForResult(Activity context, Class clazz, Bundle bundle, int requestCode) {

        Intent intent = new Intent();
        intent.setClass(context, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startActivity(Context context, Class clazz, Bundle bundle, int falgs) {

        Intent intent = new Intent();
        intent.setClass(context, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        if (falgs != -1) {
            intent.setFlags(falgs);
        }
        context.startActivity(intent);
    }

    public static final int CAMERA_PICKED_WITH_DATA = 0x123;
    public static File startCameraForResult(Activity activity,int code){
        File outFile = startCamera(activity);
       return startCameraForResult(activity,code,outFile);
    }
    public static File startCameraForResult(Activity activity,int code,File outFile){
//		File outFile = startCamera(activity);
        Uri imageFileUri = Uri.fromFile(outFile);// 获取文件的Uri
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 跳转到相机Activity
        it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
        activity.startActivityForResult(it, code);
        return outFile;
    }

    public static File startCameraForResult(Activity activity,int code,Fragment fragment){
        File outFile = startCamera(activity);
        Uri imageFileUri = Uri.fromFile(outFile);// 获取文件的Uri
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 跳转到相机Activity
        it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
        fragment.startActivityForResult(it, code);
        return outFile;
    }

    private static File startCamera(Activity activity){
        String fileName = System.currentTimeMillis() + ".jpg";
        File outFile= new File(activity.getCacheDir(),fileName);
        return outFile;
    }
}
