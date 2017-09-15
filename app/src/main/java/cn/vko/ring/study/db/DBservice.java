package cn.vko.ring.study.db;

import java.util.LinkedList;
import java.util.List;

import cn.vko.ring.VkoContext;
import cn.vko.ring.study.model.DownloadInfo;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBservice {
	private static final String TAG = "DBservice";
	private DBOpenHepler dbOpenHepler;
	private String userid;
	public DBservice(Context context) {
		// 1 -> database version
		userid = VkoContext.getInstance(context).getUserId();
		if(userid == null){
			userid = "1";
		}
		dbOpenHepler = new DBOpenHepler(context,3);
	}

	public void addDownloadFile(DownloadInfo info) {
		SQLiteDatabase db = dbOpenHepler.getWritableDatabase();
		String sql = "insert into downloadvideo(vid,videoid,goodid,title,duration,filesize,bitrate,userid) values(?,?,?,?,?,?,?,?)";
		db.execSQL(sql,new Object[] { info.getVid(),info.getVideoid(),info.getGoodid(),info.getTitle(), info.getDuration(),info.getFilesize(),info.getBitrate(),userid});
	}
	
	
	
	public void deleteDownloadFile(DownloadInfo info) {
		SQLiteDatabase db = dbOpenHepler.getWritableDatabase();
		String sql = "delete from downloadvideo where (userid=1 or userid=?) and vid=? and bitrate=?";
		db.execSQL(sql,new Object[] {userid, info.getVid(),info.getBitrate() });
	}
	public void updatePercent(DownloadInfo info,int percent) {
		SQLiteDatabase db = dbOpenHepler.getWritableDatabase();
		String sql = "update downloadvideo set percent=? where (userid=1 or userid=?) and vid=? and bitrate=?";
		db.execSQL(sql,new Object[] {percent,userid, info.getVid(),info.getBitrate() });
	}
	
	public LinkedList<DownloadInfo> getCompleteFiles(){
		LinkedList<DownloadInfo> infos = new LinkedList<DownloadInfo>();
		SQLiteDatabase db = dbOpenHepler.getWritableDatabase();
		String sql="select vid,videoid,goodid,title,duration,filesize,bitrate from downloadvideo where (userid=1 or userid="+ userid +") and percent=100";
		Cursor cursor=db.rawQuery(sql, null);
		while(cursor.moveToNext()){
			String vid=cursor.getString(cursor.getColumnIndex("vid"));
			String videoid=cursor.getString(cursor.getColumnIndex("videoid"));
			String goodid=cursor.getString(cursor.getColumnIndex("goodid"));
			String title=cursor.getString(cursor.getColumnIndex("title"));
			String duration=cursor.getString(cursor.getColumnIndex("duration"));			
			int filesize=cursor.getInt(cursor.getColumnIndex("filesize"));
			int bitrate=cursor.getInt(cursor.getColumnIndex("bitrate"));
			DownloadInfo info = new DownloadInfo(vid, videoid,goodid,duration, filesize,bitrate);	
			info.setTitle(title);
			infos.addLast(info);
		}
		return infos;
	}
	public boolean isAdd(DownloadInfo info) {
		SQLiteDatabase db = dbOpenHepler.getWritableDatabase();
		String sql = "select vid,videoid,goodid,duration,filesize,bitrate from downloadvideo where (userid=1 or userid="+ userid +") and vid=? and bitrate=" + info.getBitrate();
		Cursor cursor = db.rawQuery(sql, new String[] { info.getVid() });
		if (cursor.getCount() >= 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public LinkedList<DownloadInfo> getDownloadFiles(){
		LinkedList<DownloadInfo> infos = new LinkedList<DownloadInfo>();
		SQLiteDatabase db = dbOpenHepler.getWritableDatabase();
		String sql ="select vid,videoid,goodid,title,duration,filesize,bitrate,percent from downloadvideo where (userid=1 or userid="+ userid +") and percent<100";
		Cursor cursor= db.rawQuery(sql, null);
		while(cursor.moveToNext()){
			String vid=cursor.getString(cursor.getColumnIndex("vid"));
			String videoid=cursor.getString(cursor.getColumnIndex("videoid"));
			String goodid=cursor.getString(cursor.getColumnIndex("goodid"));
			String title=cursor.getString(cursor.getColumnIndex("title"));
			String duration=cursor.getString(cursor.getColumnIndex("duration"));			
			int filesize=cursor.getInt(cursor.getColumnIndex("filesize"));
			int bitrate=cursor.getInt(cursor.getColumnIndex("bitrate"));
			int percent=cursor.getInt(cursor.getColumnIndex("percent"));
			DownloadInfo info = new DownloadInfo(vid, videoid,goodid,duration, filesize,bitrate);
			info.setPercent(percent);
			info.setTitle(title);
			infos.addLast(info);
		}
		return infos;
	}
	
	public DownloadInfo getLoadInfo(String videoId){
		LinkedList<DownloadInfo> infos = new LinkedList<DownloadInfo>();
		SQLiteDatabase db = dbOpenHepler.getWritableDatabase();
		String sql = "select vid,videoid,goodid,duration,filesize,bitrate from downloadvideo where (userid=1 or userid="+ userid +") and videoid=? and  percent=100";
		Cursor cursor = db.rawQuery(sql, new String[] {videoId});
		while(cursor.moveToNext()){
			String vid=cursor.getString(cursor.getColumnIndex("vid"));
			String videoid=cursor.getString(cursor.getColumnIndex("videoid"));
			String goodid=cursor.getString(cursor.getColumnIndex("goodid"));
			String duration=cursor.getString(cursor.getColumnIndex("duration"));
			int bitrate=cursor.getInt(cursor.getColumnIndex("bitrate"));
			DownloadInfo info = new DownloadInfo(vid, videoid,goodid,duration, 0,bitrate);
			infos.addLast(info);
		}
		if (infos==null || infos.size() ==0) {
			return null;
		}
		return infos.get(0);
	}
	
	
	
}
