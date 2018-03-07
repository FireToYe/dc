/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.msg.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.api.basemodel.ResultModel;
import com.zhilink.srm.manager.modules.msg.util.DcMailSendUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.manager.modules.msg.entity.DcMessageSendProperties;
import com.zhilink.srm.manager.modules.msg.service.DcMessageSendPropertiesService;

import java.util.HashMap;
import java.util.Map;

/**
 * 信息发送配置Controller
 * @author chrisye
 * @version 2018-02-06
 */
@Controller
@RequestMapping(value = "${adminPath}/msg/dcMessageSendProperties")
public class DcMessageSendPropertiesController extends BaseController {

	@Autowired
	private DcMessageSendPropertiesService dcMessageSendPropertiesService;
	
	@ModelAttribute
	public DcMessageSendProperties get(@RequestParam(required=false) String id) {
		DcMessageSendProperties entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dcMessageSendPropertiesService.get(id);
		}
		if (entity == null){
			entity = new DcMessageSendProperties();
		}
		return entity;
	}
	
	@RequiresPermissions("msg:dcMessageSendProperties:view")
	@RequestMapping(value = {"list", ""})
	public String list(DcMessageSendProperties dcMessageSendProperties, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DcMessageSendProperties> page = dcMessageSendPropertiesService.findPage(new Page<DcMessageSendProperties>(request, response), dcMessageSendProperties); 
		model.addAttribute("page", page);
		return "modules/msg/dcMessageSendPropertiesList";
	}

	@RequiresPermissions("msg:dcMessageSendProperties:view")
	@RequestMapping(value = "form")
	public String form(DcMessageSendProperties dcMessageSendProperties, Model model) {
		model.addAttribute("dcMessageSendProperties", dcMessageSendProperties);
		return "modules/msg/dcMessageSendPropertiesForm";
	}

	@RequiresPermissions("msg:dcMessageSendProperties:edit")
	@RequestMapping(value = "save")
	public String save(DcMessageSendProperties dcMessageSendProperties, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, dcMessageSendProperties)){
			return form(dcMessageSendProperties, model);
		}
		if("2".equals(dcMessageSendProperties.getType())&&StringUtils.isEmpty(dcMessageSendProperties.getEmail())){
			dcMessageSendProperties.setEmail(dcMessageSendProperties.getAccountNumber());
		}
		dcMessageSendPropertiesService.save(dcMessageSendProperties);
		addMessage(redirectAttributes, "保存信息发送配置成功");
		return "redirect:"+Global.getAdminPath()+"/msg/dcMessageSendProperties/?repage";
	}
	
	@RequiresPermissions("msg:dcMessageSendProperties:edit")
	@RequestMapping(value = "delete")
	public String delete(DcMessageSendProperties dcMessageSendProperties, RedirectAttributes redirectAttributes) {
		dcMessageSendPropertiesService.delete(dcMessageSendProperties);
		addMessage(redirectAttributes, "删除信息发送配置成功");
		return "redirect:"+Global.getAdminPath()+"/msg/dcMessageSendProperties/?repage";
	}
	@RequiresPermissions("msg:dcMessageSendProperties:edit")
	@RequestMapping(value = "/testSend")
	@ResponseBody
	public ResultModel testSend(@RequestBody DcMessageSendProperties dcMessageSendProperties) {
		ResultModel result = new ResultModel();
		String msg = DcMailSendUtil.testSend(dcMessageSendProperties,dcMessageSendProperties.getToEmailTest(),"测试","测试消息");
		Map<String,String> map = new HashMap<String, String>();
		map.put("message",msg);
		result.setBody(map);
		return result;
	}

}