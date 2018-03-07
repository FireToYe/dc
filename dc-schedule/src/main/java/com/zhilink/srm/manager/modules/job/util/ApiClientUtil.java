package com.zhilink.srm.manager.modules.job.util;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhilink.api.adapter.ApiClient;
import com.zhilink.manager.framework.common.basemodel.PageApiResult;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@ApiClient
public interface ApiClientUtil {

	@Headers("Content-Type: application/json")
	@RequestLine("POST /{serviceCode}")
	public Map<String,Object> execute(@Param("serviceCode") String  serviceCode,JSONObject object);
	
}
