/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.job.web;

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
import com.zhilink.srm.manager.modules.job.entity.JobLog;
import com.zhilink.srm.manager.modules.job.service.JobLogService;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;

/**
 * 调度日志Controller
 * @author chrisye
 * @version 2017-11-07
 */
@Controller
@RequestMapping(value = "${adminPath}/job/jobLog")
public class JobLogController extends BaseController {

	@Autowired
	private JobLogService jobLogService;
	
	@ModelAttribute
	public JobLog get(@RequestParam(required=false) String id) {
		JobLog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jobLogService.get(id);
		}
		if (entity == null){
			entity = new JobLog();
		}
		return entity;
	}
	
	@RequiresPermissions("job:jobLog:view")
	@RequestMapping(value = {"list", ""})
	public String list(JobLog jobLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JobLog> page = jobLogService.findPage(new Page<JobLog>(request, response), jobLog); 
		model.addAttribute("page", page);
		return "modules/job/jobLogList";
	}

	@RequiresPermissions("job:jobLog:view")
	@RequestMapping(value = "form")
	public String form(JobLog jobLog, Model model) {
		model.addAttribute("jobLog", jobLog);
		return "modules/job/jobLogForm";
	}

	@RequiresPermissions("job:jobLog:edit")
	@RequestMapping(value = "save")
	public String save(JobLog jobLog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, jobLog)){
			return form(jobLog, model);
		}
		jobLogService.save(jobLog);
		addMessage(redirectAttributes, I18n.i18nMessage("job.save_log_success"));
		return "redirect:"+Global.getAdminPath()+"/job/jobLog/?repage";
	}
	
	@RequiresPermissions("job:jobLog:edit")
	@RequestMapping(value = "delete")
	public String delete(JobLog jobLog, RedirectAttributes redirectAttributes) {
		jobLogService.delete(jobLog);
		addMessage(redirectAttributes, I18n.i18nMessage("job.delete_log_success"));
		return "redirect:"+Global.getAdminPath()+"/job/jobLog/?repage";
	}

}