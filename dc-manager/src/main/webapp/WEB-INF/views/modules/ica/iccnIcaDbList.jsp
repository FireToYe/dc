<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('ica.db_list')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				layer.confirm("${fns:i18nMessage('ica.db.export.tip')}", {
					offset: 't' ,
					  btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"] //按钮
					},function(){
						$("#searchForm").attr("action","${ctx}/ica/iccnIcaDb/export");
						$("#searchForm").submit();
						layer.closeAll('dialog');
					});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/ica/iccnIcaDb/");
			$("#searchForm").submit();
	    	return false;
	    }

		function testConn(id){
			var index = layer.load(0, {shade: false});
			$.ajax({
				url:ctx+'/ica/iccnIcaDb/testConn?id='+id,
				type:'POST',
				data:id,
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
		function deleteAtId(tip,href){
			layer.confirm("${fns:i18nMessage('ica.db_delete_confrim')}", {
				offset: 't' ,
				 btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"]  //按钮
				}, function(){
					$("#searchForm").attr("action",href);
					$("#searchForm").submit();
				});
		}
/* 		function exportExcel(){
			window.location.href = ctx+'/ica/iccnIcaDb/export?dbId='+$("#dbId").val();
		} */
	</script>
</head>
<body>
	<div class="modal inmodal fade" id="importBox" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">${fns:i18nMessage('ica.import_template')}</h4>
            </div>
            <div class="modal-body modal-office-body">	
				<form id="importForm" action="${ctx}/ica/iccnIcaDb/import" method="post" enctype="multipart/form-data"
					class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('${fns:i18nMessage('ica.import_now_tip')}');"><br/>
					<div class="form-group">
						<input id="uploadFile" name="file" type="file"  class="form-control btn-primary" />　
					
					</div>
					<div class="form-group">
					<input id="btnImportSubmit" class="btn btn-primary" type="submit" value=" ${fns:i18nMessage('common.import')}  "/>
					<a href="${ctx}/ica/iccnIcaDb/import/template">${fns:i18nMessage('ica.import_template')}</a>${fns:i18nMessage('ica.import_tip')}
					</div>
				</form>
			</div> 
            <div class="modal-footer">
            </div>
        </div>
      </div>
 	</div>
<div class="wrapper wrapper-content">
	<div class="tabs-container">
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/ica/iccnIcaDb/">${fns:i18nMessage('ica.db_list')}</a></li>
		<shiro:hasPermission name="ica:iccnIcaDb:edit"><li><a href="${ctx}/ica/iccnIcaDb/form">${fns:i18nMessage('ica.db_add')}</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="iccnIcaDb" action="${ctx}/ica/iccnIcaDb/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="col-sm-2 control-label">${fns:i18nMessage('ica.db_id')}：</label>
				<div class="col-sm-2">
					<form:input path="dbId" htmlEscape="false" maxlength="255" class="form-control input-medium"/>
				</div>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" onclick="return page(false,false);"/>
				<input id="btnExport" class="btn btn-primary" type="button" value="${fns:i18nMessage('common.export')}"/>
				<button type="button" id="btnImport" data-target="#importBox" data-toggle="modal"  class="btn btn-primary">${fns:i18nMessage('common.import')}</button>		
			</div>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>${fns:i18nMessage('ica.db_id')}</th>
					<th>${fns:i18nMessage('ica.db_name')}</th>
					<th>${fns:i18nMessage('ica.db_type')}</th>
					<th>${fns:i18nMessage('ica.db_version')}</th>
<!-- 					<th>数据库驱动</th> -->
					<th>${fns:i18nMessage('ica.jdbc_url')}</th>
					<th>${fns:i18nMessage('ica.jdbc_username')}</th>
				<!-- 	<th>数据库密码</th> -->
					<th>${fns:i18nMessage('ica.description')}</th>
					<th>${fns:i18nMessage('ica.status')}</th>
<!-- 					<th>创建时间</th>
					<th>更新时间</th> -->
					<shiro:hasPermission name="ica:iccnIcaDb:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="iccnIcaDb">
				<tr>
					<td><a href="${ctx}/ica/iccnIcaDb/form?id=${iccnIcaDb.id}">
						${iccnIcaDb.dbId}
					</a></td>
					<td>
						${iccnIcaDb.dbName}
					</td>
					<td>
						${fns:getDictLabel(iccnIcaDb.dbType, 'iccn_ica_db_type', '')}
					</td>
					<td>
						${fns:getDictLabel(iccnIcaDb.dbVersion, 'iccn_ica_db_version', '')}
					</td>
<%-- 					<td>
						${iccnIcaDb.jdbcDriverclassname}
					</td> --%>
					<td>
						${iccnIcaDb.jdbcUrl}
					</td>
					<td>
						${iccnIcaDb.jdbcUsername}
					</td>
<%-- 					<td>
						${iccnIcaDb.jdbcPassword}
					</td> --%>
					<td>
						${iccnIcaDb.description}
					</td>
					<td>
						${fns:getDictLabel(iccnIcaDb.status, 'iccn_ica_status', '')}
					</td>
	<%-- 				<td>
						<fmt:formatDate value="${iccnIcaDb.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<fmt:formatDate value="${iccnIcaDb.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td> --%>
					<shiro:hasPermission name="ica:iccnIcaDb:edit"><td>
	    				<a href="${ctx}/ica/iccnIcaDb/form?id=${iccnIcaDb.id}">${fns:i18nMessage('common.modify')}</a>
	    				<a href="javascript:void(0);" onclick="deleteAtId('ica.db.delete.confrim', '${ctx}/ica/iccnIcaDb/delete?id=${iccnIcaDb.id}')">${fns:i18nMessage('common.delete')}</a>
						<%-- <a href="${ctx}/ica/iccnIcaDb/delete?id=${iccnIcaDb.id}" onclick="return confirmx('确认要删除该数据库连接管理吗？', this.href)">删除</a> --%>
						<a  onclick="testConn(${iccnIcaDb.id})" style="cursor:pointer;">${fns:i18nMessage('ica.test')}</a>
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