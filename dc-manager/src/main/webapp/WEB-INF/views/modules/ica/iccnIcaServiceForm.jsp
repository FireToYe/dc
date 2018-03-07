<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统服务管理管理</title>
	<meta name="decorator" content="default"/>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- <link href="assets/css/bootstrap-responsive.css" rel="stylesheet"> -->
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading("${fns:i18nMessage('common.loading')}...");
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
			var iccnIcaService = new Object();
			try{
				iccnIcaService.serviceParams = JSON.stringify(JSON.parse($("#serviceParams").val()).bodyMap);
			}catch(e){
				showTip("${fns:i18nMessage('ica.service_json_error_msg')}");
				return;
			}
			if(!iccnIcaService.serviceParams){
				iccnIcaService.serviceParams=$("#serviceParams").val();
			}
			iccnIcaService.responeTemplate = $("#responeTemplate").val();
			iccnIcaService.dbSql = $("#dbSql").val();
			iccnIcaService.serviceName = $("#serviceName").val();
			iccnIcaService.dbId = $("#dbId").val();
			iccnIcaService.serviceCode = $("#serviceCode").val();
			 var oldDate = new Date().getTime();
			$.ajax({
				url:ctx+'/ica/api/testService',
				type:'POST',
				data:JSON.stringify(iccnIcaService),
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
			window.location.href =ctx+ "/ica/iccnIcaService"
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/ica/iccnIcaService/">${fns:i18nMessage('ica.service_list')}</a></li>
		<li class="active"><a href="${ctx}/ica/iccnIcaService/form?id=${iccnIcaService.id}">${fns:i18nMessage('ica.service_manager')} <shiro:hasPermission name="ica:iccnIcaService:edit"><c:choose><c:when test="${not empty iccnIcaService.id}">${fns:i18nMessage('common.modify')}</c:when><c:otherwise>${fns:i18nMessage('common.add')}</c:otherwise></c:choose></shiro:hasPermission><shiro:lacksPermission name="ica:iccnIcaService:edit">${fns:i18nMessage('common.view')}}</shiro:lacksPermission></a></li>
	</ul>
		<div class="tab-content">
<div class="tab-pane active">
<div class="panel-body ">	
	<form:form id="inputForm" modelAttribute="iccnIcaService" action="${ctx}/ica/iccnIcaService/save" method="post" class="form-horizontal" target="${not empty iccnIcaService.id?'nm_iframe':'_self'}">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
				<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.db_id')}：</label>
			<div class="col-sm-3">
				<form:input path="dbId" htmlEscape="false" maxlength="255" class="form-control input-xlarge required"/>
			</div>
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.service_code')}：</label>
			<div class="col-sm-3">
				<form:input path="serviceCode" htmlEscape="false" maxlength="255" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.service_name')}：</label>
			<div class="col-sm-3">
				<form:input path="serviceName" htmlEscape="false" maxlength="255" class="form-control input-xlarge required"/>
			</div>
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.description')}：</label>
			<div class="col-sm-3">
				<form:input path="description" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.status')}：</label>
			<div class="col-sm-3">
				<form:select path="status" class="form-control input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('iccn_ica_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.service_group')}：</label>
			<div class="col-sm-3">
				<form:select path="serviceGroup" class="form-control input-xlarge">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('iccn_ica_service_group')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
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
			<label class="col-sm-1 control-label">${fns:i18nMessage('ica.service_params')}：</label>
			<div class="col-sm-5">
				<form:textarea path="serviceParams" rows="20" class="form-control input-xxlarge "/>
			</div>
			<label class="col-sm-1 control-label">${fns:i18nMessage('ica.respone_template')}：</label>
			<div class="col-sm-5">
				<form:textarea path="responeTemplate" rows="20" class="form-control input-xxlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-1 control-label">${fns:i18nMessage('ica.db_sql')}：</label>
			<div class="col-sm-5">
				<form:textarea path="dbSql" rows="20" class="form-control input-xxlarge "/>
			</div>
			<label class="col-sm-1 control-label">${fns:i18nMessage('ica.service_result')}：</label>
			<div class="col-sm-5">
				<textarea id="result" name="result"  rows="20" class="form-control input-xxlarge " readonly="readonly"></textarea>
			</div>
		</div>
		<div class="form-group">
		   <div class="col-sm-4 col-sm-offset-2">
			<shiro:hasPermission name="ica:iccnIcaService:edit"><input id="btnSubmit" class="btn btn-primary" type="button" onclick="submitBeforeAsk()" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="goServerHome()"/>
			<input type="button" class="btn" value="${fns:i18nMessage('ica.test')}" onclick="send()">
			<input type="button" class="btn" value="${fns:i18nMessage('ica.service_clear_data')}" onclick="revertResult()">
			<span id="sendDate"></span>
		             </div>
        </div>
	</form:form>
	</div>
	</div>
	</div>
	<iframe id="id_iframe" name="nm_iframe" style="display:none;"></iframe>  
	</div></div>
</body>
</html>