package cn.vko.ring.study.activity;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.shikh.crop.camera.CropImageIntentBuilder;
import cn.shikh.utils.StartActivityUtil;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.camera.CameraPreview;
import cn.vko.ring.common.listener.ITakePictureListener;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog.OnSweetClickListener;
import cn.vko.ring.utils.FileUtil;


public class CameraActivity extends BaseActivity implements ITakePictureListener {
	private static int REQUEST_PICTURE = 1 << 1;
	private static int REQUEST_CROP_PICTURE = 1 << 2;

	@Bind(R.id.camera_preview)
	public FrameLayout previewLayout;
	@Bind(R.id.iv_take)
	public ImageView ivTake;
	@Bind(R.id.iv_close)
	public ImageView ivClose;
	@Bind(R.id.iv_local)
	public ImageView ivLocal;
	@Bind(R.id.iv_flash)
	public ImageView ivFlash;

	@Bind({ R.id.iv_close })
	List<View> closeView;
	@Bind({ R.id.iv_local })
	List<View> localView;
	@Bind({ R.id.iv_take })
	List<View> takeView;
	private String from;
	private CameraPreview mCamera;
	private File croppedImageFile;
	SweetAlertDialog dialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			showDialog();
		};
	};

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_camera;
	}
	@Override
	public void onCreateBefore(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateBefore(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去掉Activity上面的状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

	}

	@Override
	protected boolean isEnableSwipe() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initView() {
		hideSorftForce();
		from = getIntent().getExtras().getString("FROM");
		mCamera = new CameraPreview(this);
		mCamera.setCallback(new CameraPreview.Callback() {

			@Override
			public void onError() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
			}
		});
		previewLayout.addView(mCamera, 0);
		mCamera.setOnITakePictureListener(this);

	}

	@OnClick(R.id.iv_take)
	void sayTakePhoto() {
		mCamera.takePicture();
		ButterKnife.apply(takeView, ALPHA_FADE);
	}

	@OnClick(R.id.iv_close)
	void sayClose() {

		finish();
		ButterKnife.apply(takeView, ALPHA_FADE);
	}

	@OnClick(R.id.iv_local)	
	void sayLocalPhoto() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, REQUEST_PICTURE);
		ButterKnife.apply(takeView, ALPHA_FADE);
	}

	@OnClick(R.id.iv_flash)
	void sayFlashModel() {
		if (mCamera.switchFlashMode()) {
			ivFlash.setImageResource(R.drawable.ask_photo_openflashd);
		} else {
			ivFlash.setImageResource(R.drawable.ask_photo_openflash);

		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	private void showDialog() {
		// TODO Auto-generated method stub
		dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
		dialog.setContentText("请检查是否拍照权限被禁");
		dialog.setTitle("拍照异常");
		dialog.setCancelable(false);
		dialog.setConfirmText("OK");
		dialog.setConfirmClickListener(new OnSweetClickListener() {

			@Override
			public void onClick(SweetAlertDialog sweetAlertDialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				CameraActivity.this.finish();
			}
		});
		dialog.show();
	}

	@Override
	public void onTokePicture(File photoFile) {
		// TODO Auto-generated method stub
		croppedImageFile = photoFile;
		Uri croppedImage = Uri.fromFile(croppedImageFile);
		CropImageIntentBuilder cropImage = new CropImageIntentBuilder(5, 2, 0,
				0, croppedImage);
		cropImage.setOutlineColor(0xFF03A9F4);
		cropImage.setSourceImage(Uri.fromFile(photoFile));

		// ACache.get(this).put(key, value);
		startActivityForResult(cropImage.getIntent(this), REQUEST_CROP_PICTURE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == REQUEST_PICTURE) {
			croppedImageFile = new File(FileUtil.getCameraDir(),
					System.currentTimeMillis() + ".jpg");
			Uri croppedImage = Uri.fromFile(croppedImageFile);
			CropImageIntentBuilder cropImage = new CropImageIntentBuilder(5, 2,
					0, 0, croppedImage);
			cropImage.setOutlineColor(0xFF03A9F4);
			cropImage.setSourceImage(data.getData());
			Toast.makeText(this, data.getData() + "", Toast.LENGTH_SHORT)
					.show();
			Log.e("CameraActivity", data.getData() + "");
			startActivityForResult(cropImage.getIntent(this),
					REQUEST_CROP_PICTURE);
		} else if (requestCode == REQUEST_CROP_PICTURE
				&& resultCode == RESULT_OK) {
			if (croppedImageFile.exists()) {
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
						Uri.fromFile(croppedImageFile)));
				Bundle bundle = new Bundle();
				bundle.putString("URL", croppedImageFile.getAbsolutePath());
				if (from.equals("ASK")) {
					setResult(RESULT_OK, new Intent().putExtras(bundle));
				} else if (from.equals("SEARCH")) {
					bundle.putString("FROM", "SEARCH");
					StartActivityUtil.startActivity(this, SearchQuestionActivity.class, bundle, Intent.FLAG_ACTIVITY_SINGLE_TOP);
				}
				croppedImageFile = null;
				finish();
			} else {
				Toast.makeText(this, R.string.exists_pic_hint, Toast.LENGTH_LONG).show();
			}

		} else if (requestCode == REQUEST_CROP_PICTURE
				&& resultCode == RESULT_CANCELED) {
			croppedImageFile = null;
			mCamera.startPreview();
		}
	}

	/*
	 * 
	 * @Description: TODO
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// EventBus.getDefault().post(new EventRefresh());
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// mCamera.stopPreview();
	}
}
