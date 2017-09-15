package cn.shikh.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	/**
	 * 判断是否存在sd�?
	 */
	public static boolean hasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 拷贝文件
	 * 
	 * @param oldPath
	 *            需要拷贝的地址
	 * @param newPath
	 *            新文件地址
	 * @throws Exception
	 */
	public static void copyFile(String oldPath, String newPath)
			throws Exception {

		int byteread = 0;
		File oldfile = new File(oldPath);
		if (oldfile.exists()) { // 文件存在时
			InputStream inStream = new FileInputStream(oldPath); // 读入原文件
			FileOutputStream fs = new FileOutputStream(newPath);
			byte[] buffer = new byte[2048];

			while ((byteread = inStream.read(buffer)) != -1) {

				fs.write(buffer, 0, byteread);
			}
			inStream.close();
		}

	}

	/**
	 * 保存文件
	 * 
	 * @param path
	 * @param data
	 * @throws IOException
	 */
	public static final void save(String path, String data) throws IOException {
		if (data == null)
			return;
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter buffOut = new BufferedWriter(
				new FileWriter(file));
		buffOut.write(data);
		buffOut.flush();
		buffOut.close();
		buffOut = null;
	}

	public static final void save(String path, byte[] data) throws IOException {
		if (data == null)
			return;
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedOutputStream buffOut = new BufferedOutputStream(
				new FileOutputStream(file));
		buffOut.write(data);
		buffOut.flush();
		buffOut.close();
		buffOut = null;
	}

	public static String getMIMEType(String fName) {
		String type = "";
		String end;
		/* 取得扩展名 */
		if (fName.contains(".")) {
			end = fName.substring(fName.lastIndexOf(".") + 1, fName.length())
					.toLowerCase();
		} else {
			end = fName.toLowerCase();
		}

		/* 依扩展名的类型决定MimeType */
		if (end.equals("html") || end.endsWith("htmlx")) {
			type = "text/html";
		} else if (end.equals("pdf")) {
			type = "application/pdf";//
		} else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio/*";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video/*";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image/*";
		} else if (end.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			type = "application/vnd.android.package-archive";
		} else if (end.equals("pptx") || end.equals("ppt")) {
			type = "application/vnd.ms-powerpoint";
		} else if (end.equals("docx") || end.equals("doc")) {
			type = "application/msword";
		} else if (end.equals("xlsx") || end.equals("xls")) {
			type = "application/vnd.ms-excel";
		} else if (end.equals("txt")) {
			type = "text/plain";
		} else {
			// /*如果无法直接打开，就跳出软件列表给用户选择 */
			type = "*/*";
		}
		return type;
	}

	public static void openFile(Context context, String paveUrl) {
		try {
			String type = getMIMEType(paveUrl);
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// 设置intent的Action属性
			intent.setAction(Intent.ACTION_VIEW);
			// 获取文件file的MIME类型

			// 设置intent的data和Type属性。
			File file = new File(paveUrl);
			intent.setDataAndType(/* uri */Uri.fromFile(file), type);
			// 跳转
			context.startActivity(intent);
			// Intent.createChooser(intent, "请选择对应的软件打开该附件！");

		} catch (ActivityNotFoundException e) {
			// TODO: handle exception
			Toast.makeText(context, "没有相当软件打开此文件", Toast.LENGTH_LONG).show();
		}
	}

	public static String getExt(String name) {
		int pos = name.lastIndexOf('.');
		if (pos > -1) {
			return name.substring(pos + 1).toLowerCase();
		}
		return null;
	}

	/**
	 * 在SD卡上创建
	 * 
	 * @param path
	 * @return
	 */
	public static File createSDDir(String path) {

		String newPath = new StringBuffer()
				.append(Environment.getExternalStorageDirectory()
						.getAbsolutePath()).append(File.separator).append(path)
				.toString();

		File f = new File(newPath);
		f.mkdirs();
		return f;
	}

	public static String getFileNameFromUrl(String url) {
		return url.substring(url.lastIndexOf("/") + 1, url.length());
	}
	/** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
	  public static void deleteFilesByDirectory(File directory) {
	    if (directory != null && directory.exists() && directory.isDirectory()) {
	      for (File item : directory.listFiles()) {
	        item.delete();
	      }
	    }
	  }
	
	/**
	 * 删除指定目录下文件及目录
	 * 
	 * @param deleteThisPath
	 * @param filePath
	 * @return
	 */
	public static void deleteFolderFile(String filePath, boolean deleteThisPath)
			throws IOException {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			deleteFolderFile(file, deleteThisPath);
		}
	}

	/**
	 * 删除指定目录下文件及目录
	 * 
	 * @param deleteThisPath
	 * @param file
	 * @return
	 */
	public static void deleteFolderFile(File file, boolean deleteThisPath)
			throws IOException {
		if (file.isDirectory()) {// 处理目录
			File files[] = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFolderFile(files[i].getAbsolutePath(), true);
			}
		}
		if (deleteThisPath) {
			if (!file.isDirectory()) {// 如果是文件，删除
				Log.e("fuxue", "" + file.delete());
			} else {// 目录
				if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
					file.delete();
				}
			}
		}
	}

}
