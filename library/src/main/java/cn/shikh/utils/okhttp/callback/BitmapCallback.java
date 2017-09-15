package cn.shikh.utils.okhttp.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class BitmapCallback extends Callback<Bitmap>
{
    private int w,h;
    public BitmapCallback(int w,int h){
        this.w = w;
        this.h = h;
    }
    public BitmapCallback(){}
    @Override
    public Bitmap parseNetworkResponse(Response response) throws Exception
    {
        /*if(w*h != 0){
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(response.body().byteStream());
            opts.inSampleSize = computeSampleSize(opts, -1, w * h);
            opts.inJustDecodeBounds = false;
            try {
                Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream(),null,opts);
                if(bmp != null){
                    return bmp;
                }
            } catch (OutOfMemoryError err) {

            }
        }*/
        return BitmapFactory.decodeStream(response.body().byteStream());
    }
    public int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }
    private int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
    double w = options.outWidth;
    double h = options.outHeight;
    int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
            .sqrt(w * h / maxNumOfPixels));
    int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
            Math.floor(w / minSideLength), Math.floor(h / minSideLength));
    if (upperBound < lowerBound) {
        // return the larger one when there is no overlapping zone.
        return lowerBound;
    }
    if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
        return 1;
    } else if (minSideLength == -1) {
        return lowerBound;
    } else {
        return upperBound;
    }
}

}
