<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('ica.db_list')}</title>
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
					$("#messageBox").text("${fns:i18nMessage('common.form.error.msg')}");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});

		function testConn(){
			var iccnIcaDb = new Object();
			iccnIcaDb.dbName = $("#dbName").val();
			iccnIcaDb.dbType = $("#dbType").val();
			iccnIcaDb.dbVersion = $("#dbVersion").val();
			iccnIcaDb.jdbcDriverclassname = $("#jdbcDriverclassname").val();
			iccnIcaDb.jdbcUrl = $("#jdbcUrl").val();
			iccnIcaDb.jdbcUsername = $("#jdbcUsername").val();
			iccnIcaDb.jdbcPassword = $("#jdbcPassword").val();
			var index = layer.load(0, {shade: false});
			$.ajax({
				url:ctx+'/ica/iccnIcaDb/testConnection',
				type:'POST',
				data:JSON.stringify(iccnIcaDb),
				contentType:"application/json",
				timeout:5000,
				dataType:"json",
				success:function(msg){
					layer.close(index);
					layer.msg(msg.message);
				},
				error:function(msg){
					layer.close(index);
					layer.msg("${fns:i18nMessage('ica.db_connection_timeout')}");
				}
			})
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/ica/iccnIcaDb/">${fns:i18nMessage('ica.db_list')}</a></li>
		<li class="active"><a href="${ctx}/ica/iccnIcaDb/form?id=${iccnIcaDb.id}"><shiro:hasPermission name="ica:iccnIcaDb:edit"><c:choose><c:when test="${not empty iccnIcaDb.id}">${fns:i18nMessage('ica.db_modify')}</c:when><c:otherwise>${fns:i18nMessage('ica.db_add')}</c:otherwise></c:choose></shiro:hasPermission><shiro:lacksPermission name="ica:iccnIcaDb:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="iccnIcaDb" action="${ctx}/ica/iccnIcaDb/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.db_id')}：</label>
			<div class="col-sm-3">
				<form:input path="dbId" htmlEscape="false" maxlength="255" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.db_name')}：</label>
			<div class="col-sm-3">
				<form:input path="dbName" htmlEscape="false" maxlength="255" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.db_type')}：</label>
			<div class="col-sm-3">
				<form:select path="dbType" class="form-control input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('iccn_ica_db_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.db_version')}：</label>
			<div class="col-sm-3">
				<form:select path="dbVersion" class="form-control input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('iccn_ica_db_version')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.jdbc_driverclassname')}：</label>
			<div class="col-sm-3">
				<form:input path="jdbcDriverclassname" htmlEscape="false" maxlength="255" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.jdbc_url')}：</label>
			<div class="col-sm-3">
				<form:input path="jdbcUrl" htmlEscape="false" maxlength="255" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.jdbc_username')}：</label>
			<div class="col-sm-3">
				<form:input path="jdbcUsername" htmlEscape="false" maxlength="255" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.jdbc_password')}：</label>
			<div class="col-sm-3">
				<input type="password" id="jdbcPassword" name="jdbcPassword" value="${iccnIcaDb.jdbcPassword }" htmlEscape="false" maxlength="255" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.description')}：</label>
			<div class="col-sm-3">
				<form:input path="description" htmlEscape="false" maxlength="255" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			${fns:i18nMessage('ica.status')}：</label>
			<div class="col-sm-3">
				<form:select path="status" class="form-control input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('iccn_ica_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
         			<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.saves')}"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
			<input  onclick="testConn()" class="btn" type="button" value="${fns:i18nMessage('ica.test')}"/>
             </div>
        </div>
	</form:form>
	</div></div></div>
</body>
</html>