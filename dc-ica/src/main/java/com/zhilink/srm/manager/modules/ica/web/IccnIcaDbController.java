/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/jaray/zhilink">zhilink</a> All rights reserved.
 */
package com.zhilink.srm.manager.modules.ica.web;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zhilink.manager.common.utils.I18n;
import com.zhilink.manager.framework.common.api.basemodel.ResultModel;
import com.zhilink.manager.framework.common.basemodel.PageApiResult;
import com.zhilink.manager.framework.common.beanvalidator.BeanValidators;
import com.zhilink.manager.framework.common.utils.DateUtils;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.exception.CommonException;
import com.zhilink.srm.common.persistence.Page;
import com.zhilink.srm.common.utils.excel.ExportExcel;
import com.zhilink.srm.common.utils.excel.ImportExcel;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.ica.entity.IccnIcaDb;
import com.zhilink.srm.manager.modules.ica.service.IccnIcaDbService;
import com.zhilink.srm.manager.modules.ica.utils.IcaSpringContextHolder;
import com.zhilink.srm.manager.modules.sys.entity.User;
import com.zhilink.srm.manager.modules.sys.service.SystemService;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;


/**
 * 集成管理Controller
 * @author chrisye
 * @version 2017-09-08
 */
@Controller
@RequestMapping(value = "${adminPath}/ica/iccnIcaDb")
public class IccnIcaDbController extends BaseController {

	@Autowired
	private IccnIcaDbService iccnIcaDbService;
	
	@ModelAttribute
	public IccnIcaDb get(@RequestParam(required=false) String id) {
		IccnIcaDb entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = iccnIcaDbService.get(id);
		}
		if (entity == null){
			entity = new IccnIcaDb();
		}
		return entity;
	}
	
	@RequiresPermissions("ica:iccnIcaDb:view")
	@RequestMapping(value = {"list", ""})
	public String list(IccnIcaDb iccnIcaDb, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<IccnIcaDb> page = iccnIcaDbService.findPage(new Page<IccnIcaDb>(request, response), iccnIcaDb); 
		model.addAttribute("page", page);
		return "modules/ica/iccnIcaDbList";
	}

	@RequiresPermissions("ica:iccnIcaDb:view")
	@RequestMapping(value = "form")
	public String form(IccnIcaDb iccnIcaDb, Model model) {
		model.addAttribute("iccnIcaDb", iccnIcaDb);
		return "modules/ica/iccnIcaDbForm";
	}

	@RequiresPermissions("ica:iccnIcaDb:edit")
	@RequestMapping(value = "save")
	public String save(final IccnIcaDb iccnIcaDb, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, iccnIcaDb)){
			return form(iccnIcaDb, model);
		}
		iccnIcaDbService.save(iccnIcaDb);
		new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				IcaSpringContextHolder.updateDataSource(iccnIcaDb);
			}
		}).start();
		addMessage(redirectAttributes,I18n.i18nMessage("ica.save_db_success") );
		return "redirect:"+Global.getAdminPath()+"/ica/iccnIcaDb/?repage";
	}
	
	@RequiresPermissions("ica:iccnIcaDb:edit")
	@RequestMapping(value = "delete")
	public String delete(IccnIcaDb iccnIcaDb, RedirectAttributes redirectAttributes) {
		iccnIcaDb = iccnIcaDbService.get(iccnIcaDb);
		iccnIcaDbService.delete(iccnIcaDb);
		IcaSpringContextHolder.deleteDataSource(iccnIcaDb);
		addMessage(redirectAttributes, I18n.i18nMessage("ica.delete_db_success"));
		return "redirect:"+Global.getAdminPath()+"/ica/iccnIcaDb/?repage";
	}

	/**
	 * 验证DBID是否存在
	 * @param oldDbId
	 * @param dbId
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions("ica:iccnIcaDb:edit")
	@RequestMapping(value = "checkDbId")
	public String checkDbId(String oldDbId, String dbId) {
		if (dbId != null && dbId.equals(oldDbId)) {
			return "true";
		} else if (dbId != null && iccnIcaDbService.checkOnlyByDbId(dbId) == null) {
			return "true";
		}
		return "false";
	}
	@RequiresPermissions("ica:iccnIcaDb:view")
	@RequestMapping(value ="export")
	public String export(IccnIcaDb iccnIcaDb, HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			String fileName = I18n.i18nMessage("ica.db_data")+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";		
			List<IccnIcaDb> result = iccnIcaDbService.findList(iccnIcaDb);
			new ExportExcel(I18n.i18nMessage("ica.db_data"), IccnIcaDb.class,true).setDataList(result).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, I18n.i18nMessage("ica.db_export_error_controller_msg")+ "：" + e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ica/iccnIcaDb/?repage";
	}
	/**
	 * 下载导入用户数据模板
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ica:iccnIcaDb:view")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = I18n.i18nMessage("ica.db_export_template_name");
			List<IccnIcaDb> list = Lists.newArrayList();
			List<IccnIcaDb> result = iccnIcaDbService.findList(new IccnIcaDb());
			if(result!=null&&result.size()>0){
				list.add(result.get(0));
			}
			new ExportExcel(I18n.i18nMessage("ica.db_manager"), IccnIcaDb.class, 2,true).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, I18n.i18nMessage("ica.import_error_template_msg")+"：" + e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ica/iccnIcaDb/?repage";
	}
	/**
	 * 导入用户数据
	 * 
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("ica:iccnIcaDb:edit")
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
			List<IccnIcaDb> list = ei.getDataList(IccnIcaDb.class);
			for (IccnIcaDb iccnIcaDb : list) {
				try {
					if ("true".equals(checkDbId("", iccnIcaDb.getDbId()))) {
						BeanValidators.validateWithException(validator, iccnIcaDb);
						iccnIcaDbService.save(iccnIcaDb);
						successNum++;
					} else {
						failureMsg.append("<br/>"+I18n.i18nMessage("ica.db_id")+" " + iccnIcaDb.getDbId() +" "+ I18n.i18nMessage("ica.import_exist")+"; ");
						failureNum++;
					}
				} catch (ConstraintViolationException ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("ica.db_id")+" " + iccnIcaDb.getDbId()+" " + I18n.i18nMessage("ica.import_failure")+"：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList) {
						failureMsg.append(message + "; ");
						failureNum++;
					}
				} catch (Exception ex) {
					failureMsg.append("<br/>"+I18n.i18nMessage("ica.db_id")+" " + iccnIcaDb.getDbId() + " "+I18n.i18nMessage("ica.import_failure")+"：" + ex.getMessage());
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，"+I18n.i18nMessage("ica.failure")+" " + failureNum + " "+I18n.i18nMessage("ica.db_import_number_error_msg")+"：");
			}
			if(successNum>0){
				IcaSpringContextHolder.initDataSource();
			}
			addMessage(redirectAttributes, I18n.i18nMessage("ica.impory_success_al") + successNum + " "+I18n.i18nMessage("ica.db_import_number_error_msg")+" " + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, I18n.i18nMessage("ica.db_import_error_controller_msg")+"：" + e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/ica/iccnIcaDb/?repage";
	}
	
	
	@RequestMapping(value="testConn")
	@ResponseBody
	public String testConn(IccnIcaDb iccnIcaDb,HttpServletRequest request, HttpServletResponse response, Model model){
		Map<String,Object> map = new HashMap<String, Object>();
		String jdbcDriverclassname = iccnIcaDb.getJdbcDriverclassname();
		String jdbcUrl = iccnIcaDb.getJdbcUrl();
		String jdbcUsername = iccnIcaDb.getJdbcUsername();
		String jdbcPassword = iccnIcaDb.getJdbcPassword();
		String beanName = iccnIcaDb.getMd5BeanName();
		try{
			//测试连接
			if (connTestByDriver(jdbcDriverclassname, jdbcUrl, jdbcUsername, jdbcPassword)) {
				map.put("success", "success");
				map.put("message",I18n.i18nMessage("ica.db_connection_success"));
			}else{
				map.put("message",I18n.i18nMessage("ica.db_connection_error"));
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("message",I18n.i18nMessage("ica.db_connection_error")+"："+e.getMessage());
		}
		return JSON.toJSONString(map);
	}
	@RequestMapping(value="testConnection")
	@ResponseBody
	public String testConnection(@RequestBody IccnIcaDb iccnIcaDb,HttpServletRequest request, HttpServletResponse response, Model model){
		Map<String,Object> map = new HashMap<String, Object>();
		String jdbcDriverclassname = iccnIcaDb.getJdbcDriverclassname();
		String jdbcUrl = iccnIcaDb.getJdbcUrl();
		String jdbcUsername = iccnIcaDb.getJdbcUsername();
		String jdbcPassword = iccnIcaDb.getJdbcPassword();
		String beanName = iccnIcaDb.getMd5BeanName();
		try{
			if (connTestByDriver(jdbcDriverclassname, jdbcUrl, jdbcUsername, jdbcPassword)) {
				map.put("success", "success");
				map.put("message",I18n.i18nMessage("ica.db_connection_success"));
			}else{
				map.put("message",I18n.i18nMessage("ica.db_connection_error"));
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("message",I18n.i18nMessage("ica.db_connection_error")+"："+e.getMessage());
		}
		return JSON.toJSONString(map);
	}
	public boolean connTestByDriver(String driver, String dbUrl, String user, String password) throws Exception {
		// 创建数据库连接对象  
	    Connection connnection = null;  
		
		boolean result = false;
		// 加载驱动程序
		try {
			Driver driverClass = (Driver) Class.forName(driver).newInstance();
			// 获取连接
			Properties info = new Properties();
			if(!StringUtils.isEmpty(user)){
				info.put("user", user);
			}
			if(!StringUtils.isEmpty(password)){
				info.put("password", password);
			}
			connnection = driverClass.connect(dbUrl, info);
			//connnection = DriverManager.getConnection(dbUrl, user, password);
			if(connnection!=null){
				result = true;
			}else{
				throw new Exception(I18n.i18nMessage("ica.db_driver_is_error"));
			}
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new Exception(I18n.i18nMessage("ica.db_driver")+e.getMessage()+I18n.i18nMessage("ica.none_exist")+";",e);
		}catch(SQLException e1){
			e1.printStackTrace();
			throw new Exception(I18n.i18nMessage("ica.db_connection_msg")+"："+e1.getMessage(),e1);
		}catch(Exception e2){
			e2.printStackTrace();
			throw e2;
		}
		finally{
			if(connnection != null){
				try {
					connnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

}