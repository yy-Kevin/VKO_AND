package cn.vko.ring.mine.adapter;

import java.util.LinkedList;

import com.easefun.polyvsdk.PolyvDownloader;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog;
import cn.vko.ring.common.widget.alertdialog.SweetAlertDialog.OnSweetClickListener;
import cn.vko.ring.study.db.DBservice;
import cn.vko.ring.study.model.DownloadInfo;


public class MyVideoFinishAdapter extends BaseListAdapter<DownloadInfo> {

	private DBservice dbService;
	private SweetAlertDialog dialog;
	private Context ctx;
	public MyVideoFinishAdapter(Context ctx) {
		super(ctx);		
		this.ctx=ctx;
		init(ctx);
	}
	
	private void init(Context ctx) {
		this.dbService = new DBservice(ctx);
		
	}
	@Override
	protected ViewHolder createViewHolder(
			View root) {
		MyViewHolder holder = new MyViewHolder(root);
		holder.tv_kownlage = (TextView) root.findViewById(R.id.tv_kownlage);
		holder.tv_load_speed = (TextView) (TextView) root.findViewById(R.id.tv_load_speed);
		holder.iv_start_stop=(ImageView) root.findViewById(R.id.iv_start_stop);
		return holder;
	}

	@Override
	protected void fillView(View root, DownloadInfo item,
			ViewHolder holder,
			int position) {
		if (item == null) {
			return;
		}
		final MyViewHolder h = (MyViewHolder) holder;
		h.tv_kownlage.setText(item.getTitle());
		Log.e("知识点名字", item.getTitle());			
		
		h.iv_start_stop.setOnClickListener(new DeleteListener(item, position));
	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_finish_download;
	}

	class MyViewHolder extends ViewHolder {
		TextView tv_kownlage;
		TextView tv_load_speed;
		ImageView iv_start_stop;
		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}

	}

	class DeleteListener implements View.OnClickListener {
		private DownloadInfo info;
		int p;

		public DeleteListener(DownloadInfo info, int p) {
			this.info = info;
			this.p = p;
		}

		@Override
		public void onClick(View v) {
			//PolyvDownloader downloader = downloaders.get(p);
			showAlerDialog(info);
			
		}

		private void DeleteVideo(DownloadInfo info) {
			PolyvDownloader downloader = new PolyvDownloader(info.getVid(),info.getBitrate());
			if (downloader != null) {
				downloader.stop();
				downloader.deleteVideo(info.getVid(), info.getBitrate());
				dbService.deleteDownloadFile(info);
				if(deletelistener!=null){
					deletelistener.deletedowninfo();
				}
				
			}
		}
		

		private void showAlerDialog(final DownloadInfo info) {
			// TODO Auto-generated method stub
				dialog = new SweetAlertDialog(ctx,
						SweetAlertDialog.WARNING_TYPE);
				dialog.setContentText("是否删除视频");
				dialog.setTitleText("提示！");
				dialog.setCancelText("取消");
				dialog.setConfirmText("确定");
				dialog.setCancelClickListener(new OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						
					}
				});
				dialog.setConfirmClickListener(new OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						
						dialog.dismiss();
						DeleteVideo(info);
					}
				});
		
			dialog.show();
		}

	}
	
	public interface IDeleteListener{
		void deletedowninfo();
	}
	
	private IDeleteListener deletelistener;

	public void setDeletelistener(IDeleteListener deletelistener) {
		this.deletelistener = deletelistener;
	}
	
	
}

