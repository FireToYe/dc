/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.ica.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhilink.manager.framework.common.utils.MD5Tools;
import com.zhilink.srm.common.persistence.DataEntity;
import com.zhilink.srm.common.utils.excel.annotation.ExcelField;


/**
 * 集成管理Entity
 * @author chrisye
 * @version 2017-09-08
 */
public class IccnIcaDb extends DataEntity<IccnIcaDb> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="ica.db_id",align=2,sort=1)
	private String dbId;		// 数据库链接id标识
	@ExcelField(title="ica.db_name",align=2,sort=2)
	private String dbName;		// 数据库链接名称
	@ExcelField(title="ica.db_type",align=2,sort=3)
	private String dbType;		// 数据库类型
	@ExcelField(title="ica.db_version",align=2,sort=4)
	private String dbVersion;		// 数据库版本
	@ExcelField(title="ica.jdbc_driverclassname",align=2,sort=5)
	private String jdbcDriverclassname;		// 数据库驱动
	@ExcelField(title="ica.jdbc_url",align=2,sort=6)
	private String jdbcUrl;		// 数据库链接url
	@ExcelField(title="ica.jdbc_username",align=2,sort=7)
	private String jdbcUsername;		// 数据库用户名
	@ExcelField(title="ica.jdbc_password",align=2,sort=8)
	private String jdbcPassword;		// 数据库密码
	@ExcelField(title="ica.description",align=2,sort=9)
	private String description;		// 描述
	@ExcelField(title="ica.status",align=2,sort=10,dictType="iccn_ica_status")
	private String status;		// 状态  0:关闭 1:启用
	private Date createTime;		// 创建时间
	private Date updateTime;		// 更新时间
	
	public IccnIcaDb() {
		super();
	}

	public IccnIcaDb(String id){
		super(id);
	}

	@Length(min=0, max=255, message="数据库链接id标识长度必须介于 0 和 255 之间")
	public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}
	
	@Length(min=0, max=255, message="数据库链接名称长度必须介于 0 和 255 之间")
	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	@Length(min=0, max=255, message="数据库类型长度必须介于 0 和 255 之间")
	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	@Length(min=0, max=255, message="数据库版本长度必须介于 0 和 255 之间")
	public String getDbVersion() {
		return dbVersion;
	}

	public void setDbVersion(String dbVersion) {
		this.dbVersion = dbVersion;
	}
	
	@Length(min=0, max=255, message="数据库驱动长度必须介于 0 和 255 之间")
	public String getJdbcDriverclassname() {
		return jdbcDriverclassname;
	}

	public void setJdbcDriverclassname(String jdbcDriverclassname) {
		this.jdbcDriverclassname = jdbcDriverclassname;
	}
	
	@Length(min=0, max=255, message="数据库链接url长度必须介于 0 和 255 之间")
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	
	@Length(min=0, max=255, message="数据库用户名长度必须介于 0 和 255 之间")
	public String getJdbcUsername() {
		return jdbcUsername;
	}

	public void setJdbcUsername(String jdbcUsername) {
		this.jdbcUsername = jdbcUsername;
	}
	
	@Length(min=0, max=255, message="数据库密码长度必须介于 0 和 255 之间")
	public String getJdbcPassword() {
		return jdbcPassword;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
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
	
	public String getMd5BeanName() {
		return MD5Tools.MD5(jdbcDriverclassname+":"+jdbcUrl+":"+jdbcUsername+":"+jdbcPassword);
	}
}