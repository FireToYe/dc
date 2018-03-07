/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.rfa.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.utils.excel.ExportExcel;
import com.zhilink.srm.common.utils.excel.ImportExcel;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.beanvalidator.BeanValidators;
import com.zhilink.manager.framework.common.utils.DateUtils;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaDb;
import com.zhilink.srm.manager.modules.ica.utils.IcaSpringContextHolder;
import com.zhilink.srm.manager.modules.rfa.entity.RfaUrl;
import com.zhilink.srm.manager.modules.rfa.service.RfaUrlService;

/**
 * rfa连接管理Controller
 * @author chrisye
 * @version 2017-11-16
 */
@Controller
@RequestMapping(value = "${adminPath}/rfa/rfaUrl")
public class RfaUrlController extends BaseController {

	@Autowired
	private RfaUrlService rfaUrlService;
	
	@ModelAttribute
	public RfaUrl get(@RequestParam(required=false) String id) {
		RfaUrl entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = rfaUrlService.get(id);
		}
		if (entity == null){
			entity = new RfaUrl();
		}
		return entity;
	}
	
	@RequiresPermissions("rfa:rfaUrl:view")
	@RequestMapping(value = {"list", ""})
	public String list(RfaUrl rfaUrl, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RfaUrl> page = rfaUrlService.findPage(new Page<RfaUrl>(request, response), rfaUrl); 
		model.addAttribute("page", page);
		return "modules/rfa/rfaUrlList";
	}

	@RequiresPermissions("rfa:rfaUrl:view")
	@RequestMapping(value = "form")
	public String form(RfaUrl rfaUrl, Model model) {
		model.addAttribute("rfaUrl", rfaUrl);
		return "modules/rfa/rfaUrlForm";
	}

	@RequiresPermissions("rfa:rfaUrl:edit")
	@RequestMapping(value = "save")
	public String save(RfaUrl rfaUrl, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rfaUrl)){
			return form(rfaUrl, model);
		}
		rfaUrlService.save(rfaUrl);
		addMessage(redirectAttributes, I18n.i18nMessage("rfa.save_url_success"));
		return "redirect:"+Global.getAdminPath()+"/rfa/rfaUrl/?repage";
	}
	
	@RequiresPermissions("rfa:rfaUrl:edit")
	@RequestMapping(value = "delete")
	public String delete(RfaUrl rfaUrl, RedirectAttributes redirectAttributes) {
		rfaUrlService.delete(rfaUrl);
		addMessage(redirectAttributes, I18n.i18nMessage("rfa.delete_url_success"));
		return "redirect:"+Global.getAdminPath()+"/rfa/rfaUrl/?repage";
	}

	/**
	 * 验证登录名是否有效
	 * 
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("rfa:rfaUrl:edit")
	@RequestMapping(value = "checkDbId")
	public String checkDbId(String oldurlId, String urlId) {
		if (urlId != null && urlId.equals(oldurlId)) {
			return "true";
		} else if (urlId != null && rfaUrlService.checkOnly(urlId) == null) {
			return "true";
		}
		return "false";
	}
	@RequiresPermissions("rfa:rfaUrl:view")
	@RequestMapping(value ="export")
	public String export(RfaUrl rfaUrl, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			String fileName =I18n.i18nMessage("rfa.url_data")+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";		
			List<RfaUrl> result = rfaUrlService.findList(rfaUrl);
			new ExportExcel(I18n.i18nMessage("rfa.url_data"), RfaUrl.class,true).setDataList(result).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, I18n.i18nMessage("rfa.url_export_error_controller_msg")+"： " + e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rfa/rfaUrl/?repage";
	}
	/**
	 * 下载导入用户数据模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("rfa:rfaUrl:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = I18n.i18nMessage("rfa.url_export_template_name");
			List<RfaUrl> list = Lists.newArrayList();
			List<RfaUrl> result = rfaUrlService.findList(new RfaUrl());
			if(result!=null&&result.size()>0){
				list.add(result.get(0));
			}
			new ExportExcel(I18n.i18nMessage("rfa.url_data"), RfaUrl.class, 2,true).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, I18n.i18nMessage("ica.import_error_template_msg")+"： " + e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rfa/rfaUrl/?repage";
	}
	/**
	 * 导入用户数据
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("rfa:rfaUrl:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/rfa/rfaUrl/?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<RfaUrl> list = ei.getDataList(RfaUrl.class);
			for (RfaUrl rfaUrl : list) {
				try {
					if ("true".equals(checkDbId("", rfaUrl.getUrlId()))) {
						BeanValidators.validateWithException(validator, rfaUrl);
						rfaUrlService.save(rfaUrl);
						successNum++;
					} else {
						failureMsg.append("<br/>"+I18n.i18nMessage("rfa.rfa_url_id")+" " + rfaUrl.getUrlId() + " "+I18n.i18nMessage("ica.import_exist")+"; ");
						failureNum++;
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("rfa.rfa_url_id")+" " + rfaUrl.getUrlId() + I18n.i18nMessage("ica.import_failure")+"： ");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("rfa.rfa_url_id")+" " + rfaUrl.getUrlId() + " "+I18n.i18nMessage("ica.import_failure")+"： " + ex.getMessage());
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "， "+I18n.i18nMessage("ica.failure")+" " + failureNum + " "+I18n.i18nMessage("rfa.service_import_number_error_msg")+"： ");
			}
			if(successNum>0){
				IcaSpringContextHolder.initDataSource();
			}
			addMessage(redirectAttributes, I18n.i18nMessage("ica.impory_success_al")+" " + successNum + " "+I18n.i18nMessage("rfa.url_import_number_error_msg") + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, I18n.i18nMessage("rfa.service_import_error_controller_msg") +"： "+ e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rfa/rfaUrl/?repage";
	}
}