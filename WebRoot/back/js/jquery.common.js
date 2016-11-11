var ShopBase = {
	path:"/szdisplays",
	locale: "zh_CN"
};

(function($) {
$.extend($.messager.defaults,{
	ok:"确定",
	cancel:"取消"
});

var config = {
	priceScale: "2",
	priceRoundType: "roundHalfUp",
	currencySign: "￥",
	currencyUnit: "元",
	uploadImageExtension: "jpg,jpeg,bmp,gif,png",
	uploadFlashExtension: "swf,flv",
	uploadMediaExtension: "swf,flv,mp3,wav,avi,rm,rmvb",
	uploadFileExtension: "zip,rar,7z,doc,docx,xls,xlsx,ppt,pptx"
};

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
		isNullByTarget:"必填",
		compare:"不允许小于最小值"
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
$.message = function(message){
	var title = "";
	if(message.type == "error"){
		title = "错误";
	}else if(message.type == "success"){
		title = "提示";
	}else{
		itle = "警告";
	}
	$.messager.show({
		title:title,
		msg:message.content,
		showType:'fade',
		timeout:2000,
		style:{ right:'',bottom:''}
	});
}

$.extend({
	loading:function(){
    	var overlay = $("<div></div>").css({
			"position": "fixed",
			"z-index": 999999999,
			"top": "0px",
			"left": "0px",
			"height":"100%",
			"width":"100%",
			"background": "#fff",
			"text-align":"center",
			"filer":"alpha(opacity=80)",
			"-moz-opacity":0.8,
    		"opacity":0.8

		}).appendTo($("body"));
    	$("<img src='"+ShopBase.path+"/back/images/loading_1.gif'/>").css({
    		"position": "absolute",
    		"top":"30%",
    		"left":"45%"
    	}).appendTo(overlay);
    	return overlay;
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
			browserUrl: ShopBase.path+"/back/file/browser.shtml",
			uploadUrl: ShopBase.path+"/back/file/upload.shtml",
			callback: null
		};
		$.extend(settings, options);
		
		var $mainClick = $(this);
		
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
						settings.callback(data,$mainClick);
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
			var url = $this.attr("url");
			if(isDirectory == "true"){
				if(navs != null && navs.length > 0){
					url = navs[navs.length-1] + "/" + url;
				}
				loadFile(url);
				navs.push(url);
			}else{
				$box.dialog('close');
				var data = {"type":"success","content":url};
				if(settings.callback){
					settings.callback(data,$mainClick);
				}
				navs = new Array();
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

$(document).ajaxComplete(function(event, request, settings) {
	var loginStatus = request.getResponseHeader("loginStatus");
	var tokenStatus = request.getResponseHeader("tokenStatus");
	
	if (loginStatus == "accessDenied") {
		var message = {type:"warn",content:"登录超时，请重新登录"};
		$.message(message);
		window.setTimeout(function() {
			window.location.reload(true);
		}, 2000);
	} else if (tokenStatus == "accessDenied") {
		
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


