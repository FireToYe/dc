package com.zhilink.srm.manager.modules.ica.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.zhilink.api.adapter.ApiClient;
import com.zhilink.manager.common.web.ResourceSqlBean;
import com.zhilink.srm.common.utils.JdbcUtils;

/**
 * h2db移植至sys自动版本升级
 * @author Administrator
 *
 */

public class H2DBInitResourse implements  InitializingBean, ApplicationListener<ApplicationEvent>{

	private static Logger logger = LoggerFactory.getLogger(H2DBInitResourse.class);
	private Resource[] resourceFiles; // 资源文件
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private String resourceUrl = "classpath*:/h2db/*.sql";
//	@Value("${jdbc.driver}")
//	private String jdbcDriver;
	private static final String H2_DRIVER = "org.h2.Driver";
	public void onApplicationEvent(ApplicationEvent event) {
		
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
/*				try {
					if(!H2_DRIVER.equals(jdbcDriver)){
						return;
					}
					if(!"H2 JDBC Driver".equals(jdbcTemplate.getDataSource().getConnection().getMetaData().getDriverName())){
						return;
					}
					if(!isExistTableName("sys_user")){
						ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();        
					     //将加载多个绝对匹配的所有Resource  
					    //将首先通过ClassLoader.getResources("META-INF")加载非模式路径部分  
					    //然后进行遍历模式匹配  
						resourceFiles=resolver.getResources(resourceUrl); 
						if(resourceFiles==null){
							return;
						}
						logger.info("h2db数据库执行开始");
					    for(Resource resource:resourceFiles){
					    	if(!resource.getURI().toString().endsWith(".sql")){
					    		continue;
					    	}
					    	String content = readFile(resource);
					    	String[] _sqls=content.split(";[\\s]*(\\r\\n|\\n)");
					    	List<String> list = new ArrayList<String>();
					    	for(String s : _sqls){
					    		list.add(s);
							}
					    	JdbcUtils.doExecuteBatch(jdbcTemplate, list);
					    }
					    logger.info("h2db数据库执行结束");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("h2db数据库执行异常:"+e.getMessage());
				}*/
	}

	/**
	 * 执行指定sql文件，不更新版本号
	 * @param appName 模块名称
	 * @param fileNme 文件名
	 * @return
	 */
	public boolean executeSqlFile(String appName, String fileNme){
		boolean result=false;
		try {
			String content=null;
			for (Resource rs : this.resourceFiles) {
				String tpath = rs.getURI().toString();
				if(tpath.contains(appName) && tpath.contains(fileNme)){
					content = IOUtils.toString(rs.getInputStream(),"UTF-8");
					break;
				}
			}
			if(!StringUtils.isBlank(content)){
				result=JdbcUtils.executeBatchSqls(jdbcTemplate, content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 读取资源文件内容
	 * @param rs
	 * @return
	 */
	public String readFile(Resource rs){
		String content=null;
		try {
			if(rs !=null){
				content = IOUtils.toString(rs.getInputStream(),"UTF-8");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	@SuppressWarnings("deprecation")
	public boolean isExistTableName(String tableName){
		String sql = "SELECT count(1) FROM information_schema.TABLES WHERE table_name ='xxxxx'".replace("xxxxx", tableName);
		int count = this.jdbcTemplate.queryForInt(sql);
		if(count>0){
			return true;
		}
		return false;
	}
}
