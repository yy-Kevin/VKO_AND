package cn.vko.ring.common.listener;


import cn.vko.ring.study.model.BookInfo;

public interface ISwitchLearnListener {
	void onUpdateLearn(String grade);

	void onChangeTextBook(BookInfo book);
}
