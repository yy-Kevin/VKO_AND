package cn.vko.ring.circle.activity;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import cn.shikh.utils.BitmapUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.ViewUtils;
import cn.shikh.utils.photo.PhotoBrowserAdapter;
import cn.shikh.utils.photo.PhotoGallery;
import cn.shikh.utils.photo.PhotoView;
import cn.vko.ring.R;
import cn.vko.ring.circle.model.ImageInfo;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.utils.FileUtil;


public class PhotoGalleryActivity extends BaseActivity {
	@Bind(R.id.tv_photo_num)
	public TextView textNum;
	@Bind(R.id.image_browser_phototgallery)
	public PhotoGallery gallery;
	@Bind(R.id.tv_photo_save)
	public TextView textSave;
	private PhotoBrowserAdapter adapter;
	private List<String> imageFiles = new ArrayList<String>();
	private int index;
	private Bitmap watermark;

	public static void startImageInfo(Context context,
									  List<ImageInfo> imageFiles, int selectIndex) {
		if (null == imageFiles || imageFiles.size() == 0)
			return;
		Bundle bundle = new Bundle();
		bundle.putSerializable("IMAGES", (Serializable) imageFiles);
		bundle.putInt("POSITION", selectIndex);
		StartActivityUtil.startActivity(context, PhotoGalleryActivity.class, bundle,
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
	}

	public static void startUrl(Context context, String imageFile) {
		ArrayList<String> files = new ArrayList<String>(1);
		files.add(imageFile);
		startListUrl(context, files, 0);
	}

	public static void startListUrl(Context context,
			ArrayList<String> imageFiles, int selectIndex) {
		if (null == imageFiles || imageFiles.size() == 0)
			return;
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("FILES", imageFiles);
		bundle.putInt("POSITION", selectIndex);
		StartActivityUtil.startActivity(context, PhotoGalleryActivity.class, bundle,
				Intent.FLAG_ACTIVITY_SINGLE_TOP);
	}

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_photo_gallery;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tintManager.setStatusBarTintResource(R.color.black);
		gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
		gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
		gallery.setDetector(new GestureDetector(this,
				new BroswerSimpleGesture()));

		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				showHeadSelectedTitle(pos + 1, adapter.getCount());

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		watermark = BitmapUtils.readBitMap(this, R.drawable.icon_watermark);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		List<ImageInfo> imagelist = (List<ImageInfo>) bundle
				.getSerializable("IMAGES");
		index = bundle.getInt("POSITION", 0);
		if (imagelist != null && imagelist.size() > 0) {
			for (ImageInfo img : imagelist) {
				imageFiles.add(img.getBurl() != null ? img.getBurl() : img
						.getUrl());
			}
		} else {
			imageFiles = bundle.getStringArrayList("FILES");
		}

		if (imageFiles.size() > 0) {
			showHeadSelectedTitle(index, imageFiles.size());
			if (!imageFiles.get(0).startsWith("http")) {
				textSave.setVisibility(View.GONE);
			}
			adapter = new PhotoBrowserAdapter(this.getApplicationContext(),
					imageFiles);
			gallery.setAdapter(adapter);
			gallery.setSoundEffectsEnabled(true);
			gallery.setSelection(index);
		}

	}

	private void showHeadSelectedTitle(int index, int count) {
		textNum.setText(new StringBuilder().append(index).append("/")
				.append(count).toString());
	}

	private class BroswerSimpleGesture extends SimpleOnGestureListener {
		// 按两下的第二下Touch down时触发
		public boolean onDoubleTap(MotionEvent e) {
			View view = gallery.getSelectedView();
			if (view instanceof PhotoView) {
				PhotoView imageView = (PhotoView) view;
				if (imageView.getScale() > imageView.getMiniZoom()) {
					imageView.zoomTo(imageView.getMiniZoom());
				} else {
					imageView.zoomTo(imageView.getMaxZoom());
				}

			} else {

			}
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			finish();
			return true;
		}
	}

	@OnClick(R.id.tv_photo_save)
	void saySavePhoto() {
		View view = gallery.getSelectedView();
		String path = imageFiles.get(index);
		if (view instanceof PhotoView) {
			PhotoView imageView = (PhotoView) view;
			Bitmap bm = imageView.getImageBitmap();
			if (bm != null) {
				bm = BitmapUtils.watermarkBitmap(bm, watermark, null);
			}
			if (bm != null) {
				try {
					File file = new File(FileUtil.getCameraDir(),
							FileUtil.getFileName(path));
					BitmapUtils.saveBmpToSd(bm, file, 100);
					Toast.makeText(this, "已保存到" + file.getAbsolutePath(), 0).show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
