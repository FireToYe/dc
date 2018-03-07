<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('rfa.url_connection_manager')}</title>
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
		<li><a href="${ctx}/rfa/rfaUrl/">${fns:i18nMessage('rfa.url_connection_manager_list')}</a></li>
		<li class="active"><a href="${ctx}/rfa/rfaUrl/form?id=${rfaUrl.id}">${fns:i18nMessage('rfa.url_connection_manager')}<shiro:hasPermission name="rfa:rfaUrl:edit"><c:choose><c:when test="${not empty rfaUrl.id}">${fns:i18nMessage('common.modify')}</c:when><c:otherwise>${fns:i18nMessage('common.add')}</c:otherwise></c:choose></shiro:hasPermission><shiro:lacksPermission name="rfa:rfaUrl:edit">${fns:i18nMessage('common.view')}</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="rfaUrl" action="${ctx}/rfa/rfaUrl/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.rfa_url_id')}：</label>
			<div class="col-sm-3">
				<form:input path="urlId" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('rfa.url')}：</label>
			<div class="col-sm-3">
				<form:input path="url" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.description')}：</label>
			<div class="col-sm-3">
				<form:input path="description" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
         			<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
             </div>
        </div>
	</form:form>
</body>
</html>