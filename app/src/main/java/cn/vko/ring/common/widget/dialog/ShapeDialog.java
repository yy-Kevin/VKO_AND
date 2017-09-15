package cn.vko.ring.common.widget.dialog;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import cn.shikh.utils.ViewUtils;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseDialog;
import cn.vko.ring.home.adapter.ShareGvAdapter;
import cn.vko.ring.home.model.Recommond;

public class ShapeDialog extends BaseDialog {

	private GridView mGv;
	private TextView tv;
	private ShareGvAdapter mAdapter;
	private List<Recommond> mRecommondList;
	public void setOnItemClickListener(OnItemClickListener listener){
		mGv.setOnItemClickListener(listener);
	}
	public ShapeDialog(Context context) {
		super(context, R.style.MaskDialog);
		// TODO Auto-generated constructor stub
		int[] shareImg = new int[] { R.drawable.class_share_b_wechat,
				R.drawable.class_share_b_fc, R.drawable.class_share_b_qq,
				R.drawable.class_share_b_qzone };
		String[] shareTitle = { "微信", "朋友圈", "QQ", "QQ空间" };
		mRecommondList = new ArrayList<Recommond>();
		for (int i = 0; i < shareImg.length; i++) {
			Recommond r = new Recommond(shareImg[i], shareTitle[i]);
			mRecommondList.add(r);
		}
		mAdapter = new ShareGvAdapter(context, mRecommondList);
		mGv.setAdapter(mAdapter);
	}

	@Override
	public void setUpViews(View root) {
		// TODO Auto-generated method stub
		tv = (TextView) root.findViewById(R.id.bt_close_pop);
		mGv = (GridView) root.findViewById(R.id.share_gv);
		
	}

	@Override
	public void setUpListener() {
		// TODO Auto-generated method stub
		tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	@Override
	public boolean getCancelableOnclick() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getGravity() {
		// TODO Auto-generated method stub
		return Gravity.BOTTOM;
	}

	@Override
	public int getRootId() {
		// TODO Auto-generated method stub
		return R.layout.pop_share_view;
	}

	@Override
	public int getAnimatStly() {
		// TODO Auto-generated method stub
		return R.style.dialog_anim;
	}

	@Override
	public LayoutParams getILayoutParams() {
		// TODO Auto-generated method stub
		LayoutParams lp = new LayoutParams(ViewUtils.getScreenWidth(context),
				LayoutParams.WRAP_CONTENT);
		return lp;
	}

}
