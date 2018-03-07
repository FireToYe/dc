/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.rfa.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 接口适配器调用日志Entity
 * @author chrisye
 * @version 2017-11-17
 */
public class RfaServiceLog extends DataEntity<RfaServiceLog> {
	
	private static final long serialVersionUID = 1L;
	private String urlId;		// url链接id
	private String serviceCode;		// 服务编码
	private String serviceName;		// 服务名称
	private String requestParams;		// 接口请求参数
	private String serviceParams;		// 服务参数
	private String result;		// 返回结果
	private String description;		// 描述
	private String url;		// 请求地址
	private String headers;		// 请求头信息
	private String method;		// 请求方式
	private String contentType;		// 解析类型
	private Date createTime;		// 创建时间
	private String isSuccess;		// 状态
	private String ip;		// ip地址
	private String resDuration;
	private String  responseResult;
	public RfaServiceLog() {
		super();
	}

	public RfaServiceLog(String id){
		super(id);
	}

	public RfaServiceLog(String urlId, String serviceCode, String serviceName,
			String requestParams, String serviceParams, String result,
			String description, String url, String headers, String method,
			String contentType, String isSuccess, String ip, String resDuration,String responseResult) {
		super();
		this.urlId = urlId;
		this.serviceCode = serviceCode;
		this.serviceName = serviceName;
		this.requestParams = requestParams;
		this.serviceParams = serviceParams;
		this.result = result;
		this.description = description;
		this.url = url;
		this.headers = headers;
		this.method = method;
		this.contentType = contentType;
		this.isSuccess = isSuccess;
		this.ip = ip;
		this.resDuration = resDuration;
		this.responseResult = responseResult;
	}

	@Length(min=0, max=50, message="url链接id长度必须介于 0 和 50 之间")
	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
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
	
	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}
	
	public String getServiceParams() {
		return serviceParams;
	}

	public void setServiceParams(String serviceParams) {
		this.serviceParams = serviceParams;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@Length(min=0, max=255, message="描述长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=255, message="请求地址长度必须介于 0 和 255 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}
	
	@Length(min=0, max=255, message="请求方式长度必须介于 0 和 255 之间")
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	@Length(min=0, max=11, message="解析类型长度必须介于 0 和 11 之间")
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Length(min=0, max=11, message="状态长度必须介于 0 和 11 之间")
	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	@Length(min=0, max=255, message="ip地址长度必须介于 0 和 255 之间")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getResDuration() {
		return resDuration;
	}

	public void setResDuration(String resDuration) {
		this.resDuration = resDuration;
	}

	public String getResponseResult() {
		return responseResult;
	}

	public void setResponseResult(String responseResult) {
		this.responseResult = responseResult;
	}
	
}