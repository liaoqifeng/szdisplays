<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>${fn:call("call.product.add")}</title>
	<%@ include file="/back/common/metainfo.jsp" %>
	<link rel="stylesheet" href="${path }/common/kindeditor/themes/default/default.css" />
	<script type="text/javascript" src="${path }/common/kindeditor/kindeditor-min.js" charset="utf-8"></script>
	<script type="text/javascript" src="${path }/common/kindeditor/lang/zh_CN.js" charset="utf-8"></script>
	<script type="text/javascript">
		KindEditor.ready(function(K) {
			var editor1 = K.create('textarea[name="introduction"]', {
				uploadJson : '${path }/back/file/upload.shtml',
				fileManagerJson : '${path }/back/file/browser.shtml',
				allowFileManager : true,
				afterCreate : function() {},
				afterBlur: function(){this.sync();}
			});
		});
	</script>
	<script type="text/javascript">
		$().ready(function(){
			var $mainBox = $("#mainBox");
			var $mainPanel = $("#mainPanel");
			var $productCategory = $("#productCategory");
			var $number = $("#number");
			var $searchProductImage = $("a.searchProductImage");
			var $deleteProductImage = $("a.deleteProductImage");
			var $vipPriceTr = $("#vipPriceTr");
			var $isVipPrice = $("#isVipPrice");
			var $tab = $("#tab");
			var $infoTab = $("#infoTab");
			var $parameterTab = $("#parameterTab");
			var $propertyTab = $("#propertyTab");
			var $specTab = $("#specTab");
			var $specValueTab = $("#specValueTab");
			var $specificationSelect = $("#specTab :checkbox");
			var $addProductSpecification = $("#addProductSpecification");
			var $uploadSpecImg = $(":file[name='uploadImage']");
			var $imagesTab = $("#imagesTab");
			var $addProductImage = $("#addProductImage");
			var $deleteProductImage = $("a.deleteProductImage");
			var $editForm = $("#editForm");
			var $saveButton = $("#saveButton");
			
			var productImageIndex = 0;
			var productSpecIndex = 0;
			$mainPanel.panel({
				width:$mainBox.width(),
				height:$mainBox.height(),
				tools:[{
					iconCls:'icon-reload',
					handler:function(){window.location.reload();}
				}]
			});
			$(window).resize(function() { 
				 var width= $mainBox.width();
				 var height=  $mainBox.height();
				 $mainPanel.panel('resize', { width : width,height:height }); 
			});
			if ($.tools != null) {
				$tab.tabs("table.tabContent", {
					tabs: "input"
				});
			}
			$("input.tip").tooltip({       
				position: "center right",
				offset: [0, 10],
				opacity: 0.7
			});

			$("#browserButton").browser({callback:function(data){
				$("#showImgBtn").val(data.content);
			}});
			
			/*****************基本信息         开始****************/
			$isVipPrice.click(function(){
				if($(this).prop("checked")){
					$vipPriceTr.show().find("input").prop("disabled", false);
				}else{
					$vipPriceTr.hide().find("input").prop("disabled", true);
				}
			});
			$productCategory.change(function(){
				if($(this).val() != ""){
					initParameter($(this).val());
					initProperty($(this).val());
				}else{
					$parameterTab.find("tr").remove();
					$propertyTab.find("tr").remove();
				}
			});
			var initParameter = function(typeId){
				$.ajax({
				    type: "POST",                                       
                    url: "${path}/back/common/getParamByCategory.shtml",          
                    dataType: "json",
                    data: { productCategoryId: typeId},
                    beforeSend: function() {
                    	$parameterTab.empty();
        			},     
                    success: function (data) {
                 	    if(data == null || data == "") return;
                 	    var title = '<tr height="30" bgcolor="#e6e6e6" class="head"><td colspan="2" align="center"><b>'+data.name+'</b></td></tr>';
					    $parameterTab.append(title);
						var parmValue = $.evalJSON(data.value);
						if(parmValue != null && parmValue.length > 0){
							$(parmValue).each(function(i,res){
								var html = '<tr name="'+res.name+'"><td height="30" width="45%" align="right">'+'&nbsp;'+res.name+'：</td>';
								html += '<td width="55%" align="left">&nbsp;<input type="text" name="parameter_'+res.id+'" class="text middle" value="" maxlength="255" size="20"/></td></tr>';
								$parameterTab.append(html);
							});
	  				    } 
                    }        
				});
			}

			var initProperty = function(typeId){
				$.ajax({
					type: "POST",                                       
                  	url: "${path}/back/common/getPropByCategory.shtml",          
                  	dataType: "json",
                  	data: { productCategoryId: typeId},
                  	beforeSend: function() {
                  		$propertyTab.empty();
        			},         
                  	success: function (data) {
	               	    if(data == null || data == "") return;
                     	$(data).each(function(j){
	                  	 	var html = '<tr><td height="30" width="44%" align="right">'+'&nbsp;'+data[j].name+'：</td>';
						    html += '<td width="55%">&nbsp;<select name="property_'+data[j].id+'" class="select">';
						    $(data[j].options).each(function(i,res){
						   		html += '<option value="'+res+'">'+res+'</option>';
						    });
						    html += '</select></td></tr>';
						    $propertyTab.append(html);
	                     });
                     }        
				});
			}
			/*****************基本信息         结束****************/
			
			
			/*****************商品规格         开始****************/
			$specificationSelect.click(function(){
				var $this = $(this);
				if ($this.prop("checked")) {
					$specTab.find("td.specification_" + $this.val()).show().find("select").prop("disabled", false);
				} else {
					$specTab.find("td.specification_" + $this.val()).hide().find("select").prop("disabled", true);
				}
			});
			$addProductSpecification.click(function(){
				if($specificationSelect.filter(":checked").size() == 0){
					$.messager.alert('提示','至少要选择一个规格!');
					return;
				}
				var $tr = $specValueTab.find("tbody tr:eq(0)").clone().show();
				$tr.appendTo($specValueTab.find("tbody"));
				$tr.find(":file").attr("id","productSpecImgs_"+productSpecIndex+"_"+$.encryption());
				productSpecIndex++;
			});
			$specValueTab.on("click",".deleteProductSpecification",function(){
				var $this = $(this);
				$.messager.confirm('提示', '确定删除?', function(r){
					if (r){
						$this.closest("tr").remove();
					}
				});
			});
			$specValueTab.on("change",".uploadImage",function(){
				var id = "spec_image_"+$.encryption();
				var $this = $(this).attr("id",id);
				var $image = $this.prev();
				var $hidden = $this.next();
				$.ajaxFileUpload({
					url:'${path}/back/file/upload.shtml',
					type:'GET',
					secureuri:false,
					fileElementId:id,
					dataType: 'json',
					data:{"fileType":"image"},
					success: function (data, status){
						if(data.type == "success"){
							$image.attr("src",data.content);
							$hidden.val(data.content);
						}else{
							$.messager.alert('提示',data.content);
						}
					},
					error: function (data, status, e){
						$.messager.alert('提示','上传规格图片失败!');
					}
				});
			});
			$specValueTab.on("change","select.image",function(){
				var $this = $(this);
				$this.next().attr("src",$this.find("option:selected").data("url"));
				$this.siblings(":hidden").val("");
			});
			
			/*****************商品规格         结束****************/
			
			/*****************商品图片        开始****************/
			$addProductImage.click(function(){
				var html = '<tr height="30">'
					+'	<td>'
					+'		&nbsp;<input type="file" name="productImages['+productImageIndex+'].file" class="text long"/>'
					+'	</td>'
					+'	<td><input type="text" name="productImages['+productImageIndex+'].title" value="" class="text middle" maxlength="255"/></td>'
					+'	<td><input type="text" name="productImages['+productImageIndex+'].orderList" value="" class="text short orderList" maxlength="10" /></td>'
					+'	<td>'
					+'		<input type="hidden" name="productImages['+productImageIndex+'].url" value=""/>'
					+'		<a href="#" class="deleteProductImage">删除</a>'
					+'	</td>'
					+'</tr>';
				$(html).appendTo($imagesTab);
				productImageIndex++;
			});
			$imagesTab.on("click",".deleteProductImage",function(){
				var $this = $(this);
				$.messager.confirm('提示', '确定删除?', function(r){
					if (r){
						$this.closest("tr").remove();
					}
				});
			});
			/*****************商品图片        结束****************/
			$.validator.addClassRules({
				vipPrice: {
					decimal:{
						length:18,
						scale:${fn:config("priceScale")}
					},
					min: 0
				},
				productImageFile: {
					required: true,
					extension: '${fn:config("uploadImageExtension")}'
				},
				orderList:{
					integer:true
				}
			});
			$editForm.validate({
				rules: {
					"number":{
						pattern: /^[0-9a-zA-Z_-]+$/,
						remote: {
							url: "${path}/back/product/view/check.shtml?oldNumber=${product.number }",
							cache: false
						}
					},
					"name": {
						required: true,
						maxlength: 255
					},
					"productCategory.id": { required: true },
					"brand.id": { required: true },
					"salePrice":{ 
						decimal:{
							length:18,
							scale:${fn:config("priceScale")}
						}
					},
					"costPrice":{
						decimal:{
							length:18,
							scale:${fn:config("priceScale")}
						}
					},
					"marketPrice":{
						decimal:{
							length:18,
							scale:${fn:config("priceScale")}
						}
					},
					"score":{
						required: true,
						digits: true
					},
					"stock":{
						digits: true
					},
					"weight":{
						decimal:{
							length:18,
							scale:4
						}
					}
				},
				messages: {
					number: {
						pattern: "商品编号格式错误",
						remote: "商品编号已经存在"
					}
				},
				submitHandler: function(form) {
					$specValueTab.find("tbody tr:eq(0)").remove();
					form.submit();
				}
			});
			
			$saveButton .click(function(){
				$editForm.submit();
			});
		});
	</script>
</head>
<body>
	<div id="mainBox" class="main-box">
		<div id="mainPanel" class="easyui-panel" iconCls="icon-edit" title="&nbsp;&nbsp;${fn:call('call.product.add')}  ">
			<form id="editForm" action="${path }/back/product/add.shtml" method="post" enctype="multipart/form-data">
		    	<ul id="tab" class="tab">
					<li><input value="基本信息" type="button" /></li>
					<li class="hidden"><input value="商品参数" type="button" /></li>
					<li><input value="商品属性" type="button" /></li>
					<li class="hidden"><input value="商品规格" type="button" /></li>
					<li><input value="商品图片" type="button" /></li>
					<li><input value="商品简介" type="button" /></li>
				</ul>
				<table id="infoTab" width="100%" class="input tabContent">
					<tr>
						<td height="30" width="15%" align="right"><font color="red">*</font>&nbsp;名称：</td>
						<td width="35%">
							<input type="text" name="name" class="text long" maxlength="255"/>
						</td>
						<td width="" align="right">编号：</td>
						<td>
							<input type="text" id="number" name="number" class="text tip" title="不填默认由系统自动生成"/>
						</td>
					</tr>
					<tr>
						<td height="30" align="right"><font color="red">*</font>&nbsp;商品分类： </td>
						<td>
							<select id="productCategory" name="productCategory.id" class="select">
								<option value="">--请选择--</option>
								<c:if test="${productCategorys != null}">
									<c:forEach var="row" items="${productCategorys}">
										<option value="${row.id }">
											<c:forEach var="i" begin="0" end="${row.level}" step="1">
											&nbsp;&nbsp;
											</c:forEach>
											${row.name }
										</option>
									</c:forEach>
								</c:if>
							</select>
						</td>
						<td height="30" align="right"><font color="red">*</font>&nbsp;品牌： </td>
						<td>
							<select name="brand.id" class="select">
								<option value="">--请选择--</option>
								<c:if test="${brands != null}">
									<c:forEach var="row" items="${brands}">
									<option value="${row.id }">${row.name }</option>
									</c:forEach>
								</c:if>
							</select>
						</td>
					</tr>
					<tr>
						<td height="30" align="right">&nbsp;成本价： </td>
						<td>
							<input type="text" name="costPrice" class="text short" value="0" maxlength="10"/>
						</td>
						<td height="30" align="right">&nbsp;市场价： </td>
						<td>
							<input type="text" name="marketPrice" class="text short tip" value="0" title="不填默认由系统自动生成" maxlength="10"/>
						</td>
					</tr>
					<tr>
						<td height="30" align="right">&nbsp;销售价： </td>
						<td>
							<input type="text" name="salePrice" class="text short" value="0" maxlength="10"/>
						</td>
						<td height="30" align="right">&nbsp;重量： </td>
						<td>
							<input type="text" name="weight" class="text short tip" title="单位:克" value="0" maxlength="10"/>
						</td>
					</tr>
					<tr>
						<td height="30" align="right">&nbsp;库存： </td>
						<td>
							<input type="text" name="stock" class="text short" value="0" maxlength="10"/>
						</td>
						<td height="30" align="right">&nbsp;库位： </td>
						<td>
							<input type="text" name="cargoSpace" class="text short" value="" maxlength="100"/>
						</td>
					</tr>
					<tr class="hidden">
						<td height="30" align="right">&nbsp;单位： </td>
						<td>
							<input type="text" name="unit" class="text short" maxlength="10"/>
						</td>
						<td height="30" align="right">赠送积分： </td>
						<td>
							<input type="text" name="score" class="text short tip" value="0" title="不填默认由系统自动生成" maxlength="10"/>
						</td>
					</tr>
					<tr class="hidden">
						<td height="30" align="right">会员价：</td>
						<td>
							<input type="checkbox" id="isVipPrice" name="isVipPrice" value="true" />是否启用会员价
						</td>
						<td height="30" align="right">&nbsp;标签： </td>
						<td>
							<c:if test="${!empty tags }">
								<c:forEach var="row" items="${tags}">
									<input type="checkbox" name="tagIds" value="${row.id }"/>${row.name }
								</c:forEach>
							</c:if>
						</td>
					</tr>
					<tr>
						<td height="30" align="right">&nbsp;设置： </td>
						<td>
							<input type="checkbox" name="isShow" value="true" />是否显示
							<input type="checkbox" name="isTop" value="true" />是否置顶
							<input type="checkbox" name="isGift" value="true" class="hidden"/>
						</td>
						<td height="30" align="right">商品上架：</td>
						<td>
							&nbsp;<input type="checkbox" name=isPublish value="true" />是否上架
						</td>
					</tr>
					<tr id="vipPriceTr" class="hidden" >
						<td bgcolor="#e6e6e6"></td>
						<td align="left" colspan="3"  bgcolor="#e6e6e6">
							<c:if test="${grades != null }">
							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
								<c:forEach var="row" items="${grades}" varStatus="i">
								<td>${row.name }:<input type="text" name="memberPrice_${row.id}" maxlength="10" class="text short vipPrice"/></td>
								</c:forEach>
								</tr>
							</table>
							</c:if>
						</td>
					</tr>
					<tr>
						<td height="30" align="right">展示图片： </td>
						<td>
							<input type="text" id="showImgBtn" name="showImg" class="text" maxlength="255"/>&nbsp;
							<input type="button" id="browserButton" class="button" value="选择文件.." />
						</td>
						<td height="30" align="right">备注：</td>
						<td><input type="text" name="remark" class="text long" maxlength="255"/></td>
					</tr>
					<tr>
						<td height="30" align="right">搜索关键字：</td>
						<td><input type="text" name="searchKey" class="text long" maxlength="255"/></td>
						<td height="30" align="right">页面标题：</td>
						<td><input type="text" name="title" class="text long" maxlength="255"/></td>
					</tr>
					<tr>
						<td height="30" align="right">页面关键词：</td>
						<td><input type="text" name="keywords" class="text long" maxlength="255"/></td>
						<td height="30" align="right">页面描述：</td>
						<td align="left" colspan="3"><input type="text" name="describtion" class="text long" size="40" maxlength="255"/></td>
					</tr>
				</table>
				<table id="parameterTab" width="100%" class="input tabContent hidden"></table>
				<table id="propertyTab" width="100%" class="input tabContent"></table>
				<table id="specTab" class="input tabContent table-spec hidden" >
					<tr height="30" bgcolor="#f2f2f2">
		    			<td width="5%" align="right"></td>
		    			<td width="95%"><b>选择规格</b></td>
		    		</tr>
		    		<tr height="40" bgcolor="#f2f2f2">
		    			<td colspan="2" valign="middle">
		    				<ul>
	    					<c:if test="${specs != null }">
	    						<c:forEach var="row" items="${specs}">
	    							<li style="float: left;margin-left: 10px;">
	    								<input type="checkbox" name="specificationIds" value="${row.id }" data-type="${row.specType }" />&nbsp;&nbsp;${row.name }[${row.remark }]
	    							</li>
	    						</c:forEach>
	    					</c:if>
		    				</ul>
		    			</td>
		    		</tr>
		    		<tr height="40">
		    			<td width="5%" align="right"></td>
		    			<td width="95%">
		    				<a href="#" id="addProductSpecification" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加规格</a>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td colspan="2" style="border: 0px;">
		    				<table id="specValueTab" cellpadding="0" cellspacing="0" width="100%">
		    					<thead>
		    						<tr height="30" bgcolor="#f2f2f2">
		    							<td width="0%"></td>
			    						<c:if test="${specs != null }">
				    						<c:forEach var="row" items="${specs}">
				    							<td style="border: 0px;" class="specification_${row.id } hidden" >${row.name }[${row.remark }]</td>
				    						</c:forEach>
				    						<td style="border: 0px">操作</td>
				    					</c:if>
		    						</tr>
		    					</thead>
		    					<tbody>
		    						<tr height="40" class="hidden">
		    							<td width="0%"></td>
			    						<c:if test="${specs != null }">
				    						<c:forEach var="row" items="${specs}">
				    							<td style="border: 0px;" class="specification_${row.id } hidden">
												<c:if test="${row.specAttributes != null}">
													<select name="specification_${row.id }" class="${row.specType }">
													<c:forEach var="spec" items="${row.specAttributes}">
													<option value="${spec.id }" data-url="${path }${spec.url}">${spec.name }</option>
													</c:forEach>
													</select>
													<c:if test="${row.specType=='image'}">
														<img src="${path }${row.specAttributes[0].url }" name="specImage_${row.id }" url="${row.specAttributes[0].url }" style="width:30px;height:30px;vertical-align: middle;"/>
														<input type="file" name="file" class="text-one uploadImage" style="width:69px;"/>
														<input type="hidden" name="attribute_value_${row.id }" value=""/>
													</c:if>
												</c:if>
												</td>
				    						</c:forEach>
				    						<td style="border: 0px"><a href="javascript:;" class="deleteProductSpecification">删除</a></td>
				    					</c:if>
			    					</tr>
		    						<tr height="40">
		    						<c:if test="${specs != null }">
		    							<td width="0%"></td>
			    						<c:forEach var="row" items="${specs}">
		    							<td style="border: 0px;" class="specification_${row.id } hidden">
											<c:if test="${row.specAttributes != null}">
												<select name="specification_${row.id }" class="${row.specType }">
												<c:forEach var="spec" items="${row.specAttributes}">
													<option value="${spec.id }" data-url="${path }${spec.url}">${spec.name }</option>
												</c:forEach>
												</select>
												<c:if test="${row.specType=='image'}">
													<img src="${path }${row.specAttributes[0].url}" name="specImage_${row.id }" style="width:30px;height:30px;vertical-align: middle;"/>
													<input type="file" name="file" class="text-one uploadImage" style="width:69px;"/>
													<input type="hidden" name="attribute_value_${row.id }" value=""/>
												</c:if>
											</c:if>
										</td>
			    						</c:forEach>
			    						<td style="border: 0px">-</td>
			    					</c:if>
			    					</tr>
		    					</tbody>
		    				</table>
		    			</td>
		    		</tr>
	    		</table>
	    		<table id="imagesTab" class="input tabContent table-spec" >
	    			<tr height="40" bgcolor="#f2f2f2" name="head">
		    			<td colspan="4">&nbsp;&nbsp;<a href="#" id="addProductImage" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加图片</a></td>
		    		</tr>
		    		<tr height="30" bgcolor="#f2f2f2" name="head">
		    			<td>文件</td>
		    			<td>标题</td>
		    			<td width="15%">排序</td>
		    			<td>操作</td>
		    		</tr>
	    		</table>
	    		<table class="input tabContent" >
					<tbody>
						<tr class="title">
							<td><textarea name="introduction" style="width:1000px;height:400px;">${product.introduction }</textarea></td>
						</tr>
					</tbody>
				</table>
				<div class="edit-tools">
		   			<a href="#" id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		   			<a href="#" id="backButton" button="back" class="easyui-linkbutton" data-options="iconCls:'icon-back'">返回</a>
				</div>
		    </form>
		</div>
	</div>
	
</body>
</html>
