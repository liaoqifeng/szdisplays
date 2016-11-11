<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% request.setAttribute("path",request.getContextPath()); %>
<style>
	body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,input,button,p,th,td{margin:0;padding:0;}
	a,img{border:0;}
	.index_banner{position:relative;}
	.index_banner ul{overflow:hidden;}
	.index_banner li{position:absolute;top:0;left:0;overflow:hidden;width:100%;height:350px;}
	.index_banner li a{display:block;margin:0 auto;}
	.index_banner cite{position:absolute;bottom:10px;left:50%;z-index:999;display:block;margin-left:-144px;width:288px;height:15px;_display:none;}
	.index_banner cite span{float:left;display:block;margin:0 4px;width:40px;height:8px;background-color:#e5e5e5;text-indent:-999em;opacity:.8;cursor:pointer;}
	.index_banner cite span:hover{background-color:#f5f5f5;}
	.index_banner cite span.cur{background-color:#1d8bd8;cursor:default;}
	.clear{clear:both;}
</style>
<div class="index_banner" id="banner_tabs">
	<ul>
		<li><a href="#" target="_blank"><img src="${path }/front/images/banner.jpg" width="100%"></a></li>
		<li><a href="#" target="_blank"><img src="${path }/front/images/banner_2.jpg" width="100%"></a></li>      
		<li><a href="#" target="_blank"><img src="${path }/front/images/banner_3.jpg" width="100%"></a></li>    
		<li><a href="#" target="_blank"><img src="${path }/front/images/banner_4.jpg" width="100%"></a></li>
	</ul>
	<!--此处的img是用来占位的，在实际使用中，可以另外制作一张全空的图片-->
	<img style="visibility:hidden;" src="${path }/front/images/banner.jpg" width="100%">
	<cite>
		<span class="cur">1</span>
		<span>2</span>
		<span>3</span>
		<span>4</span>
	</cite>
	<div class="clear"></div>
</div>
<script type="text/javascript">
	(function(){
		if(!Function.prototype.bind){
			Function.prototype.bind = function(obj){
				var owner = this,args = Array.prototype.slice.call(arguments),callobj = Array.prototype.shift.call(args);
				return function(e){e=e||top.window.event||window.event;owner.apply(callobj,args.concat([e]));};
			};
		}
	})();
	var banner_tabs = function(id){
		this.ctn = document.getElementById(id);
		this.adLis = null;
		this.btns = null;
		this.animStep = 0.2;//动画速度0.1～0.9
		this.switchSpeed = 3;//自动播放间隔(s)
		this.defOpacity = 1;
		this.tmpOpacity = 1;
		this.crtIndex = 0;
		this.crtLi = null;
		this.adLength = 0;
		this.timerAnim = null;
		this.timerSwitch = null;
		this.init();
	};
	banner_tabs.prototype = {
		fnAnim:function(toIndex){
			if(this.timerAnim){window.clearTimeout(this.timerAnim);}
			if(this.tmpOpacity <= 0){
				this.crtLi.style.opacity = this.tmpOpacity = this.defOpacity;
				this.crtLi.style.filter = 'Alpha(Opacity=' + this.defOpacity*100 + ')';
				this.crtLi.style.zIndex = 0;
				this.crtIndex = toIndex;
				return;
			}
			this.crtLi.style.opacity = this.tmpOpacity = this.tmpOpacity - this.animStep;
			this.crtLi.style.filter = 'Alpha(Opacity=' + this.tmpOpacity*100 + ')';
			this.timerAnim = window.setTimeout(this.fnAnim.bind(this,toIndex),50);
		},
		fnNextIndex:function(){
			return (this.crtIndex >= this.adLength-1)?0:this.crtIndex+1;
		},
		fnSwitch:function(toIndex){
			if(this.crtIndex==toIndex){return;}
			this.crtLi = this.adLis[this.crtIndex];
			for(var i=0;i<this.adLength;i++){
				this.adLis[i].style.zIndex = 0;
			}
			this.crtLi.style.zIndex = 2;
			this.adLis[toIndex].style.zIndex = 1;
			for(var i=0;i<this.adLength;i++){
				this.btns[i].className = '';
			}
			this.btns[toIndex].className = 'cur'
			this.fnAnim(toIndex);
		},
		fnAutoPlay:function(){
			this.fnSwitch(this.fnNextIndex());
		},
		fnPlay:function(){
			this.timerSwitch = window.setInterval(this.fnAutoPlay.bind(this),this.switchSpeed*1000);
		},
		fnStopPlay:function(){
			window.clearTimeout(this.timerSwitch);
		},
		init:function(){
			this.adLis = this.ctn.getElementsByTagName('li');
			this.btns = this.ctn.getElementsByTagName('cite')[0].getElementsByTagName('span');
			this.adLength = this.adLis.length;
			for(var i=0,l=this.btns.length;i<l;i++){
				with({i:i}){
					this.btns[i].index = i;
					this.btns[i].onclick = this.fnSwitch.bind(this,i);
					this.btns[i].onclick = this.fnSwitch.bind(this,i);
				}
			}
			this.adLis[this.crtIndex].style.zIndex = 2;
			this.fnPlay();
			this.ctn.onmouseover = this.fnStopPlay.bind(this);
			this.ctn.onmouseout = this.fnPlay.bind(this);
		}
	};
	var player1 = new banner_tabs('banner_tabs');
</script>