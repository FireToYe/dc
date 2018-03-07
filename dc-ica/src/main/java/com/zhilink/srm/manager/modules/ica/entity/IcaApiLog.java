/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.ica.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhilink.srm.common.persistence.DataEntity;


/**
 * ica请求日志Entity
 * @author chrisye
 * @version 2017-09-15
 */
public class IcaApiLog extends DataEntity<IcaApiLog> {
	
	private static final long serialVersionUID = 1L;
	private String serviceCode;		// 服务编码
	private String serviceName;		// 服务名称
	private String serviceParams;		// 服务参数
	private String dbSql;		// 数据库sql
	private String responeTemplate;		// 返回模板
	private String result;		// 返回结果
	private String ip;		// 请求地址
	private String resDuration;		// 响应时长
	private String isSuccess;		// 状态  0:失败 1:成功
	private Date createTime;		// 创建时间
	private String beginCreateTime;		// 开始 创建时间
	private String endCreateTime;		// 结束 创建时间
	private String dbId;
	public IcaApiLog() {
		super();
	}

	public IcaApiLog(String id){
		super(id);
	}

	@Length(min=0, max=255, message="服务编码长度必须介于 0 和 255 之间")
	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	@Length(min=0, max=255, message="服务名称长度必须介于 0 和 255 之间")
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getServiceParams() {
		return serviceParams;
	}

	public void setServiceParams(String serviceParams) {
		this.serviceParams = serviceParams;
	}
	
	public String getDbSql() {
		return dbSql;
	}

	public void setDbSql(String dbSql) {
		this.dbSql = dbSql;
	}
	
	public String getResponeTemplate() {
		return responeTemplate;
	}

	public void setResponeTemplate(String responeTemplate) {
		this.responeTemplate = responeTemplate;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@Length(min=0, max=255, message="请求地址长度必须介于 0 和 255 之间")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getBeginCreateTime() {
		return beginCreateTime;
	}

	public void setBeginCreateTime(String beginCreateTime) {
		this.beginCreateTime = beginCreateTime;
	}
	
	public String getEndCreateTime() {
		return endCreateTime;
	}

	public void setEndCreateTime(String endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}
	
}