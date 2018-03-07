/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.ica.web;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.zhilink.manager.common.config.PropertiesHolder;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.beanvalidator.BeanValidators;
import com.zhilink.manager.framework.common.exception.CommonException;
import com.zhilink.manager.framework.common.utils.DateUtils;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.utils.excel.ExportExcel;
import com.zhilink.srm.common.utils.excel.ImportExcel;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaDb;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaService;
import com.zhilink.srm.manager.modules.ica.service.IccnIcaDbService;
import com.zhilink.srm.manager.modules.ica.service.IccnIcaServiceService;
import com.zhilink.srm.manager.modules.ica.utils.VEUtils;
import com.zhilink.srm.manager.modules.sys.service.SysPropertiesService;

/**
 * ica系统服务管理Controller
 * @author chrisyeye
 * @version 2017-09-11
 */
@Controller
@RequestMapping(value = "${adminPath}/ica/iccnIcaService")
public class IccnIcaServiceController extends BaseController implements ApplicationListener<ContextRefreshedEvent>{
	@Value("${ica.erpType}")
	private String erpType;
	@Value("${oss_url:http://www.oss.zhilink.com}")
	private String ossUrl;
	@Autowired
	private IccnIcaServiceService iccnIcaServiceService;
	
	private static final String ICA_NAME = "ica";
	
	@Autowired
	private IccnIcaDbService iccnIcaDbService;
	
	@Autowired
	private SysPropertiesService sysPropertiesService;
	
	private static VelocityEngine ve = new VelocityEngine();
	
	private static final String ICA_VERSION_KEY = "iccn-ica-version";
	@ModelAttribute
	public IccnIcaService get(@RequestParam(required=false) String id) {
		IccnIcaService entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = iccnIcaServiceService.get(id);
		}
		if (entity == null){
			entity = new IccnIcaService();
		}
		return entity;
	}
	
	@RequiresPermissions("ica:iccnIcaService:view")
	@RequestMapping(value = {"list", ""})
	public String list(IccnIcaService iccnIcaService, HttpServletRequest request, HttpServletResponse response, Model model,@RequestParam(required=false) String dataArray) {
		iccnIcaService.setErpType(erpType);
		Page<IccnIcaService> page = iccnIcaServiceService.findPage(new Page<IccnIcaService>(request, response), iccnIcaService); 
		model.addAttribute("dataArray", dataArray);
		model.addAttribute("page", page);
		return "modules/ica/iccnIcaServiceList";
	}

	@RequiresPermissions("ica:iccnIcaService:view")
	@RequestMapping(value = "form")
	public String form(IccnIcaService iccnIcaService, Model model) {
		model.addAttribute("iccnIcaService", iccnIcaService);
		return "modules/ica/iccnIcaServiceForm";
	}

	@RequiresPermissions("ica:iccnIcaService:view")
	@RequestMapping(value = "copy")
	public String copy(IccnIcaService iccnIcaService, Model model) {
		iccnIcaService.setId("");
		iccnIcaService.setDbId("");
		iccnIcaService.setServiceCode("");
		iccnIcaService.setServiceName("");
		iccnIcaService.setDescription("");
		model.addAttribute("iccnIcaService", iccnIcaService);
		return "modules/ica/iccnIcaServiceForm";
	}
	
	@RequiresPermissions("ica:iccnIcaService:edit")
	@RequestMapping(value = "save")
	public String save(IccnIcaService iccnIcaService, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, iccnIcaService)){
			return form(iccnIcaService, model);
		}
		iccnIcaService.setServiceParams(StringEscapeUtils.unescapeHtml4(iccnIcaService.getServiceParams()));
		iccnIcaService.setDbSql(StringEscapeUtils.unescapeHtml4(iccnIcaService.getDbSql()));
		iccnIcaService.setResponeTemplate(StringEscapeUtils.unescapeHtml4(iccnIcaService.getResponeTemplate()));
		iccnIcaService.setErpType(erpType);
		iccnIcaService.setSystemVersion("v_"+DateUtils.getDate("yyyyMMddHHmmssSSS"));
		iccnIcaServiceService.save(iccnIcaService);
		addMessage(redirectAttributes, I18n.i18nMessage("ica.save_service_success"));
		return "redirect:"+Global.getAdminPath()+"/ica/iccnIcaService/form/?id="+iccnIcaService.getId();
	}
	
	@RequiresPermissions("ica:iccnIcaService:edit")
	@RequestMapping(value = "delete")
	public String delete(IccnIcaService iccnIcaService, RedirectAttributes redirectAttributes) {
		iccnIcaServiceService.delete(iccnIcaService);
		addMessage(redirectAttributes, I18n.i18nMessage("ica.delete_service_success"));
		return "redirect:"+Global.getAdminPath()+"/ica/iccnIcaService/?repage";
	}
	
	private Map<String, Object> transtojsonmap(Map<String, String> singleValueMap) {
		Map<String, Object> headerMap = new HashMap<String, Object>();
		for (Map.Entry<String, String> entry : singleValueMap.entrySet()) {
			try {
				Object json = JSON.parse(entry.getValue());
				headerMap.put(entry.getKey(), json);
			} catch (Exception e) {
				headerMap.put(entry.getKey(), entry.getValue());
			}
		}
		return headerMap;
	}
	
	/**
	 * 验证登录名是否有效
	 * 
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("ica:iccnIcaService:edit")
	@RequestMapping(value = "checkDbId")
	public String checkDbId(String oldServiceCode, String serviceCode,String erpTypeNew) {
		IccnIcaService iccnIcaService = new IccnIcaService();
		iccnIcaService.setErpType(erpTypeNew);
		iccnIcaService.setServiceCode(serviceCode);
		if (oldServiceCode != null && serviceCode.equals(oldServiceCode)) {
			return "true";
		} else if (serviceCode != null && iccnIcaServiceService.checkOnlyByServiceCode(iccnIcaService) == null) {
			return "true";
		}
		return "false";
	}
	@RequiresPermissions("ica:iccnIcaService:view")
	@RequestMapping(value ="export")
	public String export(IccnIcaService iccnIcaService, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			String fileName = I18n.i18nMessage("ica.service_data")+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";		
			iccnIcaService.setErpType(erpType);
			List<IccnIcaService> result = iccnIcaServiceService.findList(iccnIcaService);
			new ExportExcel(I18n.i18nMessage("ica.service_data"), IccnIcaService.class,true).setDataList(result).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes,  I18n.i18nMessage("ica.service_export_error_controller_msg")+"： "+ e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ica/iccnIcaService/?repage";
	}
	
	@RequiresPermissions("ica:iccnIcaService:view")
	@RequestMapping(value ="exportByChoose")
	public String export(@RequestParam(required=false) String dataArray, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			String fileName = I18n.i18nMessage("ica.service_data")+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";		
			//iccnIcaService.setErpType(erpType);
			List<String> ids = JSON.parseArray(dataArray, String.class);
			List<IccnIcaService> result = iccnIcaServiceService.exportList(ids);
			new ExportExcel(I18n.i18nMessage("ica.service_data"), IccnIcaService.class,true).setDataList(result).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			addMessage(redirectAttributes, I18n.i18nMessage("ica.service_export_error_controller_msg") + e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ica/iccnIcaService/?repage";
	}
	/**
	 * 下载导入用户数据模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ica:iccnIcaService:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = I18n.i18nMessage("ica.service_export_template_name");
			List<IccnIcaService> list = Lists.newArrayList();
			IccnIcaService iccnIcaService = new IccnIcaService();
			iccnIcaService.setErpType(erpType);
			List<IccnIcaService> result = iccnIcaServiceService.findList(iccnIcaService);
			if(result!=null&&result.size()>0){
				list.add(result.get(0));
			}
			new ExportExcel(I18n.i18nMessage("ica.service_data"), IccnIcaService.class, 2,true).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes,I18n.i18nMessage("ica.service_import_number_error_msg") +"： "+ e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ica/iccnIcaService/?repage";
	}
	/**
	 * 导入用户数据
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ica:iccnIcaService:edit")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/ica/iccnIcaDb/?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<IccnIcaService> list = ei.getDataList(IccnIcaService.class);
			for (IccnIcaService iccnIcaService : list) {
				IccnIcaService dbService = null;
				try {
					if(StringUtils.isEmpty(iccnIcaService.getErpType())){
						iccnIcaService.setErpType(erpType);
					}
					if ("true".equals(checkDbId("", iccnIcaService.getServiceCode(),iccnIcaService.getErpType()))) {
						BeanValidators.validateWithException(validator, iccnIcaService);
						iccnIcaServiceService.save(iccnIcaService);
						successNum++;
					} else {
						iccnIcaService.setIsNewRecord(true);
						BeanValidators.validateWithException(validator, iccnIcaService);
						if("Y".equals(iccnIcaService.getForceUpdate())){
							iccnIcaServiceService.updateByServiceCodeAndErpType(iccnIcaService);
							successNum++;
						}else if((dbService = iccnIcaServiceService.checkOnlyByServiceCode(iccnIcaService)).getCustomVersion()!=null){
							if(!dbService.getCustomVersion().startsWith("CV_")){
								iccnIcaServiceService.updateByServiceCodeAndErpType(iccnIcaService);
								successNum++;
							}else{
								failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code") + iccnIcaService.getServiceCode() +" "+ I18n.i18nMessage("ica.import_exist")+"; ");
								failureNum++;
							}
						}else{
							iccnIcaServiceService.updateByServiceCodeAndErpType(iccnIcaService);
							successNum++;
						}
						/*failureMsg.append("<br/>数据库连接ID" + iccnIcaService.getDbId() + " 已存在; ");
						failureNum++;*/
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code") + iccnIcaService.getServiceCode() + " "+I18n.i18nMessage("ica.import_failure")+"：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code") + iccnIcaService.getServiceCode() +  " "+I18n.i18nMessage("ica.import_failure")+"："+ ex.getMessage());
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，"+I18n.i18nMessage("ica.failure") + failureNum + " "+I18n.i18nMessage("ica.service_import_error_controller_msg")+"：");
			}
			addMessage(redirectAttributes, I18n.i18nMessage("ica.impory_success_al")+ " "+ successNum + " "+I18n.i18nMessage("ica.service_import_number_error_msg")+" " + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, I18n.i18nMessage("ica.service_import_error_controller_msg") + e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ica/iccnIcaService/?repage";
	}

	@RequiresPermissions("ica:iccnIcaService:edit")
	@RequestMapping(value = "slectVersionFile")
	public String slectVersionFile(IccnIcaService iccnIcaService,
			RedirectAttributes redirectAttributes,Model model) {
		try {
			JSONArray versionArray = iccnIcaServiceService.getVersionList(erpType,ICA_NAME);
			model.addAttribute("versionList",versionArray);
			return "modules/ica/selectVersionFileForm";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "404";
		}
	}

	/**
	 * 指定版本导入，如果指定版本为空，则获取最新版本
	 * @param customVersion 当前版本
	 * @param selectVersion 选择版本
	 * @return
	 */
	@RequiresPermissions("ica:iccnIcaService:edit")
	@RequestMapping(value = "importVersion", method = RequestMethod.POST)
	@ResponseBody
	public String importVersion(@RequestParam(required = false)String customVersion,String selectVersion){
		File file =null;
		if(StringUtils.isEmpty(customVersion)){
			try{
			customVersion = sysPropertiesService.getValue(ICA_VERSION_KEY);
			}catch(Exception e){
				e.printStackTrace();
				customVersion = "";
			}
		}
		if(customVersion==null){
			customVersion="";
		}
		if(StringUtils.isEmpty(selectVersion)){
			JSONArray versionArray = iccnIcaServiceService.getVersionList(erpType,ICA_NAME);
			if(versionArray==null||versionArray.size()==0){
				return "更新失败，当前erp不存在版本信息！";
			}
			if(StringUtils.isEmpty(selectVersion)){
				selectVersion = versionArray.getJSONObject(0).getString("name");
			}
		}
		try {
			String fileName = iccnIcaServiceService.saveVersion(customVersion, selectVersion,erpType,ICA_NAME);
			file = new File(fileName);
			
			String msg = excelImport(file);
			try{
				sysPropertiesService.saveByKeyValue(ICA_VERSION_KEY, selectVersion);
			}catch(Exception e){
				e.printStackTrace();
			}
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		//当服务启动时，不存在适配器数据，则开始使用自动从本地导入，如果本地不存在，则从oss中导入版本
			IccnIcaService iccnIcaService = new IccnIcaService();
			iccnIcaService.setErpType(erpType);
			List<IccnIcaService> list = iccnIcaServiceService.findList(iccnIcaService);
			if(list==null||list.size()==0){
					new Thread(new Runnable() {
						
						public void run() {
							// TODO Auto-generated method stub
							logger.info("首次自动导入启动开始");
							try{
								//检测tomcat是否有文件
								String filePath = PropertiesHolder.getADPHome()+"/conf/icaFile/"+erpType;
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

	
	public String excelImport(File file){
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<IccnIcaService> list = ei.getDataList(IccnIcaService.class);
			for (IccnIcaService iccnIcaService : list) {
				IccnIcaService dbService = null;
				try {
					if(StringUtils.isEmpty(iccnIcaService.getErpType())){
						iccnIcaService.setErpType(erpType);
					}
					if ("true".equals(checkDbId("", iccnIcaService.getServiceCode(),iccnIcaService.getErpType()))) {
						BeanValidators.validateWithException(validator, iccnIcaService);
						iccnIcaServiceService.save(iccnIcaService);
						successNum++;
					} else {
						iccnIcaService.setIsNewRecord(true);
						BeanValidators.validateWithException(validator, iccnIcaService);
						if("Y".equals(iccnIcaService.getForceUpdate())){
							iccnIcaServiceService.updateByServiceCodeAndErpType(iccnIcaService);
							successNum++;
						}else if((dbService = iccnIcaServiceService.checkOnlyByServiceCode(iccnIcaService)).getCustomVersion()!=null){
							if(!dbService.getCustomVersion().startsWith("CV_")){
								iccnIcaServiceService.updateByServiceCodeAndErpType(iccnIcaService);
								successNum++;
							}else{
								failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code") + iccnIcaService.getServiceCode() +" "+ I18n.i18nMessage("ica.import_exist")+"; ");
								failureNum++;
							}
						}else{
							iccnIcaServiceService.updateByServiceCodeAndErpType(iccnIcaService);
							successNum++;
						}
						/*failureMsg.append("<br/>数据库连接ID" + iccnIcaService.getDbId() + " 已存在; ");
						failureNum++;*/
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code") + iccnIcaService.getServiceCode() + " "+I18n.i18nMessage("ica.import_failure")+"：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("ica.service_code") + iccnIcaService.getServiceCode() +  " "+I18n.i18nMessage("ica.import_failure")+"："+ ex.getMessage());
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，"+I18n.i18nMessage("ica.failure") + failureNum + " "+I18n.i18nMessage("ica.service_import_error_controller_msg")+"：");
			}
			return  "import_success:"+I18n.i18nMessage("ica.impory_success_al")+ " "+ successNum + " "+I18n.i18nMessage("ica.service_import_number_error_msg")+" " + failureMsg;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return IOUtils.toString(new FileInputStream(file),"UTF-8");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw new CommonException(e1.getMessage());
			} 
			
		}finally{
			//不删除下载文件
//			if(file!=null)
//				file.delete();
		}
	}
	
}