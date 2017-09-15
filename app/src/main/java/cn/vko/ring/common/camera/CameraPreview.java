package cn.vko.ring.common.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.shikh.utils.BitmapUtils;
import cn.shikh.utils.ViewUtils;
import cn.vko.ring.common.listener.ITakePictureListener;
import cn.vko.ring.utils.FileUtil;

public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback{

	private String TAG = "CameraPreview";
	/**
	 * Surface的控制器，用来控制预览等操作
	 */
	private SurfaceHolder mHolder;
	/**
	 * 相机实例
	 */
	private Camera mCamera = null;
	/**
	 * 图片处理
	 */
	public static final int MEDIA_TYPE_IMAGE = 1;
	/**
	 * 预览状态标志
	 */
	private boolean isPreview = false;
	/**
	 * 闪光灯状态标志
	 */
	private boolean isFlash = false;

	private int screenWidth, screenHight;
	/**
	 * 是否支持自动聚焦，默认不支持
	 */
	private Boolean isSupportAutoFocus = false;
	/**
	 * 获取当前的context
	 */
	private Context mContext;
	/**
	 * 当前传感器的方向，当方向发生改变的时候能够自动从传感器管理类接受通知的辅助类
	 */
	MyOrientationDetector cameraOrientation;
	private int cameraPosition = 1;// 0代表前置摄像头，1代表后置摄像头

	private ITakePictureListener listener;

	public void setOnITakePictureListener(ITakePictureListener listener) {

		this.listener = listener;
	}

	private GestureDetector mGestureDetector;

	@SuppressLint("WrongCall")
	@SuppressWarnings("deprecation")
	public CameraPreview(Context context) {

		super(context);
		this.mContext = context;
		screenWidth = ViewUtils.getScreenWidth(mContext);
		screenHight = ViewUtils.getScreenHeight(mContext);
		isSupportAutoFocus = context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_AUTOFOCUS);
		mHolder = getHolder();
		// 兼容android 3.0以下的API，如果超过3.0则不需要设置该方法
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		mHolder.addCallback(this);// 绑定当前的回调方法
		// mGestureDetector = new GestureDetector(new gestureListener());
		// //使用派生自
		// mGestureDetector.setOnDoubleTapListener(new doubleTapListener());
	}

	@SuppressLint("NewApi")
	public void switchCamera() {
		// TODO Auto-generated method stub
		CameraInfo cameraInfo = new CameraInfo();
		int cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数
		for (int i = 0; i < cameraCount; i++) {
			Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
			if (cameraPosition == 1) {
				// 现在是后置，变更为前置
				if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
					stopPreview();// 停掉原来摄像头的预览
					mCamera.release();// 释放资源
					mCamera = null;// 取消原来摄像头
					mCamera = Camera.open(i);// 打开当前选中的摄像头
					try {
						// mCamera.setDisplayOrientation(90);// 竖屏
						mCamera.setPreviewDisplay(mHolder);// 通过surfaceview显示取景画面
						setCameraParms();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startPreview();// 开始预览
					cameraPosition = 0;
					break;
				}
			} else {
				// 现在是前置， 变更为后置
				if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
																				// CAMERA_FACING_BACK后置
					stopPreview();// 停掉原来摄像头的预览
					mCamera.release();// 释放资源
					mCamera = null;// 取消原来摄像头
					mCamera = Camera.open(i);// 打开当前选中的摄像头
					try {
						// mCamera.setDisplayOrientation(90);// 竖屏
						mCamera.setPreviewDisplay(mHolder);// 通过surfaceview显示取景画面
						setCameraParms();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startPreview();// 开始预览
					cameraPosition = 1;
					break;
				}
			}

		}
	}

	/**
	 * 创建的时候自动调用该方法
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		if (mCamera == null) {
			mCamera = CameraCheck.getCameraInstance(mContext);
		}
		try {
			if (mCamera != null) {
				// mCamera.setDisplayOrientation(90);// 竖屏
				mCamera.setPreviewDisplay(holder);
				// setCameraParms();
				// initPreviewView();
				reAutoFocus();
			}
		} catch (IOException e) {
			if (null != mCamera) {
				mCamera.release();
				mCamera = null;
				isPreview = false;
			}
			e.printStackTrace();
		}finally{
			if(mCamera == null){//异常
				if(callback != null){
					callback.onError();
				}
			}
		}
		// mCamera.startPreview();

	}

	/**
	 * 设置闪光灯
	 */
	public boolean switchFlashMode() {
		Parameters p = mCamera.getParameters();
		if (!isFlash) {
			p.setFlashMode(Parameters.FLASH_MODE_TORCH);
		} else {
			p.setFlashMode(Parameters.FLASH_MODE_OFF);
		}
		isFlash = !isFlash;
		mCamera.setParameters(p);
		return isFlash;
	}

	protected DisplayMetrics getScreenWH() {

		DisplayMetrics dMetrics = new DisplayMetrics();
		dMetrics = this.getResources().getDisplayMetrics();
		return dMetrics;
	}

	/**
	 * 当surface的大小发生改变的时候自动调用的
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		if (mHolder.getSurface() == null) {
			return;
		}
		try {
			// mCamera.setDisplayOrientation(90);// 竖屏
			mCamera.setPreviewDisplay(holder);
			setCameraParms();
			startPreview();
			// reAutoFocus();
		} catch (Exception e) {
			// Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}

	@SuppressLint("NewApi")
	private void setCameraParms() {

		Parameters parameters = mCamera.getParameters();
		parameters.setJpegQuality(100);
		// if
		// (TextUtils.isEmpty(VkoConfigure.getConfigure(mContext).getString("SIZE")))
		// {
		float r = (float) screenWidth / (float) screenHight;
		try{
			Size previews = CamParaUtil.getInstance().getPropPreviewSize(parameters.getSupportedPictureSizes(), r, screenWidth);
			// 设置分辨率
			
			System.out.println("screenWidth="+screenWidth +" width="+getWidth() +" screenHight="+screenHight+" height="+getHeight());
			parameters.setPreviewSize(previews.width, previews.height);
			// }
			Size pictures = CamParaUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(), r, screenWidth);
			parameters.setPictureSize(pictures.width, pictures.height);
		}catch(Exception e){
			
		}
		try {
			mCamera.setParameters(parameters);
		} catch (Exception e) {
			System.out.println("error=" + e.getMessage());
		}

		if (parameters.getMaxNumDetectedFaces() > 0) {
			mCamera.startFaceDetection();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		if (mCamera != null) {
			stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * Call the camera to Auto Focus
	 */
	public void reAutoFocus() {

		if (isSupportAutoFocus) {
			try {
				mCamera.autoFocus(new AutoFocusCallback() {

					@Override
					public void onAutoFocus(boolean success, Camera camera) {

					}
				});
			} catch (Exception e) {
				startPreview();
			}
		}
	}

	/**
	 * 自动聚焦，然后拍照
	 */
	public void takePicture() {
		if (mCamera != null) {
			try {
				mCamera.autoFocus(autoFocusCallback);
			} catch (Exception e) {
				
			}
			
		}
	}

	public void startPreview() {
		synchronized (ALPHA) {
			if (mCamera != null) {
				try {
					mCamera.startPreview();
					reAutoFocus();
				} catch (Exception e) {
					
				}
			}
		}

	}

	public void stopPreview() {

		synchronized (ALPHA) {
			if (mCamera != null) {
				mCamera.stopPreview();
			}
		}

	}

	private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {

		public void onAutoFocus(boolean success, Camera camera) {

			// TODO Auto-generated method stub
			if (success) {
				Log.i(TAG, "autoFocusCallback: success...");
				takePhoto();
			} else {
				Log.i(TAG, "autoFocusCallback: fail...");
				if (isSupportAutoFocus) {
					takePhoto();
				}
			}
		}
	};

	/**
	 * 调整照相的方向，设置拍照相片的方向
	 */
	private void takePhoto() {

		cameraOrientation = new MyOrientationDetector(mContext);
		if (mCamera != null) {
			int orientation = cameraOrientation.getOrientation();
			Parameters cameraParameter = mCamera.getParameters();
			// cameraParameter.setRotation(90);
			// cameraParameter.set("rotation", 90);
			if ((orientation >= 45) && (orientation < 135)) {
				cameraParameter.setRotation(180);
				cameraParameter.set("rotation", 180);
			}
			if ((orientation >= 135) && (orientation < 225)) {
				cameraParameter.setRotation(270);
				cameraParameter.set("rotation", 270);
			}
			if ((orientation >= 225) && (orientation < 315)) {
				cameraParameter.setRotation(0);
				cameraParameter.set("rotation", 0);
			}
			mCamera.setParameters(cameraParameter);
			try {
				mCamera.takePicture(shutterCallback, pictureCallback, mPicture);
			} catch (Exception e) {
				Toast.makeText(mContext, "takePicture==="+e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	private ShutterCallback shutterCallback = new ShutterCallback() {

		@Override
		public void onShutter() {

			// TODO Auto-generated method stub
		}
	};

	private PictureCallback pictureCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {

			// TODO Auto-generated method stub

		}
	};
	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			new SavePictureTask().execute(data);
			mHandler.sendEmptyMessage(0);
			// mCamera.stopPreview();
		}
	};

	public class SavePictureTask extends AsyncTask<byte[], String, File> {

		@SuppressLint("SimpleDateFormat")
		@Override
		protected File doInBackground(byte[]... params) {

			File pictureFile = null;
			Bitmap b = null;
			if (null != params[0]) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeByteArray(params[0], 0, params[0].length,
						options);
				options.inJustDecodeBounds = false;
				options.inSampleSize = BitmapUtils.computeSampleSize(options,
						-1, screenWidth * screenHight);
				try {
					b = BitmapFactory.decodeByteArray(params[0], 0,
							params[0].length, options); // 返回缩略图
				} catch (OutOfMemoryError e) {
					Toast.makeText(mContext, "error===outofMemoryError", Toast.LENGTH_SHORT).show();
				}
			}
			if (null != b) {
				// 设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation",
				// 90)失效。
				// 图片竟然不能旋转了，故这里要旋转下
				if (b.getWidth() > b.getHeight()) {
					if (cameraPosition != 1) {// 前置
						b = BitmapUtils.rotateBitmapByDegree(b, 180);
					}
				} else {
					if (cameraPosition != 1) {// 前置
						b = BitmapUtils.rotateBitmapByDegree(b, 270);
					} else {
						b = BitmapUtils.rotateBitmapByDegree(b, 90);
					}
				}
				pictureFile = saveBitmap(b);
			}
			return pictureFile;
		}

		@Override
		protected void onPostExecute(File result) {

			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null && listener != null) {
				listener.onTokePicture(result);
			}
		}
	}

	/**
	 * 保存Bitmap到sdcard
	 * 
	 * @param b
	 */
	public File saveBitmap(Bitmap b) {
		if(b == null) return null;
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
		String filename = format.format(date) + ".jpg";
		File fileFolder = FileUtil.getCameraDir();
		File pictureFile = new File(fileFolder, filename);
		try {
			FileOutputStream fout = new FileOutputStream(pictureFile);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "saveBitmap:失败");
			e.printStackTrace();
		}
		return pictureFile;

	}

	int count;
	long firClick, secClick, lastDownTime, eventTime;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		reAutoFocus();
		return true;
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:
				startPreview();// 重新开始预览
				break;
			}
		};
	};
	
	private Callback callback;

	public void setCallback(Callback callback) {

		this.callback = callback;
	}

	public static interface Callback {
		void onError();
	}
}