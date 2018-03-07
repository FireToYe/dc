/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.job.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 定时任务调度Entity
 * @author chrisye
 * @version 2017-10-11
 */
public class JobEntity extends DataEntity<JobEntity> {
	
	private static final long serialVersionUID = 1L;
	private String jobName;		// 任务名字
	private String jobGroup;		// 任务组名
	private String detail;		// 描述
	private String cronExpression;		// 执行cron表达式
	private String jobState;		// 状态：1:启动  0：停止
	private String isConcurrent;		// 是否有状态
	private String executeType;		// 执行方式
	private String parameter;		// 执行参数
	private String springId;		// springBeanId
	private String beanClassName;		// bean的包名+类名
	private String methodName;		// 实现方法名
	private Date createTime;		// 创建时间
	private Date updateTime;		// 更新时间
	private String serviceCode;		// 请求serviceCode
	private String dependLast;
	private String dependLastTime;
	private String resServiceCode;
	private String dateFormat;
	private String dataParameterName;
	public static final String STATUS_RUNNING = "1";
	public static final String STATUS_NOT_RUNNING = "0";
	public static final String CONCURRENT_IS = "1";
	public static final String CONCURRENT_NOT = "0";
	public JobEntity() {
		super();
	}

	public JobEntity(String id){
		super(id);
	}

	@Length(min=0, max=255, message="任务名字长度必须介于 0 和 255 之间")
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	@Length(min=0, max=255, message="任务组名长度必须介于 0 和 255 之间")
	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	
	@Length(min=0, max=255, message="描述长度必须介于 0 和 255 之间")
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	@Length(min=0, max=255, message="执行cron表达式长度必须介于 0 和 255 之间")
	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	
	@Length(min=1, max=255, message="状态：1:启动  0：停止长度必须介于 1 和 255 之间")
	public String getJobState() {
		return jobState;
	}

	public void setJobState(String jobState) {
		this.jobState = jobState;
	}
	
	@Length(min=0, max=255, message="是否有状态长度必须介于 0 和 255 之间")
	public String getIsConcurrent() {
		return isConcurrent;
	}

	public void setIsConcurrent(String isConcurrent) {
		this.isConcurrent = isConcurrent;
	}
	
	@Length(min=0, max=255, message="执行方式长度必须介于 0 和 255 之间")
	public String getExecuteType() {
		return executeType;
	}

	public void setExecuteType(String executeType) {
		this.executeType = executeType;
	}
	
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	@Length(min=0, max=255, message="springBeanId长度必须介于 0 和 255 之间")
	public String getSpringId() {
		return springId;
	}

	public void setSpringId(String springId) {
		this.springId = springId;
	}
	
	@Length(min=0, max=255, message="bean的包名+类名长度必须介于 0 和 255 之间")
	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}
	
	@Length(min=0, max=255, message="实现方法名长度必须介于 0 和 255 之间")
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Length(min=0, max=255, message="请求serviceCode长度必须介于 0 和 255 之间")
	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getDependLast() {
		return dependLast;
	}

	public void setDependLast(String dependLast) {
		this.dependLast = dependLast;
	}

	public String getDependLastTime() {
		return dependLastTime;
	}

	public void setDependLastTime(String dependLastTime) {
		this.dependLastTime = dependLastTime;
	}

	public String getResServiceCode() {
		return resServiceCode;
	}

	public void setResServiceCode(String resServiceCode) {
		this.resServiceCode = resServiceCode;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDataParameterName() {
		return dataParameterName;
	}

	public void setDataParameterName(String dataParameterName) {
		this.dataParameterName = dataParameterName;
	}
	
	
	
}