<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('job.job_task_manager')}</title>
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
		
		function deleteAtId(tip,href){
			layer.confirm(tip, {
				offset: 't' ,
				 btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"] //按钮
				}, function(){
					$("#searchForm").attr("action",href);
					$("#searchForm").submit();
				});
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/job/job/">${fns:i18nMessage('job.job_task_list')}</a></li>
		<shiro:hasPermission name="job:job:edit"><li><a href="${ctx}/job/job/form">${fns:i18nMessage('job.job_task_add')}</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="job" action="${ctx}/job/job/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
<!-- 				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
 -->		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>${fns:i18nMessage('job.id')}</th>
					<th>${fns:i18nMessage('job.job_name')}</th>
					<th>${fns:i18nMessage('job.job_group')}</th>
					<th>${fns:i18nMessage('job.cron_expression')}</th>
					<th>${fns:i18nMessage('job.is_concurrent')}</th>
					<th>${fns:i18nMessage('job.job_state')}</th>
					<th>${fns:i18nMessage('job.depend_last_time')}</th>
					<th>${fns:i18nMessage('job.execute_type')}</th>
					<th>${fns:i18nMessage('job.detail')}</th>
					<shiro:hasPermission name="job:job:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="job">
				<tr>
				<td>${job.id}</td>
				<td>${job.jobName}</td>
				<td>${job.jobGroup}</td>
				<td>${job.cronExpression}</td>
				<td>${fns:getDictLabel(job.isConcurrent, 'yes_no', '')}</td>
				<td>${fns:getDictLabel(job.jobState, 'iccn_ica_status', '')}</td>
				<td>${fns:getDictLabel(job.dependLastTime, 'srm_yes_no', '')}</td>
				<td>${fns:getDictLabel(job.executeType, 'dc_job_execute_type', '')}</td>
				<td>${job.detail}</td>
					<shiro:hasPermission name="job:job:edit"><td>
	    				<a href="${ctx}/job/job/form?id=${job.id}">${fns:i18nMessage('common.modify')}</a>
	    			<c:choose>
						<c:when test="${'0'.equals(job.jobState)}">
							<a href="${ctx}/job/job/changeJobState?id=${job.id}&jobState=1">${fns:i18nMessage('common.open')}</a>
						</c:when>
						<c:when test="${'1'.equals(job.jobState)}">
								<a href="${ctx}/job/job/changeJobState?id=${job.id}&jobState=0">${fns:i18nMessage('common.close')}</a>
						</c:when>
					</c:choose>
	    				<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('job.delete_confrim')}', '${ctx}/job/job/delete?id=${job.id}')">${fns:i18nMessage('common.delete')}</a>		
<%-- 						<a href="${ctx}/job/job/delete?id=${job.id}" onclick="return confirmx('确认要删除该定时任务吗？', this.href)">删除</a> --%>
					</td></shiro:hasPermission>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		${page}
		<strong>${fns:i18nMessage('job.job_plan')}</strong>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
			<tr>
					<th>${fns:i18nMessage('job.id')}</th>
					<th>${fns:i18nMessage('job.job_name')}</th>
					<th>${fns:i18nMessage('job.job_group')}</th>
					<th>${fns:i18nMessage('job.cron_expression')}</th>
					<th>${fns:i18nMessage('job.is_concurrent')}</th>
					<th>${fns:i18nMessage('job.job_state')}</th>
					<th>${fns:i18nMessage('job.execute_type')}</th>
					<th>${fns:i18nMessage('job.detail')}</th>
					<shiro:hasPermission name="job:job:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${task}" var="job">
				<tr>
				<td>${job.id}</td>
				<td>${job.jobName}</td>
				<td>${job.jobGroup}</td>
				<td>${job.cronExpression}</td>
				<td>${fns:getDictLabel(job.isConcurrent, 'yes_no', '')}</td>
				<td>${job.jobState}</td>
				<td>${fns:getDictLabel(job.executeType, 'dc_job_execute_type', '')}</td>
				<td>${job.detail}</td>
				<td>
				<c:choose>
					<c:when test="${'NORMAL'.equals(job.jobState)}">
						<a href="${ctx}/job/job/pauseJob?id=${job.id}">${fns:i18nMessage('job.pause')}</a>
					</c:when>
					<c:when test="${'PAUSED'.equals(job.jobState)}">
					<a href="${ctx}/job/job/resumeJob?id=${job.id}">${fns:i18nMessage('job.resume')}</a>		
					</c:when>
					<c:otherwise>
					<a href="${ctx}/job/job/pauseJob?id=${job.id}">${fns:i18nMessage('job.pause')}</a>
					</c:otherwise>
				</c:choose>
	    				<%-- <a href="${ctx}/job/job/form?id=${job.id}"></a>
	    				<a href="javascript:void(0);" onclick="deleteAtId('确认要删除该定时任务吗？', '${ctx}/job/job/delete?id=${job.id}')">删除</a>		 --%>
<%-- 						<a href="${ctx}/job/job/delete?id=${job.id}" onclick="return confirmx('确认要删除该定时任务吗？', this.href)">删除</a> --%>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<strong>${fns:i18nMessage('job.job_exe')}</strong>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>${fns:i18nMessage('job.id')}</th>
					<th>${fns:i18nMessage('job.job_name')}</th>
					<th>${fns:i18nMessage('job.job_group')}</th>
					<th>${fns:i18nMessage('job.cron_expression')}</th>
					<th>${fns:i18nMessage('job.is_concurrent')}</th>
					<th>${fns:i18nMessage('job.job_state')}</th>
					<th>${fns:i18nMessage('job.execute_type')}</th>
					<th>${fns:i18nMessage('job.detail')}</th>
					<shiro:hasPermission name="job:job:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${executeTask}" var="job">
				<tr>
				<td>${job.id}</td>
				<td>${job.jobName}</td>
				<td>${job.jobGroup}</td>
				<td>${job.cronExpression}</td>
				<td>${fns:getDictLabel(job.isConcurrent, 'yes_no', '')}</td>
				<td>${job.jobState}</td>
				<td>${fns:getDictLabel(job.executeType, 'dc_job_execute_type', '')}</td>
				<td>${job.detail}</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	</div>
	</div>
	</div>
</div>
</body>
</html>