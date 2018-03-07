/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.rfa.web;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.utils.excel.ExportExcel;
import com.zhilink.srm.common.utils.excel.ImportExcel;
import com.zhilink.manager.common.config.PropertiesHolder;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.beanvalidator.BeanValidators;
import com.zhilink.manager.framework.common.utils.DateUtils;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaService;
import com.zhilink.srm.manager.modules.ica.service.IccnIcaServiceService;
import com.zhilink.srm.manager.modules.ica.utils.IcaSpringContextHolder;
import com.zhilink.srm.manager.modules.rfa.entity.RfaService;
import com.zhilink.srm.manager.modules.rfa.entity.RfaUrl;
import com.zhilink.srm.manager.modules.rfa.service.RfaServiceService;
import com.zhilink.srm.manager.modules.sys.service.SysPropertiesService;

/**
 * rfa系统服务管理Controller
 * @author chrisye
 * @version 2017-11-14
 */
@Controller
@RequestMapping(value = "${adminPath}/rfa/rfaService")
public class RfaServiceController extends BaseController implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
	private RfaServiceService rfaServiceService;
	
	@Autowired
	private IccnIcaServiceService iccnIcaServiceService;
	@Value("${ica.erpType}")
	private String erpType;
	private static final String RFA_NAME = "rfa";
	
	@Autowired
	private SysPropertiesService sysPropertiesService;
	
	
	private static final String RFA_VERSION_KEY = "iccn-rfa-version";
	@ModelAttribute
	public RfaService get(@RequestParam(required=false) String id) {
		RfaService entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = rfaServiceService.get(id);
		}
		if (entity == null){
			entity = new RfaService();
		}
		return entity;
	}
	
	@RequiresPermissions("rfa:rfaService:view")
	@RequestMapping(value = {"list", ""})
	public String list(RfaService rfaService, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RfaService> page = rfaServiceService.findPage(new Page<RfaService>(request, response), rfaService); 
		model.addAttribute("page", page);
		return "modules/rfa/rfaServiceList";
	}

	@RequiresPermissions("rfa:rfaService:view")
	@RequestMapping(value = "form")
	public String form(RfaService rfaService, Model model) {
		model.addAttribute("rfaService", rfaService);
		return "modules/rfa/rfaServiceForm";
	}

	@RequiresPermissions("rfa:rfaService:edit")
	@RequestMapping(value = "save")
	public String save(RfaService rfaService, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rfaService)){
			return form(rfaService, model);
		}
		rfaService.setHeaders(StringEscapeUtils.unescapeHtml4(rfaService.getHeaders()));
		rfaService.setServiceParams(StringEscapeUtils.unescapeHtml4(rfaService.getServiceParams()));
		rfaService.setParamsResolve(StringEscapeUtils.unescapeHtml4(rfaService.getParamsResolve()));
		rfaService.setResponeTemplate(StringEscapeUtils.unescapeHtml4(rfaService.getResponeTemplate()));
		rfaService.setResponeExample(StringEscapeUtils.unescapeHtml4(rfaService.getResponeExample()));
		rfaServiceService.save(rfaService);
		addMessage(redirectAttributes, I18n.i18nMessage("rfa.save_service_success"));
		return "redirect:"+Global.getAdminPath()+"/rfa/rfaService/form?id="+rfaService.getId();
	}
	
	@RequiresPermissions("rfa:rfaService:edit")
	@RequestMapping(value = "delete")
	public String delete(RfaService rfaService, RedirectAttributes redirectAttributes) {
		rfaServiceService.delete(rfaService);
		addMessage(redirectAttributes, I18n.i18nMessage("rfa.delete_service_success"));
		return "redirect:"+Global.getAdminPath()+"/rfa/rfaService/?repage";
	}

	
	/**
	 * 验证登录名是否有效
	 * 
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("rfa:rfaService:edit")
	@RequestMapping(value = "checkDbId")
	public String checkDbId(String oldServiceCode, String serviceCode) {
		if (serviceCode != null && serviceCode.equals(oldServiceCode)) {
			return "true";
		} else if (serviceCode != null && rfaServiceService.checkOnlyServiceCode(serviceCode) == null) {
			return "true";
		}
		return "false";
	}
	@RequiresPermissions("rfa:rfaService:view")
	@RequestMapping(value ="export")
	public String export(RfaService rfaService, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			String fileName = I18n.i18nMessage("rfa.service_data")+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";		
			List<RfaService> result = rfaServiceService.findList(rfaService);
			new ExportExcel(I18n.i18nMessage("rfa.service_data"), RfaService.class,true).setDataList(result).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, I18n.i18nMessage("rfa.service_export_error_controller_msg")+"： " + e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rfa/rfaService/?repage";
	}
	/**
	 * 下载导入用户数据模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("rfa:rfaService:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = I18n.i18nMessage("rfa.service_export_template_name");
			List<RfaService> list = Lists.newArrayList();
			List<RfaService> result = rfaServiceService.findList(new RfaService());
			if(result!=null&&result.size()>0){
				list.add(result.get(0));
			}
			new ExportExcel(I18n.i18nMessage("rfa.service_data"), RfaService.class, 2,true).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, I18n.i18nMessage("ica.import_error_template_msg")+"： " + e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rfa/rfaService/?repage";
	}
	/**
	 * 导入用户数据
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("rfa:rfaService:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/rfa/rfaService/?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<RfaService> list = ei.getDataList(RfaService.class);
			for (RfaService rfaService : list) {
				try {
					RfaService dbService = null;
					if ("true".equals(checkDbId("", rfaService.getServiceCode()))) {
						BeanValidators.validateWithException(validator, rfaService);
						rfaServiceService.save(rfaService);
						successNum++;
					} else {
						rfaService.setIsNewRecord(true);
						BeanValidators.validateWithException(validator, rfaService);
						if("Y".equals(rfaService.getForceUpdate())){
							rfaServiceService.updateByServiceCode(rfaService);
							successNum++;
						}else if((dbService =rfaServiceService.checkOnlyServiceCode(rfaService.getServiceCode())).getCustomVersion()!=null){
							if(!dbService.getCustomVersion().startsWith("CV_")){
								rfaServiceService.updateByServiceCode(rfaService);
								successNum++;
							}else{
								failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code") + rfaService.getServiceCode() +" "+ I18n.i18nMessage("ica.import_exist")+"; ");
								failureNum++;
							}
						}else{
							rfaServiceService.updateByServiceCode(rfaService);
							successNum++;
						}
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code")+" "+ rfaService.getServiceCode()  + " "+I18n.i18nMessage("ica.import_failure")+"： ");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code")+" "+ rfaService.getServiceCode()  + " 导入失败：" + ex.getMessage());
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "， "+I18n.i18nMessage("ica.failure")+" " + failureNum + " "+I18n.i18nMessage("rfa.service_import_number_error_msg")+"： ");
			}
			addMessage(redirectAttributes, I18n.i18nMessage("ica.impory_success_al")+" " + successNum + " "+I18n.i18nMessage("rfa.service_import_number_error_msg")+" " + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, I18n.i18nMessage("rfa.service_import_error_controller_msg")+"： " + e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rfa/rfaService/?repage";
	}
	
	@RequiresPermissions("ica:iccnIcaService:edit")
	@RequestMapping(value = "slectVersionFile")
	public String slectVersionFile(IccnIcaService iccnIcaService,
			RedirectAttributes redirectAttributes,Model model) {
		try {
			JSONArray versionArray = iccnIcaServiceService.getVersionList(erpType,RFA_NAME);
			model.addAttribute("versionList",versionArray);
			return "modules/ica/selectVersionFileForm";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "404";
		}
	}
	@RequiresPermissions("ica:iccnIcaService:edit")
	@RequestMapping(value = "importVersion", method = RequestMethod.POST)
	@ResponseBody
	public String importVersion(@RequestParam(required = false)String customVersion,String selectVersion){
		File file =null;
		if(StringUtils.isEmpty(customVersion)){
			try{
			customVersion = sysPropertiesService.getValue(RFA_VERSION_KEY);
			}catch(Exception e){
				e.printStackTrace();
				customVersion = "";
			}
		}
		if(customVersion==null){
			customVersion="";
		}
		if(StringUtils.isEmpty(selectVersion)){
			JSONArray versionArray = iccnIcaServiceService.getVersionList(erpType,RFA_NAME);
			if(versionArray==null||versionArray.size()==0){
				return "更新失败，当前erp不存在版本信息！";
			}
			if(StringUtils.isEmpty(selectVersion)){
				selectVersion = versionArray.getJSONObject(0).getString("name");
			}
		}
		try {
			String fileName = iccnIcaServiceService.saveVersion(customVersion, selectVersion,erpType,RFA_NAME);
			file = new File(fileName);
			
			String msg = excelImport(file);
			try{
				sysPropertiesService.saveByKeyValue(RFA_VERSION_KEY, selectVersion);
			}catch(Exception e){
				e.printStackTrace();
			}
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	private String excelImport(File file){
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<RfaService> list = ei.getDataList(RfaService.class);
			for (RfaService rfaService : list) {
				try {
					RfaService dbService = null;
					if ("true".equals(checkDbId("", rfaService.getServiceCode()))) {
						BeanValidators.validateWithException(validator, rfaService);
						rfaServiceService.save(rfaService);
						successNum++;
					} else {
						rfaService.setIsNewRecord(true);
						BeanValidators.validateWithException(validator, rfaService);
						if("Y".equals(rfaService.getForceUpdate())){
							rfaServiceService.updateByServiceCode(rfaService);
							successNum++;
						}else if((dbService =rfaServiceService.checkOnlyServiceCode(rfaService.getServiceCode())).getCustomVersion()!=null){
							if(!dbService.getCustomVersion().startsWith("CV_")){
								rfaServiceService.updateByServiceCode(rfaService);
								successNum++;
							}else{
								failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code") + rfaService.getServiceCode() +" "+ I18n.i18nMessage("ica.import_exist")+"; ");
								failureNum++;
							}
						}else{
							rfaServiceService.updateByServiceCode(rfaService);
							successNum++;
						}
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code")+" "+ rfaService.getServiceCode()  + " "+I18n.i18nMessage("ica.import_failure")+"： ");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code")+" "+ rfaService.getServiceCode()  + " 导入失败：" + ex.getMessage());
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "， "+I18n.i18nMessage("ica.failure")+" " + failureNum + " "+I18n.i18nMessage("rfa.service_import_number_error_msg")+"： ");
			}
			if(successNum>0){
				IcaSpringContextHolder.initDataSource();
			}
			return I18n.i18nMessage("ica.impory_success_al")+" " + successNum + " "+I18n.i18nMessage("rfa.service_import_number_error_msg")+" " + failureMsg;
		} catch (Exception e) {
			return I18n.i18nMessage("rfa.service_import_error_controller_msg")+"： " + e.getMessage();
		}
	}

	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		RfaService rfaService = new RfaService();
//		rfaServiceService.setErpType(erpType);
		List<RfaService> list = rfaServiceService.findList(rfaService);
		if(list==null||list.size()==0){
				new Thread(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						logger.info("首次自动导入启动开始");
						try{
							//检测tomcat是否有文件
							String filePath = PropertiesHolder.getADPHome()+"/conf/rfaFile/"+erpType;
							File file = new File(filePath);
							File excelFile = null;
							if(file.exists()&&file.isDirectory()){
								File[] fileList =  file.listFiles(new FileFilter() {
									
									public boolean accept(File pathname) {
										// TODO Auto-generated method stub
										return pathname.getName().endsWith("xlsx");
										 
									}
								});
								if(fileList!=null&& fileList.length>0){
									for(File childFile:fileList){
										if(excelFile==null){
											excelFile=childFile;
										}else{
											if(childFile.lastModified()>excelFile.lastModified()){
												excelFile=childFile;
											}
										}
										
									}
									if(excelFile!=null){
										logger.info("本地文件导入");
										String msg =excelImport(excelFile);
										logger.info(msg);
										return;
									}
								}
							}
							logger.info("oss导入");
							String msg = importVersion("","");
							logger.info(msg);
						}catch(Exception e){
							e.printStackTrace();
							logger.error("首次自动导入失败:"+e.getMessage());
						}
					}
				}).start();;
					// TODO Auto-generated method stub
			
	
		}
	}
}