package cn.shikh.utils;

import android.text.TextUtils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

import cn.shikh.utils.okhttp.OkHttpUtils;
import cn.shikh.utils.okhttp.callback.FileCallBack;
import okhttp3.Call;

public class DownloadUtils {
	private static DownloadUtils mInstance;
	private  File downloadDir = FileUtils.createSDDir("download-dir");
	private  Hashtable<String,FileCallBack> downloadTasks= new Hashtable<String,FileCallBack>();

	private DownloadUtils(){};
	public static DownloadUtils getInstance(){
		if(mInstance == null){
			synchronized (DownloadUtils.class){
				mInstance = new DownloadUtils();
			}
		}
		return mInstance;
	}
	public File getDownloadDir() {
		return downloadDir;
	}

	public void setDownloadDir(File downloadDir) {
		this.downloadDir = downloadDir;
	}
	/**
	 * 是否在下载任务中
	 * @param uri
	 * @return
	 */
	public boolean isExists(String uri){
		return downloadTasks.containsKey(uri);
	}

	/**
	 * 根据下载url 和文件名 生成下载本地路径
	 */
	public  String getDownloadDesk(String uri,String fileName){
		try{
			if (TextUtils.isEmpty(fileName)) {
				fileName = MD5(uri);
			} else {
				fileName = MD5(uri).concat(fileName);
			}
		}catch(Exception e){
			fileName=java.util.UUID.randomUUID().toString();
		}
		return fileName;
	}

	public  void download(String uri,FileCallBack listener){
		download(uri,null,listener);
	}

	/**
	 * 首先判断是否 已经在下载 
	 * @param uri
	 * @param desk
	 * @param listener
	 */
	public  void download(String uri,String desk,FileCallBack listener){
		if(TextUtils.isEmpty(uri))return;
		//如果已经在下载 直接返回 加入监听器
		if(downloadTasks.containsKey(uri)){
			return;
		}
		if(TextUtils.isEmpty(desk)){
				desk=getDownloadDesk(uri,null);
		}
		if(null!=listener)
			downloadTasks.put(uri, listener);
		ThreadPools.execute(new DownloadTask(uri,desk));
	}
	public void release(){
		downloadTasks.clear();
	}

	public void remove(String uri){
		downloadTasks.remove(uri);
	}

	 class DownloadTask implements Runnable{
		private String uri;
		private DownloadCallBack callback;
		public DownloadTask(String uri,String desk){
			callback = new DownloadCallBack(downloadDir.getAbsolutePath(),desk,uri);
			this.uri=uri;
		}
		@Override
		public void run() {
			try{
				OkHttpUtils.get().url(uri).build().execute(callback);
			}catch(Exception e){
				e.printStackTrace();
				callback.onError(null,e);
			}
		}
		
	}
	  class DownloadCallBack extends FileCallBack{
		public String uri;

		  public DownloadCallBack(String destFileDir, String destFileName,String uri) {
			  super(destFileDir, destFileName);
			  this.uri = uri;
		  }

		  @Override
		public void onError(Call call, Exception e) {
			if(isExists(uri)){
				FileCallBack l= downloadTasks.remove(uri);
				if(null!=l)
					l.onError(call,e);


			}
		}

		@Override
		public void onResponse(File response) {
			if(isExists(uri)){
				FileCallBack l= downloadTasks.remove(uri);
				if(null!=l)
					l.onResponse(response);
			}
		}

		@Override
		public void inProgress(float progress, long total) {
			if(isExists(uri)){
				FileCallBack l= downloadTasks.get(uri);
				if(null!=l)
					l.inProgress(progress, total);
			}
		}


	}

	public static String MD5(String val) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(val.getBytes());
		byte[] b = md5.digest();// 加密
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(b[i]);
		}
		return sb.toString();
	}

}
