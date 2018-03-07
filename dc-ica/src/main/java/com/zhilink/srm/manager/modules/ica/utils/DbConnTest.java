package com.zhilink.srm.manager.modules.ica.utils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import com.zhilink.manager.framework.common.utils.StringUtils;


/**
 * @author Vince
 *	数据库信息配置连接测试
 */
public class DbConnTest {
	private static final String DRIVER_NAME_MYSQL = "mysql";
	private static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
	private static final String DRIVER_NAME_ORACLE = "oracle";
	private static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
	private static final String DRIVER_NAME_SQLSERVER= "sqlserver";
	private static final String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	@SuppressWarnings("finally")
	public static String dbConnTest(Map<String, Object> paramMap) {
		// 数据库驱动类名称  
	    String driver = null;
		// 创建数据库连接对象  
	    Connection connnection = null;  
	    // 数据库URL
	    String dbUrl = null;
	    // 用户名
	    String user = null;
	    // 密码
	    String password = null;
	    // 数据库类型
	    String dbTypeCode = null;
		dbTypeCode = paramMap.get("dbTypeCode").toString();
		dbUrl = paramMap.get("dbUrl").toString();
		user = paramMap.get("user").toString();
		password = paramMap.get("password").toString();

		if (dbTypeCode.toLowerCase().contains(DRIVER_NAME_MYSQL)) {
			driver = DRIVER_MYSQL;
		} else if (dbTypeCode.toLowerCase().contains(DRIVER_NAME_ORACLE)) {
			driver = DRIVER_ORACLE;
		} else if (dbTypeCode.toLowerCase().contains(DRIVER_NAME_SQLSERVER)) {
			driver = DRIVER_SQLSERVER;
		}
		
		String resultMsg = null;
		// 加载驱动程序
		try {
			Class.forName(driver);
			// 获取连接
			connnection = DriverManager.getConnection(dbUrl, user, password);
			resultMsg = "Connection success!";
		}catch (SQLException e) {
			resultMsg = "Connection fail! Please check dbUrl, dbUser and dbPsw.";
		} finally{
			if(connnection != null){
				try {
					connnection.close();
				} catch (SQLException e) {
				}
			}
			return resultMsg;
		}
	}
	
	
	public static boolean connTest(String dbTypeCode, String dbUrl, String user, String password) {
		// 数据库驱动类名称  
	    String driver = null;
		// 创建数据库连接对象  
	    Connection connnection = null;  
		if (dbTypeCode.toLowerCase().contains(DRIVER_NAME_MYSQL)) {
			driver = DRIVER_MYSQL;
		} else if (dbTypeCode.toLowerCase().contains(DRIVER_NAME_ORACLE)) {
			driver = DRIVER_ORACLE;
		} else if (dbTypeCode.toLowerCase().contains(DRIVER_NAME_SQLSERVER)) {
			driver = DRIVER_SQLSERVER;
		}
		boolean result = false;
		// 加载驱动程序
		try {
			Class.forName(driver);
			// 获取连接
			connnection = DriverManager.getConnection(dbUrl, user, password);
			result = true;
		}catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(connnection != null){
				try {
					connnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}
	
	public static boolean connTestByDriver(String driver, String dbUrl, String user, String password) {
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
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(connnection != null){
				try {
					connnection.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}
	/**
	 * 设置超时的db链接方法
	 * @param driver
	 * @param dbUrl
	 * @param user
	 * @param password
	 * @param timeOut
	 * @return
	 */
	public static boolean connTestByDriver(final String driver,final String dbUrl,final String user,final String password,long timeOut) {
		// 创建数据库连接对象  
  	  	boolean lastResult = false; 
		final ExecutorService exec = Executors.newFixedThreadPool(1);  
		Callable<Boolean> call = new Callable<Boolean>() {  
	        public Boolean call() throws Exception {  
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
	    			}
	    		}catch (Exception e) {
	    			e.printStackTrace();
	    		} finally{
	    			if(connnection != null){
	    				try {
	    					connnection.close();
	    				} catch (SQLException e) {
	    				}
	    			}
	    		}
	    		return result;
	        }  
	    };  
	    try{
	    	  Future<Boolean> future = exec.submit(call);  
	    	  lastResult = future.get(timeOut, TimeUnit.MILLISECONDS);  
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return lastResult;
	}
}
