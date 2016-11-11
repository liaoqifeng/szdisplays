(function($) {
var ShopBase = {
	path:"/szdisplays"
}
$.extend($.messager.defaults,{
	ok:"确定",
	cancel:"取消"
});
// 验证消息
if($.validator != null) {
	$.extend($.validator.messages, {
	    required: "必填",
		email: "E-mail格式错误",
		url: "网址格式错误",
		date: "日期格式错误",
		dateISO: "日期格式错误",
		pointcard: "信用卡格式错误",
		number: "请输入数字",
		digits: "请输入零或正整数",
		minlength: "长度不允许小于{0}",
		maxlength: "长度不允许大于{0}",
		rangelength: "长度必须在{0}-{1}之间",
		min: "不允许小于{0}",
		max: "不允许大于{0}",
		maximu: "数值不正确",
		range: "必须在{0}-{1}之间",
		accept: "输入后缀错误",
		equalTo: "两次输入不一致",
		remote: "输入错误",
		integer: "请输入整数",
		positive: "请输入正数",
		negative: "请输入负数",
		decimal: "输入错误",
		pattern: "格式错误",
		extension: "文件格式错误",
		isMobileOrTel:"请输入联系电话",
		isNullByTarget:"必填"
	});
	$.validator.setDefaults({
		errorClass: "valid-message",
		ignore: ".ignore",
		ignoreTitle: true,
		errorPlacement: function(error, element) {
			var fieldSet = element.closest("span.fieldSet");
			if (fieldSet.size() > 0) {
				error.appendTo(fieldSet);
			} else {
				error.insertAfter(element);
			}
		}
	});
}
$.fn.extend({
	//禁止输入特殊字符    允许输入 .  "^[A-Za-z0-9.]+$"      
	disSpecialChar:function(){
		$(this).keypress(function(e){
			if ((event.keyCode > 32 && event.keyCode < 48) || (event.keyCode > 57 && event.keyCode < 65) || (event.keyCode > 90 && event.keyCode < 97) || event.ctrlKey) 
				if(event.keyCode==46){
					event.returnValue = true;
				}else{
					event.returnValue = false;
				}
		});
	},
	/*
	 * 限制输入框字符长度
	 * len  长度
	 * obj  显示可输入字符长度对象
	 */
	limitStringLength:function(len,obj){
		if($.browser.msie){
			$(this).bind("propertychange",function(){
				 var curLength = $(this).val().length;
				 var content=$(this).val().substring(0,len);
				    if (curLength>len){
				        $(this).val($(this).val().substring(0,len));
				        curLength = len;
				    }
				obj.text(len-$(this).val().length);
			});
		}else{
			$(this).bind("keyup",function(){
				 var curLength = $(this).val().length;
				 var content=$(this).val().substring(0,len);
				    if (curLength>len){
				        $(this).val($(this).val().substring(0,len));
				        curLength = len;
				    }
				obj.text(len-$(this).val().length);
			});
		}
	},
	//只能输入数字
	onlypressnum:function(){
		$(this).bind("keydown",function(e){
			if(e.shiftKey && (e.which >= 48 && e.which <= 57)){
				return false;
			}
			if((e.which >= 48 && e.which <= 57)){
				return true;
			}
			if((e.which >= 96 && e.which<=105)){
				return true;
			}
			if((e.which==8)){
				return true;
			}else{
				return false;
			}
		});
	},
	//只能输入数字和.
	onlypressPrice:function(){
		$(this).bind("keydown",function(e){
			if(e.shiftKey && (e.which >= 48 && e.which <= 57)){
				return false;
			}
			if((e.which >= 48 && e.which <= 57)){
				return true;
			}
			if((e.which >= 96 && e.which<=105)){
				return true;
			}
			if((e.which==8) || (e.which==190) || (e.which==110)){
				return true;
			}else{
				return false;
			}
		});
	}
});
//货币规则
$.currency = function(value,isSign,isUnit){
	if (value != null) {
		var price;
		if (config.priceRoundType == "roundHalfUp") {
			price = (Math.round(value * Math.pow(10, config.priceScale)) / Math.pow(10, config.priceScale)).toFixed(config.priceScale);
		} else if (setting.priceRoundType == "roundUp") {
			price = (Math.ceil(value * Math.pow(10, config.priceScale)) / Math.pow(10, config.priceScale)).toFixed(config.priceScale);
		} else {
			price = (Math.floor(value * Math.pow(10, config.priceScale)) / Math.pow(10, config.priceScale)).toFixed(config.priceScale);
		}
		if (isSign) {
			price = config.currencySign + price;
		}
		if (isUnit) {
			price += config.currencyUnit;
		}
		return price;
	}
}
$.message = function(value){
	return "<font color='#FFB042'>"+value+"</font>";
}
$.fn.extend({
	lSelect: function(options) {
		var settings = {
			url : "",
			cityId : null,
			countyId : null
		};
		$.extend(settings, options);
		settings.cityId = (typeof settings.cityId == 'string' ? $(settings.cityId) : settings.cityId);
		settings.countyId = (typeof settings.countyId == 'string' ? $(settings.countyId) : settings.countyId);
		return this.each(function() {
			var $this = $(this);
			var $city = settings.cityId;
			var $county = settings.countyId;
			var provinceId = $this.attr("areaId");
			var cityId = settings.cityId.attr("areaId");
			var countyId = settings.countyId.attr("areaId");
			loadSelect($this,0,provinceId);
			loadSelect(settings.cityId,provinceId,cityId);
			loadSelect(settings.countyId,cityId,countyId);
			$this.change(function(){
				loadSelect($city,$(this).val(),"");
			});
			$city.change(function(){
				loadSelect($county,$(this).val(),"");
			});
			function loadSelect(position,parentId,currentId){
				$.ajax({
					url: settings.url,
					type: "GET",
					dataType: "json",
					data:{parentId:parentId},
					cache: false,
					async: false,
					success: function(data) {
						position.find("option").remove();
						var option = '<option value="" selected="selected">--请选择--</option>';
						$(data).each(function(i,r){
							if(r.id == currentId) {
								option += '<option value="' + r.id + '" selected="selected">' + r.name + '</option>';
							} else {
								option += '<option value="' + r.id + '">' + r.name + '</option>';
							}
						});
						position.append(option);
					}
				});
			}
		});
	}
});

$.fn.extend({
	localLoading:function(){
		var $this = $(this).css({"position":"relative"});
		var $overlay = $("<div></div>").css({
			"position": "absolute",
			"z-index": 999999998,
			"top": "0px",
			"left": "0px",
			"height":"100%",
			"width":"100%",
			"background-color": "#fff",
			"background-image": "url("+ShopBase.path+"/back/images/local_loading.gif)",
			"background-repeat":"no-repeat",
			"background-position":"center center",
			"text-align":"center",
			"filer":"alpha(opacity=80)",
			"-moz-opacity":0.8,
			"opacity":0.8
		}).appendTo($this);
		return {
			remove:function(){
				$overlay.remove();
			}
		};
	},
	browser: function(options) {
		var settings = {
			type: "image",
			title: "",
			isUpload: true,
			browserUrl: ShopBase.path + "/back/file/browser.shtml",
			uploadUrl: ShopBase.path + "/back/file/upload.shtml",
			callback: null
		};
		$.extend(settings, options);
		var $browseBar = $('<div class="browserBar"></div>');
		var $browseList = $('<div class="browseList"><ul class="list"></ul></div>');
		var $box = $('<div></div>').append($browseBar).append($browseList);
		
		var browserFrameId = "browserFrame" + (new Date()).valueOf() + Math.floor(Math.random() * 1000000);
		var $browserFrame = $('<iframe id="' + browserFrameId + '" name="' + browserFrameId + '" style="display: none;"><\/iframe>').appendTo($browseBar);
		var $browserForm = $('<form action="' + settings.uploadUrl + '" method="post" encType="multipart/form-data" target="' + browserFrameId + '"><input type="hidden" name="fileType" value="' + settings.type + '" \/><\/form>').appendTo($browseBar);
		var $browserUploadButton = $('<div class="new-contentarea tc left"> <a href="javascript:void(0)" class="upload-img"><label for="upload-file">上传文件</label></a><input type="file" class="" name="file" id="upload-file" /></div>').appendTo($browserForm);
		var $returnBth = $('<input class="toolBtn left prev" type="button" value="返回上级目录" \/>').appendTo($browseBar);
		
		$browserUploadButton.find(":file").change(function(){
			$browserForm.submit();
			
			var io = $browserFrame[0];
			var load = function(){
				var data = $.evalJSON(io.contentWindow.document.body.innerHTML);
				if(data.type == "error"){
					$.messager.alert('提示',data.content);
				}else{
					$box.dialog('close');
					if(settings.callback){
						settings.callback(data);
					}
				}
			}
			if(io.attachEvent){ // IE  
				io.attachEvent('onload', load);  
			}else if(window.addEventListener){ // nonIE  
				io.addEventListener("load", load, false);
			}else{
				io.onload = load();
			}
		});
		
		this.click(function(){
			loadFile("/");
			$box.dialog({
				title: '    文件选择',
				width: 600,
				height: 400,
				closed: false,
				cache: false,
				modal: true
			});
		});
		
		var navs = new Array();
		$browseBar.find(".prev").click(function(){
			navs.pop();
			var url = "/";
			if(navs != null && navs.length >= 0){
				url = navs[navs.length-1];
			}
			loadFile(url);
		});
		
		$browseList.on("click","img",function(){
			var $this = $(this);
			var isDirectory = $this.attr("isDirectory");
			if(isDirectory == "true"){
				var url = $this.attr("url");
				loadFile(url);
				navs.push(url);
			}
		});
		
		var loadFile = function(path){
			var $loading = null;
			$.ajax({
				url: settings.browserUrl,
				type: "GET",
				dataType: "json",
				data:{path:path, fileType:settings.type, orderType:"name",jession:$.encryption()},
				cache: false,
				beforeSend:function(){
					$loading = $browseList.parent().localLoading();
				},
				success: function(data) {
					if(data != null && data.length > 0){
						var list = new Array();
						for(var i=0;i<data.length;i++){
							var url = "";
							if(data[i].isDirectory){
								url = ShopBase.path + "/back/images/folder.png";
							}else{
								url = data[i].url
							}
							list.push("<li><img src='"+url+"' isDirectory='"+data[i].isDirectory+"' url='"+data[i].url+"'/><div class='title'>"+data[i].name+"</div></li>")
						}
						$browseList.find(".list").empty().append(list.join(""));
					}
					$loading.remove();
				},
				error:function(){
					$loading.remove();
					$.messager.alert('提示','服务器异常!');
				}
			});
		}
	}
});

$.encryption = function(){    
	var date = new Date();    
	var times1970 = date.getTime();    
	var times = date.getDate() + "" + date.getHours() + "" + date.getMinutes() + "" + date.getSeconds();    
	var encrypt = times * times1970;    
	if(arguments.length == 1){        
		return arguments[0] + encrypt;    
	}else{       
		return encrypt;    
	}
}
$.getRandomNum = function(Min,Max){
	var Range = Max - Min;   
	var Rand = Math.random();   
	return(Min + Math.round(Rand * Range)); 
}

function log() {
	if ($.fn.ajaxSubmit.debug) {
		var msg = '[jquery.form] ' + Array.prototype.join.call(arguments,'');
		if (window.console && window.console.log) {
			window.console.log(msg);
		}
		else if (window.opera && window.opera.postError) {
			window.opera.postError(msg);
		}
	}
};

})(jQuery);


