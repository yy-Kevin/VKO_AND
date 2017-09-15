package cn.vko.ring.study.model;

import cn.vko.ring.home.model.BaseResultInfo;

public class BaseSectionScoreInfo extends BaseResultInfo {
	private SectionScoreInfo data;
	
	
	public SectionScoreInfo getData() {
		return data;
	}


	public void setData(SectionScoreInfo data) {
		this.data = data;
	}


	public class SectionScoreInfo {
		private int needScore;
		
		private int score;

		public int getNeedScore() {
			return needScore;
		}

		public void setNeedScore(int needScore) {
			this.needScore = needScore;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}
		
		
	}
	
	
}
