<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('job.log_schedule_log_manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/job/jobLog/">${fns:i18nMessage('job.schedule_log_list')}</a></li>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="jobLog" action="${ctx}/job/jobLog/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="col-sm-1 control-label">${fns:i18nMessage('job.log_job_id')}：</label>
				<div class="col-sm-2">
					<form:input path="jobId" htmlEscape="false" maxlength="255" class="form-control input-medium"/>
				</div>
				<label class="col-sm-1 control-label">${fns:i18nMessage('job.log_execute_type')}：</label>
				<div class="col-sm-2">
					<form:select path="executeType" class="form-control input-medium">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('dc_job_execute_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>				
				</div>
				<label class="col-sm-1 control-label">${fns:i18nMessage('job.log_group_name')}：</label>
				<div class="col-sm-2">
					<form:input path="groupName" htmlEscape="false" maxlength="255" class="form-control input-medium"/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label">${fns:i18nMessage('job.service_code')}：</label>
				<div class="col-sm-2">
					<form:input path="serviceCode" htmlEscape="false" maxlength="255" class="form-control input-medium"/>
				</div>
				<label class="col-sm-1 control-label">${fns:i18nMessage('job.log_is_success')}：</label>
				<div class="col-sm-2">
					<form:select path="isSuccess" class="form-control input-medium">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('ica_log_is_success')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" />
			</div>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-hover table-striped">
			<thead>
				<tr>
					<th>${fns:i18nMessage('job.id')}</th>
					<th>${fns:i18nMessage('job.log_job_id')}</th>
					<th>${fns:i18nMessage('job.log_execute_type')}</th>
					<th>${fns:i18nMessage('job.log_group_name')}</th>
					<th>${fns:i18nMessage('job.log_method')}</th>
					<th>${fns:i18nMessage('job.service_code')}</th>
					<th>${fns:i18nMessage('job.log_start_time')}</th>
					<th>${fns:i18nMessage('job.log_end_time')}</th>
					<th>${fns:i18nMessage('job.log_is_success')}</th>
					<shiro:hasPermission name="job:jobLog:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="jobLog">
				<tr>
					<td><a href="${ctx}/job/jobLog/form?id=${jobLog.id}">
						${jobLog.id}
					</a></td>
					<td>
						${jobLog.jobId}
					</td>
					<td>
						${fns:getDictLabel(jobLog.executeType, 'dc_job_execute_type', '')}
					</td>
					<td>
						${jobLog.groupName}
					</td>
					<td>
						${jobLog.method}
					</td>
					<td>
						${jobLog.serviceCode}
					</td>
					<td>
						<fmt:formatDate value="${jobLog.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${jobLog.resDuration}
					</td>
					<td>
						${fns:getDictLabel(jobLog.isSuccess, 'ica_log_is_success', '')}
					</td>
					<shiro:hasPermission name="job:jobLog:edit"><td>
	    				<a href="${ctx}/job/jobLog/form?id=${jobLog.id}">${fns:i18nMessage('common.modify')}</a>
						<a href="${ctx}/job/jobLog/delete?id=${jobLog.id}" onclick="return confirmx('${fns:i18nMessage('job.delete_log_confrim ')}', this.href)">${fns:i18nMessage('common.delete')}</a>
					</td></shiro:hasPermission>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		${page}
	</div>
	</div>
	</div>
	</div>
</div>
</body>
</html>