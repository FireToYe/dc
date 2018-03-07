package com.zhilink.srm.manager.modules.ica.thread;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.zhilink.manager.framework.common.utils.Threads;
import com.zhilink.srm.manager.modules.ica.entity.IcaApiLog;
import com.zhilink.srm.manager.modules.ica.service.IcaApiLogService;
import com.zhilink.srm.manager.modules.rfa.entity.RfaServiceLog;
import com.zhilink.srm.manager.modules.rfa.service.RfaServiceLogService;


/**
 * ica日志工作线程
 * @author chrisyeye
 * @version 2017-09-18
 */
@Component
public class RfaLogInsertThread{
	
	/**
	 * 日志记录对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private volatile LogThreadState state = LogThreadState.STATE_READY;
	
	@Value("${ica.log.state}")
	protected String logState;
	/**
	 * 日志队列
	 */
	private static final int THREAD_SIZE = 20;
	private LinkedBlockingQueue<RfaServiceLog> queue = new LinkedBlockingQueue<RfaServiceLog>();
	/**
	 * 当日志队列满时，则开启线程去进行阻塞队列
	 */
	private ExecutorService executor = Executors.newCachedThreadPool();
	@Autowired
	private RfaServiceLogService rfaServiceLogService;

	
	public class IcaApiLogThread implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			while(true){
				try {
					if(state==LogThreadState.STATE_DESTROYED){//如果线程标志为STATE_DESTROYED状态，代表结束线程，不可重启状态。
						logger.info("日志工作线程已经关闭");
						return;
					}
					RfaServiceLog rfaServiceLog = queue.take();
					try{
					rfaServiceLogService.save(rfaServiceLog);
					}catch(Exception e){
						logger.error(e.getMessage());
						logger.info(rfaServiceLog.toString());//存入日志异常时。将实体存入到日志文件中
						e.printStackTrace();
					}
					logger.debug("当前rfa日志工作队列大小为：{},处理队列：{}",queue.size(),Thread.currentThread().getName());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	/**
	 * 线程变为等待状态变为等待
	 */
	public synchronized void waitThread(){
		state=LogThreadState.STATE_READY;
		queue.clear();
	}
	
	public synchronized void startThread(){
		state=LogThreadState.STATE_STARTED;
	}
	
	
	public synchronized void endThread(){
		state=LogThreadState.STATE_DESTROYED;
		Threads.gracefulShutdown(executor, 30, 60, TimeUnit.SECONDS);
	}
	/**
	 * 将日志实体类添加到日志队列中，如果超出队列大小，则开启线程阻塞
	 * @param icaApiLog
	 */
	public void insert(RfaServiceLog rfaServiceLog) {
		if(state==LogThreadState.STATE_STARTED){
			if(!queue.offer(rfaServiceLog)){
				logger.info(rfaServiceLog.toString());
			}
		};
	}
	public void setState(LogThreadState stateStarted) {
		// TODO Auto-generated method stub
		this.state = state;
	}

	@PostConstruct
	public void startExecutors(){
		if("true".equals(logState)){
			state = LogThreadState.STATE_STARTED;
		}else{
			state = LogThreadState.STATE_READY;
		}
		for(int i=0;i<THREAD_SIZE;i++){
			executor.execute(new IcaApiLogThread());
		}
		executor.shutdown();
	}

}
