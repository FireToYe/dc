<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>rfa连接管理管理${fns:i18nMessage('rfa.url_connection_manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				layer.confirm("${fns:i18nMessage('rfa.url_export_manager_confrim')}", {
					offset: 't' ,
					 btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"]  //按钮
					},function(){
						$("#searchForm").attr("action","${ctx}/rfa/rfaUrl//export");
						$("#searchForm").submit();
						layer.closeAll('dialog');
					});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/rfa/rfaUrl/");
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
	<div class="modal inmodal fade" id="importBox" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">${fns:i18nMessage('ica.import')}</h4>
            </div>
            <div class="modal-body modal-office-body">	
				<form id="importForm" action="${ctx}/rfa/rfaUrl/import" method="post" enctype="multipart/form-data"
					class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('${fns:i18nMessage('common.loading')}...');"><br/>
					<div class="form-group">
						<input id="uploadFile" name="file" type="file"  class="form-control btn-primary" />　
					
					</div>
					<div class="form-group">
					<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.import')}"/>
					<a href="${ctx}/rfa/rfaUrl/import/template">${fns:i18nMessage('ica.import_template')}</a>	${fns:i18nMessage('ica.import_tip')}
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
		<li class="active"><a href="${ctx}/rfa/rfaUrl/">${fns:i18nMessage('rfa.url_connection_manager_list')}</a></li>
		<shiro:hasPermission name="rfa:rfaUrl:edit"><li><a href="${ctx}/rfa/rfaUrl/form">${fns:i18nMessage('rfa.url_connection_manager_add')}</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="rfaUrl" action="${ctx}/rfa/rfaUrl/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="col-sm-1 control-label">${fns:i18nMessage('rfa.url')}：</label>
				<div class="col-sm-2">
					<form:input path="url" htmlEscape="false" maxlength="255" class="form-control input-medium"/>
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
					<th>${fns:i18nMessage('rfa.rfa_url_id')}</th>
					<th>${fns:i18nMessage('rfa.url')}</th>
					<th>${fns:i18nMessage('ica.description')}</th>
					<shiro:hasPermission name="rfa:rfaUrl:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="rfaUrl">
				<tr>
					<td><a href="${ctx}/rfa/rfaUrl/form?id=${rfaUrl.id}">
						${rfaUrl.urlId}
					</a></td>
					<td>
						${rfaUrl.url}
					</td>
					<td>
						${rfaUrl.description}
					</td>
					<shiro:hasPermission name="rfa:rfaUrl:edit"><td>
	    				<a href="${ctx}/rfa/rfaUrl/form?id=${rfaUrl.id}">${fns:i18nMessage('common.modify')}</a>
	    										<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('rfa.url_delete_confrim')}', '${ctx}/rfa/rfaUrl/delete?id=${rfaUrl.id}')">${fns:i18nMessage('common.delete')}</a>
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