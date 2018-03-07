/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.msg.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import com.zhilink.srm.common.persistence.DataEntity;

import java.util.Date;

/**
 * 信息发送详情Entity
 * @author chrisye
 * @version 2018-02-06
 */
public class DcMessageSendLog extends DataEntity<DcMessageSendLog> {
	
	private static final long serialVersionUID = 1L;
	private String fromAddress;		// 发送方地址
	private String toAddress;		// 接收方地址
	private String content;		// 发送内容
	private String type;		// 类别:1：短信 2：邮件
	private String subject;		// 发送主题
	private String ccAddress;		// 抄送人地址
	private String status;		// 发送状态：0 待发送 1 发送成功 2 发送失败
	private String exception;		// 失败原因
	private Date sendDate;
	private String toMailName;
	public DcMessageSendLog() {
		super();
	}

	public DcMessageSendLog(String id){
		super(id);
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	
	@Length(min=1, max=2000, message="接收方地址长度必须介于 1 和 2000 之间")
	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=11, message="类别:1：短信 2：邮件长度必须介于 0 和 11 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=64, message="发送主题长度必须介于 0 和 64 之间")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@Length(min=0, max=2000, message="抄送人地址长度必须介于 0 和 2000 之间")
	public String getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}
	
	@Length(min=0, max=11, message="发送状态：0 待发送 1 发送成功 2 发送失败长度必须介于 0 和 11 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	public Date getSendDate() {
		return sendDate;
	}

	public String getToMailName() {
		return toMailName;
	}

	public void setToMailName(String toMailName) {
		this.toMailName = toMailName;
	}
}