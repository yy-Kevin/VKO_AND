/*
 *PersonalMessageActivity.java [V 1.0.0]
 *classes : cn.vko.ring.mine.views.PersonalMessageActivity
 *宣义阳 Create at 2015-7-23 上午11:07:34
 */
package cn.vko.ring.mine.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.crop.camera.CropImageIntentBuilder;
import cn.shikh.utils.BitmapUtils;
import cn.shikh.utils.DownloadUtils;
import cn.shikh.utils.ImageCacheUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.okhttp.OkHttpUtils;
import cn.shikh.utils.okhttp.callback.StringCallback;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.VkoConfigure;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.model.ItemDialogModel;
import cn.vko.ring.common.volley.VolleyQueueController;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.RoundAngleImageView;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog.OnSweetClickListener;
import cn.vko.ring.common.widget.dialog.CommonDialog;
import cn.vko.ring.common.widget.dialog.CommonDialog.Builder.OnLvItemClickListener;
import cn.vko.ring.home.activity.BindPhoneActivity;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.fragment.MeFragment;
import cn.vko.ring.mine.model.CityInfo;
import cn.vko.ring.mine.model.DistrictInfo;
import cn.vko.ring.mine.model.MsgInfo;
import cn.vko.ring.utils.FileUtil;
import cn.vko.ring.utils.ImageUtils;
import okhttp3.Call;


import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 */
public class PersonalMessageActivity extends BaseActivity implements
		UIDataListener<CityInfo> {
	private static int REQUEST_CROP_PICTURE = 1 << 2;
	private File f, outFile;
	private Bitmap scale; // 系统相册
	private UserInfo userInfo;
	@Bind(R.id.iv_head)
	public RoundAngleImageView ivHead;
	@Bind(R.id.sexy_layout)
	public RelativeLayout sexyLayout;
	@Bind(R.id.tv_title)
	public TextView tvContent;
	@Bind(R.id.tv_name)
	public TextView tvName;
	@Bind(R.id.tv_account)
	public TextView tvAccount;
	@Bind(R.id.tv_address)
	public TextView tvAddress;
	@Bind(R.id.tv_sex)
	public TextView tvSex;
	@Bind(R.id.tv_school)
	public TextView tvSchool;
	@Bind(R.id.tv_class)
	public TextView tvClass;
	@Bind(R.id.tv_grade)
	public TextView tvGrade;
	@Bind(R.id.tv_phone)
	public TextView tvPhone;
	@Bind(R.id.alter_pass)
	public RelativeLayout alertPwd;
	@Bind(R.id.last_view)
	public View lastView;
	private String countyId; // 地区的id
	private String school, spSchool, spAddress; // 学校名称
	private String proId;
	private String cityId;
	private String areaId;
	public SelectLocationReceiver selectLocationReceiver;
	CommonDialog dialog ;
	@Override
	public int setContentViewResId() {
		return R.layout.activity_personal_message;
	}

	@Override
	public void initView() {
		EventBus.getDefault().register(this);
		selectLocationReceiver = new SelectLocationReceiver();
		registerReceiver(selectLocationReceiver, new IntentFilter(
				"choose_location"));
		boolean isThirdLoginForAlertPwd = VkoConfigure.getConfigure(this)
				.getBoolean("isThirdLoginForAlertPwd", false);
		if (isThirdLoginForAlertPwd) {
			alertPwd.setVisibility(View.GONE);
			lastView.setVisibility(View.GONE);
		} else {
			alertPwd.setVisibility(View.VISIBLE);
			lastView.setVisibility(View.VISIBLE);
		}
	}

	// 接收EventBus传值
	@Subscribe
	public void onEventMainThread(Bitmap b) {
		Log.i("-------------", "执行了图片接收");
		Bitmap head = BitmapUtils.toRoundCorner(b, 10);
		if (head != null && ivHead != null) {
			ivHead.setImageBitmap(head);
		}
		Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void initData() {
		tvContent.setText(R.string.tv_person_msg);
		userInfo = VkoContext.getInstance(this).getUser();
		if (userInfo != null) {
			if (!(TextUtils.isEmpty(userInfo.getMobile()))
					&& userInfo.getMobile().length() == 11) {
				tvPhone.setText(userInfo.getMobile().substring(0, 3) + "***"
						+ userInfo.getMobile().substring(7));
			}
			if (userInfo.getGender() == 0) {
				tvSex.setText("男");
			} else if (userInfo.getGender() == 1) {
				tvSex.setText("女");
			} else {
				tvSex.setText("");
			}
			tvAccount.setText(userInfo.getNick());
			String school = userInfo.getSchool();
			if (!TextUtils.isEmpty(school)) {
				if (school.contains("(") && school.contains(")")) {
					spSchool = school.substring(0, school.indexOf("("));
					spAddress = school.substring(school.indexOf("(") + 1,
							school.indexOf(")"));
				} else {
					String proincee = TextUtils.isEmpty(userInfo
							.getProinceName()) ? "" : userInfo.getProinceName()
							+ " ";
					String city = TextUtils.isEmpty(userInfo.getCityName()) ? ""
							: userInfo.getCityName() + " ";
					String area = TextUtils.isEmpty(userInfo.getAreaName()) ? ""
							: userInfo.getAreaName() + " ";
					spAddress = proincee + city + area;
				}
				tvSchool.setText(spSchool == null ? school : spSchool);
			}
			tvName.setText(userInfo.getName());
			tvGrade.setText(userInfo.getGrade());
			if (userInfo.getClasz() != 0) {
				tvClass.setText(userInfo.getClasz() + "");
			}
			if (spAddress != null && spAddress.length() >= 13) {
				String spAdd = spAddress.substring(0, 11);
				tvAddress.setText(spAdd + "...");
			} else {
				tvAddress.setText(spAddress);
			}
		ImageLoader.ImageListener listener = ImageLoader.getImageListener(
		ivHead, R.drawable.icon_member_default_avatar, R.drawable.icon_member_default_avatar);
			VolleyQueueController.getInstance().getImageLoader().get(userInfo.getSface(), listener, 100, 100);
//			ImageUtils.loadPerviewImage(userInfo.getSface(), 100, 100,ivHead);
		}
	}

	@OnClick(R.id.iv_back)
	public void ivFinish() {
		this.finish();
	}

	/** 修改姓名 */
	@OnClick(R.id.advert_layout)
	public void alterNameClick() {
		Intent nickNameIntent = new Intent(this, NickNameActivity.class);
		nickNameIntent.putExtra("name", tvName.getText().toString().trim());
		startActivityForResult(nickNameIntent, 7);
	}

	/** 修改班级 */
	@OnClick(R.id.class_layout)
	public void alterClassClick() {
		Intent alterClassIntent = new Intent(this, ModifyClassActivity.class);
		alterClassIntent.putExtra("class", tvClass.getText().toString().trim());
		startActivityForResult(alterClassIntent, 8);
	}

	/** 修改性别 */
	@OnClick(R.id.politics_layout)
	public void alterSexClick() {
		Bundle bundle = new Bundle();
		bundle.putInt("GENDER", userInfo.getGender());
		StartActivityUtil.startForResult(this, SelectSexActivity.class, bundle, 4);
	}

	/** 修改密码 */
	@OnClick(R.id.alter_pass)
	public void alterPassClick() {
		Intent intentRlPassword = new Intent(this, AlertPwdActivity.class);
		startActivity(intentRlPassword);
	}

	/** 选择头像 */
	@OnClick(R.id.sexy_layout)
	public void headLayoutClick() {
		showDialogPhoto();
	}

	@OnClick(R.id.iv_head)
	public void headClick() {
		showDialogPhoto();
	}

	/** 选择学校地点 */
	@OnClick(R.id.rl_address)
	public void rlAddressClick() {
		// getProvinceTask();
	}

	/** 二维码 */
	@OnClick(R.id.code_layout)
	public void myCodeClick() {
		// getProvinceTask();
		StartActivityUtil.startActivity(this,My2VCodeActivity.class);
	}

	/** 选择学校 */
	@OnClick(R.id.rl_school)
	public void selectSchoolClick() {
		Intent intent = new Intent(PersonalMessageActivity.this,
				SelectSchoolActivity.class);
		startActivityForResult(intent, 2);
	}

//	@OnClick(R.id.tv_school)
//	public void alertSchoolClick() {
//		Intent intent = new Intent(PersonalMessageActivity.this,
//				SelectSchoolActivity.class);
//		startActivityForResult(intent, 2);
//	}

	/** 手机号 */
	@OnClick(R.id.rl_bind)
	public void bindPhoneClick() {
		// Intent intentBindPhone = new Intent(this, BindPhoneActivity.class);
		// this.startActivity(intentBindPhone);
		Bundle bundle = new Bundle();
		bundle.putString("BIND", "UPDATE");
		StartActivityUtil.startForResult(this, BindPhoneActivity.class, bundle, 9);
	}


	private void showDialogPhoto() {
		dialog = new CommonDialog.Builder(this)
				.items(new ItemDialogModel("拍照"))
				.items(new ItemDialogModel("从相册选择"))
				.items(new ItemDialogModel("取消")).setOnLvItemClickListener(new OnLvItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
						switch (position) {
							case 0:
								takePhoto();

								break;
							case 1:
								getPhotoPicture();
								break;
						}
						dialog.dismiss();
					}
				}).build();

		if (!dialog.isShowing()) {
			dialog.show();
		}
	}
	// 调用图库,选择头像
	protected void getPhotoPicture() {
		// TODO Auto-generated method stub
		Intent intent;
		intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, 10);
	}

	// 调用相机拍头像
	protected void takePhoto() {
		outFile = new File(FileUtil.getCameraDir(), "head.jpg");
		StartActivityUtil.startCameraForResult(this, 20, outFile);
	}

	private void startCropView(Intent data, int resultCode) {


		f = new File(FileUtil.getCameraDir(), "head.jpg");
		Uri croppedImage = Uri.fromFile(f);

		CropImageIntentBuilder cropImage = new CropImageIntentBuilder(400, 400,
				croppedImage);
		// 设置截图框的颜色
		cropImage.setOutlineColor(0xFF378dcc).setSquareCrop(true);
		// 设置截图框的形状
		// cropImage.setCircleCrop(true);
		if (resultCode == 10) {
			if (data != null) {
				cropImage.setSourceImage(data.getData());
				startActivityForResult(cropImage.getIntent(this),
						REQUEST_CROP_PICTURE);
			}
		} else {
			cropImage.setSourceImage(Uri.fromFile(outFile));
			startActivityForResult(cropImage.getIntent(this),
					REQUEST_CROP_PICTURE);
		}

	}

	// 上传头像并缓存
	private void uploadHead(final File f) {
		if (!f.exists()) {
			return;
		}
		final Map<String, String> map = new HashMap<String, String>();
		map.put("token", VkoContext.getInstance(this).getUser().getToken());

		File file = BitmapUtils.scalBitmap(f.getAbsolutePath(),10*1024,FileUtil.getCacheDir());
		System.out.print("token ="+VkoContext.getInstance(this).getUser().getToken() +" path="+file.getAbsolutePath());
		OkHttpUtils.post().params(map).addFile("file",file.getName(),file).url(ConstantUrl.VKOIP
				+ "/user/upload").build().execute(new StringCallback() {
			@Override
			public void onError(Call call, Exception e) {
				Toast.makeText(PersonalMessageActivity.this, "上传失败",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(String result) {
				if (!TextUtils.isEmpty(result)) {
					MsgInfo parseObject = JSONObject.parseObject(result, MsgInfo.class);
					// 问题在此parseObject.isData() = false
					Log.i("-----------", result + parseObject.isData());
					if (parseObject != null && parseObject.isData()) {
						// ivHead.setImageBitmap(BitmapUtils.toRoundBitmap(scale));
						scale = BitmapFactory.decodeFile(f.getAbsolutePath());
						// 把头像变为圆角
						// scale = BitmapUtils.toRoundCorner(scale, 50);
						if(userInfo.getSface() == null){
							userInfo.setSface(f.getAbsolutePath());
							VkoContext.getInstance(PersonalMessageActivity.this).setUser(userInfo);
						}
						ImageCacheUtils.getInstance().addBitmapToCache(userInfo.getSface(), scale);
						File head = new File(DownloadUtils.getInstance().getDownloadDesk(userInfo.getSface(), null));
						if (head.exists()) {
							head.delete();
						}
						// 把头像回传给MineFragment和自己
						EventBus.getDefault().post(scale);
					}
				} else {
					Toast.makeText(PersonalMessageActivity.this, "上传失败",
							Toast.LENGTH_SHORT).show();

				}
			}
		});
	}

	private void getProvinceTask() {
		String url = ConstantUrl.VKOIP + "/base/org";
		VolleyUtils<CityInfo> volleyUtils = new VolleyUtils<CityInfo>(this,CityInfo.class);
		volleyUtils.setUiDataListener(this);
		volleyUtils.sendGETRequest(false,volleyUtils.getBuilder(url));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 10 || requestCode == 20) {
				Log.e("===========", "执行了onActivityResult");
				startCropView(data, requestCode);
				return;
			}
		}
		if (data != null) {
			if ((requestCode == REQUEST_CROP_PICTURE)
					&& (resultCode == RESULT_OK)) {
				uploadHead(f);
				return;
			}

			switch (resultCode) {
			case 2:
				school = data.getStringExtra("school");
				// String[] str = school.split("(");
				userInfo.setSchool(school);
				if (!TextUtils.isEmpty(school)) {
					if (school.contains("(") && school.contains(")")) {
						spSchool = school.substring(0, school.indexOf("("));
						spAddress = school.substring(school.indexOf("(") + 1,
								school.indexOf(")"));
					} else {
						spAddress = userInfo.getProinceName() + " "
								+ userInfo.getCityName() + " "
								+ userInfo.getAreaName();
					}
					tvSchool.setText(spSchool);
				}
				proId = data.getStringExtra("proId");
				cityId = data.getStringExtra("cityId");
				areaId = data.getStringExtra("areaId");

				if (spAddress.length() >= 13) {
					String spAdd = spAddress.substring(0, 11);
					tvAddress.setText(spAdd + "...");
				} else {
					tvAddress.setText(spAddress);
				}
				userInfo.setAreaId(areaId);
				userInfo.setCityId(cityId);
				userInfo.setProvinceId(proId);
				break;
			case 3:
				tvSex.setText(data.getStringExtra("sex"));
				String sexId = data.getStringExtra("sexId");
				userInfo.setGender(Integer.parseInt(sexId));
				break;
			case 7:
				tvName.setText(data.getStringExtra("name"));
				userInfo.setName(data.getStringExtra("name"));
				break;
			case 8:
				tvClass.setText(data.getStringExtra("newClass"));
				userInfo.setClasz(Integer.parseInt(data
						.getStringExtra("newClass")));
				break;
			case 9:
				// 修改手机号
				// tvPhone.setText(data.getStringExtra("newBindPhone"));
				userInfo.setMobile(data.getStringExtra("newBindPhone"));
				if (!(TextUtils.isEmpty(userInfo.getMobile()))) {
					tvPhone.setText(userInfo.getMobile().substring(0, 3)
							+ "***" + userInfo.getMobile().substring(7));
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onDataChanged(CityInfo t) {
		if (t != null) {
			if ("0".equals(t.code)) {
				/*
				 * CityDB cityDB = CityDB.getCityDB(); List<Object> queryForList
				 * = cityDB.queryForList(null, "select * from user_city", new
				 * String[]{}); queryForList.size(); cityDB.execSQL();
				 * ContentValues values = new ContentValues(); for(int i =
				 * 0;i<t.data.size();i++){ values.put("id", t.data.get(i).id);
				 * values.put("name", t.data.get(i).name); }
				 * cityDB.insert("user_city", values); cityDB.execSQL();
				 */
				// Intent intent = new Intent(this, CityListActivity.class);
				// intent.putExtra("cityInfo", t);
				// startActivityForResult(intent, 1);
			}
		}
	}

	@Override
	public void onErrorHappened(String errorCode, String errorMessage) {

	}

	/**
	 * 所有选择了的省市区信息触发的广播接收者
	 * 
	 */
	public class SelectLocationReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				String location = intent.getStringExtra("location");
				// intent.putExtra("preId", preId);
				// intent.putExtra("cityId", cityId);
				// intent.putExtra("countyId", countyId);
				// 省id
				String proinceId = intent.getStringExtra("preId");
				if (proinceId == null) {
					proinceId = "";
				}
				// 市id
				String cityId = intent.getStringExtra("cityId");
				if (cityId == null) {
					cityId = "";
				}
				// 地区的id
				countyId = intent.getStringExtra("areaId");
				VkoConfigure.getConfigure(PersonalMessageActivity.this).put(
						"location", location);
				VkoConfigure.getConfigure(PersonalMessageActivity.this).put(
						"countyId", countyId);
				tvAddress.setText(location);
				// userInfo.setAreaId(countyId);
				String[] split = location.split(" ");
				if (split.length > 0) {
					if (split.length == 2) {
						userInfo.setProinceName(split[0]);
						userInfo.setCityName(split[1]);
						userInfo.setAreaName("");
						userInfo.setProvinceId(proinceId);
						userInfo.setCityId(cityId);
						userInfo.setAreaId(Long.toString(0));
					} else if (split.length == 3) {
						userInfo.setProinceName(split[0]);
						userInfo.setCityName(split[1]);
						userInfo.setAreaName(split[2]);
						userInfo.setProvinceId(proinceId);
						userInfo.setCityId(cityId);
						userInfo.setAreaId(countyId);
					}
				}
			}
		}

	}

	/**
	 * 反注册广播
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (selectLocationReceiver != null) {
			unregisterReceiver(selectLocationReceiver);
		}
		VkoContext.getInstance(this).setUser(userInfo);
		Message msg = Message.obtain();
		// 发送
		msg.obj = userInfo;
		msg.what = 200;
		MeFragment.mHandler.sendMessage(msg);
		EventBus.getDefault().unregister(this);
	}

}
