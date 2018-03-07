<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>rfa系统服务管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#btnExport").click(function(){
			layer.confirm("${fns:i18nMessage('rfa.service_export_manager_confrim')}", {
				offset: 't' ,
				 btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"]  //按钮
				},function(){
					$("#searchForm").attr("action","${ctx}/rfa/rfaService/export");
					$("#searchForm").submit();
					layer.closeAll('dialog');
				});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		});
	});
	function page(n,s){
		if(n) $("#pageNo").val(n);
		if(s) $("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/rfa/rfaService/");
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
	
	function importVersion(){
		layer.open({
			  type: 2,
			  title: "${fns:i18nMessage('ica.version_import_select')}",
			  shadeClose: true,
			  shade: 0.8,
			  area: ['90%', '90%'],
			  content: '${ctx}/rfa/rfaService/slectVersionFile', //iframe的url
			  btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"], //按钮
               yes: function(index){
            	   var load = layer.load(0, {shade: false});
                        //当点击‘确定’按钮的时候，获取弹出层返回的值
                        var res = window["layui-layer-iframe" + index].callbackdata();
                        //打印返回的值，看是否有我们想返回的值。
                        console.log(res);
                        //最后关闭弹出层
                        layer.close(index);
                        
                         $.ajax({
                        	url :'${ctx}/rfa/rfaService/importVersion?selectVersion='+res,
                        	type:'post',
                        	timeout:600000,
                        	success:function(body){
                        		layer.close(load);
                        		layer.alert(body,function(){history.go(0);});
                        		
                        	},
                        	error:function(msg){
                        		layer.close(load);
                        		console.log(msg);
                        		layer.alert("${fns:i18nMessage('ica.version_import_error_tip')}", {
                        			  icon: 5,
                        			  skin: 'layer-ext-moon' //该皮肤由layer.seaning.com友情扩展。关于皮肤的扩展规则，去这里查阅
                        			})
                        	}
                        }); 
                       	
                    },
                    cancel: function(){
                        //右上角关闭回调
                    }
			}); 
		

}
	function importNew(){
		layer.confirm("${fns:i18nMessage('ica.version_import_new')}", {
			offset: 't' ,
			 btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"]  //按钮
			}, function(index){
				layer.close(index);
				var load = layer.load(0, {shade: false});
				  $.ajax({
		            	url :'${ctx}/rfa/rfaService/importVersion',
		            	type:'post',
		            	timeout:600000,
		            	success:function(body){
		            		layer.close(load);
		            		layer.alert(body,function(){history.go(0);});
		            		
		            	},
		            	error:function(msg){
		            		layer.close(load);
		            		console.log(msg);
		            		layer.alert("${fns:i18nMessage('ica.version_import_error_tip')}", {
		            			  icon: 5,
		            			  skin: 'layer-ext-moon' //该皮肤由layer.seaning.com友情扩展。关于皮肤的扩展规则，去这里查阅
		            			})
		            	}
		            }); 
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
				<form id="importForm" action="${ctx}/rfa/rfaService/import" method="post" enctype="multipart/form-data"
					class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('${fns:i18nMessage('ica.import_now_tip')}...');"><br/>
					<div class="form-group">
						<input id="uploadFile" name="file" type="file"  class="form-control btn-primary" />　
					
					</div>
					<div class="form-group">
					<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.import')} "/>
					<a href="${ctx}/rfa/rfaService/import/template">${fns:i18nMessage('import_template')}</a>	${fns:i18nMessage('ica.import_tip')}
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
		<li class="active"><a href="${ctx}/rfa/rfaService/">${fns:i18nMessage('rfa.service_connection_manager_list')}</a></li>
		<shiro:hasPermission name="rfa:rfaService:edit"><li><a href="${ctx}/rfa/rfaService/form">${fns:i18nMessage('rfa.service_connection_manager_add')}</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="rfaService" action="${ctx}/rfa/rfaService/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="col-sm-1 control-label">${fns:i18nMessage('rfa.service_code')}：</label>
				<div class="col-sm-2">
					<form:input path="serviceCode" htmlEscape="false" maxlength="255" class="form-control input-medium"/>
				</div>
				<label class="col-sm-1 control-label">${fns:i18nMessage('rfa.service_name')}：</label>
				<div class="col-sm-2">
					<form:input path="serviceName" htmlEscape="false" maxlength="255" class="form-control input-medium"/>
				</div>
				<label class="col-sm-1 control-label">${fns:i18nMessage('rfa.service_content_type')}：</label>
				<div class="col-sm-2">
					<form:select path="contentType" class="form-control input-medium">
						<form:option value="" label=""/>
						<form:option value="json" label="json"/>
						<form:option value="xml" label="xml"/>
					</form:select>
				</div>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" onclick="return page(false,false);"/>
				<input id="btnExport" class="btn btn-primary" type="button" value="${fns:i18nMessage('common.export')}"/>
				<button type="button" id="btnImport" data-target="#importBox" data-toggle="modal"  class="btn btn-primary">${fns:i18nMessage('common.import')}</button>		
				<input id="btnchooseVersion" onclick="importVersion()" class="btn btn-primary" type="button" value="导入版本"/>	
				<input id="btnupdateVersion" class="btn btn-primary"  onclick="importNew()"  type="button" value="导入最新"/>	
				</div>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>${fns:i18nMessage('rfa.rfa_url_id')}</th>
					<th>${fns:i18nMessage('rfa.service_code')}</th>
					<th>${fns:i18nMessage('rfa.service_name')}</th>
					<th>${fns:i18nMessage('ica.description')}</th>
					<th>${fns:i18nMessage('rfa.service_content_type')}</th>
					<th>${fns:i18nMessage('ica.status')}</th>
					<th>${fns:i18nMessage('ica.force_update')}</th>
					<th>${fns:i18nMessage('ica.custom_version')}</th>
					<shiro:hasPermission name="rfa:rfaService:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="rfaService">
				<tr>
					<td>
						${rfaService.urlId}
					</td>
					<td><a href="${ctx}/rfa/rfaService/form?id=${rfaService.id}">
						${rfaService.serviceCode}
					</a></td>
					<td>
						${rfaService.serviceName}
					</td>
					<td>
						${rfaService.description}
					</td>
					<td>
						${rfaService.contentType}
					</td>
					<td>
						${fns:getDictLabel(rfaService.status, 'iccn_ica_status', '')}
					</td>
					<td>
						${fns:getDictLabel(rfaService.forceUpdate, 'srm_yes_no', '')}
					</td>
					<td>
						${rfaService.customVersion}
					</td>
					<shiro:hasPermission name="rfa:rfaService:edit"><td>
	    				<a href="${ctx}/rfa/rfaService/form?id=${rfaService.id}">${fns:i18nMessage('common.modify')}</a>	
	    				<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('rfa.service_delete_confrim')}', '${ctx}/rfa/rfaService/delete?id=${rfaService.id}')">${fns:i18nMessage('common.delete')}</a>
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