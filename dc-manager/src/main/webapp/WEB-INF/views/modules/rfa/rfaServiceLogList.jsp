<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('rfa.log_api_manager')}</title>
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
		<li class="active"><a href="${ctx}/rfa/rfaServiceLog/">${fns:i18nMessage('rfa.log_api_manager_list')}</a></li>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="rfaServiceLog" action="${ctx}/rfa/rfaServiceLog/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="col-sm-1 control-label">${fns:i18nMessage('rfa.rfa_url_id')}：</label>
				<div class="col-sm-2">
					<form:input path="urlId" htmlEscape="false" maxlength="50" class="form-control input-medium"/>
				</div>
				<label class="col-sm-1 control-label">${fns:i18nMessage('rfa.service_name')}：</label>
				<div class="col-sm-2">
					<form:input path="serviceName" htmlEscape="false" maxlength="255" class="form-control input-medium"/>
				</div>
				<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.service_request_address')}：</label>
				<div class="col-sm-2">
					<form:input path="url" htmlEscape="false" maxlength="255" class="form-control input-medium"/>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label">${fns:i18nMessage('common.status')}：</label>
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
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>${fns:i18nMessage('ica.id')}</th>
					<th>${fns:i18nMessage('rfa.rfa_url_id')}</th>
					<th>${fns:i18nMessage('rfa.service_code')}</th>
					<th>${fns:i18nMessage('rfa.service_name')}</th>
					<th>${fns:i18nMessage('ica.service_request_time')}</th>
					<th>${fns:i18nMessage('rfa.service_request_address')}</th>
					<th>${fns:i18nMessage('rfa.service_method')}</th>
					<th>${fns:i18nMessage('rfa.service_content_type')}</th>
					<th>${fns:i18nMessage('common.status')}</th>
					<th>ip</th>
					<shiro:hasPermission name="rfa:rfaServiceLog:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="rfaServiceLog">
				<tr>
					<td><a href="${ctx}/rfa/rfaServiceLog/form?id=${rfaServiceLog.id}">
						${rfaServiceLog.id}
					</a></td>
					<td>
						${rfaServiceLog.urlId}
					</td>
					<td>
						${rfaServiceLog.serviceCode}
					</td>
					<td>
						${rfaServiceLog.serviceName}
					</td>
					<td>
					<fmt:formatDate value="${rfaServiceLog.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${rfaServiceLog.url}
					</td>
					<td>
						${rfaServiceLog.method}
					</td>
					<td>
						${rfaServiceLog.contentType}
					</td>
					<td>
						${fns:getDictLabel(rfaServiceLog.isSuccess, 'ica_log_is_success', '')}
					</td>
					<td>
						${rfaServiceLog.ip}
					</td>
					<shiro:hasPermission name="rfa:rfaServiceLog:edit"><td>
	    				<a href="${ctx}/rfa/rfaServiceLog/form?id=${rfaServiceLog.id}">${fns:i18nMessage('common.modify')}</a>
						<a href="${ctx}/rfa/rfaServiceLog/delete?id=${rfaServiceLog.id}" onclick="return confirmx('${fns:i18nMessage('rfa.log_delete_confrim')}', this.href)">${fns:i18nMessage('common.delete')}</a>
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