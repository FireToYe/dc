<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>信息发送详情管理</title>
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
		<li class="active"><a href="${ctx}/msg/dcMessageSendLog/">信息发送详情列表</a></li>
		<shiro:hasPermission name="msg:dcMessageSendLog:edit"><li><a href="${ctx}/msg/dcMessageSendLog/form">信息发送详情添加</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="dcMessageSendLog" action="${ctx}/msg/dcMessageSendLog/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="col-sm-1 control-label">发送方地址：</label>
				<div class="col-sm-2">
					<form:select path="fromAddress" class="form-control input-xlarge ">
						<form:option value="" label=""/>
						<form:options items="${propertiesList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
					</form:select>
				</div>
				<label class="col-sm-1 control-label">接收方地址：</label>
				<div class="col-sm-2">
					<form:input path="toAddress" htmlEscape="false" maxlength="2000" class="form-control input-medium"/>
				</div>
				<label class="col-sm-1 control-label">类别：</label>
				<div class="col-sm-2">
					<form:select path="type" class="form-control input-xlarge">
						<form:option value="" label=""/>
						<%--<form:option value="1" label="短信"/>--%>
						<form:option value="2" label="邮件"/>
					</form:select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label">发送状态：</label>
				<div class="col-sm-2">
					<form:select path="status" class="form-control input-xlarge">
						<form:option value="" label=""/>
						<form:option value="0" label="待发送"/>
						<form:option value="1" label="发送成功"/>
						<form:option value="2" label="发送失败"/>
					</form:select>
				</div>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" />
			</div>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>发送方名</th>
					<th>接收方地址</th>
					<th>类别:</th>
					<th>发送主题</th>
					<th>发送状态</th>
					<th>更新时间</th>
					<shiro:hasPermission name="msg:dcMessageSendLog:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="dcMessageSendLog">
				<tr>
					<td>
						${dcMessageSendLog.toMailName}
					</td>
					<td>
						${dcMessageSendLog.toAddress}
					</td>
					<td>
						<c:if test="${dcMessageSendLog.type eq '1'}">
							短信
						</c:if>
						<c:if test="${dcMessageSendLog.type eq '2'}">
							邮件
						</c:if>
					</td>
					<td>
						${dcMessageSendLog.subject}
					</td>
					<td>
						<c:if test="${dcMessageSendLog.status eq '0'}">
							待发送
						</c:if>
						<c:if test="${dcMessageSendLog.status eq '1'}">
							发送成功
						</c:if>
						<c:if test="${dcMessageSendLog.status eq '2'}">
							发送失败
						</c:if>
					</td>
					<td>
						<fmt:formatDate value="${dcMessageSendLog.sendDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<shiro:hasPermission name="msg:dcMessageSendLog:edit"><td>
	    				<a href="${ctx}/msg/dcMessageSendLog/form?id=${dcMessageSendLog.id}">修改</a>
						<a href="javascript:void(0);" onclick="deleteAtId('确认要删除该信息发送详情吗？', '${ctx}/msg/dcMessageSendLog/delete?id=${dcMessageSendLog.id}')">删除</a>
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