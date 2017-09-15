package cn.vko.ring.common.base;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.shikh.utils.ImageCacheUtils;
import cn.shikh.utils.okhttp.callback.BitmapCallback;
import cn.vko.ring.ConstantUrl;
import cn.vko.ring.R;
import okhttp3.Call;

public abstract class BaseListAdapter<T> extends BaseAdapter {

	private static final int DATA_CHANGE = 1 << 1;
	protected List<T> list;
	protected Context ctx;
	private View emptyView;
	private boolean isEmpty;
	/**
	 * 加载过的图片
	 */
	private List<String> loadList = new ArrayList<String>(0);

	protected Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DATA_CHANGE:
				judgeHasEmpty();
				notifyDataSetChanged();
				break;
			default:
				BaseListAdapter.this.handleMessage(msg);
				break;
			}
		}

	};

	public BaseListAdapter(Context ctx) {
		this.ctx = ctx;
		emptyView = View.inflate(ctx,R.layout.layout_empty_view, null);
	}

	/**
	 * 异步提交 通知数据变更
	 */
	public synchronized void postNotifyDataSetChanged() {
		handler.sendEmptyMessage(DATA_CHANGE);
	}

	protected void handleMessage(Message msg) {
		
	}

	public void add(T t) {
		if (null == list) {
			list = new ArrayList<T>(0);
		}
		if(!list.contains(t)){
			list.add(t);
		}
	}

	public void add(T t, int index) {
		if (null == list) {
			list = new ArrayList<T>(0);
		}
		list.add(index, t);
	}

	public void switch2FirstItem(T t){
		if(t == null) return;
		if (null == list) {
			list = new ArrayList<T>(0);
		}
		if (list.contains(t)) {
			list.remove(t);
		}
		list.add(0, t);
	}
	public void switch2IndexItem(T t,int index){
		if(t == null) return;
		if (null == list) {
			list = new ArrayList<T>(0);
		}
		if (list.contains(t)) {
			list.remove(t);
		}
		list.add(index, t);
	}
	public void add(List<T> t) {
		if (null == list) {
			list = new ArrayList<T>(0);
		}

		list.addAll(t);
	}

	public void addBefore(List<T> t) {
		if (null == list) {
			list = new ArrayList<T>(0);
		}
		list.addAll(0, t);
	}

	public void replace(T t){
		if (null == list) {
			list = new ArrayList<T>(0);
		}
		if(list.contains(t)){
			int index = list.indexOf(t);
			list.set(index, t);
		}
	}
	
	public void remove(int position) {
		if (null == list) {
			list = new ArrayList<T>(0);
		}
		if (position > -1 && position < this.getCount()) {
			list.remove(position);
		}
	}

	public void remove(T t) {
		if (null == list) {
			list = new ArrayList<T>(0);
		}
		if (list.contains(t)) {
			list.remove(t);
		}
	}
	
	public void removeList(List<T> tlist){
		if (null == list) {
			list = new ArrayList<T>(0);
		}
		if(tlist != null){
			list.removeAll(tlist);
		}
	}

	public void setList(List<T> t) {
		this.list = t;
	}

	public List<T> getList() {
		return list;
	}

	public void clear() {
		if (null != list) {
			this.list.clear();
		}
		clearLoadList();
		this.notifyDataSetChanged();
	}

	public void clearLoadList() {
		if (null != this.loadList) {
			this.loadList.clear();
		}
	}

	@Override
	public int getCount() {
		if (isEmpty){
			return 1;
		}
		return null == list ? 0 : list.size();
	}

	public T getListItem(int position) {
		if (position > -1 && list != null && position < list.size()) {
			return list.get(position);
		}
		return null;
	}

	@Override
	public Object getItem(int position) {
		if (list != null && position > -1 && position < this.getCount()) {
			return list.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (isEmpty) {
			emptyView.setLayoutParams(new AbsListView.LayoutParams(parent.getWidth(), parent.getHeight()));
			return emptyView;
		}
		T t = this.getListItem(position);
		if (null == convertView || convertView.getTag() == null) {
			convertView = View.inflate(ctx, getItemViewId(), null);
			holder = createViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		fillView(convertView, t, holder, position);
		// getItemPosition(position, holder);

		return convertView;
	}

	/**
	 * 加载缩略图
	 * 
	 * @param shortUri
	 * @param w
	 * @param defaultImageResource
	 */
	protected void loadImageView(final ImageView view, String shortUri,
								 int defaultImageResource, final int w, final int h) {
		if(TextUtils.isEmpty(shortUri)){
			view.setImageResource(defaultImageResource);
			return;
		}
		final String imgeUrl = ConstantUrl.getPerviewUrl(shortUri);
			// 先设置默认图片，再下载
			view.setImageResource(defaultImageResource);
			Bitmap bm = ImageCacheUtils.getInstance().loadImage(imgeUrl, new BitmapCallback() {

				@Override
				public void onError(Call call, Exception e) {

				}

				@Override
				public void onResponse(Bitmap response) {
					postNotifyDataSetChanged();
					// 添加到成功 列表中
					loadList.add(imgeUrl);
					view.setImageBitmap(response);
				}
			},w,h);

	}


	/**
	 * 创建ViewHolder
	 * 
	 * @param root
	 * @return
	 */
	protected abstract ViewHolder createViewHolder(View root);

	/**
	 * 填充viewholder 数据
	 * 
	 * @param root
	 * @param item
	 * @param holder
	 */
	protected abstract void fillView(View root, T item, ViewHolder holder,
			int position);

	/**
	 * 子布局id
	 * 
	 * @return
	 */
	protected abstract int getItemViewId();

	protected static class ViewHolder {
		public ViewHolder(View view){
			ButterKnife.bind(this, view);
		}
	}

	public boolean judgeHasEmpty() {
		if (list == null || list.size() == 0) {
			isEmpty = true;
		} else {
			isEmpty = false;
		}
		return isEmpty ;
		
	}
}
