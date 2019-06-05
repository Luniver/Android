package hrst.sczd.utils;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 沈月美
 * @describe 线程池类
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class ThreadPoolExecutors {
    /**
     * 构造方法，创建线程池
     * 
     * @return ThreadPoolExecutor
     */
	public static ThreadPoolExecutor getThread() {
		ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 30, 3L,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
				new ThreadPoolExecutor.AbortPolicy());
		return pool;
	}
}
