<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>信息发送详情管理</title>
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

		function sendSubmit(){
            $("#inputForm").attr("action","${ctx}/msg/dcMessageSendLog/sendSave");
            $("#inputForm").submit();
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/msg/dcMessageSendLog/">信息发送详情列表</a></li>
		<li class="active"><a href="${ctx}/msg/dcMessageSendLog/form?id=${dcMessageSendLog.id}">信息发送详情<shiro:hasPermission name="msg:dcMessageSendLog:edit">${not empty dcMessageSendLog.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="msg:dcMessageSendLog:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="dcMessageSendLog" action="${ctx}/msg/dcMessageSendLog/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label">
			发送方地址：</label>
			<div class="col-sm-3">
				<form:select path="fromAddress" class="form-control input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${propertiesList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			接收方地址：</label>
			<div class="col-sm-3">
				<form:input path="toAddress" htmlEscape="false" maxlength="2000" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">发送内容：</label>
			<div class="col-sm-3">
				<form:textarea path="content"  rows="4" class="form-control input-xxlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">类别：</label>
			<div class="col-sm-3">
				<form:select path="type" class="form-control input-xlarge ">
					<form:option value="2" label="邮件"/>
	<%--				<form:option value="1" label="短信"/>--%>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">发送主题：</label>
			<div class="col-sm-3">
				<form:input path="subject"  maxlength="64" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">抄送人地址：</label>
			<div class="col-sm-3">
				<form:input path="ccAddress" htmlEscape="false" maxlength="2000" class="form-control input-xlarge "/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">发送状态：</label>
			<div class="col-sm-3">
				<c:if test="${dcMessageSendLog.status eq '0'}">
					<input value="待发送" htmlEscape="false" maxlength="2000" class="form-control input-xlarge " readonly="true"/>
				</c:if>
				<c:if test="${dcMessageSendLog.status eq '1'}">

					<input value="发送成功" htmlEscape="false" maxlength="2000" class="form-control input-xlarge " readonly="true"/>
				</c:if>
				<c:if test="${dcMessageSendLog.status eq '2'}">
					<input value="发送失败" htmlEscape="false" maxlength="2000" class="form-control input-xlarge " readonly="true"/>
				</c:if>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">失败原因：</label>
			<div class="col-sm-3">
				<form:textarea path="exception" htmlEscape="false" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">发送时间：</label>
			<div class="col-sm-3">
				<input id="sendDate" name="sendDate" value="<fmt:formatDate value="${dcMessageSendLog.sendDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" htmlEscape="false" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>

		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
         			<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				 <input id="send" class="btn" type="button" value="发送保存" onclick="sendSubmit()"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
             </div>
        </div>
	</form:form>
</body>
</html>