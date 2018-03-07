package com.zhilink.srm.manager.modules.msg.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.zhilink.manager.common.utils.Exceptions;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.manager.modules.msg.entity.DcMessageSendLog;
import com.zhilink.srm.manager.modules.msg.entity.DcMessageSendProperties;

public class DcMailSendUtil {
	private static Map<String, String> hostMap = new HashMap<String, String>();
	private static final String charSet = "utf-8";
	static {
		// 126
		hostMap.put("smtp.126", "smtp.126.com");
		// qq
		hostMap.put("smtp.qq", "smtp.qq.com");

		// 163
		hostMap.put("smtp.163", "smtp.163.com");

		// sina
		hostMap.put("smtp.sina", "smtp.sina.com.cn");

		// tom
		hostMap.put("smtp.tom", "smtp.tom.com");

		// 263
		hostMap.put("smtp.263", "smtp.263.net");

		// yahoo
		hostMap.put("smtp.yahoo", "smtp.mail.yahoo.com");

		// hotmail
		hostMap.put("smtp.hotmail", "smtp.live.com");

		// gmail
		hostMap.put("smtp.gmail", "smtp.gmail.com");
		hostMap.put("smtp.port.gmail", "465");
	}
	public static String sendMail(DcMessageSendLog dcMessageSendLog,DcMessageSendProperties dcMessageSendProperties){
		if(!"2".equals(dcMessageSendLog.getType())){
			return "发送失败：该配置非邮件发送";
		}
		if(dcMessageSendProperties==null){
			dcMessageSendLog.setStatus("2");
			dcMessageSendLog.setException("发送配置不存在");
			return "发送失败，失败原因：发送配置不存在！";
		}
		HtmlEmail email = new HtmlEmail();
		try{
			String smtp = dcMessageSendProperties.getSmtp();
			
			if(StringUtils.isEmpty(smtp)){
				smtp = getHost(dcMessageSendProperties.getEmail());
			}
			email.setCharset(charSet);
			email.setHostName(smtp);
			email.setSmtpPort(getSmtpPort(dcMessageSendProperties.getEmail()));
			email.setFrom(dcMessageSendProperties.getEmail(), dcMessageSendProperties.getName());  
			email.setAuthentication(dcMessageSendProperties.getAccountNumber(), dcMessageSendProperties.getPassword());
			setTo(email,dcMessageSendLog.getToAddress());
			setCC(email, dcMessageSendLog.getCcAddress());
			email.setSubject(dcMessageSendLog.getSubject());
			email.setMsg(dcMessageSendLog.getContent());
			dcMessageSendLog.setSendDate(new Date());
			dcMessageSendLog.setFromAddress(dcMessageSendProperties.getId());
			email.send();
			dcMessageSendLog.setStatus("1");
			dcMessageSendLog.setException("");
			return "发送成功";
		}catch(Exception e){
			e.printStackTrace();
			dcMessageSendLog.setStatus("2");
			dcMessageSendLog.setException(Exceptions.getStackTraceAsString(e));
			return "发送失败，失败原因："+e.getMessage();
		}
	}
	
	public static String getHost(String email) throws Exception {
		Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
		Matcher matcher = pattern.matcher(email);
		String key = "unSupportEmail";
		if (matcher.find()) {
			key = "smtp." + matcher.group(1);
		}
		if (hostMap.containsKey(key)) {
			return hostMap.get(key);
		} else {
			throw new Exception("unSupportEmail");
		}
	}
	public static int getSmtpPort(String email) throws Exception {
		Pattern pattern = Pattern.compile("\\w+@(\\w+)(\\.\\w+){1,2}");
		Matcher matcher = pattern.matcher(email);
		String key = "unSupportEmail";
		if (matcher.find()) {
			key = "smtp.port." + matcher.group(1);
		}
		if (hostMap.containsKey(key)) {
			return Integer.parseInt(hostMap.get(key));
		} else {
			return 25;
		}
	}
	
	/**
	 * 设置收件人
	 * @param email
	 * @param toMail
	 * @throws Exception
	 */
	 private static void setTo(HtmlEmail email, String toMail) throws Exception{
		 if(StringUtils.isEmpty(toMail)){
			 throw new Exception("收件人邮箱不能为空");
		 }
		 if(toMail.indexOf(";")>-1){
			 String[] toArray = toMail.split(";");
			 for(String mail:toArray){
				 email.addTo(mail);
			 }
		 }else{
			 email.addTo(toMail);
		 }
	 }
	 /**
	  * 设置抄送人
	  * @param email
	  * @param CCMail
	  * @throws Exception
	  */
	 private static void setCC(HtmlEmail email, String CCMail) throws Exception{
		 if(StringUtils.isEmpty(CCMail)){
			 return;
		 }
		 if(CCMail.indexOf(";")>-1){
			 String[] toArray = CCMail.split(";");
			 for(String mail:toArray){
				 email.addCc(mail);
			 }
		 }else{
			 email.addCc(CCMail);
		 }
	 }
	 
	 public static String testSendMail(DcMessageSendProperties dcMessageSendProperties,String tomail,String subject,String message){
		 HtmlEmail hemail = new HtmlEmail();
			try {
				String from = dcMessageSendProperties.getEmail();
				String password = dcMessageSendProperties.getPassword();
				String fromName = dcMessageSendProperties.getName();
				hemail.setHostName(getHost(from));
				hemail.setSmtpPort(getSmtpPort(from));
				hemail.setCharset(charSet);
				hemail.addTo(tomail);
				hemail.setFrom(from, fromName);
				hemail.setAuthentication(dcMessageSendProperties.getAccountNumber(), password);
				hemail.setSubject(subject);
				hemail.setMsg(message);
				hemail.send();
				return "发送成功";
			} catch (Exception e) {
				e.printStackTrace();
				return "发送失败，失败原因："+e.getMessage();
			}
	 }

	public static String testSend(DcMessageSendProperties dcMessageSendProperties,String tomail,String subject,String message){
	 	if("2".equals(dcMessageSendProperties.getType())){
			return testSendMail(dcMessageSendProperties,tomail,subject,message);
		}
		return "暂不支持其它发送";
	}
	public static String send(DcMessageSendLog dcMessageSendLog,DcMessageSendProperties dcMessageSendProperties){
		if("2".equals(dcMessageSendProperties.getType())){
			return sendMail(dcMessageSendLog,dcMessageSendProperties);
		}
		return "暂不支持其它发送";
	}
}
