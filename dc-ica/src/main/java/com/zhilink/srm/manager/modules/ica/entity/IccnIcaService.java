/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.ica.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhilink.srm.common.persistence.DataEntity;
import com.zhilink.srm.common.utils.excel.annotation.ExcelField;


/**
 * ica系统服务管理Entity
 * @author chrisyeye
 * @version 2017-09-11
 */
public class IccnIcaService extends DataEntity<IccnIcaService> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="ica.db_id",align=2,sort=1)
	private String dbId;		// 数据库链接id
	@ExcelField(title="ica.service_code",align=2,sort=2)
	private String serviceCode;		// 服务编码
	@ExcelField(title="ica.service_name",align=2,sort=3)
	private String serviceName;		// 服务名称
	@ExcelField(title="ica.service_params",align=2,sort=4)
	private String serviceParams;		// 服务参数
	@ExcelField(title="ica.db_sql",align=2,sort=5)
	private String dbSql;		// 数据库sql
	@ExcelField(title="ica.respone_template",align=2,sort=6)
	private String responeTemplate;		// 返回模板
	@ExcelField(title="ica.description",align=2,sort=7)
	private String description;		// 描述
	@ExcelField(title="ica.service_group",align=2,sort=9)
	private String serviceGroup;
	@ExcelField(title="ica.status",align=2,sort=8,dictType="iccn_ica_status")
	private String status;		// 状态  0:关闭 1:启用
	@ExcelField(title="ica.erp_type",align=2,sort=10)
	private String erpType;		// erp类型
	private Date createTime;		// 创建时间
	private Date updateTime;		// 更新时间
	@ExcelField(title="ica.custom_version",align=2,sort=11)
	private String customVersion;
	@ExcelField(title="ica.system_version",align=2,sort=12)
	private String systemVersion;
	@ExcelField(title="ica.force_update",align=2,sort=13) 
	private String forceUpdate;
	
	public IccnIcaService() {
		super();
	}

	public IccnIcaService(String id){
		super(id);
	}

	@Length(min=0, max=255, message="数据库链接id长度必须介于 0 和 255 之间")
	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
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
	
	@Length(min=0, max=255, message="描述长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=11, message="状态  0:关闭 1:启用长度必须介于 0 和 11 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getErpType() {
		return erpType;
	}

	public void setErpType(String erpType) {
		this.erpType = erpType;
	}

	public String getServiceGroup() {
		return serviceGroup;
	}

	public void setServiceGroup(String serviceGroup) {
		this.serviceGroup = serviceGroup;
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