package cn.vko.ring.circle.activity;

import java.io.File;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;

import cn.shikh.crop.camera.CropImageIntentBuilder;
import cn.shikh.utils.BitmapUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.okhttp.OkHttpUtils;
import cn.shikh.utils.okhttp.callback.StringCallback;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import cn.vko.ring.circle.event.Event;
import cn.vko.ring.circle.event.EventManager;

import cn.vko.ring.circle.model.GroupClassInfo;
import cn.vko.ring.circle.model.GroupInfo;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.common.listener.IClickAlertListener;
import cn.vko.ring.common.widget.RoundAngleImageView;
import cn.vko.ring.common.widget.dialog.ChosePicDialog;
import cn.vko.ring.mine.model.BaseResultInfo;
import cn.vko.ring.utils.FileUtil;
import cn.vko.ring.utils.ImageUtils;
import okhttp3.Call;

public class SettingGroupInfoActivity extends BaseActivity {
	private static int REQUEST_CROP_PICTURE = 1 << 2;
	@Bind(R.id.tv_title)
	public TextView tvTitle;
	@Bind(R.id.iv_group_avatar)
	public RoundAngleImageView ivAvatar;

	@Bind(R.id.tv_group_no)
	public TextView tvGroupNo;
	@Bind(R.id.tv_class)
	public TextView tvGroupClass;
	@Bind(R.id.tv_group_name)
	public TextView tvGroupName;

	private GroupInfo group;
	private ChosePicDialog mDialog;
	private File picFile, saveFile;
	private GroupClassInfo zlass;
	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_setting_group;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		tvTitle.setText("群设置");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		group = (GroupInfo) getIntent().getExtras().get("GROUP");
		if (group == null)
			return;
		ImageUtils.loadPerviewImage(group.getHeadImg(),
				80, 80,ivAvatar);
		tvGroupNo.setText(group.getId());
		tvGroupClass.setText(group.getTypeName());
		tvGroupName.setText(group.getName());
	}

	@OnClick(R.id.iv_back)
	void sayBack() {
		finish();
	}

	@OnClick(R.id.layout_avatar)
	void sayUpdateAvatar() {
		if (mDialog == null) {
			mDialog = new ChosePicDialog(this);
			mDialog.setCanceledOnTouchOutside(true);
			mDialog.setOnClickAlertListener(new IClickAlertListener() {

				@Override
				public void onClick(int type) {
					// TODO Auto-generated method stub
					if (type == 1) {
						takeCamear();
					} else {
						getPhotoPicture();
					}
				}
			});
		}
		mDialog.show();
	}

	// 调用图库,选择头像
	protected void getPhotoPicture() {
		// TODO Auto-generated method stub
		Intent intent;
		intent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, 10);
	}

	// 调用相机拍头像
	protected void takeCamear() {
		picFile = new File(FileUtil.getCameraDir(), "head.jpg");
		StartActivityUtil.startCameraForResult(this, 20, picFile);
	}

	@OnClick(R.id.layout_group_class)
	void sayUpdateClass() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("GROUP", group);
		StartActivityUtil.startForResult(this, CreateGroupTwoActivity.class, bundle,
				200);
	}

	@OnClick(R.id.layout_group_intro)
	void sayUpdateIntro() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("GROUP", group);
		bundle.putInt("TYPE", 1);
		StartActivityUtil.startForResult(this, GroupIntroActivity.class, bundle, 210);
	}

	@OnClick(R.id.layout_group_name)
	void sayUpdateName() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("GROUP", group);
		StartActivityUtil.startForResult(this, GroupNameUpdateActivity.class, bundle,220);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 10 || requestCode == 20) {
				startCropView(data, requestCode);
				return;
			}
			if (data != null && requestCode == REQUEST_CROP_PICTURE) {
				updateGroupInfo();
				return;
			}
			if (data != null && requestCode == 200) {
				zlass = (GroupClassInfo) data.getExtras().get("CLASSINFO");
				if (zlass != null) {
					tvGroupClass.setText(zlass.getName());
					group.setGroupClassInfo(zlass);
					notifyChange();
				}
				return;
			}
			if (data != null && requestCode == 210) {
				String intro = data.getExtras().getString("INTRO");
				group.setIntroduction(intro);
				notifyChange();
				return;
			}
			if (data != null && requestCode == 220) {
				String name = data.getExtras().getString("NAME");
				group.setName(name);
				tvGroupName.setText(name);
				notifyChange();
				return;
			}

		}
	}

	private void notifyChange(){
		EventManager.fire(new Event<GroupInfo>(group, Event.UPDATE_EVENT));
	}

	private void startCropView(Intent data, int resultCode) {
		saveFile = new File(FileUtil.getCameraDir(), "head.jpg");
		Uri croppedImage = Uri.fromFile(saveFile);

		CropImageIntentBuilder cropImage = new CropImageIntentBuilder(120, 120,croppedImage);
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
			cropImage.setSourceImage(Uri.fromFile(picFile));
			startActivityForResult(cropImage.getIntent(this),
					REQUEST_CROP_PICTURE);
		}

	}

	private void updateGroupInfo(){
		File file = BitmapUtils.scalBitmap(saveFile.getAbsolutePath(), 10*1024,FileUtil.getCameraDir());
		OkHttpUtils.post().addFile("file",file.getName(),file).addParams("groupId", group.getId()).url(ConstantUrl.VK_CIRCLE_UPDATE_GROUP).build().execute(new StringCallback() {
			@Override
			public void onError(Call call, Exception e) {

			}

			@Override
			public void onResponse(String result) {
				if(result != null){
					BaseResultInfo response = com.alibaba.fastjson.JSONObject.parseObject(result, BaseResultInfo.class);
					if(response != null && response.getCode() == 0){
						if(response.isData()){
							Bitmap scale = BitmapFactory.decodeFile(saveFile
									.getAbsolutePath());
//						// 把头像变为圆角
//						scale = BitmapUtils.toRoundCorner(scale, 50);
							group.setHeadImg(saveFile.getAbsolutePath());
							ivAvatar.setImageBitmap(scale);
							notifyChange();
						}
					}
				}
			}
		});
	}

}
