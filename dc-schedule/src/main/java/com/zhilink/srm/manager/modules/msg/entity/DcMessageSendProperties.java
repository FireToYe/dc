/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.msg.entity;

import org.hibernate.validator.constraints.Length;

import com.zhilink.srm.common.persistence.DataEntity;

/**
 * 信息发送配置Entity
 * @author chrisye
 * @version 2018-02-06
 */
public class DcMessageSendProperties extends DataEntity<DcMessageSendProperties> {
	
	private static final long serialVersionUID = 1L;
	private String accountNumber;		// 账号
	private String password;		// 密码
	private String isDefault;		// 是否默认
	private String type;		// 类别:1：短信 2：邮件
	private String smtp;		// smtp地址
	private String smsUrl;		// 短信请求接口地址
	private String name;
	private String email;
	private String toEmailTest;
	public DcMessageSendProperties() {
		super();
	}

	public DcMessageSendProperties(String id){
		super(id);
	}

	@Length(min=1, max=32, message="账号长度必须介于 1 和 32 之间")
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	@Length(min=1, max=64, message="密码长度必须介于 1 和 64 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Length(min=0, max=11, message="是否默认长度必须介于 0 和 11 之间")
	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
	@Length(min=0, max=11, message="类别:1：短信 2：邮件长度必须介于 0 和 11 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=64, message="smtp地址长度必须介于 0 和 64 之间")
	public String getSmtp() {
		return smtp;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}
	
	@Length(min=0, max=64, message="短信请求接口地址长度必须介于 0 和 64 之间")
	public String getSmsUrl() {
		return smsUrl;
	}

	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToEmailTest() {
		return toEmailTest;
	}

	public void setToEmailTest(String toEmailTest) {
		this.toEmailTest = toEmailTest;
	}
}