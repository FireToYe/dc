/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.rfa.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.zhilink.srm.common.persistence.DataEntity;
import com.zhilink.srm.common.utils.excel.annotation.ExcelField;

/**
 * rfa连接管理Entity
 * @author chrisye
 * @version 2017-11-16
 */
public class RfaUrl extends DataEntity<RfaUrl> {
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="rfa.rfa_url_id",align=2,sort=1)
	private String urlId;		// url链接id标识
	@ExcelField(title="rfa.url",align=2,sort=2)
	private String url;		// 地址
	@ExcelField(title="ica.description",align=2,sort=3)
	private String description;		// 描述
	private Date createTime;		// 创建时间
	private Date updateTime;		// 更新时间
	
	public RfaUrl() {
		super();
	}

	public RfaUrl(String id){
		super(id);
	}

	@Length(min=0, max=255, message="url链接id标识长度必须介于 0 和 255 之间")
	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}
	
	@Length(min=0, max=255, message="地址长度必须介于 0 和 255 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Length(min=0, max=255, message="描述长度必须介于 0 和 255 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}