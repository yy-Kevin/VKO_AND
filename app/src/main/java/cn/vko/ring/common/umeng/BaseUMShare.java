package cn.vko.ring.common.umeng;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;

import java.util.HashMap;
import java.util.Map;

import cn.vko.ring.R;
import cn.vko.ring.utils.ImageUtils;

/**
 * UM SHARE
 * Created by shikh on 2016/5/30.
 */
public class BaseUMShare implements UMShareListener{

    private Activity mAct;
    private UMShareListener umShareListener;
    private SHARE_MEDIA platform;
    private UMShareAPI mShareAPI = null;
    private Map<SHARE_MEDIA,String> platName = new HashMap<>();
    public BaseUMShare(Activity mAct,SHARE_MEDIA platform){
        this.mAct = mAct;
        this.platform = platform;
//        this.umShareListener = umShareListener;
        mShareAPI = UMShareAPI.get(mAct);
        initMap();

    }

    private void initMap() {
        platName.put(SHARE_MEDIA.WEIXIN,"微信");
        platName.put(SHARE_MEDIA.WEIXIN_CIRCLE,"朋友圈");
        platName.put(SHARE_MEDIA.QQ,"QQ");
        platName.put(SHARE_MEDIA.QZONE,"QQ空间");
    }

    public void setShareListener(UMShareListener listener){
        this.umShareListener = listener;
    }

    public void shareByImgId(String title, String content, int imgId,String url) {
        shareImage(title, content,new UMImage(mAct, imgId), url);
    }

    public void shareByImgLink(String title, String content, String imgLink, String url) {
        shareImage(title, content, new UMImage(mAct, imgLink), url);
    }

    public void shareByImgPath(String title, String content,
                                  String imgPath, String url) {
        shareImage(title, content, getImg(imgPath, 0, 0), url);
    }

    public void shareByBitmap(String title,String content, Bitmap bm, String url) {
        shareImage(title, content, new UMImage(mAct, bm), url);
    }

    public void shareImage(String title,String content,UMImage image,String url){
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(mAct,"设置分享地址不能为null",Toast.LENGTH_SHORT).show();
            return;
        }
            if(!mShareAPI.isInstall(mAct,platform)){
                Toast.makeText(mAct,"未安装软件",Toast.LENGTH_SHORT).show();
                return;
            }

        new ShareAction(mAct).setPlatform(platform).setCallback(this)
                .withMedia(image)
                .withTitle(title)
                .withTargetUrl(url)
                .withText(content)
                .withExtra(new UMImage(mAct, R.drawable.ic_logo))
                .share();
    }

    public void shareAction(ShareAction shareContent){
        if (shareContent == null) {
            Toast.makeText(mAct,"设置分享内容不能为null",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!mShareAPI.isInstall(mAct,platform)){
            Toast.makeText(mAct,"未安装软件",Toast.LENGTH_SHORT).show();
            return;
        }
        shareContent.setPlatform(platform).setCallback(this);
        shareContent.share();
    }
   public void shareVideo(String title, String content, String url,String thumb){
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(mAct,"设置分享地址不能为null",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!mShareAPI.isInstall(mAct,platform)){
            Toast.makeText(mAct,"未安装软件",Toast.LENGTH_SHORT).show();
            return;
        }
        UMVideo video = new UMVideo(url);
        if(!TextUtils.isEmpty(title)){
            video.setTitle(title);
        }
        if(!TextUtils.isEmpty(thumb)){
            video.setThumb(thumb);
        }
       video.setH5Url(url);
        new ShareAction(mAct).setPlatform(platform).setCallback(this)
                .withMedia(video)
                .withTitle(title)
                .withText(content)
                .withExtra(new UMImage(mAct, R.drawable.ic_logo))
                .share();
    }

    private UMImage getImg(String path, int width, int height) {
        if (width <= 0 || height <= 0) {
            width = 720;
            height = 1280;
        }
        return new UMImage(mAct, ImageUtils.getBitmapForLocal(path, width, height));
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        if(umShareListener != null){
            umShareListener.onResult(share_media);
        }
        if(platform.name().equals("WEIXIN_FAVORITE")){
            Toast.makeText(mAct,platName.get(platform) + " 收藏成功啦",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mAct, platName.get(platform) + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        if(umShareListener != null){
            umShareListener.onError(share_media,throwable);
        }
        Toast.makeText(mAct,platName.get(platform) + " 分享失败啦", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        if(umShareListener != null){
            umShareListener.onCancel(share_media);
        }
        Toast.makeText(mAct,platName.get(platform) + " 分享取消了", Toast.LENGTH_SHORT).show();
    }

}
