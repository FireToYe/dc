/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.msg.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhilink.manager.common.utils.I18n;
import com.zhilink.srm.manager.modules.msg.entity.DcMessageSendProperties;
import com.zhilink.srm.manager.modules.msg.service.DcMessageSendPropertiesService;
import com.zhilink.srm.manager.modules.msg.util.DcMailSendUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.manager.modules.msg.entity.DcMessageSendLog;
import com.zhilink.srm.manager.modules.msg.service.DcMessageSendLogService;

/**
 * 信息发送详情Controller
 * @author chrisye
 * @version 2018-02-06
 */
@Controller
@RequestMapping(value = "${adminPath}/msg/dcMessageSendLog")
public class DcMessageSendLogController extends BaseController {

	@Autowired
	private DcMessageSendLogService dcMessageSendLogService;
	@Autowired
	private DcMessageSendPropertiesService dcMessageSendPropertiesService;
	@ModelAttribute
	public DcMessageSendLog get(@RequestParam(required=false) String id) {
		DcMessageSendLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dcMessageSendLogService.get(id);
		}
		if (entity == null){
			entity = new DcMessageSendLog();
		}
		return entity;
	}
	
	@RequiresPermissions("msg:dcMessageSendLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(DcMessageSendLog dcMessageSendLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DcMessageSendLog> page = dcMessageSendLogService.findPage(new Page<DcMessageSendLog>(request, response), dcMessageSendLog); 
		model.addAttribute("page", page);
		DcMessageSendProperties dcMessageSendProperties = new DcMessageSendProperties();
		dcMessageSendProperties.setType("2");
		model.addAttribute("propertiesList",dcMessageSendPropertiesService.findList(dcMessageSendProperties));
		return "modules/msg/dcMessageSendLogList";
	}

	@RequiresPermissions("msg:dcMessageSendLog:view")
	@RequestMapping(value = "form")
	public String form(DcMessageSendLog dcMessageSendLog, Model model) {
		model.addAttribute("dcMessageSendLog", dcMessageSendLog);
		DcMessageSendProperties dcMessageSendProperties = new DcMessageSendProperties();
		dcMessageSendProperties.setType("2");
		model.addAttribute("propertiesList",dcMessageSendPropertiesService.findList(dcMessageSendProperties));
		return "modules/msg/dcMessageSendLogForm";
	}

	@RequiresPermissions("msg:dcMessageSendLog:edit")
	@RequestMapping(value = "save")
	public String save(DcMessageSendLog dcMessageSendLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, dcMessageSendLog)){
			return form(dcMessageSendLog, model);
		}
		dcMessageSendLog.setContent(StringEscapeUtils.unescapeHtml4(dcMessageSendLog.getContent()));
		if(StringUtils.isEmpty(dcMessageSendLog.getStatus())){
			dcMessageSendLog.setStatus("0");
		}
		dcMessageSendLogService.save(dcMessageSendLog);
		addMessage(redirectAttributes, "保存信息发送详情成功");
		return "redirect:"+Global.getAdminPath()+"/msg/dcMessageSendLog/?repage";
	}
	@RequiresPermissions("msg:dcMessageSendLog:edit")
	@RequestMapping(value = "sendSave")
	public String sendSave(DcMessageSendLog dcMessageSendLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, dcMessageSendLog)){
			return form(dcMessageSendLog, model);
		}
		dcMessageSendLog.setContent(StringEscapeUtils.unescapeHtml4(dcMessageSendLog.getContent()));
		String msg = DcMailSendUtil.send(dcMessageSendLog,dcMessageSendPropertiesService.getByCache(dcMessageSendLog.getFromAddress(),dcMessageSendLog.getType()));
		dcMessageSendLogService.save(dcMessageSendLog);
		addMessage(redirectAttributes, msg);
		return "redirect:"+Global.getAdminPath()+"/msg/dcMessageSendLog/form?id="+dcMessageSendLog.getId();
	}
	@RequiresPermissions("msg:dcMessageSendLog:edit")
	@RequestMapping(value = "delete")
	public String delete(DcMessageSendLog dcMessageSendLog, RedirectAttributes redirectAttributes) {
		dcMessageSendLogService.delete(dcMessageSendLog);
		addMessage(redirectAttributes, "删除信息发送详情成功");
		return "redirect:"+Global.getAdminPath()+"/msg/dcMessageSendLog/?repage";
	}

	@RequiresPermissions("msg:dcMessageSendLog:edit")
	@RequestMapping(value = "changeStateSave")
	public String changeState(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String state = (String) request.getParameter("sendMsgStatus");
		dcMessageSendLogService.changeState(state);
//		dcMessageSendLogService.insertMail("786963531@qq.com","1111","2222");
		return "redirect:"+Global.getAdminPath()+"/msg/dcMessageSendLog/changeState/?repage";
	}

	@RequiresPermissions("msg:dcMessageSendLog:edit")
	@RequestMapping(value = "changeState")
	public String changeState(Model model) {
		String state  =dcMessageSendLogService.getSendMsgStatus();
		model.addAttribute("sendMsgStatus", state);
		model.addAttribute("sendMsgStatusStr", state.equals("true")? I18n.i18nMessage("common.open"):I18n.i18nMessage("common.close"));
		return "modules/msg/sendMsgState";
	}
}