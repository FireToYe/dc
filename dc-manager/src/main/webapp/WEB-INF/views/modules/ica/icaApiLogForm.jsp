<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('ica.service_log_manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();s
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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/ica/icaApiLog/">${fns:i18nMessage('ica.service_log_list')}</a></li>
			<li class="active"><a href="${ctx}/ica/icaApiLog/form?id=${icaApiLog.id}">${fns:i18nMessage('ica.log_view')}</a></li>

	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="icaApiLog" action="${ctx}/ica/icaApiLog/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.service_code')}：</label>
			<div class="col-sm-3">
				<form:input path="serviceCode" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">
			${fns:i18nMessage('ica.db_id')}：</label>
			<div class="col-sm-3">
				<form:input path="dbId" htmlEscape="false" maxlength="255" class="form-control input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.service_name')}：</label>
			<div class="col-sm-3">
				<form:input path="serviceName" htmlEscape="false" maxlength="255" class="form-control input-xlarge "  readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.log_request_params')}：</label>
			<div class="col-sm-10">
				<form:textarea path="serviceParams" htmlEscape="false" rows="15" class="form-control input-logxxlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.log_return_result')}：</label>
			<div class="col-sm-10">
				<form:textarea path="result" rows="20" class="form-control input-logxxlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.db_sql')}：</label>
			<div class="col-sm-10">
				<form:textarea path="dbSql" rows="20" class="form-control input-logxxlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.respone_template')}：</label>
			<div class="col-sm-10">
				<form:textarea path="responeTemplate" rows="20" class="form-control input-logxxlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">ip：</label>
			<div class="col-sm-2">
				<form:input path="ip" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.log_response_time')}：</label>
			<div class="col-sm-3">
				<form:input path="resDuration" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.log_status')}：</label>
			<div class="col-sm-3">
			<%-- 	<form:select path="isSuccess" class="form-control input-xlarge " disabled="disabled">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('ica_log_is_success')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select> --%>
				<input value="${fns:getDictLabel(icaApiLog.isSuccess, 'ica_log_is_success', '')}" type="text" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.request_time')}：</label>
			<div class="col-sm-3">
				<div class="input-group date" id="datepicker-createTime">  
					<%-- <input name="createTime" type="text" readonly="readonly" maxlength="20" class="form-control input-medium" 
					value="<fmt:formatDate value="${icaApiLog.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" /> 
					<span class="input-group-btn input-group-addon"> <i class="fa fa-calendar"></i></span> --%>
						<input value="<fmt:formatDate value="${icaApiLog.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"   type="text" maxlength="255" class="form-control input-xlarge " readonly="true"/>
				</div>
				<script type="text/javascript">
					/* $('#datepicker-createTime').datepicker({
					todayBtn: "linked",
					keyboardNavigation: false,
					forceParse: false,
					calendarWeeks: true,
					autoclose: true}); */
				</script>	
			</div>
		</div>
		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
         			<!-- <shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission> -->
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
             </div>
        </div>
	</form:form>
</body>
</html>