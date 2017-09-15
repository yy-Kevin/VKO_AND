package cn.vko.ring.study.model;

public class DownloadInfo {
	 private String vid;
	    private String duration;
	    private long filesize;
	    private int bitrate;
	    private int percent;
	    private String title;
	    private String date;	    
	    public String videoid ;
	    public String goodid;	    
	    	    		

		public String getVideoid() {
			return videoid;
		}

		public void setVideoid(String videoid) {
			this.videoid = videoid;
		}

		public String getGoodid() {
			return goodid;
		}

		public void setGoodid(String goodid) {
			this.goodid = goodid;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public int getPercent() {
			return percent;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setPercent(int percent) {
			this.percent = percent;
		}

		public void setFilesize(long filesize) {
			this.filesize = filesize;
		}

		public int getBitrate() {
			return bitrate;
		}

		public void setBitrate(int bitrate) {
			this.bitrate = bitrate;
		}

		public DownloadInfo(){
	    	
	    }
	    
		public DownloadInfo(String vid, String videoid,String goodid,String duration, long filesize, int bitrate) {
			this.vid = vid;
			this.videoid=videoid;
			this.goodid=goodid;
			this.duration = duration;
			this.filesize = filesize;
			this.bitrate = bitrate;
		}			
		public String getVid() {
			return vid;
		}
		public void setVid(String vid) {
			this.vid = vid;
		}
		public String getDuration() {
			return duration;
		}
		public void setDuration(String duration) {
			this.duration = duration;
		}
		public long getFilesize() {
			return filesize;
		}
		public void setFilesize(int filesize) {
			this.filesize = filesize;
		}
		@Override
		public String toString() {
			return "DownloadInfo [vid=" + vid + ", duration=" + duration
					+ ", filesize=" + filesize + "]";
		}
}


