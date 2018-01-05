<%@ page language="java" contentType="text/html;charset=utf-8"
	pageEncoding="utf-8"%>
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
$(function(){
	
	$('.submit').click(function(){
		
		$.ajax({
			url:'./doll',
			type:'post',
			data:{
				action:'insertnewdolldata',
				dollname:$('#dollname').val(),
				buyonedollprice:$('#buyonedollprice').val(),
			},
			success:function(data){
				alert(data);
				$('#dollname').remove();
				$('#buyonedollprice').remove();
			},
			error:function(){
				alert('有錯誤發生!');
			}
			
			
		})
		
	});
	
	
	
	
});
</script>
<style>
.content>div{
	float:left;
}
body{
	text-align:center;
}
.title{
	background:yellow;
}
.submit{
	border-radius:300px;
}
</style>
<title>insertnewdoll</title>
</head>
<body>
	<div class="title">新增娃娃</div>
	
	<div class="insertdata">
		<form action="./picturefile" method="post">
			<div class="previewimgdiv"><img class="previewimg" src=""></div>
			<input type="file" class="picturefile" name="picturefile" value="">
			<label for="dollname">娃娃名稱:</label>
			<input id="dollname" type="text" name="dollname" value="" required>
			<label for="buyonedollprice">娃娃單價:</label>
			<input id="buyonedollprice" type="number" name="buyonedollprice" value="" required>
			<input type="submit" value="提交" class="submit"><a href="main.jsp">回主畫面</a>
		</form>
	</div>
	
	<div class="content">
		<c:if test="${requestScope.dlist!=null}">
			<c:forEach var="onedoll" items="${requestScope.dlist}">
				<div>
					<table>
						<tr>
							<td><img width="100px" height="100px" src="${onedoll.dollpictureurl}"></td>
						</tr>
						<tr>
							<td>娃娃名稱:<span>${onedoll.dollname}</span></td>
						</tr>
						<tr>
							<td>娃娃成本:<span>${onedoll.buyonedollprice}</span></td>
						</tr>
					</table>
				</div>
			</c:forEach>
		</c:if>
	</div>
	
</body>
</html>