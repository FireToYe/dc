package com.zhilink.srm.manager.modules.rfa.util;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilink.manager.common.utils.Exceptions;
import com.zhilink.manager.framework.common.utils.StringUtils;

public class RfaHttpUtils {
    
	/**
	 * post请求，发送json格式的数据
	 */
	/**
	 * 日志对象
	 */
	protected static Logger logger = LoggerFactory.getLogger(RfaHttpUtils.class);
    public static String sendPost(String url, String json,String headers){  
        
        String strResult = null;  
        CloseableHttpClient httpClient = null;
        try{  
        	httpClient = HttpClients.createDefault();  
        	HttpPost httpPost = new HttpPost(url);  
            StringEntity se = new StringEntity(json,"UTF-8");
            //se.setContentType("application/json");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));
            httpPost.setEntity(se);
            if(!StringUtils.isEmpty(headers)){
            	try{
	            	Map<String,Object> headerMap = (Map<String, Object>) JSON.parse(headers);
	            	Set<Entry<String,Object>> headSet = headerMap.entrySet();
	            	for(Entry<String, Object> entry:headSet){
	            		String value = entry.getValue().toString();
	                    httpPost.setHeader( new BasicHeader(entry.getKey(), entry.getValue()!=null?value:"\"\""));
	            	}
            	}catch(Exception e){
            		e.printStackTrace();
            		logger.error("请求头设置错误："+e.getMessage());
            	}
            }
            HttpResponse response=httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {  
                strResult = EntityUtils.toString(response.getEntity(),"UTF-8");  
                
            }else{  
            	return errorMap("-1",String.valueOf(response.getStatusLine().getStatusCode()),response.getStatusLine().toString()); 
            }  
        } catch (Exception e) {  
        	e.printStackTrace();
            return errorMap("-1", e.getMessage(), Exceptions.getStackTraceAsString(e)) ;  
        }finally{
        	try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        } 
        return strResult;  
    }  
    
    /**
	 * post请求，发送xml格式的数据
	 */
    public static String sendXmlPost(String url, String xml,String headers){  
        
        String strResult = null;  
        CloseableHttpClient httpClient = null;
        try{  
        	httpClient = HttpClients.createDefault();  
        	HttpPost httpPost = new HttpPost(url);  
            StringEntity se = new StringEntity(xml,"UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));
            httpPost.setHeader( new BasicHeader("Connection", "Keep-Alive"));
            httpPost.setHeader( new BasicHeader("Content-Type", "text/xml; charset=utf-8"));
            httpPost.setHeader( new BasicHeader("SOAPAction", "\"\""));
            httpPost.setHeader( new BasicHeader("User-Agent", "Apache-HttpClient/4.1.1"));
            httpPost.setEntity(se);
            if(!StringUtils.isEmpty(headers)){
            	try{
	            	Map<String,Object> headerMap = (Map<String, Object>) JSON.parse(headers);
	            	Set<Entry<String,Object>> headSet = headerMap.entrySet();
	            	for(Entry<String, Object> entry:headSet){
	                    httpPost.setHeader( new BasicHeader(entry.getKey(), entry.getValue()!=null?(String)entry.getValue():"\"\""));
	            	}
            	}catch(Exception e){
            		logger.info("请求头设置错误："+e.getMessage());
            	}
            }
 //           System.out.println(httpPost.getRequestLine().toString());
            HttpResponse response=httpClient.execute(httpPost);
              
            if (response.getStatusLine().getStatusCode() == 200) {  
                strResult = EntityUtils.toString(response.getEntity(),"UTF-8");  
                
            }else{  
            	return errorXML("-1",String.valueOf(response.getStatusLine().getStatusCode()),response.getStatusLine().toString()); 
            }
            
        } catch (Exception e) {  
        	e.printStackTrace();
           return errorXML("-1", e.getMessage(), Exceptions.getStackTraceAsString(e)) ;
        }finally{
        	try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        } 
        return strResult;  
    }  
  
    public static String get(String url,String type,String headers) { 
        
    	CloseableHttpClient httpClient = null;    
        String strResult = "";  
        try {
            HttpResponse res = null;  
            HttpGet httpGet = new HttpGet(url);
            if(!StringUtils.isEmpty(headers)){
            	try{
	            	Map<String,Object> headerMap = (Map<String, Object>) JSON.parse(headers);
	            	Set<Entry<String,Object>> headSet = headerMap.entrySet();
	            	for(Entry<String, Object> entry:headSet){
	            		httpGet.setHeader( new BasicHeader(entry.getKey(), entry.getValue()!=null?(String)entry.getValue():"\"\""));
	            	}
            	}catch(Exception e){
            		logger.info("请求头设置错误："+e.getMessage());
            	}
            }
            httpClient = HttpClients.createDefault();  
            HttpResponse response=httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {  
                strResult = EntityUtils.toString(response.getEntity(),"UTF-8");  
                
            }else{  
            	return errorMap("-1",String.valueOf(response.getStatusLine().getStatusCode()),response.getStatusLine().toString()); 
            }  
        }catch(Exception e){
        	e.printStackTrace();
        	if("xml".equals(type)){
                return errorXML("-1", e.getMessage(), Exceptions.getStackTraceAsString(e)) ;
        	}else{
        		return errorMap("-1", e.getMessage(), Exceptions.getStackTraceAsString(e)) ; 
        	}
        }finally {  
        		try {
    				httpClient.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        }  
        return strResult;  
    }  
    
    public static String errorMap(String code,String sqlCode,String description){
    	String result = "	{"
    			+"		\"std_data\":{"
    			+"			\"execution\":{"
    			+"				\"code\":"+code+","
    			+"				\"sqlcode\":"+sqlCode+","
    			+"				\"description\":rfa远程调用失败："+description
    			+"			}"
    			+"		}"
    			+"	}";
    	return result;
    }
    public static String errorXML(String code,String sqlCode,String description){
    	String result = "<std_data>"
    			+"	<execution>"
    			+"		<code>"+code+"</code>"
    			+"		<sqlcode>"+sqlCode+"</sqlcode>"
    			+"		<description>rfa远程调用失败："+description+"</description>"
    			+"	</execution>"
    			+"	</std_data>";
    	return result;
    }

}

