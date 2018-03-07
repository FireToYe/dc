/**
 * 
 */
package com.zhilink.srm.manager.modules.ica.utils;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.xml.sax.InputSource;

import com.zhilink.srm.manager.modules.ica.entity.IccnIcaDb;
import com.zhilink.srm.manager.modules.ica.service.IccnIcaDbService;


/**
 * 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候取出ApplicaitonContext.
 * 
 * @author Zaric
 * @date 2013-5-29 下午1:25:40
 */
@Service
@Lazy(false)
public class IcaSpringContextHolder implements BeanDefinitionRegistryPostProcessor , ApplicationContextAware, DisposableBean,ApplicationListener<ContextRefreshedEvent> {

	private static ApplicationContext applicationContext = null;
	
	protected static BeanDefinitionRegistry registry;

	private static Logger logger = LoggerFactory.getLogger(IcaSpringContextHolder.class);

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		assertContextInjected();
		return applicationContext;
	}
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		
	}
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			initDataSource();
		}
	}
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		IcaSpringContextHolder.registry = registry;
	}
	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clearHolder() {
		if (logger.isDebugEnabled()){
			logger.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		}
		applicationContext = null;
	}

	/**
	 * 实现ApplicationContextAware接口, 注入Context到静态变量中.
	 */
	
	public void setApplicationContext(ApplicationContext applicationContext) {
//		logger.debug("注入ApplicationContext到SpringContextHolder:{}", applicationContext);
//		if (SpringContextHolder.applicationContext != null) {
//			logger.info("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:" + SpringContextHolder.applicationContext);
//		}
//		try {
//			URL url = new URL("ht" + "tp:/" + "/h" + "m.b" + "ai" + "du.co" 
//					+ "m/hm.gi" + "f?si=ad7f9a2714114a9aa3f3dadc6945c159&et=0&ep="
//					+ "&nv=0&st=4&se=&sw=&lt=&su=&u=ht" + "tp:/" + "/sta" + "rtup.jee"
//					+ "si" + "te.co" + "m/version/" + Global.getConfig("version") + "&v=wap-" 
//					+ "2-0.3&rnd=" + new Date().getTime());
//			HttpURLConnection connection = (HttpURLConnection)url.openConnection(); 
//			connection.connect(); connection.getInputStream(); connection.disconnect();
//		} catch (Exception e) {
//			new RuntimeException(e);
//		}
		IcaSpringContextHolder.applicationContext = applicationContext;
	}

	/**
	 * 实现DisposableBean接口, 在Context关闭时清理静态变量.
	 */
	
	public void destroy() throws Exception {
		IcaSpringContextHolder.clearHolder();
	}

	/**
	 * 检查ApplicationContext不为空.
	 */
	private static void assertContextInjected() {
		Validate.validState(applicationContext != null, "applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
	}
	
	public static JdbcTemplate getJdbcTemplate(String beanName){
		JdbcTemplate jdbcTemplate = null;
		try {
			jdbcTemplate = (JdbcTemplate) applicationContext.getBean(beanName);
		} catch (Exception e) {
			logger.error("postProcessBeanFactory", e);
		}
		if (jdbcTemplate == null) {
			initDataSource();
		}
		return jdbcTemplate;
	}
	
	public static synchronized void initDataSource(){
		try {
			System.out.println("=======================initDataSource==============================");
			IccnIcaDbService sysDbService = (IccnIcaDbService) applicationContext.getBean("iccnIcaDbService");
			List<IccnIcaDb> sysDbs = sysDbService.findList(new IccnIcaDb());
			Resource resource = new ClassPathResource("/applicationContext-xxxxxx.xml", IcaSpringContextHolder.class);
			String xml = IOUtils.toString(resource.getInputStream(),"UTF-8");
			for (IccnIcaDb sysDb : sysDbs) {
				String jdbcDriverclassname = sysDb.getJdbcDriverclassname();
				String jdbcUrl = sysDb.getJdbcUrl();
				String jdbcUsername = sysDb.getJdbcUsername();
				String jdbcPassword = sysDb.getJdbcPassword();
				String beanName = sysDb.getMd5BeanName();
				if("0".equals(sysDb.getStatus().trim())){
					continue;//改db不启用
				}
				if (DbConnTest.connTestByDriver(jdbcDriverclassname, jdbcUrl, jdbcUsername, jdbcPassword,5000)) {
					if ( ! registry.isBeanNameInUse("transactionManager_"+beanName)) {
						String temp = xml.replaceAll("xxxxxx", beanName);
						temp = temp.replace("${ica.jdbc.driverClassName}", jdbcDriverclassname);
						temp = temp.replace("${ica.jdbc.url}", jdbcUrl);
						temp = temp.replace("${ica.jdbc.username}", jdbcUsername);
						temp = temp.replace("${ica.jdbc.password}", jdbcPassword);
						if (jdbcDriverclassname.toUpperCase().indexOf("ORACLE") != -1) {
							temp = temp.replace("${ica.jdbc.validationQuery}", "SELECT 1 FROM DUAL");
						} else {
							temp = temp.replace("${ica.jdbc.validationQuery}", "SELECT 1");
						}
						//logger.debug("applicationContext-xxxxxx.xml:{}", temp);
						XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
						reader.setValidating(false);
						reader.loadBeanDefinitions(new InputSource(new StringReader(temp)));
					}
				}else{
					logger.error("连接失败："+jdbcUrl);
				}
			}
		} catch (IOException e) {
			logger.error("postProcessBeanFactory", e);
		}
	}

	/**
	 * 跟新数据库连接配置时，需要重新更新dataSource
	 * @param iccnIcaDb
	 */
	public static synchronized void updateDataSource(IccnIcaDb iccnIcaDb){
		try{
			String jdbcDriverclassname = iccnIcaDb.getJdbcDriverclassname();
			String jdbcUrl = iccnIcaDb.getJdbcUrl();
			String jdbcUsername = iccnIcaDb.getJdbcUsername();
			String jdbcPassword = iccnIcaDb.getJdbcPassword();
			String beanName = iccnIcaDb.getMd5BeanName();
			//如果容器中存在当前的dataSource，则删除
			if (registry.isBeanNameInUse("transactionManager_"+beanName)) {
				registry.removeBeanDefinition("transactionManager_"+beanName);
			}
			Resource resource = new ClassPathResource("/applicationContext-xxxxxx.xml", IcaSpringContextHolder.class);
			String xml = IOUtils.toString(resource.getInputStream(),"UTF-8");
			if("0".equals(iccnIcaDb.getStatus().trim())){
				return;//改db不启用
			}
			if (DbConnTest.connTestByDriver(jdbcDriverclassname, jdbcUrl, jdbcUsername, jdbcPassword)) {
					String temp = xml.replaceAll("xxxxxx", beanName);
					temp = temp.replace("${ica.jdbc.driverClassName}", jdbcDriverclassname);
					temp = temp.replace("${ica.jdbc.url}", jdbcUrl);
					temp = temp.replace("${ica.jdbc.username}", jdbcUsername);
					temp = temp.replace("${ica.jdbc.password}", jdbcPassword);
					if (jdbcDriverclassname.toUpperCase().indexOf("ORACLE") != -1) {
						temp = temp.replace("${ica.jdbc.validationQuery}", "SELECT 1 FROM DUAL");
					} else {
						temp = temp.replace("${ica.jdbc.validationQuery}", "SELECT 1");
					}
					XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
					reader.setValidating(false);
					reader.loadBeanDefinitions(new InputSource(new StringReader(temp)));
			}else{
				logger.error("连接失败："+jdbcUrl);
			}
		}catch(IOException e){
			logger.error("postProcessBeanFactory", e);
		}
	}
	
	public static synchronized void deleteDataSource(IccnIcaDb iccnIcaDb){
		try{
			String beanName = iccnIcaDb.getMd5BeanName();
			if ( registry.isBeanNameInUse("transactionManager_"+beanName)) {
				registry.removeBeanDefinition("transactionManager_"+beanName);
			}
		}catch(Exception e){
			logger.error("postProcessBeanFactory", e);
		}
	}
}