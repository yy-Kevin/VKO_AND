package cn.vko.ring.utils;

import java.io.File;

import cn.vko.ring.R;

import android.os.Environment;
import android.text.TextUtils;

public class FileUtil {
	private final static String SUBDIR = "vko";
	public static final String CACHE = "cache";
	public static final String VIDEO = "video";
	public static final String CAMERA = "camera";
	public static final String AUDIO = "audio";
	public static final String DOWNLOAD = "vkodown";
	public static final String WEBVIEW = "webview";

	private static File getDir() {
		File workDir = new File(Environment.getExternalStorageDirectory(),
				SUBDIR);
		if (!workDir.exists()) {
			workDir.mkdirs();
		}
		return workDir;
	}

	private static File getWorkDir(String subDir) {
		File workDir = getDir();
		if (!TextUtils.isEmpty(subDir)) {
			workDir = new File(workDir, subDir);
			if (!workDir.exists()) {
				workDir.mkdirs();
			}
		}
		return workDir;
	}

	public static File getAudioDir() {
		return getWorkDir(AUDIO);
	}

	public static File getCameraDir() {
		return getWorkDir(CAMERA);
	}

	public static File getVideoDir() {
		return getWorkDir(VIDEO);
	}

	public static File getDownloadDir() {
		return getWorkDir(DOWNLOAD);
	}

	public static File getWebViewDir() {
		return getWorkDir(WEBVIEW);
	}

	/**
	 * 获取临时目录
	 * 
	 * @return
	 */
	public static File getCrashDir() {
		return getWorkDir("crash");
	}

	/**
	 * 文件缓存 目录
	 * 
	 * @return
	 */
	public static File getCacheDir() {

		return getWorkDir(CACHE);
	}

	public static String getFileName(String filePath) {
		if (!TextUtils.isEmpty(filePath)) {
			return filePath.substring(filePath.lastIndexOf("/") + 1);
		}
		return null;
	}

	public static int getFileIcon(File f) {
		if (f.isDirectory()) {
			return R.drawable.folderopen;
		}
		return getFileIcon(f.getName());
	}

	public static int getFileIcon(String fName) {
		int type = -1;
		String end;
		/* 取得扩展名 */
		if (fName.contains(".")) {
			end = fName.substring(fName.lastIndexOf(".") + 1, fName.length())
					.toLowerCase();
		} else {
			end = fName;
		}
		/* 依扩展名的类型决定MimeType */
		if (end.equals("html") || end.endsWith("htmlx")) {
			type = R.drawable.file_icon_html;
		} else if (end.equals("pdf")) {
			type = R.drawable.file_icon_pdf;
		} else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = R.drawable.file_icon_mp3;
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = R.drawable.file_icon_video;
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = R.drawable.file_icon_jpg;
		} else if (end.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			type = R.drawable.file_icon_apk;
		} else if (end.equals("pptx") || end.equals("ppt")) {
			type = R.drawable.file_icon_ppt;
		} else if (end.equals("docx") || end.equals("doc")) {
			type = R.drawable.file_icon_doc;
		} else if (end.equals("xlsx") || end.equals("xls")) {
			type = R.drawable.file_icon_xls;
		} else if (end.equals("txt")) {
			type = R.drawable.file_icon_txt;
		} else if (end.equals("zip")) {
			type = R.drawable.file_icon_zip;
		} else {
			// /*如果无法直接打开，就跳出软件列表给用户选择 */
			type = R.drawable.file_icon_unknow;
		}
		return type;
	}

}
