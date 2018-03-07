<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('ica.service_log_manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		    $('#datepicker').datepicker({
                keyboardNavigation: false,
                forceParse: false,
                autoclose: true,
				todayBtn: "linked"
            });
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
				 btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"]  //按钮
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
		<li class="active"><a href="${ctx}/ica/icaApiLog/">${fns:i18nMessage('ica.service_log_list')}</a></li>
<%-- 		<shiro:hasPermission name="ica:icaApiLog:edit"><li><a href="${ctx}/ica/icaApiLog/form">日志添加</a></li></shiro:hasPermission>
 --%>	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="icaApiLog" action="${ctx}/ica/icaApiLog/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="col-sm-2 control-label">${fns:i18nMessage('ica.service_code')}：</label>
				<div class="col-sm-2">
					<form:input path="serviceCode" htmlEscape="false" maxlength="255" class="form-control input-medium"/>
				</div>
				<label class="col-sm-2 control-label">${fns:i18nMessage('ica.service_name')}：</label>
				<div class="col-sm-2">
					<form:input path="serviceName" htmlEscape="false" maxlength="255" class="form-control input-medium"/>
				</div>
				<label class="col-sm-1 control-label"> ${fns:i18nMessage('ica.log_status')}：</label>
				<div class="col-sm-2">
					<form:select path="isSuccess" class="form-control input-medium">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('ica_log_is_success')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				</div>
				<div class="form-group">
						<label class="col-sm-2 control-label">${fns:i18nMessage('ica.request_time')}：</label>
						<div class="col-sm-6">
							<div class="input-daterange input-group" id="datepicker"> 
			                 <form:input path="beginCreateTime" type="text"  readonly="readonly" maxlength="20"  class="input-medium required  form-control" name="start"/>
			                 <span class="input-group-addon">-</span>
			                 <form:input path="endCreateTime" type="text"  readonly="readonly" maxlength="20"  class="input-medium required  form-control" name="end"/>
		             		</div>
	             		</div>
				
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" />
				</div>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>${fns:i18nMessage('ica.id')}</th>
					<th>${fns:i18nMessage('ica.service_code')}</th>
					<th>${fns:i18nMessage('ica.db_id')}</th>
					<th>${fns:i18nMessage('ica.service_name')}</th>
					<th>${fns:i18nMessage('ica.slog_request_address')}</th>
					<th>${fns:i18nMessage('ica.log_response_time')}</th>
					<th>${fns:i18nMessage('ica.log_status ')}</th>
					<th>${fns:i18nMessage('ica.request_time')}</th>
					<shiro:hasPermission name="ica:icaApiLog:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="icaApiLog">
				<tr>
					<td><a href="${ctx}/ica/icaApiLog/form?id=${icaApiLog.id}">
						${icaApiLog.id}
					</a></td>
					<td>
						${icaApiLog.serviceCode}
					</td>
					<td>
						${icaApiLog.dbId}
					</td>
					<td>
						${icaApiLog.serviceName}
					</td>
					<td>
						${icaApiLog.ip}
					</td>
					<td>
						${icaApiLog.resDuration}
					</td>
					<td>
						${fns:getDictLabel(icaApiLog.isSuccess, 'ica_log_is_success', '')}
					</td>
					<td>
					<fmt:formatDate value="${icaApiLog.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<shiro:hasPermission name="ica:icaApiLog:edit"><td>
<%-- 	    				<a href="${ctx}/ica/icaApiLog/form?id=${icaApiLog.id}">修改</a> --%>						
<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('ica.log_delete_confrim')}', '${ctx}/ica/icaApiLog/delete?id=${icaApiLog.id}')">${fns:i18nMessage('common.delete')}</a>	</td></shiro:hasPermission>
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