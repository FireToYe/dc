<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统服务测试</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
/* 		var oldDate; */
		function send(){
			var data_ = $("#serviceParams").val();
			 var oldDate = new Date().getTime();
			$.ajax({
				url:ctx+'/ica/api/service/${iccnIcaService.serviceCode}',
				type:'POST',
				data:data_,
				contentType:"application/json",
				timeout:30000,
				success:function(data){
			        var date = new Date().getTime()-oldDate; 
	        		$("#sendDate").text("请求时间："+date+"毫秒");
					if(data.head){
						$("#result").val(JSON.stringify(data));
					}else{
						console.log(data);
						$("#result").val(data);
					}
			    },
			    error:function(e){
			        alert(e);
			    }
			});
		}
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/ica/iccnIcaService/">系统服务管理列表</a></li>
		<li class="active"><a href="#">系统服务测试</a></li>
	</ul><br/>
	
	<%-- <div class="control-group">
		<label class="control-label">服务编码：${iccnIcaService.serviceCode}</label>
	</div>
	
	<div class="control-group">
		<label class="control-label">服务名称：${iccnIcaService.serviceName}</label>
	</div>
	
	<div class="control-group">
		<label class="control-label">服务参数：</label><br/>
		<textarea id="param" htmlEscape="false" rows="4" class="input-xxlarge " >${iccnIcaService.serviceParams}</textarea>
	</div>
	
	<div class="form-actions">
		<input id="sendTes" value="Send" type="button" onclick="test()"/>
	</div>
	
	<div>
		<textarea id="result" rows="10" value=""></textarea>
	</div> --%>
	
	<form:form id="textForm" modelAttribute="iccnIcaService" class="form-horizontal">
		<form:hidden path="id"/>
		<div class="control-group">
			<label class="control-label">服务编码：</label>
			<label style="text-align: left;padding-left: 24px">${iccnIcaService.serviceCode}</label>
		</div>
		<div class="control-group">
			<label class="control-label">服务名称：</label>
			<label style="text-align: left;padding-left: 24px">${iccnIcaService.serviceName}</label>
		</div>
	<div class="control-group">
				<label class="control-label">服务名称：</label>
				<div class="controls">
				<textarea id="serviceParams" class="input-xxlarge " rows="5" style="height: 300px;max-width: 300px;min-width: 300px;">${iccnIcaService.serviceParams}</textarea>
			</div>
				
		</div>
		<div class="control-group" style="padding-left: 200px">
			<input id="sendpost" value="Send" type="button" class="btn btn-primary" onclick="send()"/>
			<span id = "sendDate" style="padding-left: 100px"></span>
		</div>
	</form:form>
	
			
		<div class="control-group" style="position:absolute;right:600px;top:62px;width:30px;height:30px;">
			<label class="control-label" style="width:100px;text-align: center">返回结果：</label>
			<div class="controls" style="padding-top: 18px">
				<textarea id="result" rows="10" class="input-xxlarge " style="min-height: 350px;min-width: 600px;"></textarea>
			</div>
		</div>
	
</body>
</html>

