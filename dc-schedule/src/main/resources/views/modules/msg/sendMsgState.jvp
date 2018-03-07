<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('ica.service_log_manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			if("true" == "${sendMsgStatus}"){
				$("#btnSubmit").val("${fns:i18nMessage('common.close')}");
			}else{
				$("#btnSubmit").val("${fns:i18nMessage('common.open')}");
			}
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="#">信息发送状态<shiro:hasPermission name="msg:dcMessageSendProperties:edit"></shiro:hasPermission><shiro:lacksPermission name="msg:dcMessageSendProperties:edit"></shiro:lacksPermission></a></li>
	</ul><br/>
	<form id="inputForm" action="${ctx}/msg/dcMessageSendLog/changeStateSave" method="post" class="form-horizontal">
		<sys:message content="${message}"/>		
		<input type="text" style="display: none;" id="sendMsgStatus" name="sendMsgStatus" value="${'true'.equals(sendMsgStatus)?'false':'true'}"/>
	<div class="form-group">
			<label class="col-sm-1 col-sm-offset-1 control-label">当前状态：</label>
			<label class="col-sm-1 control-label">${sendMsgStatusStr}</label>
		</div>
	<div class="form-group">
	<div class="col-sm-4 col-sm-offset-2">
			<shiro:hasPermission name="msg:dcMessageSendProperties:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value=""/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="${fns:i18nMessage('common.backs')}" onclick="history.go(-1)"/>
		</div>
		</div>
	</form>
</body>
</html>