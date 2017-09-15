package cn.shikh.utils.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.shikh.utils.BitmapUtils;
import cn.shikh.utils.ImageCacheUtils;
import cn.shikh.utils.ViewUtils;
import cn.shikh.utils.okhttp.callback.BitmapCallback;
import okhttp3.Call;
import cn.shikh.utils.R;

public class PhotoBrowserAdapter extends BaseAdapter {

	private List<ViewHolder> photoViews;

	private Context ctx;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			PhotoBrowserAdapter.this.notifyDataSetChanged();
		};
	};

	public PhotoBrowserAdapter(Context ctx, List<String> imageFiles) {

		this.photoViews = new ArrayList<ViewHolder>(0);
		this.ctx = ctx;
		if (null != imageFiles) {
			for (String uri : imageFiles) {
				photoViews.add(new ViewHolder(uri));
			}
		}

	}

	public void addImage(String imagePath) {
		if (!TextUtils.isEmpty(imagePath)) {
			photoViews.add(new ViewHolder(imagePath));
		}
	}
	
	public void removeImage(int index){
		if(getCount() > index){
			photoViews.remove(index);
		}
		
	}
	
	public int getIndex(Object obj){
		if(obj instanceof ViewHolder){
			if(photoViews.contains(obj)){
				return photoViews.indexOf(obj);
			}
		}
		return -1;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == photoViews ? 0 : photoViews.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void clear() {
		if (null != photoViews) {

			for (ViewHolder holder : photoViews) {
				if (holder.bitmap != null) {
					holder.bitmap.recycle();
					holder.bitmap = null;
				}
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		System.out.println("===========================" + position);

		ViewHolder holder = photoViews.get(position);
		holder.loadFromUri();
		return holder.getContentView();

	}

	class ViewHolder {
		// 加载状态
		int loadState;
		// 本地加载失败
		final int LOAD_LOCAL_FAIL = 1 << 1;
		// 服务器加载失败
		final int LOAD_SERVER_FAIL = 1 << 2;
		// 正在加载
		final int LOADING = 1 << 3;
		// 已经加载
		final int LOADED = 1 << 4;
		Bitmap bitmap;
		// 显示正常土片
		PhotoView photoView;
		// 正在加载
		View loadingView;
		// 加载失败 文本提示
		View loadFailTextView;
		// 加载失败图片提示
		View loadFailImageView;
		boolean loaded = false;
		String url;
		ViewHolder(String url) {
			this.url = url;
		}

		void loadFromUri() {
			if (loaded) {
				return;
			}
			loaded = true;
			try {
				load(url);
			} catch (Exception e) {
				e.printStackTrace();
				loadState = LOAD_SERVER_FAIL;
			}
		}
		View getContentView() {
			if (null != bitmap) {
				if (null == photoView) {
					photoView = (PhotoView) View.inflate(ctx, R.layout.photo_browser_item, null);
					photoView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				}
				photoView.setImageBitmap(bitmap);
				return photoView;
			}

			switch (loadState) {
			case LOAD_LOCAL_FAIL:

				// 表示加载本地文件失败
				if (null == loadFailTextView) {
					loadFailTextView = View.inflate(ctx,
							R.layout.photo_browser_load_file_fail, null);

					loadFailTextView
							.setLayoutParams(new Gallery.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT));

				}
				return loadFailTextView;

			case LOAD_SERVER_FAIL:
				if (null == loadFailImageView) {
					loadFailImageView = View.inflate(ctx,
							R.layout.photo_browser_load_url_fail, null);
					loadFailImageView
							.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				}
				return loadFailImageView;

			default:
				if (null == loadingView) {
					loadingView = View.inflate(ctx,
							R.layout.photo_browser_loading, null);
					loadingView
							.setLayoutParams(new Gallery.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT));
				}
				return loadingView;

			}

		}

		void load(String url) {
			loadState = LOADING;
			// 如果是网络图片 先查看 是否已经下载过

			if (url.startsWith("http://")) {

					bitmap= ImageCacheUtils.getInstance().loadImage(url, new BitmapCallback() {
						@Override
						public void onError(Call call, Exception e) {
							loadState = LOAD_SERVER_FAIL;
							// System.out.println("=========fail=============="+uri);
							PhotoBrowserAdapter.this.notifyDataSetChanged();
						}

						@Override
						public void onResponse(Bitmap response) {
							bitmap = response;
							mHandler.sendEmptyMessage(0);
						}
						@Override
						public void inProgress(final float progress) {
							//super.inProgress(progress);
							loadState = LOADING;
							if (null != loadingView) {
								loadingView.post(new Runnable() {
									@Override
									public void run() {
										try {
											final  float p = progress;
											TextView tv = (TextView) loadingView
													.findViewById(R.id.photo_broswer_loading_info);
											tv.setText(String.valueOf(p).concat(" %"));
										} catch (Exception e) {
											e.printStackTrace();
										}

									}

								});
							}
						}
					},ViewUtils.getScreenWidth(ctx),1);
					// 如果不为空
					if (null == bitmap) {
						loadState = LOAD_SERVER_FAIL;
					} else {
						loadState = LOADED;
					}
			} else {
				bitmap = loadFromFile(new File(url));
				// 如果不为空
				if (null == bitmap) {
					loadState = LOAD_LOCAL_FAIL;
				} else {
					loadState = LOADED;
				}
			}
		}

		Bitmap loadFromFile(File fileDesk) {

			if (fileDesk.exists()) {
				int screenWidth = ViewUtils.getScreenWidth(ctx);
				// 根据屏幕宽度 获取缩略图
				return BitmapUtils.getBitmapPreview(fileDesk.getAbsolutePath(),
						screenWidth, 1);

			}
			return null;
		}


	}

}
