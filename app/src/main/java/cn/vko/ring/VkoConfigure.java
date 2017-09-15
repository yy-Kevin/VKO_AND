package cn.vko.ring;



import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class VkoConfigure {
	private final static String SHARED_NAME = "vko_bm";
	private SharedPreferences settings;
	private static VkoConfigure configure = null;

	private VkoConfigure(Context context) {
		settings = context.getSharedPreferences(SHARED_NAME, 0);
	}

	public static VkoConfigure getConfigure(Context context) {
		if (null == configure) {
			configure = new VkoConfigure(context);
		}
		return configure;
	}

	public void put(String key, String value) {
		Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void put(String key, boolean value) {
		Editor editor = settings.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void put(String key, int value) {
		Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public String getString(String key) {
		if (settings != null) {
			return settings.getString(key, "");
		}
		return "";

	}

	public int getInt(String key, int defaultValue) {
		if (settings != null) {
			return settings.getInt(key, defaultValue);
		}
		return defaultValue;
	}

	public String getString(String key, String defaultValue) {

		if (settings.contains(key)) {
			return settings.getString(key, defaultValue);
		}
		return defaultValue;
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		if (settings.contains(key)) {
			return settings.getBoolean(key, defaultValue);
		}
		return defaultValue;
	}
	public void clearConfigure(){
		if (settings != null) {
			settings.edit().clear();
			settings.edit().commit();
		}
	}
}
