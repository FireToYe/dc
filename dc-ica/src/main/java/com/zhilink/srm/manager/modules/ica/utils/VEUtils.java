package com.zhilink.srm.manager.modules.ica.utils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zhilink.srm.manager.modules.ica.entity.IccnIcaDb;
import com.zhilink.srm.manager.modules.ica.service.IccnIcaDbService;
import com.zhilink.srm.manager.modules.ica.utils.IcaSpringContextHolder;

/**
 * 模板引擎工具类
 */
public class VEUtils {
	private static final Logger log = LoggerFactory.getLogger(VEUtils.class);
	
	public static Integer parseInt(String s) {
		return Integer.parseInt(s);
	}
	
	public static Integer parseInt(Integer i) {
		return i;
	}
	
	public static String SqlIn (List data) {
		String result = "(";
		for (int i = 0; i < data.size(); i++) {
			if (i == 0) {
				result += "'"+data.get(i)+"'";
			} else {
				result += ","+"'"+data.get(i)+"'";
			}
		}
		return result+")";
	}
	
	public static List<Map<String, Object>> queryForList(String dbId,String sql) {
		String logTag = UUID.randomUUID().toString().replace("-", "");
		log.info("{}queryForList,dbId参数{},sql参数{}", logTag, dbId, sql);
		IccnIcaDbService sysDbService =  IcaSpringContextHolder.getApplicationContext().getBean(IccnIcaDbService.class);
		IccnIcaDb sysDbQuery = new IccnIcaDb();
		sysDbQuery.setDbId(dbId);
		IccnIcaDb sysDbBean = sysDbService.get(sysDbQuery);
		JdbcTemplate jdbcTemplate = IcaSpringContextHolder.getJdbcTemplate("jdbcTemplate_" + sysDbBean.getMd5BeanName());
		List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
		log.info("{}queryForList,响应data{}", logTag, data);
		return data;
	}
	
	public static String queryForString(String dbId,String sql) {
		IccnIcaDbService sysDbService = (IccnIcaDbService) IcaSpringContextHolder.getApplicationContext().getBean(IccnIcaDbService.class);
		IccnIcaDb sysDbQuery = new IccnIcaDb();
		sysDbQuery.setDbId(dbId);
		IccnIcaDb sysDbBean = sysDbService.get(sysDbQuery);
		JdbcTemplate jdbcTemplate = IcaSpringContextHolder.getJdbcTemplate("jdbcTemplate_" + sysDbBean.getMd5BeanName());
		String data = jdbcTemplate.queryForObject(sql, String.class);
		return data;
	}
	
	public static Integer queryForInteger(String dbId,String sql) {
		IccnIcaDbService sysDbService = IcaSpringContextHolder.getApplicationContext().getBean(IccnIcaDbService.class);
		IccnIcaDb sysDbQuery = new IccnIcaDb();
		sysDbQuery.setDbId(dbId);
		IccnIcaDb sysDbBean = sysDbService.get(sysDbQuery);
		JdbcTemplate jdbcTemplate = IcaSpringContextHolder.getJdbcTemplate("jdbcTemplate_" + sysDbBean.getMd5BeanName());
		Integer data = jdbcTemplate.queryForObject(sql, Integer.class);
		return data;
	}
}
