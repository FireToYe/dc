<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('rfa.service_connection_manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();s
			$("#inputForm").validate({
				submitHandler: function(form){
					loading("${fns:i18nMessage('common.loading')}");
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("${fns:i18nMessage('common.form.error.msg')}。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		function send(){
			var rfaService = new Object();
			try{
				rfaService.serviceParams = JSON.stringify(JSON.parse($("#serviceParams").val()).bodyMap);
			}catch(e){
				showTip("${fns:i18nMessage('ica.service_json_error_msg')}");
				return;
			}
			if(!rfaService.serviceParams){
				rfaService.serviceParams=$("#serviceParams").val();
			}
			rfaService.responeTemplate = $("#responeTemplate").val();
			rfaService.url = $("#url").val();
			rfaService.responeTemplate = $("#responeTemplate").val();
			rfaService.paramsResolve = $("#paramsResolve").val();
			rfaService.serviceName = $("#serviceName").val();
			rfaService.serviceCode = $("#serviceCode").val();
			rfaService.contentType = $("#contentType").val();
			rfaService.urlId = $("#urlId").val();
			rfaService.method = $("#method").val();
			rfaService.headers = $("#headers").val();
			 var oldDate = new Date().getTime();
			 $.ajax({
				url:ctx+'/ica/api/service/rfa/testService',
				type:'POST',
				data:JSON.stringify(rfaService),
				contentType:"application/json",
				timeout:30000,
				success:function(data){
			        var date = new Date().getTime()-oldDate; 
	        		$("#sendDate").text("${fns:i18nMessage('ica.service_request_time')}："+date+"${fns:i18nMessage('ica.service_msec')}");
					if(data.head){
						$("#result").val(JSON.stringify(data));
					}else{
						console.log(data);
						$("#result").val(data);
					}
			    },
			    error:function(e){
			        console.log(e);
			    }
			});
		}
		function revertResult(){
			$("#result").val("");
		}
		function submitBeforeAsk(){
			layer.confirm("${fns:i18nMessage('ica.service_save_acquire_message')}", {
				  btn: ["${fns:i18nMessage('ica.service_standard')}","${fns:i18nMessage('ica.service_custom')}"] //按钮
				}, function(){
					layer.prompt({title: "${fns:i18nMessage('ica.service_acquire_modify')}"}, function(pass, index){
						  $("#customVersion").val("V_"+pass);
						  layer.closeAll();
							$("#inputForm").submit();
						});
				}, function(){
					layer.prompt({title: "${fns:i18nMessage('ica.service_acquire_modify')}"}, function(pass, index){
						  $("#customVersion").val("CV_"+pass);
						  layer.closeAll();
							$("#inputForm").submit();
						});
				});
		}
		function goServerHome(){
			window.location.href =ctx+ "/rfa/rfaService"
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/rfa/rfaService/">${fns:i18nMessage('rfa.service_connection_manager_list')}</a></li>
		<li class="active"><a href="${ctx}/rfa/rfaService/form?id=${rfaService.id}">${fns:i18nMessage('rfa.url_connection_manager')}<shiro:hasPermission name="rfa:rfaService:edit"><c:choose><c:when test="${not empty rfaService.id}">${fns:i18nMessage('common.modify')}</c:when><c:otherwise>${fns:i18nMessage('common.add')}</c:otherwise></c:choose></shiro:hasPermission><shiro:lacksPermission name="rfa:rfaService:edit">${fns:i18nMessage('common.view')}</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="rfaService" action="${ctx}/rfa/rfaService/save" method="post" class="form-horizontal" target="${not empty rfaService.id?'nm_iframe':'_self'}">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.rfa_url_id')}：</label>
			<div class="col-sm-3">
				<form:input path="urlId" htmlEscape="false" maxlength="20" class="form-control input-xlarge"/>
			</div>
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.service_method')}：</label>
			<div class="col-sm-3">
					<form:select path="method" class="form-control input-medium">
						<form:option value="get" label="get"/>
						<form:option value="post" label="post"/>
					</form:select>			
				</div>
		</div>	
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.service_code')}：</label>
			<div class="col-sm-3">
				<form:input path="serviceCode" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
			
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.description')}：</label>
			<div class="col-sm-3">
				<form:input path="description" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.service_content_type')}：</label>
			<div class="col-sm-3">
					<form:select path="contentType" class="form-control input-medium">
						<form:option value="json" label="json"/>
						<form:option value="xml" label="xml"/>
					</form:select>
			</div>
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.status')}：</label>
			<div class="col-sm-3">
				<form:select path="status" class="form-control input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('iccn_ica_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.service_name')}：</label>
			<div class="col-sm-3">
				<form:input row="10" path="serviceName" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.service_request_address')}：</label>
			<div class="col-sm-3">
				<form:input row="10" path="url" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.force_update')}：</label>
			<div class="col-sm-3">
				<form:select path="forceUpdate" class="form-control input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('srm_yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.custom_version')}：</label>
			<div class="col-sm-3">
				<form:input path="customVersion" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-1 control-label">${fns:i18nMessage('rfa.service_headers')}(json)：</label>
			<div class="col-sm-5">
				<form:textarea rows="20" path="headers" class="form-control input-xlarge "/>
			</div>
			<label class="col-sm-1 control-label">${fns:i18nMessage('rfa.respone_example')}：</label>
			<div class="col-sm-5">
				<form:textarea rows="20" path="responeExample" class="form-control input-xlarge "/>
			</div>
		</div>
<%-- 		<div class="form-group">
			<label class="col-sm-1 control-label">服务参数：</label>
			<div class="col-sm-5">
				<form:textarea rows="20" path="serviceParams" class="form-control input-xlarge "/>
			</div>
			<label class="col-sm-1 control-label">参数解析：</label>
			<div class="col-sm-5">
				<form:textarea rows="20" path="paramsResolve" class="form-control input-xlarge "/>
			</div>
		</div> --%>
		<div class="form-group">
			<label class="col-sm-1 control-label">${fns:i18nMessage('rfa.service_params')}：</label>
			<div class="col-sm-5">
				<form:textarea rows="20" path="serviceParams" class="form-control input-xlarge "/>
			</div>
			<label class="col-sm-1 control-label">${fns:i18nMessage('rfa.params_resolve')}：</label>
			<div class="col-sm-5">
				<form:textarea rows="20" path="paramsResolve" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-1 control-label">${fns:i18nMessage('rfa.respone_template')}：</label>
			<div class="col-sm-5">
				<form:textarea rows="20" path="responeTemplate" class="form-control input-xlarge "/>
			</div>
			<label class="col-sm-1 control-label">${fns:i18nMessage('ica.log_return_result')}：</label>
			<div class="col-sm-5">
				<textarea rows="20" id="result" name = "result" htmlEscape="false" class="form-control input-xlarge " readonly="readonly"></textarea>
			</div>
        </div>
        <div class="form-group">
                          <div class="col-sm-4 col-sm-offset-2">
         			<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="button" onclick="submitBeforeAsk()" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="goServerHome()"/>
			<input type="button" class="btn" value="${fns:i18nMessage('ica.test')}" onclick="send()">
			<input type="button" class="btn" value="${fns:i18nMessage('ica.service_clear_data')}" onclick="revertResult()">
			<span id="sendDate"></span>
		             </div>
             </div>
        </div>
	</form:form>
	</div></div></div></div>
	<iframe id="id_iframe" name="nm_iframe" style="display:none;"></iframe>  
</body>
</html>