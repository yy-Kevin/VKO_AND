package cn.vko.ring.study.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHepler extends SQLiteOpenHelper {
	private static final String DATEBASENAME = "downloadlist.db";

	public DBOpenHepler(Context context, int version) {
		super(context, DATEBASENAME, null, version);
	}

	public DBOpenHepler(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table if not exists downloadvideo (vid varchar(20),videoid varchar(20), goodid varchar(20),title varchar(100),userid varchar(100) NOT NULL default 1,duration varchar(20),filesize int,bitrate int,percent int default 0,primary key (vid, bitrate,userid))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion == 2) {
			db.execSQL("create table downloadvideo (vid varchar(20),videoid varchar(20), goodid varchar(20),title varchar(100),userid varchar(100) NOT NULL default 1,duration varchar(20),filesize int,bitrate int,percent int default 0,primary key (vid, bitrate,userid))");
		} else if (newVersion == 3) {
			// db.execSQL("ALTER TABLE downloadvideo ADD COLUMN userid varchar(100) NOT NULL default 1");
			// db.execSQL("ALTER TABLE downloadvideo DROP PRIMARY KEY (vid, bitrate) ADD PRIMARY KEY (vid, bitrate, userid)");
			db.execSQL("ALTER TABLE downloadvideo RENAME TO downloadvideo_temp");
			db.execSQL("create table if not exists downloadvideo (vid varchar(20),videoid varchar(20), goodid varchar(20),title varchar(100),userid varchar(100) NOT NULL default 1,duration varchar(20),filesize int,bitrate int,percent int default 0,primary key (vid, bitrate,userid))");
			db.execSQL("insert into downloadvideo SELECT vid,videoid,goodid,title,"+"1"+",duration,filesize,bitrate,percent from downloadvideo_temp");
			db.execSQL("DROP TABLE downloadvideo_temp");
		}
	}

}
