package com.zhilink.srm.manager.modules.sys.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Maps;
import com.zhilink.manager.framework.common.mvc.Custom;
import com.zhilink.manager.framework.common.utils.CacheUtils;
import com.zhilink.manager.framework.common.utils.CookieUtils;
import com.zhilink.manager.framework.common.utils.IdGen;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.srm.common.config.Global;
import com.zhilink.srm.common.exception.AccessDeniedException;
import com.zhilink.srm.common.security.shiro.session.SessionDAO;
import com.zhilink.srm.common.servlet.ValidateCodeServlet;
import com.zhilink.srm.common.web.BaseController;
import com.zhilink.srm.manager.modules.sys.entity.Menu;
import com.zhilink.srm.manager.modules.sys.security.FormAuthenticationFilter;
import com.zhilink.srm.manager.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.zhilink.srm.manager.modules.sys.utils.UserUtils;

import groovyjarjarasm.asm.commons.Method;

/**
 * 登录Controller
 * 
 * @author jaray
 * 
 */
@Controller
public class LoginControllerCustom extends BaseController {

	@Autowired
	private SessionDAO sessionDAO;

	 
	
	/**
	 * 管理登录
	 */
	
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	@Custom
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		// API fix
		if (Global.isApiRequest(request)) {

			// api 的方式 直接抛出异常
			throw new AccessDeniedException();

		}
        System.out.println("======login fix =================++++++++++++++++++++++++++++=========================");
	 
		if (logger.isDebugEnabled()) {
			logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}

		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))) {
			CookieUtils.setCookie(response, "LOGINED", "false");
		}

		// 如果已经登录，则跳转到管理首页
		if (principal != null && !principal.isMobileLogin()) {
			return "redirect:" + adminPath;
		}
		 
		return "modules/sys/sysLogin";
	}
 
	public static void main(String args[]){
		LoginControllerCustom c=new LoginControllerCustom();
		for(java.lang.reflect.Method  m:c.getClass().getMethods()){
			Custom c1=m.getAnnotation( Custom.class);
			if(null!=c1){
				System.out.println(m.getName());
			}
			
		}
	}
	
	
}
