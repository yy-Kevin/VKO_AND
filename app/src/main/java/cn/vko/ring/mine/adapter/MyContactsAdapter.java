/*******************************************************************************
 * Copyright (c) 2015 by Luxury Corporation all right reserved.
 * 2015-8-11 
 * 
 *******************************************************************************/
package cn.vko.ring.mine.adapter;

import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.vko.ring.Constants;
import cn.vko.ring.R;
import cn.vko.ring.VkoContext;
import cn.vko.ring.home.model.UserInfo;
import cn.vko.ring.mine.model.ContactsInfo;

public class MyContactsAdapter extends BaseAdapter {

	private Context ctx;
	private List<ContactsInfo> list;
	private String myName;
	static String inviteKey;
	private int flag;
	private String url;

	/**
	 * @param ctx
	 * @param list
	 */
	public MyContactsAdapter(Context ctx, List<ContactsInfo> list,
			String myName, int flag) {
		super();
		this.ctx = ctx;
		this.list = list;
		this.myName = myName;
		this.flag = flag;
	}

	public MyContactsAdapter(Context ctx, List<ContactsInfo> list,
			String myName, int flag, String url) {
		super();
		this.ctx = ctx;
		this.list = list;
		this.myName = myName;
		this.flag = flag;
		this.url = url;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.isEmpty() ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.isEmpty() ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(ctx).inflate(
					R.layout.item_contacts_list, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.contacts_name);
			holder.inviteBtn = (Button) convertView
					.findViewById(R.id.contacts_invite_btn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ContactsInfo info = list.get(position);
		if (info != null) {
			holder.name.setText(info.getName());
		}
		// 通讯录点击邀请
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String phone_number = info.getPhone();
				switch (flag) {
				case 0:
					if (phone_number.equals("")) {
						Toast.makeText(ctx, "好友的电话:" + phone_number,
								Toast.LENGTH_LONG).show();
					} else {
						SmsManager smsManager = SmsManager.getDefault();
						PendingIntent sentIntent = PendingIntent.getBroadcast(
								ctx, 0, new Intent(), 0);
						inviteKey = VkoContext.getInstance(ctx).getUser()
								.getInviteKey();
						if (myName != null) {
							sendSMS(phone_number,
									"你的好友“" + myName + "”邀请您加入微课圈，"
											+ Constants.FXURL.concat(inviteKey)
											+ "。客服电话：4008-900-800。");
						} else {
							UserInfo user = VkoContext.getInstance(ctx)
									.getUser();
							if (user == null) {
								// startLogin();
								return;
							}
							sendSMS(phone_number,
									"你的好友“" + user.getMobile() + "”邀请您加入微课圈，"
											+ Constants.FXURL.concat(inviteKey)
											+ "。客服电话：4008-900-800。");
						}
					}
					break;
				case 1:
					if (phone_number.equals("")) {
						Toast.makeText(ctx, "好友的电话:" + phone_number,
								Toast.LENGTH_LONG).show();
					} else {
						SmsManager smsManager = SmsManager.getDefault();
						PendingIntent sentIntent = PendingIntent.getBroadcast(
								ctx, 0, new Intent(), 0);
						inviteKey = VkoContext.getInstance(ctx).getUser()
								.getInviteKey();
						if (myName != null) {
							sendSMS(phone_number, "你的好友“" + myName
									+ "”请求您为其支付，" + url);
						} else {
							UserInfo user = VkoContext.getInstance(ctx)
									.getUser();
							if (user == null) {
								// startLogin();
								return;
							}
							sendSMS(phone_number, "你的好友“" + myName
									+ "”请求您为其支付，" + url);
						}
					}
					break;
				default:
					break;
				}

			}
		});
		return convertView;
	}

	/**
	 * 
	 * 发送短信
	 * 
	 * @param smsBody
	 */

	private void sendSMS(String phone, String smsBody)

	{

		Uri smsToUri = Uri.parse("smsto:" + phone);

		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

		intent.putExtra("sms_body", smsBody);

		ctx.startActivity(intent);
	}

	class ViewHolder {
		TextView name;
		Button inviteBtn;
	}
}
