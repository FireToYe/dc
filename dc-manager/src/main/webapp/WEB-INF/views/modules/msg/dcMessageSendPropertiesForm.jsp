<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>信息发送配置管理</title>
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

        function testSendMsg(){
            layer.prompt({title: "请输入测试邮箱或测试手机号"}, function(pass, index){
                var dcMessageSendProperties = new Object();
                dcMessageSendProperties.accountNumber = $("#accountNumber").val();
                dcMessageSendProperties.password = $("#password").val();
                dcMessageSendProperties.type = $("#type").val();
                dcMessageSendProperties.smtp = $("#smtp").val();
                var email =$("#email").val();
                if(email){
                    email= $("#accountNumber").val();
				}
                dcMessageSendProperties.email = email;
                dcMessageSendProperties.name =$("#name").val();
                dcMessageSendProperties.toEmailTest = pass;
                $.ajax({
                    url:ctx+'/msg/dcMessageSendProperties/testSend',
                    type:"post",
                    data:JSON.stringify(dcMessageSendProperties),
                    contentType:"application/json",
                    timeout:5000,
                    dataType:"json",
                    success:function(msg){
                        layer.close(index);
                        layer.msg(msg.body.message);
                    },
                    error:function(msg){
                        layer.close(index);
                    }
                });
            });
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/msg/dcMessageSendProperties/">信息发送配置列表</a></li>
		<li class="active"><a href="${ctx}/msg/dcMessageSendProperties/form?id=${dcMessageSendProperties.id}">信息发送配置<shiro:hasPermission name="msg:dcMessageSendProperties:edit">${not empty dcMessageSendProperties.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="msg:dcMessageSendProperties:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	<form:form id="inputForm" modelAttribute="dcMessageSendProperties" action="${ctx}/msg/dcMessageSendProperties/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			账号：</label>
			<div class="col-sm-3">
				<form:input path="accountNumber" htmlEscape="false" maxlength="32" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
			密码：</label>
			<div class="col-sm-3">
				<input id="password" name="password" value="${dcMessageSendProperties.password}" type="password"  maxlength="64" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label"><span class="help-inline"><font color="red">*</font></span>
				发送名：</label>
			<div class="col-sm-3">
				<form:input path="name" htmlEscape="false" maxlength="32" class="form-control input-xlarge required"/>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">
				email：</label>
			<div class="col-sm-3">
				<form:input path="email" htmlEscape="false" maxlength="32" class="form-control input-xlarge"/>email不填时，会取账号作为email地址
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">是否默认：</label>
			<div class="col-sm-3">
				<form:select path="isDefault" class="form-control input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('srm_yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">类别：</label>
			<div class="col-sm-3">
				<form:select path="type" class="form-control input-xlarge ">
<%--					<form:option value="1" label="短信"/>--%>
					<form:option value="2" label="邮件"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">smtp地址：</label>
			<div class="col-sm-3">
				<form:input path="smtp" htmlEscape="false" maxlength="64" class="form-control input-xlarge "/>短信时，该项失效，如果不填将会根据邮箱地址自动选择
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">短信请求接口地址：</label>
			<div class="col-sm-3">
				<form:input path="smsUrl" htmlEscape="false" maxlength="64" class="form-control input-xlarge "/>邮件时，该项失效
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">备注信息：</label>
			<div class="col-sm-3">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control input-xxlarge "/>
			</div>
		</div>
		<div class="form-group">
             <div class="col-sm-4 col-sm-offset-2">
         			<shiro:hasPermission name="sys:menu:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
				 <input id="testSend" class="btn" type="button" value="测试发送" onclick="testSendMsg()"/>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
             </div>
        </div>
	</form:form>
</body>
</html>