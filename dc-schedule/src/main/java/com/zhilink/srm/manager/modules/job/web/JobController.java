/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.job.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.manager.modules.job.entity.JobEntity;
import com.zhilink.srm.manager.modules.job.service.JobService;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;

/**
 * 定时任务调度Controller
 * @author chrisye
 * @version 2017-10-11
 */
@Controller
@RequestMapping(value = "${adminPath}/job/job")
public class JobController extends BaseController {

	@Autowired
	private JobService jobService;
	
	@ModelAttribute
	public JobEntity get(@RequestParam(required=false) String id) {
		JobEntity entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = jobService.get(id);
		}
		if (entity == null){
			entity = new JobEntity();
		}
		return entity;
	}
	
	@RequiresPermissions("job:job:view")
	@RequestMapping(value = {"list", ""})
	public String list(JobEntity job, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<JobEntity> page = jobService.findPage(new Page<JobEntity>(request, response), job); 
		model.addAttribute("page", page);
		try {
			model.addAttribute("task",jobService.getAllJob());
			model.addAttribute("executeTask",jobService.getAllExcuteJob());
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "modules/job/jobList";
	}

	@RequiresPermissions("job:job:view")
	@RequestMapping(value = "form")
	public String form(JobEntity job, Model model) {
		model.addAttribute("job", job);
		return "modules/job/jobForm";
	}

	@RequiresPermissions("job:job:edit")
	@RequestMapping(value = "save")
	public String save(JobEntity job, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, job)){
			return form(job, model);
		}
		job.setParameter(StringEscapeUtils.unescapeHtml4(job.getParameter()));
		try {
			if(!StringUtils.isEmpty(job.getId())){
				JobEntity oldEntity = jobService.get(job.getId());
				jobService.deleteJob(oldEntity);
			}
			jobService.addJob(job);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jobService.save(job);
		addMessage(redirectAttributes, I18n.i18nMessage("job.save_job_success"));
		return "redirect:"+Global.getAdminPath()+"/job/job/?repage";
	}
	
	@RequiresPermissions("job:job:edit")
	@RequestMapping(value = "delete")
	public String delete(JobEntity jobEntity, RedirectAttributes redirectAttributes) {
		jobService.delete(jobEntity);
		try {
			jobService.deleteJob(jobEntity);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addMessage(redirectAttributes, I18n.i18nMessage("job.delete_job_success"));
		return "redirect:"+Global.getAdminPath()+"/job/job/?repage";
	}
	
	@RequestMapping(value="pauseJob")
	public String pauseJob(JobEntity job, RedirectAttributes redirectAttributes){
		try {
			jobService.pauseJob(job);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addMessage(redirectAttributes, I18n.i18nMessage("job.pause_job_success"));
		return "redirect:"+Global.getAdminPath()+"/job/job/?repage";
	}
	
	@RequestMapping(value="resumeJob")
	public String resumeJob(JobEntity job, RedirectAttributes redirectAttributes){
		try {
			jobService.resumeJob(job);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addMessage(redirectAttributes, I18n.i18nMessage("job.resume_job_success"));
		return "redirect:"+Global.getAdminPath()+"/job/job/?repage";
	}

	@RequestMapping(value="changeJobState")
	public String changeJobState(JobEntity jobEntity,String jobState,RedirectAttributes redirectAttributes){
		jobEntity.setJobState(jobState);
		jobService.updateState(jobEntity);
		if("0".equals(jobState)){
			try {
				jobService.deleteJob(jobEntity);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addMessage(redirectAttributes, I18n.i18nMessage("job.close_job_success"));
		}else{
			try {
				jobService.addJob(jobEntity);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			addMessage(redirectAttributes, I18n.i18nMessage("job.open_job_success"));
		}
		return "redirect:"+Global.getAdminPath()+"/job/job/?repage";
	}
	
	@RequiresPermissions("job:job:view")
	@RequestMapping(value ="cronSelect")
	public String cronSelect( HttpServletRequest request, HttpServletResponse response, Model model) {
		
		return "modules/job/cronSelect";
	}
}