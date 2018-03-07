<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('job.log_schedule_log_manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();s
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
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
		<li><a href="${ctx}/job/jobLog/">${fns:i18nMessage('job.schedule_log_list')}</a></li>
		<li class="active"><a href="${ctx}/job/jobLog/form?id=${jobLog.id}">${fns:i18nMessage('job.log_schedule_log_view')}</a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="jobLog" action="${ctx}/job/jobLog/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			job主键${fns:i18nMessage('job.log_job_id')}：</label>
			<div class="col-sm-3">
				<form:input path="jobId" htmlEscape="false" maxlength="255" class="form-control input-xlarge required" readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.log_execute_type')}：</label>
			<div class="col-sm-3">
				<input id="executeType" value="${fns:getDictLabel(jobLog.executeType, 'dc_job_execute_type', '')}" name="isSuccess" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="readonly"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.log_group_name')}：</label>
			<div class="col-sm-3">
				<form:input path="groupName" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.service_code')}：</label>
			<div class="col-sm-3">
				<form:input path="serviceCode" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.res_service_code')}：</label>
			<div class="col-sm-3">
				<form:input path="resServiceCode" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.log_request_params')}：</label>
			<div class="col-sm-3">
				<form:textarea path="requestParams" htmlEscape="false" rows="10" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.log_pre_paramter')}：</label>
			<div class="col-sm-3">
				<form:textarea path="preParamter" htmlEscape="false" rows="10" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.log_result')}：</label>
			<div class="col-sm-3">
				<form:textarea path="result" htmlEscape="false" rows="10" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.log_exception')}：</label>
			<div class="col-sm-3">
				<form:textarea path="exception" htmlEscape="false" rows="10" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.log_start_time')}：</label>
			<div class="col-sm-3">
				<div class="input-group date" id="datepicker-startTime">  
					<input name="startTime" type="text" readonly="readonly" maxlength="20" class="form-control input-xlarge " 
					value="<fmt:formatDate value="${jobLog.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"  readonly="true"/> 
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.log_end_time')}：</label>
			<div class="col-sm-3">
				<div class="input-group date" id="datepicker-endTime">  
					<input name="endTime" type="text" readonly="readonly" maxlength="20" class="form-control input-xlarge" 
					value="<fmt:formatDate value="${jobLog.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"  readonly="true"/> 
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.log_res_duration')}：</label>
			<div class="col-sm-3">
				<form:input path="resDuration" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('job.log_is_success')}：</label>
			<div class="col-sm-3">
				<input id="isSuccess" value="${fns:getDictLabel(jobLog.isSuccess, 'ica_log_is_success', '')}" name="isSuccess" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="readonly"/>
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