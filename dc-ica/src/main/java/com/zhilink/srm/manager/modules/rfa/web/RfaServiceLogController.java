/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.rfa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.manager.modules.rfa.entity.RfaServiceLog;
import com.zhilink.srm.manager.modules.rfa.service.RfaServiceLogService;

/**
 * 接口适配器调用日志Controller
 * @author chrisye
 * @version 2017-11-17
 */
@Controller
@RequestMapping(value = "${adminPath}/rfa/rfaServiceLog")
public class RfaServiceLogController extends BaseController {

	@Autowired
	private RfaServiceLogService rfaServiceLogService;
	
	@ModelAttribute
	public RfaServiceLog get(@RequestParam(required=false) String id) {
		RfaServiceLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = rfaServiceLogService.get(id);
		}
		if (entity == null){
			entity = new RfaServiceLog();
		}
		return entity;
	}
	
	@RequiresPermissions("rfa:rfaServiceLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(RfaServiceLog rfaServiceLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RfaServiceLog> page = rfaServiceLogService.findPage(new Page<RfaServiceLog>(request, response), rfaServiceLog); 
		model.addAttribute("page", page);
		return "modules/rfa/rfaServiceLogList";
	}

	@RequiresPermissions("rfa:rfaServiceLog:view")
	@RequestMapping(value = "form")
	public String form(RfaServiceLog rfaServiceLog, Model model) {
		model.addAttribute("rfaServiceLog", rfaServiceLog);
		return "modules/rfa/rfaServiceLogForm";
	}

	@RequiresPermissions("rfa:rfaServiceLog:edit")
	@RequestMapping(value = "save")
	public String save(RfaServiceLog rfaServiceLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rfaServiceLog)){
			return form(rfaServiceLog, model);
		}
		rfaServiceLogService.save(rfaServiceLog);
		addMessage(redirectAttributes, I18n.i18nMessage("rfa.save_log_success"));
		return "redirect:"+Global.getAdminPath()+"/rfa/rfaServiceLog/?repage";
	}
	
	@RequiresPermissions("rfa:rfaServiceLog:edit")
	@RequestMapping(value = "delete")
	public String delete(RfaServiceLog rfaServiceLog, RedirectAttributes redirectAttributes) {
		rfaServiceLogService.delete(rfaServiceLog);
		addMessage(redirectAttributes, I18n.i18nMessage("rfa.delete_log_success"));
		return "redirect:"+Global.getAdminPath()+"/rfa/rfaServiceLog/?repage";
	}

}