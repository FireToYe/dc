package com.zhilink.srm.manager.modules.rfa.web.api;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.zhilink.manager.common.utils.Exceptions;
import com.zhilink.manager.framework.common.utils.DateUtils;
import com.zhilink.manager.framework.common.utils.MD5Tools;
import com.zhilink.manager.framework.common.utils.StringUtils;
import com.zhilink.manager.framework.common.web.BaseController;
import com.zhilink.srm.manager.modules.ica.service.IccnIcaDbService;
import com.zhilink.srm.manager.modules.ica.service.IccnIcaServiceService;
import com.zhilink.srm.manager.modules.ica.thread.RfaLogInsertThread;
import com.zhilink.srm.manager.modules.ica.utils.VEUtils;
import com.zhilink.srm.manager.modules.rfa.dao.RfaServiceDao;
import com.zhilink.srm.manager.modules.rfa.entity.RfaService;
import com.zhilink.srm.manager.modules.rfa.entity.RfaServiceLog;
import com.zhilink.srm.manager.modules.rfa.entity.RfaUrl;
import com.zhilink.srm.manager.modules.rfa.service.RfaServiceService;
import com.zhilink.srm.manager.modules.rfa.service.RfaUrlService;
import com.zhilink.srm.manager.modules.rfa.util.FeignBean;
import com.zhilink.srm.manager.modules.rfa.util.RfaHttpUtils;

import feign.Feign;
import feign.codec.Decoder;
import feign.codec.StringDecoder;
import feign.gson.GsonDecoder;

@Controller
@RequestMapping(value = "${adminPath}/ica/api/service/rfa")
public class RfaApiServiceController extends BaseController {
	@Autowired
	private RfaServiceService rfaServiceService;

	@Autowired
	private RfaUrlService rfaUrlService;

	@Autowired
	private RfaLogInsertThread rfaLogInsertThread;
	// private static VelocityEngine ve = new VelocityEngine();
	//
	// static {
	// ve.setProperty("velocimacro.permissions.allow.inline.local.scope", true);
	// }

	@RequestMapping("/{serviceCode}")
	@ResponseBody
	public String service(@PathVariable("serviceCode") String serviceCode,
			@RequestHeader HttpHeaders headers, @RequestBody String bodys,
			HttpServletRequest request) {
		Map<String, Object> bodyMap = (Map<String, Object>) JSON.parse(bodys);
		Map<String, Object> headerMap = transtojsonmap(headers
				.toSingleValueMap());
		RfaService rfaService = rfaServiceService.getByServiceCode(serviceCode);
		return execute(rfaService, serviceCode, bodyMap, headerMap,StringUtils.getRemoteAddr(request));
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/testService")
	@ResponseBody
	public String testService(@RequestBody RfaService rfaService,
			@RequestHeader HttpHeaders headers, HttpServletRequest request) {
		String bodys = "";
		if (rfaService != null) {
			bodys = rfaService.getServiceParams();
		}
		Map<String, Object> bodyMap = (Map<String, Object>) JSON.parse(bodys);
		Map<String, Object> headerMap = transtojsonmap(headers
				.toSingleValueMap());
		return execute(rfaService, rfaService.getServiceCode(), bodyMap,
				headerMap,StringUtils.getRemoteAddr(request));
	}

	private Map<String, Object> transtojsonmap(
			Map<String, String> singleValueMap) {
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

	public String sendPost(RfaService rfaService, String parameter,
			String url,String headers) {
		if ("get".equals(rfaService.getMethod())) {
			String result = RfaHttpUtils.get(url + parameter,
					rfaService.getContentType(), headers);
			return result;
		}
		if ("json".equals(rfaService.getContentType())) {
			return RfaHttpUtils.sendPost(url, parameter,
					headers);
		}
		if ("xml".equals(rfaService.getContentType())) {
			String result = RfaHttpUtils.sendXmlPost(url, parameter,
					headers);
			return result;
		}
		return errorMap("-1", "content-type error", "参数解析错误1！");
	}

	private static String replaceSpecial(String origin) {
		if (origin == null) {
			return "";
		}
		String ret = origin.replace("&lt;", "<").replace("&gt;", ">").trim();
		return ret;
	}

	public String execute(RfaService rfaService, String serviceCode,
			Map<String, Object> bodyMap, Map<String, Object> headerMap,String ip) {
		//返回值对象
		String result = "";
		//成功标志
		String isSuccess = "1";
		//请求参数对象
		String requestParams =null;
		//接口返回参数
		String resinterface = "";
		long startTime = System.currentTimeMillis();
		String url = "";
		if (rfaService == null) {
			result = errorMap("-1", "NullPointException",
					"查找不到对应的serviceCode");
			insertRfaLog("", serviceCode, "", "", "", "", result, "", "", "", "", "0", ip, String.valueOf(System.currentTimeMillis()-startTime),"");

			return result;
		}
		String headers = rfaService.getHeaders();
		String digiHost = "";
		String digiService="";
		try {
			RfaUrl rfaUrl = rfaUrlService.getByUrlId(rfaService.getUrlId());
			VelocityEngine ve = new VelocityEngine();
			ve.setProperty("velocimacro.permissions.allow.inline.local.scope",
					true);

			VelocityContext context = new VelocityContext();
			context.put("StringUtils", StringUtils.class);
			context.put("DateUtils", DateUtils.class);
			context.put("JSON", JSON.class);
			context.put("String", String.class);
			context.put("Integer", Integer.class);
			context.put("VEUtils", VEUtils.class);
			context.put("headerMap", headerMap);
			context.put("bodyMap", bodyMap);
			StringWriter parameterWriter = new StringWriter();
			ve.evaluate(context, parameterWriter, serviceCode,
					rfaService.getParamsResolve());
			requestParams = parameterWriter.toString();
			long a = System.currentTimeMillis();
			if("json".equals(rfaService.getContentType())){
				Map<String,Object> requestParamsMap = (Map<String, Object>) JSON.parse(requestParams);
				try{
			        if(requestParamsMap!=null){
			        	//为对接中台，加入可配置化header参数，使用方式为header 和body的json结构
			        	if(requestParamsMap.get("headers")!=null){
			        		headerMap = (Map<String, Object>) requestParamsMap.get("headers");
			        		
			        		if(requestParamsMap.get("body")!=null){
			        			requestParamsMap =(Map<String, Object>) requestParamsMap.get("body");
			        		}
			        		for(Entry<String, Object> entry:headerMap.entrySet()){
			        			String value = entry.getValue().toString();
			        			if("digi-host".equals(entry.getKey())){
			        				Map<String,String> entryMap = (Map<String, String>) entry.getValue();
			        				entryMap.put("timestamp", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
			        				digiHost = JSON.toJSONString(entryMap);
			        			}
			        			if("digi-service".equals(entry.getKey())){
			        				digiService =entry.getValue().toString();
			        			}
			        			
			        		}
			        		headerMap.put("digi-key", MD5Tools.MD5(digiHost+digiService).toLowerCase());
				        	headers = JSON.toJSONString(headerMap);
				        	requestParams = JSON.toJSONString(requestParamsMap);
			        	}
			        }
				}catch(Exception e){
					e.printStackTrace();
					result = errorMap("-1", e.getMessage(), Exceptions.getStackTraceAsString(e));
					e.printStackTrace();
					insertRfaLog(rfaService.getUrlId(), serviceCode, rfaService.getServiceName(),JSON.toJSONString(bodyMap,true), requestParams, result, rfaService.getDescription(), url, headers, rfaService.getMethod(), rfaService.getContentType(), "0", ip, String.valueOf(System.currentTimeMillis()-startTime),resinterface);
					return result;
				}
			}
			logger.info("耗时"+(System.currentTimeMillis()-a));
			if (rfaUrl != null && !StringUtils.isEmpty(rfaUrl.getUrl())) {
				url = url + rfaUrl.getUrl();
			}
			if (rfaService != null && !StringUtils.isEmpty(rfaService.getUrl())) {
				url = url + rfaService.getUrl();
			}
			String resPost = sendPost(rfaService, requestParams,
					url,headers);
			resinterface =  resPost;
			if ("xml".equals(rfaService.getContentType())) {
				resPost= XML.toJSONObject(replaceSpecial(resPost)).toString();
			} 
			//logger.info("xml转json耗时："+String.valueOf(System.currentTimeMillis()-xmlStartTime));
			if ("get".equals(rfaService.getMethod())) {
				url = url+requestParams;
			}
			if (resPost.contains("rfa远程调用失败：")) {
				insertRfaLog(rfaService.getUrlId(), serviceCode, rfaService.getServiceName(),JSON.toJSONString(bodyMap,true), requestParams, resPost, rfaService.getDescription(), url, rfaService.getHeaders(), rfaService.getMethod(), rfaService.getContentType(), "0", ip, String.valueOf(System.currentTimeMillis()-startTime),"");
				return resPost;
			}
			Map<String, Object> resMap = null;
			if(resPost!=null&&resPost.trim().startsWith("[")){
				resMap = JSON.parseArray(resPost).getJSONObject(0);
			}
			if(resPost!=null&&resPost.trim().startsWith("{")){
				resMap = (Map<String, Object>) JSON
						.parse(resPost);
			}
			context.put("map", resMap);
			StringWriter responseWriter = new StringWriter();
			ve.evaluate(context, responseWriter, serviceCode,
					rfaService.getResponeTemplate());
			result = responseWriter.toString();
			insertRfaLog(rfaService.getUrlId(), serviceCode, rfaService.getServiceName(),JSON.toJSONString(bodyMap,true), requestParams, result, rfaService.getDescription(), url, headers, rfaService.getMethod(), rfaService.getContentType(), isSuccess, ip, String.valueOf(System.currentTimeMillis()-startTime),resinterface);	
			return result;
		} catch (Exception e) {
			result = errorMap("-1", e.getMessage(), Exceptions.getStackTraceAsString(e));
			e.printStackTrace();
			insertRfaLog(rfaService.getUrlId(), serviceCode, rfaService.getServiceName(),JSON.toJSONString(bodyMap,true), requestParams, result, rfaService.getDescription(), url, headers, rfaService.getMethod(), rfaService.getContentType(), "0", ip, String.valueOf(System.currentTimeMillis()-startTime),resinterface);
			return result;
		}
	}

	public String errorMap(String code, String sqlCode, String description) {
		String result = "	{" + "		\"std_data\":{" + "			\"execution\":{"
				+ "				\"code\":" + code + "," + "				\"sqlcode\":" + sqlCode
				+ "," + "				\"description\":" + description + "			}" + "		}"
				+ "	}";
		return result;
	}

	public void insertRfaLog(String urlId,String serviceCode,String serviceName,String serviceParams,
			String requestParams, String result,
			String description, String url, String headers, String method,
			String contentType, String isSuccess, String ip,String resDuration,String responseResult) {
		RfaServiceLog rfaServiceLog = new RfaServiceLog(urlId, serviceCode, serviceName, requestParams, serviceParams, result, description, url, headers, method, contentType, isSuccess, ip, resDuration,responseResult);
		rfaLogInsertThread.insert(rfaServiceLog);
	}
	
}
