package cn.shikh.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPools {
	private static ThreadPoolExecutor threadPool=new  ThreadPoolExecutor(3, 50,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
	public static void setMaxSize(int maximumPoolSize){
		if(maximumPoolSize>threadPool.getMaximumPoolSize())
		threadPool.setMaximumPoolSize(maximumPoolSize);
	}
	public static ThreadPoolExecutor getThreadPoolExecutor(){
		
		return threadPool;
	}
	public static void execute(Runnable run){
		if(null==run)return ;
			threadPool.execute(run);
	
	}
}
