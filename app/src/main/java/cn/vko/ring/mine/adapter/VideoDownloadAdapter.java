package cn.vko.ring.mine.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import cn.shikh.utils.DateUtils;
import cn.vko.ring.R;
import cn.vko.ring.common.base.BaseListAdapter;
import cn.vko.ring.mine.listener.IRefreshDataListener;
import cn.vko.ring.study.db.DBservice;
import cn.vko.ring.study.model.DownloadInfo;
import cn.vko.ring.study.widget.RectProgressView;
import cn.vko.ring.utils.ACache;
import cn.vko.ring.utils.StringUtils;

import com.easefun.polyvsdk.PolyvDownloadProgressListener;
import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason;
import com.easefun.polyvsdk.PolyvDownloaderManager;

public class VideoDownloadAdapter extends BaseListAdapter<DownloadInfo> {

	private ACache mACache;
	private IRefreshDataListener irflistenr;
//	private Map<Integer, View> viewMap = new HashMap<Integer, View>();
	private DBservice dbService;
	private Activity act;
	private HashMap<String,MyPolyvDownloadProgressListener> map = new HashMap<String, MyPolyvDownloadProgressListener>();
	private ArrayList<MyViewHolder> holders = new ArrayList<MyViewHolder>();
	public void setIrflistenr(IRefreshDataListener irflistenr) {
		this.irflistenr = irflistenr;
	}

	public VideoDownloadAdapter(Context ctx,List<DownloadInfo> infos) {
		super(ctx);
		// TODO Auto-generated constructor stub
		this.dbService = new DBservice(ctx);
		mACache = ACache.get(ctx);
		act = (Activity) ctx;
		this.list = infos;
//		initDownloadListener();
	}

	private void initDownloadListener(){
		if(list != null && list.size() >0){
			for(int i = 0;i<list.size();i++){
				onDownloadListener(list.get(i),i);
			}
		}
	}
	private void onDownloadListener(final DownloadInfo info,final int p) {
		// TODO Auto-generated method stub
		final PolyvDownloader downloader = PolyvDownloaderManager
				.getPolyvDownloader(info.getVid(), info.getBitrate());
		downloader
		.setPolyvDownloadProressListener(new PolyvDownloadProgressListener() {

			@Override
			public void onDownloadSuccess() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDownloadFail(PolyvDownloaderErrorReason arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDownload(final long downloaded,
					final long total) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	@Override
	protected ViewHolder createViewHolder(View root) {
		// TODO Auto-generated method stub
		MyViewHolder h = new MyViewHolder(root);
		holders.add(h);
		return h;
	}

	@Override
	protected void fillView(View root, final DownloadInfo info,ViewHolder holder,int position) {
		// TODO Auto-generated method stub
		if(info == null) return;
		final MyViewHolder h = (MyViewHolder) holder;
		String vid = info.getVid();	
		String duration = info.getDuration();
		long filesize = info.getFilesize();
		int percent = info.getPercent();
		System.out.println("position=="+position);
		h.tvName.setText(info.getTitle());
		h.tvDuration.setText(StringUtils.StringToInt(duration));

		h.tvFilesize.setText("" + StringUtils.LongToString(filesize) + "MB");

		h.progressBar.setmMax(100);

		h.progressBar.setmProgress(percent);
		h.tvPercent.setText("" + percent);
		
		final PolyvDownloader downloader = PolyvDownloaderManager
				.getPolyvDownloader(vid, info.getBitrate());
		h.btnDown.setText(downloader.isDownloading() ? "暂停" : "开始");
		h.btnDele.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (downloader != null) {
					downloader.stop();
					downloader.deleteVideo(info.getVid(), info.getBitrate());
					dbService.deleteDownloadFile(info);
					remove(info);
					map.remove(info.getVid());
					notifyDataSetChanged();
				}
			}
		});

		h.btnDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (downloader != null) {
					if (downloader.isDownloading()) {
						downloader.stop();
						((Button) v).setText("开始");
					} else {
						downloader.start();
						((Button) v).setText("暂停");

					}
				}

			}
		});	

		if(map.get(vid) == null){
			map.put(vid, new MyPolyvDownloadProgressListener(info));
		}
		map.get(vid).setMyViewHolder(holders.size()>position?holders.get(position):null);
		downloader.setPolyvDownloadProressListener(map.get(vid));
	}

	@Override
	protected int getItemViewId() {
		// TODO Auto-generated method stub
		return R.layout.item_download;
	}

	class MyViewHolder extends ViewHolder {
		@Bind(R.id.download)
		Button btnDown;
		@Bind(R.id.delete)
		Button btnDele;
		@Bind(R.id.tv_vid)
		TextView tvName;
		@Bind(R.id.tv_filesize)
		TextView tvFilesize;
		@Bind(R.id.rate)
		TextView tvPercent;
		@Bind(R.id.tv_duration)
		TextView tvDuration;
		@Bind(R.id.progressBar)
		RectProgressView progressBar;
		
		public MyViewHolder(View view) {
			super(view);
			// TODO Auto-generated constructor stub
		}

	}

	class MyPolyvDownloadProgressListener implements PolyvDownloadProgressListener{
		private MyViewHolder h;
		private DownloadInfo info;
		public MyPolyvDownloadProgressListener(DownloadInfo info){
			this.info = info;
		}
		public void setMyViewHolder(MyViewHolder h){
			this.h = h;
		}
		@Override
		public void onDownload(long downloaded, long total) {
			// TODO Auto-generated method stub
			final long percent = downloaded * 100 / total;
			// 更新UI
			dbService.updatePercent(info, (int) percent);
			info.setPercent((int) percent);
			act.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						if(h != null){
							h.progressBar.setmProgress((int) percent);
							// Log.i("downlaod", percent + "");
							h.tvPercent.setText("" + percent);
						}
						//
					} catch (Exception e) {

					}
				}
			});
		}

		@Override
		public void onDownloadFail(PolyvDownloaderErrorReason arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDownloadSuccess() {
			// TODO Auto-generated method stub
			dbService.updatePercent(info, 100);
			mACache.put(DateUtils.currentData(), info.getVid());
			act.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						if(h != null){
						h.progressBar.setmProgress(100);
						h.tvPercent.setText("100");
						h.btnDown.setText("下载完成");
						h.btnDown.setEnabled(true);
						Toast.makeText(ctx, "下载成功", Toast.LENGTH_SHORT).show();
//						if (downloader.isDownloading()) {
//							downloader.stop();
//						}
						if (irflistenr != null) {
							irflistenr.refreshData();
						}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
		}
		
	}
	

}
