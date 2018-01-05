<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@	taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>costanalize</title>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
	window.onload=function(){
	var upPreviewImg=function(options){
		var _e=options.e,
		preloadImg=null;
		_e.onchange=function(){
			var _v=this.value,
			_body=document.body;
			picReg=/(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png){1}/;
			if(!picReg.test(_v)){
				alert('請選擇正確圖片格式!');
				return false;
			}
			if(typeof FileReader !=='undefined'){
				var reader=new FileReader(),
				_file=this.files[0];
				reader.onload=(function(file){
					return function(){
						options.previewImgSrc.setAttribute("src",this.result);
						options.previewImgSrc.style.display="inline-block";
					}
				})(_file);
				reader.onerror=function(){
					alert('檔案讀取資料出錯');
				}
				reader.readAsDataURL(_file);
			}
						
		}
	};
	upPreviewImg(
			{
				"e":document.getElementById("upPreviewImg"),
				"previewImgSrc":document.getElementById("previewImgSrc")
			}
		);
}
</script>
<style>
.title{
	position:absolute;
	top:0px;
	height:200px;
	width:100%;
	background:black;
	color:pink;
	text-align:center;
	
}
.content{
	position:absolute;
	top:210px;
	bottom:0px;
	width:100%;
	text-align:center;
}
#previewImg{
	height:100px;
	width:100%;
	text-align:center;
}
#previewImgSrc{
	display:none;
	height:100px;
}
</style>
</head>
<body>
<div class="title">
	<h1>上傳圖片</h1>
</div>
<div class="content">
	<form action="./picturefile" method="post" enctype="multipart/form-data">
	<div id="previewImg"><img src="" id="previewImgSrc"></div>
		<label for="dollpictureurl">照片:</label>
		<input id="upPreviewImg" type="file" name="fileimg">
		<input type="submit" value="upload">
	</form>
</div>
</body>
</html>