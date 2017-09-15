package cn.vko.ring;

import cn.vko.ring.utils.FileUtil;

/**
 * @ClassName: ConstantUrl
 * @Description: TODO
 * @author: JiaRH
 * @date: 2015-11-26 下午2:34:22
 */
public class ConstantUrl {
//		public static String VKOIP = "http://apitest.vko.cn";// 线上测试
	//	public static String VKOIP ="http://192.168.1.35:8109";//张飞
//		 public static String VKOIP ="http://192.168.1.160:8109";//张济超
//		 public static String VKOIP = "http://192.168.1.246:8109";//
	//	 public static String VKOIP = "http://192.168.1.36:8109";// 丽娟
	public static String VKOIP = "http://api.vko.cn";// 线上

	//我的群组
	public static String VK_GETUSERTEAM = VKOIP+"/groups/getUserTeam";
	//班级课程列表
	public static String VK_COURSELIST = VKOIP+"/class/courseList";

	//查询任务列表
	public static String VK_GROUP_TASK_LIST = VKOIP+"/groups/queryGroupTask";

	public static String VK_SEND_CLASS=VKOIP+"/groups/createVideoGroupTask";
	/**
	 * 埋点广告
	 */
	public static String VK_ACTIVITY = VKOIP+"/ad/activity";
	// 2.0 获取学科
	public static String VK_COURSE_SUBJECT = VKOIP + "/index/allIndex";
	/* 首页获取学科 */
	public static String VK_STUDY_SUBJECT = VKOIP + "/index/subject";
	/* 教材版本 */
	public static String VK_BOOK_VERSION = VKOIP + "/index/versionAndBook";
	// 热点推荐
	public static String VK_COURSE_RECOMMONED = VKOIP + "/index/ranK1";
	// 检测使用时长
	public static String VK_APP_GATHER = VKOIP + "/app/login_store";
	// 未读信息条数
	public static String VK_UNREAD_NUM = VKOIP + "/user/newMsgSum";
	// 检测问卷
	public static String VK_QUESTIONCHECK = VKOIP + "/app/existsResult";
	// 课程列表首页
	public static String VK_COURSE = VKOIP + "/course/detail";
	// 获取书本
	public static String VK_COURSE_BOOKLIST = VKOIP + "/base/book";
	// 新获取书本
	public static String VK_ECHOSUBJECT_BOOKLIST = VKOIP + "/user/echoSubject";
	// 小包视频
	public static String VK_COURSETYPE = VKOIP + "/course/pack/";
	// 看课轨迹
	public static String VK_COURSE_TRACK = VKOIP + "/course/track";
	// 获取视频信息
	public static String VK_GETGOODSVIDEO = VKOIP + "/course/source/";
	// 测试-二级图谱列表
	public static String VK_GETTUPU = VKOIP + "/exam/tupu";
	// 全部
	public static String VK_GETTUPU_ALL = VKOIP + "/exam/tupuall";
	// 难度系数
	public static String VK_LEVEL = VKOIP + "/base/dfct";
	// 试题测试
	public static String VK_EXAMTEST = VKOIP + "/exam/list";
	// 课后练习
	public static String VK_CLASSTEST = VKOIP + "/course/homework/";
	// 查看战绩
	public static String VK_SEERESULT = VKOIP + "/exam/record";
	// 试卷提交
	public static String VK_SUBMITTEST = VKOIP + "/exam/submit";
	// 试卷纠错
	public static String VK_ERROR_SUBMIT = VKOIP + "/exam/feedbackExam";
	// 答疑列表
	public static String VK_TOPIC_LIST = VKOIP + "/topic/listByObj";
	// 我的问题列表
	public static String VK_TOPIC_MYLIST = VKOIP + "/topic/myList";
	// 我的回答过的问题列表
	public static String VK_TOPIC_MYANSWERLIST = VKOIP + "/topic/myAnswer";
	// 发布问题
	public static String VK_SUBMIT_ANSWER = VKOIP + "/topic/pubByObj";
	// 上传文件
	public static String VK_UPLOAD_FILE = VKOIP + "/topic/uploads";
	// 搜题 http://ip:port/s/searchExamByImage
	public static String VK_SEARCH_PROBLEM = VKOIP + "/s/searchExamByImage";
	// 搜题 采纳
	public static String VK_ADOPT_QUESTION = VKOIP + "/s/accept";
	// 搜题记录
	public static String VK_SEARCH_RECORD = VKOIP + "/s/searchLogs";
	// 搜题详情
	public static String VK_SEARCH_DETAIL = VKOIP + "/s/searchDetail";
	// 问题详情
	public static String VK_TOPIC_DETAIL = VKOIP + "/topic/detail";
	// 回复问题
	public static String VK_TOPIC_REPLY = VKOIP + "/topic/reply";
	// 同问
	public static String VK_TOPIC_SAME = VKOIP + "/topic/same";
	// 采纳答案
	public static String VK_TOPIC_BEST = VKOIP + "/topic/best";
	// 专题列表
	public static String VK_SPECIAL_LIST = VKOIP + "/goods/special";
	// 我的课程专题列表
	public static String VK_MYCOURSEZT_LIST = VKOIP + "/user/myCoursezt";
	//我的课程
	public static String VK_MYCOURSE_LIST = VKOIP + "/myCourse/courseList";
	// 搜索
	public static String VK_SEARCH_FIRST = VKOIP + "/s";
	// 公式速记
	public static String VK_FORMULAR_REMEMBER = "http://m.vko.cn/find/equation.html";
	// 分页搜索
	public static String VK_SEARCH_PAGE = VK_SEARCH_FIRST + "/next";
	// 举报
	public static String VK_REPORT = VKOIP + "/app/report";
	// 问卷
	public static String VK_QUESTION = "http://quan.vko.cn/wj/index.html";;
	//
	public static String VK_COURSE_BOOK = VKOIP + "/base/book";
	// 分享的图片地址
	public static final String FXIMGPATH = FileUtil.getCameraDir().getPath().concat(Constants.SCREENSHORT);
	public static final String VK_BIND_DEVICETOKEN = VKOIP
			+ "/app/bindPushToken";
	/** ########################Message############################ */
	/* 群主审核 */
	public static final String VK_AUDIT = VKOIP
			+ "/interestgroup/validateApply";
	/** ########################Message############################ */
	/** ########################TEST############################ */
	// 获取书本
	public static String VK_TEST_BOOKLIST = VKOIP + "/exam/bookList";
	// 综合测试
	public static String VK_TEST_TKSPESECTION = VKOIP + "/exam/tkSpeSection";
	// 同步测试
	public static String VK_TEST_TKSECTION = VKOIP + "/exam/tkSection";
	// 同步分组测试题
	public static String VK_TEST_TKLIST = VKOIP + "/exam/tkList";
	// 1.04 after 同步分组测试题
	public static String VK_TEST_TKLISTV2 = VKOIP + "/exam/tkListV2";
	// 1.04 after 获取试题
	public static String VK_TEST_RANTKLIST = VKOIP + "/exam/ranTkList";
	// 获取试题
	public static String VK_TEST_TKSPECLIST = VKOIP + "/exam/tkSpecList";
	// 获取课后练习
	public static String VK_COURSE_HOMEWORK = VKOIP + "/course/homework";
	// 测评人物形象
	public static String VK_TEST_SHOWROLES = VKOIP + "/exam/showRoles";
	// 交卷
	public static String VK_TEST_NEWSUBMIT = VKOIP + "/exam/newSubmitV3";
	// 晒成绩
	public static String VK_TEST_SHOWRESULT = VKOIP + "/exam/showResult";
	// 查看做题解析
	public static String VK_TEST_QUERYRESULT = VKOIP + "/exam/queryResult";
	// 查看做战绩 （同步）
	public static String VK_TEST_NEWRECORD = VKOIP + "/exam/newRecord";
	// 查看做战绩 （综合）
	public static String VK_TEST_RECORD = VKOIP + "/exam/record";
	// 是否设置人物形象
	public static String VK_TEST_QUERYUSERROLE = VKOIP + "/user/queryUserRole";
	/** ########################New___TEST############################ */
	/* 获取章下面的节的列表 */
	public static String VK_TEST_SECTION_LIST = VKOIP + "/tbExam/sectionList";
	/* 综合测试列表 */
	public static String VK_TEST_COMP_SECTION_LIST = VKOIP + "/zhExam/k2List";
	/** ########################CIRCLE############################ */
	/* 推荐学习圈列表 */
	public static String VK_CIRCLE_INTEREST_LIST = VKOIP
			+ "/interestgroup/recommendStudyGroup";
	/* 判断是否关注过 推荐学习圈 */
	public static String VK_CIRCLE_INTEREST_CHECKED = VKOIP
			+ "/interestgroup/checkStudyGroup";
	/* 申请加群/关注 */
	public static String VK_CIRCLE_JOIN_GROUP = VKOIP
			+ "/interestgroup/joinGroup";
	/* 兴趣圈的所有二级类型 */
	public static String VK_CIRCLE_CLASS = VKOIP + "/interestgroup/types";
	/* 创建兴趣群 */
	public static String VK_CIRCLE_CREATE_GROUP = VKOIP
			+ "/interestgroup/create";
	/* 根据关键字搜群 */
	public static String VK_CIRCLE_SEARCH_GROUP = VKOIP
			+ "/interestgroup/searchGroup";
	/* 根据关键字搜群 */
	public static String VK_CIRCLE_SEARCH_MEMBER = VKOIP
			+ "/interestgroup/searchGroupMember";
	/* 解散群 */
	public static String VK_CIRCLE_DELETE_GROUP = VKOIP
			+ "/interestgroup/delete";
	/* 取消关注（退群） */
	public static String VK_CIRCLE_QUIT_GROUP = VKOIP
			+ "/interestgroup/cancel/member";
	/* 群主踢人 */
	public static String VK_CIRCLE_DELECT_MEMBER = VKOIP
			+ "/interestgroup/delete/member";
	/* 设置管理员 */
	public static String VK_CIRCLE_SET_MANAGER = VKOIP
			+ "/interestgroup/setManager";
	/* 取消管理员 */
	public static String VK_CIRCLE_CANCEL_MANAGER = VKOIP
			+ "/interestgroup/cancelManager";
	/* 群主更新群信息 */
	public static String VK_CIRCLE_UPDATE_GROUP = VKOIP
			+ "/interestgroup/updateGroup";
	/* 帖子详情 */
	public static String VK_CIRCLE_TOPIC_DETAIL = VKOIP + "/article/detail";
	/* 帖子详情 */
	public static String VK_CIRCLE_GROUP_DETAIL = VKOIP
			+ "/interestgroup/lookGroup";
	/* 发帖子 */
	public static String VK_CIRCLE_SUBMIT_ARTICLE = VKOIP
			+ "/article/pubArticle";
	/* 评论 */
	public static String VK_CIRCLE_SUBMIT_REPLY = VKOIP + "/article/reply";
	/* 上伟文件 */
	public static String VK_CIRCLE_UPLOAD_FILE = VKOIP + "/article/uploads";
	/* 删除话题 */
	public static String VK_CIRCLE_DEL_TOPIC = VKOIP + "/article/delArticle";
	/* 删除评论 */
	public static String VK_CIRCLE_DEL_REPLY = VKOIP + "/article/delReply";
	/* 评论 点赞 */
	public static String VK_CIRCLE_PRA_REPLY = VKOIP + "/article/replyZan";
	/* 首页消息 */
	public static String VK_MESSAGE_LIST = VKOIP + "/user/myMsg";
	/* 系统消息 */
	public static String VK_MESSAGE_SYS_LIST = VKOIP + "/user/mySysMsg";
	/* 购买视频*/
	public static String VK_CIRCLE_BUY = VKOIP + "/groups/groupBuy";
	/* 获取会员总积分*/
	public static String VK_CIRCLE_SCORE = VKOIP + "/user/memberTotalScore";
	/*****************VB***********************/
	/* 签到*/
	public static String VK_CIRCLE_SIGN = VKOIP + "/score/signIn";
	/* 分享视频得Vb*/
	public static String VK_VB_SHARE = VKOIP + "/score/share";
	/* 邀请好友得Vb*/
	public static String VK_VB_ASKFRIEND = VKOIP + "/score/askFriend";
	
	/**
	 * 本地课程列表
	 */
	public static String VK_LOCAL_COURSE=VKOIP+"/localCourse/localCourseList";
	/**
	 * 本地课程中的视频列表
	 */
	public static String VK_LOCAL_VIDEO=VKOIP+"/localCourse/videoList";
	/**
	 * ad
	 */
	public static String VK_AD_LIST=VKOIP+"/ad/adList";
	/**
	 * 城市列表
	 */
	public static String VK_CITY_LIST=VKOIP+"/localCourse/cityList";

	public static String VK_CUSETEM_URL=VKOIP+"/user/getUserImToken";

	public static String VK_MY_CODE = VKOIP+"/user/generteCode";
	//发布任务
	public static String VK_GROUP_SUBMIT_TASK = VKOIP
			+ "/groups/createGroupTask";
	
	
	public static String getPerviewUrl(String uri) {
		return uri;
	}
}
