package com.zhilink.srm.manager.modules.job.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.zhilink.manager.common.utils.Exceptions;
import com.zhilink.manager.framework.common.utils.SpringContextHolder;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.manager.modules.job.dao.JobLogDao;
import com.zhilink.srm.manager.modules.job.entity.JobEntity;
import com.zhilink.srm.manager.modules.job.entity.JobLog;
import com.zhilink.srm.manager.modules.sys.dao.LogDao;


/**
 * 定时任务执行service
 * @author chrisye
 * @version 2017-09-26
 */
public class TaskUtils {
	private static ApiClientUtil apiClientUtil=SpringContextHolder.getBean("apiClientUtil");
	private static JobLogDao jobLogDao=SpringContextHolder.getBean("jobLogDao");
	/**
	 * 日志对象
	 */
	public final static Logger logger = LoggerFactory.getLogger(TaskUtils.class);
	@SuppressWarnings("unchecked")
	public static void invokeMethod(JobEntity jobEntity){
		if(jobEntity==null){
			logger.debug("定时任务对象为空,请检查程序");
			return;
		}
		Object object = null;
		Class clazz  = null;
		String parameter = "";
		String result = null;
		String preResult  =null;
		Exception exception = null;
		String isSuccess = "1";
		Date startTime = new Date();
		try{
			if("Y".equals(jobEntity.getDependLastTime())){
				preResult  = getPreStartTime(jobEntity);
				Map<String,Object> paramterMap = JSON.parseObject(jobEntity.getParameter());
				if(!StringUtils.isEmpty(preResult)){
					//如获取到上次时间，插入到参数列表，并根据外部键值插入，如为空默认为dateBegin
					paramterMap.put(StringUtils.isEmpty(jobEntity.getDataParameterName())?"dateBegin":jobEntity.getDataParameterName(), preResult);
					parameter = JSON.toJSONString(paramterMap);
				}else{
					parameter = jobEntity.getParameter();
				}
			}else{
				parameter = jobEntity.getParameter();
			}
			if("1".equals(jobEntity.getExecuteType())){//1为直接执行ica方法
				startTime = new Date();
				result = execute(jobEntity,parameter);
			}else{
				if("2".equals(jobEntity.getExecuteType())){//2为SpringBean
					if(StringUtils.isNoneBlank(jobEntity.getSpringId())){
						object = SpringContextHolder.getBean(jobEntity.getSpringId());
					}
				}else if("3".equals(jobEntity.getExecuteType())){//3为java类
					if(StringUtils.isNoneBlank(jobEntity.getBeanClassName())){
						clazz = Class.forName(jobEntity.getBeanClassName());
						object = clazz.newInstance();
					}
				}
				clazz = object.getClass();
				Method method = null;
				method = clazz.getDeclaredMethod(jobEntity.getMethodName(),String.class);
				if(method!=null){
					result = (String) method.invoke(object,parameter);
				}
			} 
			logger.info("任务名称 = [" + jobEntity.getJobName() + "]----------启动成功");
			Date endTime = new Date();
			insertLog(jobEntity, parameter, result, startTime, endTime, isSuccess, preResult,exception);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("任务名称 = [" + jobEntity.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
			exception = e;
			isSuccess = "0";
			Date endTime = new Date();
			insertLog(jobEntity, parameter, result, startTime, endTime, isSuccess, preResult,exception);
			}
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String,Object> execute(String serviceCode,String parameter){
		//调用ica
		Map<String,Object> map= apiClientUtil.execute(serviceCode,JSONObject.parseObject(parameter));
//		Map<String,Object> map =(Map<String, Object>)  JSON.parse(result);
		if(map.get("service_code")!=null){
			return execute((String)map.get("service_code"), JSON.toJSONString(map.get("parameter")==null?"":map.get("parameter")));
		}
		return map;
	}
	/**
	 * 执行erp 并将最后一次执行的Ica作为下一次依赖的参数传递
	 * @param jobEntity
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private static String execute(JobEntity jobEntity,String parameter){
		//调用ica
		String serviceCode = jobEntity.getServiceCode();
		if(StringUtils.isEmpty(parameter)){
			parameter="{}";
		}
		Map<String,Object> map= apiClientUtil.execute(serviceCode,JSONObject.parseObject(parameter));
		//是否需要开启下一流程
		if(!StringUtils.isEmpty(jobEntity.getResServiceCode())){
			Map<String,Object> result =  execute(jobEntity.getResServiceCode(), JSON.toJSONString(map));
			return JSON.toJSONString(result);
		}
		return JSON.toJSONString(map);
	}
	
	public static void insertLog(JobEntity jobEntity ,String requestParams,String result,Date startTime,Date endTime,String isSuccess,String preParamter,Exception e){
		JobLog jobLog =new JobLog(jobEntity, requestParams, result, startTime, endTime, isSuccess,preParamter,Exceptions.getStackTraceAsString(e));
		jobLogDao.insert(jobLog);
	}
	/**
	 * 根据jobId获取最新一条数据的传惨
	 * @param jobId
	 * @return
	 */
	public static String getPreParameter(String jobId){
		JobLog jobLog =jobLogDao.getLastEnyity(jobId);
		if(jobLog!=null){
			String preParamter = jobLog.getResult();
			return preParamter;
		}
		return "";
	}
	
	/**
	 * 根据jobId获取上次数据的时间
	 * @param jobId
	 * @return
	 */
	public static String getPreStartTime(JobEntity jobEntity){
		JobLog jobLog =jobLogDao.getLastEnyity(jobEntity.getId());
		if(jobLog!=null){
			Date preStartDate = jobLog.getStartTime();
			//根据外部约定日期格式化格式，如为空默认yyyy/MM/dd HH:mm:ss
			SimpleDateFormat sdf = new SimpleDateFormat(StringUtils.isEmpty(jobEntity.getDateFormat())?"yyyy/MM/dd HH:mm:ss":jobEntity.getDateFormat());
			return sdf.format(preStartDate);
		}
		return null;
	}
}
