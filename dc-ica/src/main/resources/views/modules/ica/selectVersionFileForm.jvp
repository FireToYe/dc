<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>适配器版本数据管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	var list = new Object();
		$(document).ready(function() {
			//$("#name").focus();s
			<c:forEach var="versionFile" items="${versionList }">
				var id = "${versionFile.id}";
				var jsObj = new Object();
				jsObj.id= "${versionFile.id}";
				jsObj.fileUrl= "${versionFile.fileUrl}";
				jsObj.name= "${versionFile.name}";
				jsObj.versionDesc= "${versionFile.versionDesc}";
				jsObj.updateDate= "${versionFile.updateDate}";
				jsObj.erpType= "${versionFile.erpType}";
				list["${versionFile.id}"] = jsObj;
			</c:forEach>
			var fielId = $("#versionId").val();
			var obj = list[fielId];
			$("#fileUrl").val(obj.fileUrl);
			$("#versionDesc").val(obj.versionDesc);
			$("#updateDate").val(obj.updateDate);
			$("#erpType").val(obj.erpType);
			$("#versionName").val(obj.name);
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
		
		function changeSelect(){
			var fielId = $("#versionId").val();
			var obj = list[fielId];
			$("#fileUrl").val(obj.fileUrl);
			$("#versionDesc").val(obj.versionDesc);
			$("#updateDate").val(obj.updateDate);
			$("#versionName").val(obj.name);
		}
		var callbackdata = function(){
			return $("#versionName").val();
		}
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<div class="tab-content">
	 <div class="tab-pane active">
	 <div class="panel-body ">
	 <form id="inputForm" modelAttribute="ossIcaVersion" action="${ctx}/version/ossIcaVersion/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>		
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.version_name')}：</label>
			<div class="col-sm-3">
				<!-- <input id="name" htmlEscape="false" maxlength="255" class="form-control input-xlarge " readonly="true"/> -->
				<select id="versionId" onchange="changeSelect()"  class="form-control input-logxxlarge ">
					<c:forEach var="versionFile" items="${versionList }">
						<option value="${versionFile.id }">${versionFile.name}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<input id="versionName" name="versionName" value="" style="display:none">
<!-- 		<div class="form-group">
			<label class="col-sm-2 control-label">版本地址：</label>
			<div class="col-sm-6">
				<input id="fileUrl" htmlEscape="false" maxlength="100" class="form-control input-logxxlarge  " readonly="true"/>
			</div>
		</div> -->
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.version_versionDesc')}：</label>
			<div class="col-sm-6">
				<textarea id="versionDesc" maxlength="500" class="form-control input-logxxlarge  " readonly="true"></textarea>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.version_erpType')}：</label>
			<div class="col-sm-6">
				<input id="erpType" htmlEscape="false" maxlength="500" class="form-control input-logxxlarge  " readonly="true"/>
			</div>
			</div>
<%-- 		</div>
			<div class="form-group">
			<label class="col-sm-2 control-label">创建时间：</label>
			<div class="col-sm-3">
				<input id="createDate" value="<fmt:formatDate value="${ossIcaVersion.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" htmlEscape="false" maxlength="500" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div> --%>
		
		<div class="form-group">
			<label class="col-sm-2 control-label">${fns:i18nMessage('ica.version_update_date')}：</label>
			<div class="col-sm-3">
				<input id="updateDate" value="<fmt:formatDate value="${ossIcaVersion.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" htmlEscape="false" maxlength="500" class="form-control input-xlarge " readonly="true"/>
			</div>
		</div>
	</form>
</body>
</html>