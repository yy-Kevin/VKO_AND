package cn.vko.ring.common.widget.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseDialog;
import cn.vko.ring.common.listener.IOnClickItemListener;
import cn.vko.ring.home.model.BtnInfo;

public class UserVbDialog extends BaseDialog implements
		View.OnClickListener {

	private LinearLayout layoutTwo, layoutThree;
	private ImageView ivHead;
	private TextView tvContent;
	private TextView tvTwoFirst, tvTwoSecond;
	private TextView tvThreeFirst, tvThreeSecond, tvThreeThird;
	private TextView layoutOne;
	private IOnClickItemListener<String> listener;

	public UserVbDialog(Context context) {
		super(context, R.style.MaskDialog);
		// TODO Auto-generated constructor stub
	}

	public void setOnClickItemListener(IOnClickItemListener<String> listener) {
		this.listener = listener;
	}

	public void setContentText(String content) {
		tvContent.setText(content);
	}

	public void setHeadImage(int resId) {
		ivHead.setImageResource(resId);
	}

	public void setBtnInfo(BtnInfo btnInfo) {
		layoutTwo.setVisibility(View.GONE);
		layoutThree.setVisibility(View.GONE);
		layoutOne.setVisibility(View.VISIBLE);
		layoutOne.setText(btnInfo.getText());
		layoutOne.setTextColor(context.getResources().getColor(btnInfo.getTextColor()));
		layoutOne.setBackgroundResource(btnInfo.getTextBg());
	}

	public void setBtnInfo(BtnInfo btnInfo1, BtnInfo btnInfo2) {
		layoutTwo.setVisibility(View.VISIBLE);
		layoutThree.setVisibility(View.GONE);
		layoutOne.setVisibility(View.GONE);
		tvTwoFirst.setText(btnInfo1.getText());
		tvTwoFirst.setTextColor(context.getResources().getColor(btnInfo1.getTextColor()));
		tvTwoFirst.setBackgroundResource(btnInfo1.getTextBg());
		tvTwoSecond.setText(btnInfo2.getText());
		tvTwoSecond.setTextColor(context.getResources().getColor(btnInfo2.getTextColor()));
		tvTwoSecond.setBackgroundResource(btnInfo2.getTextBg());
	}

	public void setBtnInfo(BtnInfo btnInfo1, BtnInfo btnInfo2, BtnInfo btnInfo3) {
		layoutTwo.setVisibility(View.GONE);
		layoutThree.setVisibility(View.VISIBLE);
		layoutOne.setVisibility(View.GONE);
		tvThreeFirst.setText(btnInfo1.getText());
		tvThreeFirst.setTextColor(context.getResources().getColor(btnInfo1.getTextColor()));
		tvThreeFirst.setBackgroundResource(btnInfo1.getTextBg());
		tvThreeSecond.setText(btnInfo2.getText());
		tvThreeSecond.setTextColor(context.getResources().getColor(btnInfo2.getTextColor()));
		tvThreeSecond.setBackgroundResource(btnInfo2.getTextBg());
		tvThreeThird.setText(btnInfo3.getText());
		tvThreeThird.setTextColor(context.getResources().getColor(btnInfo3.getTextColor()));
		tvThreeThird.setBackgroundResource(btnInfo3.getTextBg());
	}

	@Override
	public void setUpViews(View root) {
		// TODO Auto-generated method stub
		layoutTwo = (LinearLayout) root.findViewById(R.id.layout_two);
		layoutThree = (LinearLayout) root.findViewById(R.id.layout_three);
		ivHead = (ImageView) root.findViewById(R.id.iv_head);

		tvContent = (TextView) root.findViewById(R.id.tv_content);

		tvThreeFirst = (TextView) root.findViewById(R.id.tv_three_first);
		tvThreeSecond = (TextView) root.findViewById(R.id.tv_three_second);
		tvThreeThird = (TextView) root.findViewById(R.id.tv_three_third);

		tvTwoFirst = (TextView) root.findViewById(R.id.tv_two_first);
		tvTwoSecond = (TextView) root.findViewById(R.id.tv_two_second);

		layoutOne = (TextView) root.findViewById(R.id.layout_one);
	}

	@Override
	public void setUpListener() {
		// TODO Auto-generated method stub
		tvThreeFirst.setOnClickListener(this);
		tvThreeSecond.setOnClickListener(this);
		tvThreeThird.setOnClickListener(this);
		tvTwoFirst.setOnClickListener(this);
		tvTwoSecond.setOnClickListener(this);
		layoutOne.setOnClickListener(this);
	}

	@Override
	public boolean getCancelableOnclick() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getGravity() {
		// TODO Auto-generated method stub
		return Gravity.CENTER;
	}

	@Override
	public int getRootId() {
		// TODO Auto-generated method stub
		return R.layout.dialog_user_vb;
	}

	@Override
	public int getAnimatStly() {
		// TODO Auto-generated method stub
		return R.style.popup_pay;
	}

	@Override
	public LayoutParams getILayoutParams() {
		// TODO Auto-generated method stub
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		return lp;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (listener == null)
			return;
		if (v == tvThreeFirst || v == tvTwoFirst || v == layoutOne) {
			listener.onItemClick(0, "", v);
		} else if (v == tvThreeSecond || v == tvTwoSecond) {
			listener.onItemClick(1, "", v);
		} else if (v == tvThreeThird) {
			listener.onItemClick(2, "", v);
		}
	}

}
