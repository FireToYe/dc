package com.zhilink.srm.manager.modules.rfa.util;

import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.springframework.web.bind.annotation.RequestParam;

import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface FeignBean {

  @RequestLine("GET /{url}")
  String get(@HeaderMap Map<String, Object> headerMap,@Param("url") String url,@RequestParam Map<String, Object> map);

  @RequestLine("POST /{url}")
  String post(@HeaderMap Map<String, Object> headerMap,@Param("url") String url,String parameter);
  
  @RequestLine("PUT /{url}")
  String put(@HeaderMap Map<String, Object> headerMap,@Param("url") String url,String parameter);
  
  @RequestLine("DELETE /{url}")
  String delete(@HeaderMap Map<String, Object> headerMap,@Param("url") String url,String parameter);
  
  @RequestLine("POST /{url}")
  String postXML(@HeaderMap Map<String, Object> headerMap,@Param("url") String url,String parameter);
}
