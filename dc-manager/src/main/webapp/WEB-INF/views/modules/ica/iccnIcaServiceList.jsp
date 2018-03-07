<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:i18nMessage('ica.service_manager')}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var dataArray = new Array();
		$(document).ready(function() {
				if("${dataArray}"){
					console.log("${dataArray}");
					var jsonArr = '${dataArray}';
					dataArray = eval("("+jsonArr+")");
					for(var i=0;i<dataArray.length;i++){
						$("#selectOrgButton_"+dataArray[i]).attr("checked","true");
					}
				}
				$("#btnChooseExport").click(function(){
					layer.confirm("${fns:i18nMessage('ica.service.export.tip')}", {
						offset: 't' ,
						 btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"]  //按钮
						},function(){
							if(!dataArray.length>0){
								layer.msg("${fns:i18nMessage('ica.service_choose_export_none')}");
								return;
							}
							$("#searchForm").attr("action","${ctx}/ica/iccnIcaService/exportByChoose");
							var str = JSON.stringify(dataArray);
							$("#dataArray").val(str);
							$("#searchForm").submit();
							layer.closeAll('dialog');
						});
					top.$('.jbox-body .jbox-icon').css('top','55px');
				});
				$("#btnExport").click(function(){
					layer.confirm("${fns:i18nMessage('ica.service.export.tip')}", {
						offset: 't' ,
						 btn: ["${fns:i18nMessage('common.confirm')}","${fns:i18nMessage('common.cancel')}"]  //按钮
						},function(){
							$("#searchForm").attr("action","${ctx}/ica/iccnIcaService/export");
							$("#searchForm").submit();
							layer.closeAll('dialog');
						});
					top.$('.jbox-body .jbox-icon').css('top','55px');
				});
		});
		function page(n,s){
			var str = JSON.stringify(dataArray);
			$("#searchForm").attr("action","${ctx}/ica/iccnIcaService/");
			$("#dataArray").val(str);
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		function test(){
			var a = '{"std_data": {"parameter": {}}}'
			$.ajax({
				type:'post',
				url:ctx+'/ica/api/service/test',
				contentType:"application/json",
				data:a,
			dataType:"json",
			success:function(msg){
				layer.msg(JSON.stringify(msg));
			},
			error:function(e){
			}
			});
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
		
		function allChoose(element){
			if(element.checked){
				$("input:checkbox[name=selectOrgButton]:not(:checked)").each(function () {
		            $(this).trigger('click');
		        })
			}else{
				$("input:checkbox[name=selectOrgButton]:checked").each(function () {
		            $(this).trigger('click');
		        })
			}
		}
		
		function choose(element,id){
			var bean = new Object();
			 bean = id;
			if(element.checked){
				 if(!contains(dataArray,bean)){
		                dataArray.push(bean);
		            }
			}else{
				$("#allCheck").attr("checked", false);
				 if(contains(dataArray,bean)){
		                removeByValue(bean);
		            }
			}
			console.log(dataArray);
		}
		function importVersion(){
			layer.open({
				  type: 2,
				  title: "${fns:i18nMessage('ica.version_import_select')}",
				  shadeClose: true,
				  shade: 0.8,
				  area: ['90%', '90%'],
				  content: '${ctx}/ica/iccnIcaService/slectVersionFile', //iframe的url
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
	                        	url :'${ctx}/ica/iccnIcaService/importVersion?selectVersion='+res,
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
			            	url :'${ctx}/ica/iccnIcaService/importVersion',
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
	    function contains(arr, obj) {
	        var i = arr.length;
	        while (i--) {
	            if (arr[i] === obj) {
	                return true;
	            }
	        }
	        return false;
	    }
	    function removeByValue(dx)
	    {
	        for(var i=0,n=0;i<dataArray.length;i++)
	        {
	            if(this.dataArray[i]!=dx)
	            {
	                dataArray[n++]=dataArray[i]
	            }
	        }
	        dataArray.length-=1
	    }
	</script>
</head>
<body>
	<div class="modal inmodal fade" id="importBox" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">"${fns:i18nMessage('ica.import')}"</h4>
            </div>
            <div class="modal-body modal-office-body">	
				<form id="importForm" action="${ctx}/ica/iccnIcaService/import" method="post" enctype="multipart/form-data"
					class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('${fns:i18nMessage('ica.import_now_tip')}');"><br/>
					<div class="form-group">
						<input id="uploadFile" name="file" type="file"  class="form-control btn-primary" />　
					
					</div>
					<div class="form-group">
					<input id="btnImportSubmit" class="btn btn-primary" type="submit" value=" ${fns:i18nMessage('common.import')} "/>
	<a href="${ctx}/ica/iccnIcaDb/import/template">${fns:i18nMessage('ica.import_template')}</a>${fns:i18nMessage('ica.import_tip')}					</div>
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
		<li class="active"><a href="${ctx}/ica/iccnIcaService/">${fns:i18nMessage('ica.service_list')}</a></li>
		<shiro:hasPermission name="ica:iccnIcaService:edit"><li><a href="${ctx}/ica/iccnIcaService/form">${fns:i18nMessage('ica.service_manager')}${fns:i18nMessage('common.add')}</a></li></shiro:hasPermission>
	</ul>
	<div class="tab-content">
	<div class="tab-pane active">
	<div class="panel-body">
		<form:form id="searchForm" modelAttribute="iccnIcaService" action="${ctx}/ica/iccnIcaService/" method="post" class="breadcrumb form-search form-horizontal">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div class="form-group">
				<label class="col-sm-1 control-label">${fns:i18nMessage('ica.service_code')}:</label>
				<div class="col-sm-2">
					<form:input path="serviceCode" htmlEscape="false" maxlength="255" class="form-control input-medium"/>
				</div>
				<label class="col-sm-1 control-label">${fns:i18nMessage('ica.service_name')}:</label>
				<div class="col-sm-2">
					<form:input path="serviceName" htmlEscape="false" maxlength="255" class="form-control input-medium"/>
				</div>
			<label class="col-sm-1 control-label">${fns:i18nMessage('ica.service_group')}:</label>
			<div class="col-sm-2">
				<form:select path="serviceGroup" class="form-control input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('iccn_ica_service_group')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			</div>
			<div class="form-group">
				<div class="col-sm-1"></div>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="${fns:i18nMessage('common.query')}" onclick="return page(false,false);"/>
				<input id="btnExport" class="btn btn-primary" type="button" value="${fns:i18nMessage('common.export')}"/>
				<input id="btnChooseExport" class="btn btn-primary" type="button" value="${fns:i18nMessage('ica.servoce_choose_export')}"/>
				<button type="button" id="btnImport" data-target="#importBox" data-toggle="modal"  class="btn btn-primary">${fns:i18nMessage('common.import')}</button>
				<input id="btnchooseVersion" onclick="importVersion()" class="btn btn-primary" type="button" value="导入版本"/>	
				<input id="btnupdateVersion" class="btn btn-primary"  onclick="importNew()"  type="button" value="导入最新"/>	
			</div>
			<textarea style="display:none" id = "dataArray" name="dataArray"></textarea>
		</form:form>
		<sys:message content="${message}"/>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
				<th><input id="allCheck" type="checkbox" onclick="allChoose(this)"></th>
					<th>${fns:i18nMessage('ica.db_id')}</th>
					<th>${fns:i18nMessage('ica.service_code')}</th>
					<th>${fns:i18nMessage('ica.service_name')}</th>
					<!-- <th>服务参数</th>
					<th>数据库sql</th>
					<th>返回模板</th> -->
					<th>${fns:i18nMessage('ica.service_group')}</th>
					<th>${fns:i18nMessage('ica.description')}</th>
					<th>${fns:i18nMessage('ica.status')}</th>
					<th>${fns:i18nMessage('ica.force_update')}</th>
					<th>${fns:i18nMessage('ica.custom_version')}</th>
				<!-- 	<th>创建时间</th>
					<th>更新时间</th> -->
					<shiro:hasPermission name="ica:iccnIcaService:edit"><th>${fns:i18nMessage('common.operation')}</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="iccnIcaService">
				<tr>
				<td>
						<input type="checkbox" name="selectOrgButton" id="selectOrgButton_${iccnIcaService.id}" onclick="choose(this,${iccnIcaService.id})"></td>
					<td>
						${iccnIcaService.dbId}</td>
					<td><a href="${ctx}/ica/iccnIcaService/form?id=${iccnIcaService.id}">
						${iccnIcaService.serviceCode}
						</a>
					</td>
					<td>
						${iccnIcaService.serviceName}
					</td>
				<%-- 	<td>
						${iccnIcaService.serviceParams}
					</td>
					<td>
						${iccnIcaService.dbSql}
					</td>
					<td>
						${iccnIcaService.responeTemplate}
					</td> --%>
						<td>
						${iccnIcaService.serviceGroup}
					</td>
					<td>
						${iccnIcaService.description}
					</td>
					<td>
						${fns:getDictLabel(iccnIcaService.status, 'iccn_ica_status', '')}
					</td>
					<td>
						${fns:getDictLabel(iccnIcaService.forceUpdate, 'srm_yes_no', '')}
					</td>
					<td>
						${iccnIcaService.customVersion}
					</td>
					<%-- <td>
						<fmt:formatDate value="${iccnIcaService.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<fmt:formatDate value="${iccnIcaService.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td> --%>
					<shiro:hasPermission name="ica:iccnIcaService:edit"><td>
	    				<a href="${ctx}/ica/iccnIcaService/form?id=${iccnIcaService.id}">${fns:i18nMessage('common.modify')}</a>
<a href="javascript:void(0);" onclick="deleteAtId('${fns:i18nMessage('ica.servoce_delete_confrim')}', '${ctx}/ica/iccnIcaService/delete?id=${iccnIcaService.id}')">${fns:i18nMessage('common.delete')}</a>					<%-- 			<a href="${ctx}/ica/iccnIcaService/goTest?id=${iccnIcaService.id}">测试</a> --%>
					<a href="${ctx}/ica/iccnIcaService/copy?id=${iccnIcaService.id}">${fns:i18nMessage('ica.copy')}</a>
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