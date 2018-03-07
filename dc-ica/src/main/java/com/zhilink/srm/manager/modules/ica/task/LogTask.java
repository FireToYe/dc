package com.zhilink.srm.manager.modules.ica.task;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.manager.modules.ica.service.IcaApiLogService;

@Service
@Lazy(false)
/**
 * 适配器日志删除任务
 */
public class LogTask {
	@Value("${ica.log.timeSize}")
	protected String timeSize ;
	
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IcaApiLogService icaApiLogService;
	
	@Scheduled(cron="${ica.log.cron}")
	public void job(){
		logger.info("日志定时删除任务启动");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		if(!StringUtils.isEmpty(timeSize)){
			cal.add(Calendar.DATE, 0-Integer.parseInt(timeSize));
		}else{
			cal.add(Calendar.DATE, -1);
		}
		icaApiLogService.deleteByTask(cal.getTime());
		icaApiLogService.deleteRfaByTask(cal.getTime());
	}
}
