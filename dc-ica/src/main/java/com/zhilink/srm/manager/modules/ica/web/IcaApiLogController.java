/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.ica.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.manager.modules.ica.entity.IcaApiLog;
import com.zhilink.srm.manager.modules.ica.service.IcaApiLogService;
import com.zhilink.srm.manager.modules.ica.thread.LogInsertThread;
import com.zhilink.srm.manager.modules.ica.thread.LogThreadState;
import com.zhilink.srm.manager.modules.ica.thread.RfaLogInsertThread;

/**
 * ica请求日志Controller
 * @author chrisye
 * @version 2017-09-15
 */
@Controller
@RequestMapping(value = "${adminPath}/ica/icaApiLog")
public class IcaApiLogController extends BaseController{
	
	@Value("${ica.log.state}")
	protected String logState;

	
	@Autowired
	private LogInsertThread logInsertThread;
	
	@Autowired
	private IcaApiLogService icaApiLogService;
	
	@Autowired
	private RfaLogInsertThread rfaLogInsertThread;
	
	
	@ModelAttribute
	public IcaApiLog get(@RequestParam(required=false) String id) {
		IcaApiLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = icaApiLogService.get(id);
		}
		if (entity == null){
			entity = new IcaApiLog();
		}
		return entity;
	}
	
	@RequiresPermissions("ica:icaApiLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(IcaApiLog icaApiLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<IcaApiLog> page = icaApiLogService.findPage(new Page<IcaApiLog>(request, response), icaApiLog); 
		model.addAttribute("page", page);
		return "modules/ica/icaApiLogList";
	}

	@RequiresPermissions("ica:icaApiLog:view")
	@RequestMapping(value = "form")
	public String form(IcaApiLog icaApiLog, Model model) {
		model.addAttribute("icaApiLog", icaApiLog);
		return "modules/ica/icaApiLogForm";
	}

	@RequestMapping(value = "save")
	public String save(IcaApiLog icaApiLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, icaApiLog)){
			return form(icaApiLog, model);
		}
		icaApiLogService.save(icaApiLog);
		addMessage(redirectAttributes, I18n.i18nMessage("ica.save_log_success"));
		return "redirect:"+Global.getAdminPath()+"/ica/icaApiLog/?repage";
	}
	
	@RequiresPermissions("ica:icaApiLog:edit")
	@RequestMapping(value = "delete")
	public String delete(IcaApiLog icaApiLog, RedirectAttributes redirectAttributes) {
		icaApiLogService.delete(icaApiLog);
		addMessage(redirectAttributes, I18n.i18nMessage("ica.delete_log_success"));
		return "redirect:"+Global.getAdminPath()+"/ica/icaApiLog/?repage";
	}

	@RequiresPermissions("ica:icaApiLog:edit")
	@RequestMapping(value = "changeStateSave")
	public String changeState(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String state = (String) request.getParameter("logState");
		if(logState.equals(state)){
			if("true".equals(state)){
				addMessage(redirectAttributes, I18n.i18nMessage("ica.log_start_success"));
			}else{
				addMessage(redirectAttributes, I18n.i18nMessage("ica.log_close_success"));
			}
			return "redirect:"+Global.getAdminPath()+"/ica/icaApiLog/changeState/?repage";
		}
		if("true".equals(state)){
			logInsertThread.startThread();
			rfaLogInsertThread.startThread();
			this.logState=state;
			addMessage(redirectAttributes, I18n.i18nMessage("ica.log_start_success"));
		}else if("false".equals(state)){
			logInsertThread.waitThread();
			rfaLogInsertThread.waitThread();
			this.logState=state;
			addMessage(redirectAttributes, I18n.i18nMessage("ica.log_close_success"));
		}
		return "redirect:"+Global.getAdminPath()+"/ica/icaApiLog/changeState/?repage";
	}
	
	@RequiresPermissions("ica:icaApiLog:edit")
	@RequestMapping(value = "changeState")
	public String changeState(Model model) {
		model.addAttribute("logState", logState);
		model.addAttribute("logStateStr", logState.equals("true")?I18n.i18nMessage("common.open"):I18n.i18nMessage("common.close"));
		return "modules/ica/icaApiLogState";
	}
}