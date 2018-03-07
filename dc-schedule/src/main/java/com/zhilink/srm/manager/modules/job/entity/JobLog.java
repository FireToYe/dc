/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.job.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 调度日志Entity
 * @author chrisye
 * @version 2017-11-08
 */
public class JobLog extends DataEntity<JobLog> {
	
	private static final long serialVersionUID = 1L;
	private String jobId;		// 任务ID
	private String executeType;		// 执行方式
	private String groupName;		// 组名-任务名
	private String method;		// 类名+方法名
	private String resServiceCode;
	private String serviceCode;		// 服务编码
	private String requestParams;		// 请求参数
	private String result;		// 返回结果
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private String resDuration;		// 响应时长
	private String isSuccess;		// 状态  0:失败 1:成功
	private String preParamter;		// 上一周期参数
	private String exception;		// 错误信息
	
	public JobLog(String jobId, String executeType, String groupName,
			String method, String serviceCode, String requestParams,
			String result, Date startTime, Date endTime, String resDuration,
			String isSuccess, String preParamter, String exception) {
		super();
		this.jobId = jobId;
		this.executeType = executeType;
		this.groupName = groupName;
		this.method = method;
		this.serviceCode = serviceCode;
		this.requestParams = requestParams;
		this.result = result;
		this.startTime = startTime;
		this.endTime = endTime;
		this.resDuration = resDuration;
		this.isSuccess = isSuccess;
		this.preParamter = preParamter;
		this.exception = exception;
	}

	public JobLog(JobEntity jobEntity,String requestParams,String result,Date startTime, Date endTime,String isSuccess, String preParamter, String exception){
		this.jobId = jobEntity.getId();
		this.executeType = jobEntity.getExecuteType();
		this.groupName = jobEntity.getJobGroup()+"_"+jobEntity.getJobName();
		if("2".equals(jobEntity.getExecuteType())){
			this.method = jobEntity.getSpringId()+"."+jobEntity.getMethodName();
		}
		if("3".equals(jobEntity.getExecuteType())){
			this.method = jobEntity.getBeanClassName()+"."+jobEntity.getMethodName();
		}
		this.resServiceCode = jobEntity.getResServiceCode();
		this.serviceCode = jobEntity.getServiceCode();
		this.requestParams = requestParams;
		this.result = result;
		this.startTime = startTime;
		this.endTime = endTime;
		this.resDuration = String.valueOf(endTime.getTime()-startTime.getTime());
		this.isSuccess = isSuccess;
		this.preParamter = preParamter;
		this.exception = exception;
	}
	public JobLog() {
		super();
	}

	public JobLog(String id){
		super(id);
	}

	@Length(min=0, max=255, message="任务ID长度必须介于 0 和 255 之间")
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	
	@Length(min=0, max=1, message="执行方式长度必须介于 0 和 1 之间")
	public String getExecuteType() {
		return executeType;
	}

	public void setExecuteType(String executeType) {
		this.executeType = executeType;
	}
	
	@Length(min=0, max=255, message="组名-任务名长度必须介于 0 和 255 之间")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Length(min=0, max=255, message="类名+方法名长度必须介于 0 和 255 之间")
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	@Length(min=0, max=255, message="服务编码长度必须介于 0 和 255 之间")
	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	public String getResServiceCode() {
		return resServiceCode;
	}

	public void setResServiceCode(String resServiceCode) {
		this.resServiceCode = resServiceCode;
	}

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Length(min=0, max=255, message="响应时长长度必须介于 0 和 255 之间")
	public String getResDuration() {
		return resDuration;
	}

	public void setResDuration(String resDuration) {
		this.resDuration = resDuration;
	}
	
	@Length(min=0, max=11, message="状态  0:失败 1:成功长度必须介于 0 和 11 之间")
	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public String getPreParamter() {
		return preParamter;
	}

	public void setPreParamter(String preParamter) {
		this.preParamter = preParamter;
	}
	
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	
}