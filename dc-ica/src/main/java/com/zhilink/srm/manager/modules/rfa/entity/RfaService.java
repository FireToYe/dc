/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.rfa.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhilink.srm.common.persistence.DataEntity;
import com.zhilink.srm.common.utils.excel.annotation.ExcelField;

/**
 * rfa系统服务管理Entity
 * @author chrisye
 * @version 2017-11-16
 */
public class RfaService extends DataEntity<RfaService> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="rfa.rfa_url_id",align=2,sort=1)
	private String urlId;		// url链接id
	@ExcelField(title="rfa.service_code",align=2,sort=2)
	private String serviceCode;		// 服务编码
	@ExcelField(title="rfa.service_name",align=2,sort=3)
	private String serviceName;		// 服务名称
	@ExcelField(title="rfa.service_params",align=2,sort=4)
	private String serviceParams;		// 服务参数
	@ExcelField(title="rfa.respone_template",align=2,sort=5)
	private String responeTemplate;		// 返回模板
	@ExcelField(title="rfa.params_resolve",align=2,sort=6)
	private String paramsResolve;		// 参数解析
	@ExcelField(title="rfa.respone_example",align=2,sort=7)
	private String responeExample;		// 返回示范
	@ExcelField(title="ica.description",align=2,sort=8)
	private String description;		// 描述
	@ExcelField(title="rfa.service_content_type",align=2,sort=9)
	private String contentType;		// 解析类型
	@ExcelField(title="ica.status",align=2,sort=8,dictType="iccn_ica_status")
	private String status;		// 状态  0:关闭 1:启用
	@ExcelField(title="rfa.service_request_address",align=2,sort=10)
	private String url;		// 请求地址
	private Date createTime;		// 创建时间
	private Date updateTime;		// 更新时间
	@ExcelField(title="rfa.service_headers",align=2,sort=11)
	private String headers;		// 请求头信息
	@ExcelField(title="rfa.service_method",align=2,sort=12)
	private String method;		// 请求方式
	@ExcelField(title="ica.custom_version",align=2,sort=13)
	private String customVersion;
	@ExcelField(title="ica.system_version",align=2,sort=14)
	private String systemVersion;
	@ExcelField(title="ica.force_update",align=2,sort=15) 
	private String forceUpdate;
	public RfaService() {
		super();
	}

	public RfaService(String id){
		super(id);
	}
	@Length(min=0, max=50, message="服务编码长度必须介于 0 和 50 之间")
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
	
	public String getServiceParams() {
		return serviceParams;
	}

	public void setServiceParams(String serviceParams) {
		this.serviceParams = serviceParams;
	}
	
	public String getResponeTemplate() {
		return responeTemplate;
	}

	public void setResponeTemplate(String responeTemplate) {
		this.responeTemplate = responeTemplate;
	}
	
	public String getParamsResolve() {
		return paramsResolve;
	}

	public void setParamsResolve(String paramsResolve) {
		this.paramsResolve = paramsResolve;
	}
	
	public String getResponeExample() {
		return responeExample;
	}

	public void setResponeExample(String responeExample) {
		this.responeExample = responeExample;
	}
	
	@Length(min=0, max=255, message="描述长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=11, message="解析类型长度必须介于 0 和 11 之间")
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@Length(min=0, max=11, message="状态  0:关闭 1:启用长度必须介于 0 和 11 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=255, message="请求地址长度必须介于 0 和 255 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	
	@Length(min=0, max=1024, message="请求头信息长度必须介于 0 和 1024 之间")
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

	public String getCustomVersion() {
		return customVersion;
	}

	public void setCustomVersion(String customVersion) {
		this.customVersion = customVersion;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(String forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
	
}