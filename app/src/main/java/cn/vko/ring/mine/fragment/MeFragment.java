package cn.vko.ring.mine.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri.Builder;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;
import cn.shikh.utils.DateUtils;
import cn.shikh.utils.ImageCacheUtils;
import cn.shikh.utils.StartActivityUtil;
import cn.shikh.utils.okhttp.callback.BitmapCallback;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.circle.activity.CircleHomeActivity;
import cn.vko.ring.common.base.BaseFragment;
import cn.vko.ring.common.event.SwitchLearnEvent;
import cn.vko.ring.common.listener.UIDataListener;
import cn.vko.ring.common.umeng.EventCountAction;
import cn.vko.ring.common.volley.VolleyUtils;
import cn.vko.ring.common.widget.RoundAngleImageView;
import cn.vko.ring.home.activity.ScanActivity;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.message.activity.MsgMainActivity;
import cn.vko.ring.message.activity.RecommendMsgDetailActivity;
import cn.vko.ring.mine.activity.ExchangeCodeActivity;
import cn.vko.ring.mine.activity.FeedBackActivity;
import cn.vko.ring.mine.activity.FormularRemActivity;
import cn.vko.ring.mine.activity.MembersCenterActivity;
import cn.vko.ring.mine.activity.MyClassActivity;
import cn.vko.ring.mine.activity.MyFavoriteActivity;
import cn.vko.ring.mine.activity.MyInComeActivity;
import cn.vko.ring.mine.activity.MyOrderActivity;
import cn.vko.ring.mine.activity.MyVideoListDownLoadActivity;
import cn.vko.ring.mine.activity.NotepadActivity;
import cn.vko.ring.mine.activity.PersonalMessageActivity;
import cn.vko.ring.mine.activity.RecommendActivity;
import cn.vko.ring.mine.activity.ScoreActivity;
import cn.vko.ring.mine.activity.SelectGradeActivity;
import cn.vko.ring.mine.activity.SystemActivity;
import cn.vko.ring.mine.model.MemberInfo;
import cn.vko.ring.mine.model.MemberMoney;
import cn.vko.ring.mine.presenter.SignInPresenter;
import okhttp3.Call;

import static cn.shikh.utils.okhttp.log.LoggerInterceptor.TAG;


public class MeFragment extends BaseFragment {
	@Bind(R.id.tv_exchange)
	public TextView tvExchange;
	@Bind(R.id.tv_name)
	public TextView tvName;
	@Bind(R.id.tv_school)
	public TextView tvSchool;
	@Bind(R.id.tv_class)
	public TextView tvClass;
	@Bind(R.id.iv_avatar)
	public RoundAngleImageView ivAvatar;
	@Bind(R.id.iv_vip)
	public ImageView ivVip;
	@Bind(R.id.iv_sign)
	public ImageView ivSingIn;
	@Bind(R.id.tv_vb_num)
	public TextView tvJifen;
	@Bind(R.id.scroll_view)
	public ScrollView scrollView;
	@Bind(R.id.rl_head)
	public LinearLayout layoutHead;

	@Bind(R.id.income_layout)
	public RelativeLayout layoutIncome;
	@Bind(R.id.tv_income_num)
	public TextView income_num;

	private VkoContext vkoContext;
	private VolleyUtils<MemberInfo> volleyUtils;
	private VolleyUtils<MemberMoney> volleyUtils1;
	private SignInPresenter signPresenter;

	private UserInfo user;
	private boolean isSetAvatar;
	private long endTime;
	private long nowTime;
	public static Handler mHandler;

	public void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 200:
					user = (UserInfo) msg.obj;
					initOneInfo();
					break;
				}
			}
		};
	}

	@Override
	public int setContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_me1;
	}

	@Override
	public void initView(View root) {
		// TODO Auto-generated method stub
//		scrollView.setBackgroundColor(Color.WHITE);
		EventBus.getDefault().register(this);
		vkoContext = VkoContext.getInstance(atx);
		if (!vkoContext.isLogin()) {
			tvName.setText("未登录");
		}
		ivVip.setVisibility(View.GONE);
		initHandler();
		volleyUtils = new VolleyUtils<MemberInfo>(getActivity(),MemberInfo.class);
		volleyUtils1 = new VolleyUtils<MemberMoney>(getActivity(),MemberMoney.class);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		if (signPresenter == null) {
			signPresenter = new SignInPresenter(atx, ivSingIn,tvJifen);
		}
		signPresenter.initView();
		user = VkoContext.getInstance(atx).getUser();
		// vkoConfigure = VkoConfigure.getConfigure(atx);
		initOneInfo();
		refreshData();
	}

	public void initOneInfo() {
		user = VkoContext.getInstance(atx).getUser();
		if (user != null && tvName != null) {
			if(user.getIsTeacher().equals("true")){
				layoutIncome.setVisibility(View.VISIBLE);
			}else{
				layoutIncome.setVisibility(View.GONE);
			}
			initAvatar();
			if (user.getName() != null) {
				tvName.setText(user.getName());
			} else {
				tvName.setText(user.getNick());
			}
			if (user.getClasz() != 0) {
				tvClass.setText(user.getGrade() + user.getClasz() + "班");
			} else if (user.getGrade() != null && !user.getGrade().isEmpty()) {
				Log.i(TAG,"yuyao getgrade " +  user.getGrade());
				tvClass.setText(user.getGrade());
			} else {
				tvClass.setText("");
			}
			String school = user.getSchool();
			if (!TextUtils.isEmpty(school)) {
				if (school.contains("(")) {
					school = school.substring(0, school.indexOf("("));
				}
				tvSchool.setText(school);
			} else {
				tvSchool.setVisibility(View.GONE);
			}

			if (nowTime < endTime) {
				ivVip.setVisibility(View.VISIBLE);
				ivVip.setImageResource(R.drawable.vip);
			} else {
				ivVip.setVisibility(View.GONE);
			}
			tvJifen.setText(user.getScord() + "V币");
		}else{
			layoutIncome.setVisibility(View.GONE);
		}

	}

	public void initAvatar(){
				// TODO Auto-generated method stub
		if (!isSetAvatar && ivAvatar != null) {
				if (user != null && !TextUtils.isEmpty(user.getSface())) {
					ImageCacheUtils.getInstance().loadImage(user.getSface(), new BitmapCallback() {
						@Override
						public void onError(Call call, Exception e) {
						}
						@Override
						public void onResponse(final Bitmap response) {
							if(ivAvatar == null) return;
							ivAvatar.setImageBitmap(response);
							layoutHead.setBackgroundResource(R.drawable.shape_change_color);
						}
					});

				}
				isSetAvatar = true;
		}

	}

	/*
	* 获取老师money
	*/
	private void getMemberPrice(){
        String url = ConstantUrl.VKOIP + "/tx/getUserWealth";
        Builder builder = volleyUtils1.getBuilder(url);
        builder.appendQueryParameter("token", VkoContext.getInstance(atx).getToken());
        volleyUtils1.sendGETRequest(true,builder);
        volleyUtils1.setUiDataListener(new UIDataListener<MemberMoney>() {
            @Override
            public void onDataChanged(MemberMoney response) {
				if (response != null && response.getCode() == 0 && response.getData() != null) {
					income_num.setText(response.getData().getCanWithdraw());

				}
			}

            @Override
            public void onErrorHappened(String errorCode, String errorMessage) {

            }
        });
	}

	/**
	 * 获取会员信息
	 */
	private void getMemberTask() {
		// TODO Auto-generated method stub
		String url = ConstantUrl.VKOIP + "/user/memberInfo";
		Builder builder = volleyUtils.getBuilder(url);
		builder.appendQueryParameter("token", VkoContext.getInstance(atx).getToken());
		volleyUtils.sendGETRequest(true,builder);
		volleyUtils.setUiDataListener(new UIDataListener<MemberInfo>() {
			@Override
			public void onDataChanged(MemberInfo response) {
				if (response != null && response.getCode() == 0 && response.getData() != null) {
					nowTime = response.getStime();
					if (!TextUtils.isEmpty(response.getData()
							.getExpire())) {
						endTime = DateUtils.stringToLong(response
								.getData().getExpire());
					}
					if (user != null) {
						user.setScord(response.getData().getScord());
						user.setLevel(response.getData().getLevel());
						user.setExpire(response.getData().getExpire());
						VkoContext.getInstance(atx).setUser(user);
					}
					try {
						if (atx == null || atx.isFinishing())
							return;
						initOneInfo();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

			@Override
			public void onErrorHappened(String errorCode, String errorMessage) {
//				if (!isSetAvatar && ivAvatar != null) {
//					ImageUtils.loadPerviewImage(user.getSface(), 100, 100, ivAvatar);
//					isSetAvatar = true;
//				}
//				initOneInfo();

			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		switch (resultCode) {
		case 101:
			if (user.getClasz() != 0) {
				tvClass.setText(data.getStringExtra("grade") + user.getClasz()
						+ "班");
			} else {
				tvClass.setText(data.getStringExtra("grade"));
			}
			String gradeId = data.getStringExtra("gradeId");
//			Log.e("=====gradeId",gradeId);
			String learn = user.getLearn();
			switch (Integer.parseInt(gradeId)) {
			case 0:
				user.setGrade("初一");
				learn = "52";
				break;
			case 1:
				user.setGrade("初二");
				learn = "52";
				break;
			case 2:
				user.setGrade("中考");
				learn = "52";
				break;
			case 3:
				user.setGrade("高一");
				learn = "51";
				break;
			case 4:
				user.setGrade("高二");
				learn = "51";
				break;
			case 5:
				user.setGrade("高考");
				learn = "51";
				break;
			default:
				break;
			}
			if (!gradeId.isEmpty()) {
				if (!gradeId.equals(user.getGradeId())) {// 发送一个广播
					SwitchLearnEvent.fireGrade(gradeId);
				}
				user.setGradeId(gradeId);
				user.setLearn(learn);
				VkoContext.getInstance(atx).setUser(user);
			}
			refreshData();
		}
	}

	// 更新用户信息
	private void refreshData() {
		// TODO Auto-generated method stub
		if (VkoContext.getInstance(atx).isLogin()) {
			getMemberTask();
			getMemberPrice();
		}
	}

	public void updateVipInfo() {
		ivVip.setVisibility(View.VISIBLE);
		ivVip.setImageResource(R.drawable.vip);
	}

	// EventBus回调
	@Subscribe
	public void onEventMainThread(Bitmap b) {
		if (b != null && ivAvatar != null) {
			ivAvatar.setImageBitmap(b);
			isSetAvatar = true;
		}
	}
	@Subscribe
	public void onEventMainThread(String event) {
		if (!TextUtils.isEmpty(event) && event.equals(Constants.OPEN_MEMBER)) {
			ivVip.setVisibility(View.VISIBLE);
			ivVip.setImageResource(R.drawable.vip);
		}
	}

	// 签到
	@OnClick(R.id.iv_sign)
	public void saySignIn() {
		signPresenter.signIn();
	}

	// 我的V币
	@OnClick(R.id.layout_i_vb)
	public void sayMyVB() {
		if (vkoContext.doLoginCheckToSkip(atx)) {
			return;
		}
		StartActivityUtil.startActivity(atx, ScoreActivity.class);

	}

	// 意见反馈
	@OnClick(R.id.tv_feedback)
	public void freeBackClick() {
		if (vkoContext.doLoginCheckToSkip(atx)) {
			return;
		}
		StartActivityUtil.startActivity(atx, FeedBackActivity.class);

	}
	//兑换码
	@OnClick(R.id.tv_exchange)
	public void toExchange() {
		if (vkoContext.doLoginCheckToSkip(atx)) {
			return;
		}
//		StartActivityUtil.startActivity(atx, ExchangeCodeActivity.class);

		Intent intent = new Intent(getActivity(), ExchangeCodeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityForResult(intent, 100);

	}

	// 系统设置
	@OnClick(R.id.tv_setting)
	public void tvSettingClick() {
		//
		StartActivityUtil.startActivity(atx, SystemActivity.class,
				Intent.FLAG_ACTIVITY_SINGLE_TOP);

	}

	// 邀请好友
//	@OnClick(R.id.tv_i_share)
//	public void shareClick() {
//		if (vkoContext.doLoginCheckToSkip(atx)) {
//			return;
//		}
//		StartActivityUtil.startActivity(atx, RecommendActivity.class);
//	}

	// 我的课程
	@OnClick(R.id.tv_i_course)
	public void onMeCourse() {
		if (vkoContext.doLoginCheckToSkip(atx)) {
			return;
		}
		StartActivityUtil.startActivity(atx, MyClassActivity.class);
	}

	// 错题本
	@OnClick(R.id.tv_notepad)
	public void tvNotepadClick() {
		if (vkoContext.doLoginCheckToSkip(atx)) {
			return;
		}

		StartActivityUtil.startActivity(atx, NotepadActivity.class,
				Intent.FLAG_ACTIVITY_SINGLE_TOP);

	}

	// 年级设置
	@OnClick(R.id.layout_grade)
	public void myGradeClick() {
		if (vkoContext.doLoginCheckToSkip(atx)) {
			return;
		}

		Intent intent = new Intent(getActivity(), SelectGradeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivityForResult(intent, 100);

	}

	// 我的下载
	@OnClick(R.id.tv_i_download)
	public void iDownloadClick() {
		if (vkoContext.doLoginCheckToSkip(atx)) {
			return;
		}

		StartActivityUtil.startActivity(atx, MyVideoListDownLoadActivity.class);

	}

	// 我的收藏
	@OnClick(R.id.tv_i_favorite)
	public void tvMyFavorite() {
		if (vkoContext.doLoginCheckToSkip(atx)) {
			return;
		}

		StartActivityUtil.startActivity(atx, MyFavoriteActivity.class);

	}

	// 会员中心
	@OnClick(R.id.layout_member)
	public void tvMemberCenter() {
		if (vkoContext.doLoginCheckToSkip(atx)) {
			return;
		}

		StartActivityUtil.startActivity(atx, MembersCenterActivity.class);

	}

	// 圈子
	@OnClick(R.id.layout_circle)
	public void goToCircle() {
		StartActivityUtil.startActivity(atx, MyOrderActivity.class);
	}

	// 公式速记
	@OnClick(R.id.tv_i_formula)
	public void tvFormula() {

		if (vkoContext.doLoginCheckToSkip(atx)) {
			return;
		}
		StartActivityUtil.startActivity(atx, FormularRemActivity.class);

	}

	// 我的消息
	@OnClick(R.id.tv_i_message)
	public void onMessageClicked() {
		StartActivityUtil.startActivity(getActivity(), MsgMainActivity.class);
	}
	// 扫一扫
//	@OnClick(R.id.tv_scan_me)
//	public void onScanClicked() {
//		StartActivityUtil.startActivity(atx, ScanActivity.class);
//	}

	//我的收入
	@OnClick(R.id.income_layout)
	public void onMyInComeClicked(){
		StartActivityUtil.startActivity(getActivity(), MyInComeActivity.class);
	}
	// 修改个人信息
	@OnClick(R.id.rl_head)
	public void rlPersonalClick() {
		if (vkoContext.doLoginCheckToSkip(atx)) {
			return;
		}

		StartActivityUtil.startActivity(atx, PersonalMessageActivity.class);

	}

	/*
	 *
	 * @Description: TODO
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		EventCountAction.onFragRCount(this.getClass());
	}

	/*
	 *
	 * @Description: TODO
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		EventCountAction.onFragPCount(this.getClass());
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		EventBus.getDefault().unregister(this);
	}

}
