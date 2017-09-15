package cn.vko.ring.mine.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.common.base.BaseActivity;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.adapter.MyContactsAdapter;
import cn.vko.ring.mine.model.ContactsInfo;

public class ContactsActivity extends BaseActivity {

	@Bind(R.id.tv_title)
	public TextView inviteContacts;
	private List<ContactsInfo> list;
	private MyContactsAdapter adapter;
	@Bind(R.id.contacts_listview)
	public ListView cListView;
	private String myName;
	private int flag;

	@Override
	public int setContentViewResId() {
		// TODO Auto-generated method stub
		return R.layout.activity_contacts;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		inviteContacts.setText("邀请通讯录好友");
		list = new ArrayList<ContactsInfo>();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		getPhoneContacts();
		flag = getIntent().getIntExtra("flag", 2);
		if (!list.isEmpty()) {
			switch (flag) {
			case 0:
				// 分享
				adapter = new MyContactsAdapter(this, list, myName, flag);
				break;
			case 1:
				// 代付
				String url = getIntent().getStringExtra("url");
				adapter = new MyContactsAdapter(this, list, myName, flag, url);
				break;
			default:
				break;
			}

			cListView.setAdapter(adapter);
			// adapter.notifyDataSetChanged();
		} else {
			Toast.makeText(this, "联系人为空", Toast.LENGTH_SHORT).show();
		}

	}

	@OnClick(R.id.iv_back)
	public void sayBack() {
		finish();
	}

	/**
	 * 
	 * 方法说明：得到手机通讯录联系人信息
	 * 
	 */
	private void getPhoneContacts() {
		ContentResolver resolver = this.getContentResolver();

		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, null, null,
				null, null);
		UserInfo user = VkoContext.getInstance(this).getUser();
		if (user == null) {
			return;
		}
		String myMobile = user.getMobile();
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(phoneCursor
						.getColumnIndex(Phone.NUMBER));

				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// 得到联系人名称
				String contactName = phoneCursor.getString(phoneCursor
						.getColumnIndex(Phone.DISPLAY_NAME));

				// 得到联系人ID
				int contactid = phoneCursor.getInt(phoneCursor
						.getColumnIndex(Phone._ID));
				list.add(new ContactsInfo(contactName, phoneNumber, contactid));
				if (myMobile == null) {
					if (user.getName() != null) {
						myName = user.getName();
					}
					continue;
				} else if (myMobile.equals(phoneNumber)) {
					myName = contactName;
					continue;
				}
			}
			phoneCursor.close();

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// EventCountAction.onActivityResumCount(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// EventCountAction.onActivityPauseCount(this);
	}

}
