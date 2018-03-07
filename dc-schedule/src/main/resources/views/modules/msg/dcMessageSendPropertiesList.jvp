<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>信息发送配置管理</title>
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
		<li class="active"><a href="${ctx}/msg/dcMessageSendProperties/">信息发送配置列表</a></li>
		<shiro:hasPermission name="msg:dcMessageSendProperties:edit"><li><a href="${ctx}/msg/dcMessageSendProperties/form">信息发送配置添加</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="dcMessageSendProperties" action="${ctx}/msg/dcMessageSendProperties/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="col-sm-1 control-label">账号：</label>
				<div class="col-sm-2">
					<form:input path="accountNumber" htmlEscape="false" maxlength="32" class="form-control input-medium"/>
				</div>
				<label class="col-sm-1 control-label">类别：</label>
				<div class="col-sm-2">
					<form:select path="type" class="form-control input-xlarge">
						<form:option value="" label=""/>
		<%--				<form:option value="1" label="短信"/>--%>
						<form:option value="2" label="邮件"/>
					</form:select>
				</div>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
			</div>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>账号</th>
					<th>发送名</th>
					<th>是否默认</th>
					<th>类别</th>
					<th>更新时间</th>
					<th>备注信息</th>
					<shiro:hasPermission name="msg:dcMessageSendProperties:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="dcMessageSendProperties">
				<tr>
					<td><a href="${ctx}/msg/dcMessageSendProperties/form?id=${dcMessageSendProperties.id}">
						${dcMessageSendProperties.accountNumber}
					</a></td>
					<td>
							${dcMessageSendProperties.name}
					</td>
					<td>
						${fns:getDictLabel(dcMessageSendProperties.isDefault, 'srm_yes_no', '')}
					</td>
					<td>
						<c:if test="${dcMessageSendProperties.type eq '1'}">
							短信
						</c:if>
						<c:if test="${dcMessageSendProperties.type eq '2'}">
							邮件
						</c:if>
					</td>
					<td>
						<fmt:formatDate value="${dcMessageSendProperties.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						${dcMessageSendProperties.remarks}
					</td>
					<shiro:hasPermission name="msg:dcMessageSendProperties:edit"><td>
	    				<a href="${ctx}/msg/dcMessageSendProperties/form?id=${dcMessageSendProperties.id}">修改</a>
						<a href="javascript:void(0);" onclick="deleteAtId('确认要删除该信息发送配置吗？', '${ctx}/msg/dcMessageSendProperties/delete?id=${dcMessageSendProperties.id}')">删除</a>
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