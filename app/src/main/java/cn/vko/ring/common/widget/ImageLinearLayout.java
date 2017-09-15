package cn.vko.ring.common.widget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.shikh.utils.BitmapUtils;
import cn.vko.ring.R;
import cn.vko.ring.circle.activity.PhotoGalleryActivity;
import cn.vko.ring.utils.FileUtil;
import cn.vko.ring.utils.ImageUtils;

public class ImageLinearLayout extends LinearLayout {

	private Context context;
	private int imageSize = 200;
	private ArrayList<String> urls = new ArrayList<String>();
	public ImageLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public ArrayList<String> getUrls() {
		return urls;
	}
	public void cleanView() {
		if(urls != null && urls.size() >0){
			removeViews(0, urls.size());
			urls.clear();
		}
	}
	public void addImageAndDelView(final String url, int max) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		if (urls.size() == max) {
			Toast.makeText(context,"最多只能发表" + max + "张图片",Toast.LENGTH_LONG).show();
			return;
		}
		urls.add(url);
		final View child = View.inflate(context, R.layout.layout_image_view,
				null);
		RoundAngleImageView ivThumb = (RoundAngleImageView) child
				.findViewById(R.id.iv_thumb);

		ImageView ivDel = (ImageView) child.findViewById(R.id.iv_del);
		// FragmentActivity.LayoutParams lp = new
		// RelativeLayout.LayoutParams(imageSize, imageSize);
		// lp.setMargins(20, 20, 0, 0);
		// ivThumb.setLayoutParams(lp);
		Bitmap bm = getThumbnail(url, 200);
		if (bm != null) {
			ivThumb.setImageBitmap(bm);
		}
		ivThumb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhotoGalleryActivity.startListUrl(context, urls, urls.indexOf(url));

			}
		});
		ivDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File f = new File(url);
				if (f.getParent().endsWith(FileUtil.CAMERA)) {
					f.delete();
				}
				urls.remove(url);
				removeView(child);
				if (listener != null) {
					listener.onUpdate(urls);
				}
			}
		});
		addView(child, urls.size() - 1);
		if (listener != null) {
			listener.onUpdate(urls);
		}
	}

	public Bitmap getThumbnail(String url, int width) {
		if (TextUtils.isEmpty(url))
			return null;
		Bitmap bitmap = BitmapUtils.getBitmapPreview(url, width, width, 0);
		if (bitmap == null)
			return null;
		Bitmap bm = BitmapUtils.toRoundCorner(bitmap, 10);
		return bm;
	}

	public void addImageView(final String url,boolean isClick,int defaultImage) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		urls.add(url);
		final View child = View.inflate(context, R.layout.layout_image_view,
				null);
		RoundAngleImageView ivThumb = (RoundAngleImageView) child
				.findViewById(R.id.iv_thumb);

		ImageView ivDel = (ImageView) child.findViewById(R.id.iv_del);
		// FragmentActivity.LayoutParams lp = new
		// RelativeLayout.LayoutParams(imageSize, imageSize);
		// ivThumb.setLayoutParams(lp);
		if(isClick){
			ivThumb.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PhotoGalleryActivity.startUrl(context, url);
					
				}
			});
		}
		ivDel.setVisibility(View.GONE);
		if(defaultImage != 0){
			ivThumb.setImageResource(defaultImage);
		}
		ImageUtils.loadPerviewImage(url, 200, 200,ivThumb);
		addView(child);

	}

	public interface IChildViewUpdateListener {
		void onUpdate(List<String> urls);
	}

	private IChildViewUpdateListener listener;

	public void setOnIChildViewUpdateListener(IChildViewUpdateListener listener) {
		this.listener = listener;
	}

}
