package cn.vko.ring.common.widget;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseSubjectFilterLayout;
import cn.vko.ring.home.adapter.SubjectListAdapter;
import cn.vko.ring.home.model.SubjectInfo;
import cn.vko.ring.utils.AnimationController;

/**
 * @author shikh 学科选择
 */
public class SubjectFilterLayout extends BaseSubjectFilterLayout {
	private TextView tvSubject;
	private Drawable dewUp, dewDown, blueBag, whiteBag;
	public AnimationController aminController;
	private String grade;
	public SubjectListAdapter mAdapter;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switchVisible(false);
			if (tvSubject != null) {
				tvSubject.setText(info.getName());
				switchDrawable(dewDown);
			}
		};
	};

	public void displayLayout() {
		if (subjectList == null || subjectList.size() == 0) {
			initData(grade, 0);
		}
		switchVisible(true);
		switchDrawable(dewUp);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SubjectFilterLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		dewDown = ContextCompat.getDrawable(context,R.drawable.title_drawabledown);
		dewUp = ContextCompat.getDrawable(context,R.drawable.title_drawableup);
		blueBag = ContextCompat.getDrawable(context,R.drawable.subject_bagblue);
		whiteBag = ContextCompat.getDrawable(context,R.drawable.subject_bagwhite);
		aminController = new AnimationController();
		mAdapter = new SubjectListAdapter(context);
		setAdapter(mAdapter);
	}

	public void setEvent(TextView tvSubject, final String grade, int type) {
		this.grade = grade;
		this.tvSubject = tvSubject;
		final boolean isFirst = TextUtils.isEmpty(tvSubject.getText());
		if (tvSubject != null) {
			if (isFirst) {
				switchDrawable(whiteBag);//type == 0 ? blueBag : whiteBag
			} else {
				switchDrawable(dewDown);
			}
			tvSubject.setCompoundDrawablePadding(5);
			tvSubject.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (isShown()) {
						// aminController.slideTopOut(SubjectFilterLayout.this,
						// 300, 0);
						// setVisibility(View.GONE);
						switchVisible(false);
						if (!TextUtils.isEmpty(((TextView) v).getText())) {
							switchDrawable(dewDown);
						}
					} else {
						if (subjectList == null || subjectList.size() == 0) {
							initData(grade, 0);
						}
						switchVisible(true);
						if (!TextUtils.isEmpty(((TextView) v).getText())) {
							switchDrawable(dewUp);
						}
					}
				}
			});
		}
	}

	public void switchVisible(boolean isVisible) {
		// TODO Auto-generated method stub
		if (isVisible) {
			aminController.slideTopIn(SubjectFilterLayout.this, 300, 0);
			setVisibility(View.VISIBLE);
		} else {
			aminController.slideTopOut(SubjectFilterLayout.this, 300, 0);
			// setVisibility(View.GONE);
		}
	}

	public void switchDrawable(Drawable drawable) {
		if (drawable == null) {
			return;
		}
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		tvSubject.setCompoundDrawables(null, null, drawable, null);// 画在右边
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				info = mAdapter.getListItem(position);
				if (info != null) {
					mAdapter.setSelectSub(info);
					mAdapter.notifyDataSetChanged();
					if (listener != null) {
						listener.onItemClick(position, info, view);
					}
					mHandler.sendEmptyMessageDelayed(1, 200);
				}

			}
		});

	}

	@Override
	public void fillAdapter(List<SubjectInfo> list) {
		// TODO Auto-generated method stub
		mAdapter.clear();
		mAdapter.add(list);
		mAdapter.setSelectSub(info);
		mAdapter.notifyDataSetChanged();

	}

	public void updateFilter(SubjectInfo info) {
		this.info = info;
		switchDrawable(dewDown);
		mAdapter.setSelectSub(info);
		mAdapter.notifyDataSetChanged();
	}

}
