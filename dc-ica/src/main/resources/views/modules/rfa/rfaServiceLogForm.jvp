<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('rfa.log_api_manager')}</title>
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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/rfa/rfaServiceLog/">${fns:i18nMessage('rfa.log_api_manager_list')}</a></li>
		<li class="active"><a href="${ctx}/rfa/rfaServiceLog/form?id=${rfaServiceLog.id}">${fns:i18nMessage('rfa.log_manager_view')}</a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="rfaServiceLog" action="${ctx}/rfa/rfaServiceLog/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.rfa_url_id')}：</label>
			<div class="col-sm-3">
				<form:input path="urlId" htmlEscape="false" maxlength="50" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.service_code')}：</label>
			<div class="col-sm-3">
				<form:input path="serviceCode" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.service_name')}：</label>
			<div class="col-sm-3">
				<form:input path="serviceName" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.service_params')}：</label>
			<div class="col-sm-10">
				<form:textarea path="serviceParams" htmlEscape="false" rows="10" class="form-control input-logxxlarge" readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.log_return_result')}：</label>
			<div class="col-sm-10">
				<form:textarea path="result" htmlEscape="false" rows="10" class="form-control input-logxxlarge" readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.log_request_params')}：</label>
			<div class="col-sm-10">
				<form:textarea path="requestParams" htmlEscape="false" rows="10" class="form-control input-logxxlarge" readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.log_intface_return')}：</label>
			<div class="col-sm-10">
				<form:textarea path="responseResult" htmlEscape="false" rows="10" class="form-control input-logxxlarge" readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.service_request_address')}：</label>
			<div class="col-sm-10">
				<form:input path="url" htmlEscape="false" maxlength="255" class="form-control input-logxxlarge" readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.service_headers')}：</label>
			<div class="col-sm-10">
				<form:textarea path="headers" htmlEscape="false" rows="4" class="form-control input-logxxlarge" readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.description')}：</label>
			<div class="col-sm-3">
				<form:input path="description" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.service_method')}：</label>
			<div class="col-sm-3">
				<form:input path="method" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.service_content_type')}：</label>
			<div class="col-sm-3">
				<form:input path="contentType" htmlEscape="false" maxlength="11" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.service_request_time')}：</label>
			<div class="col-sm-3">
				<div class="input-group date" id="datepicker-createTime">  
					<input name="createTime" type="text" readonly="readonly" maxlength="20" class="form-control input-xlarge " 
					value="<fmt:formatDate value="${rfaServiceLog.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly="true"/> 
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.log_response_time')}：</label>
			<div class="col-sm-3">
				<form:input path="resDuration" htmlEscape="false" maxlength="11" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.log_status')}：</label>
			<div class="col-sm-3">
			<input id="isSuccess" value="${fns:getDictLabel(rfaServiceLog.isSuccess, 'ica_log_is_success', '')}" name="isSuccess" htmlEscape="false" maxlength="11" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">ip：</label>
			<div class="col-sm-3">
				<form:input path="ip" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
             </div>
        </div>
	</form:form>
</body>
</html>