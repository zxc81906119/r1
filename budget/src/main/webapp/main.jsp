<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@	taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<c:if test="${requestScope.logout}">
<script type="text/javascript">
	$(function(){
		window.history.forward(1);
		window.location.href='login.jsp';
	});
</script>
</c:if>

<title>main</title>
<style>
.maintitle {
	text-align: center;
	background: yellow;
	height: 150px;
}	

.main {
	text-align: center;
	width: 100%;
}

.main input[type="button"] {
	height: 200px;
	width: 200px;
}
</style>
</head>
<body>
	<div class="maintitle">
		<h1>主功能畫面</h1>
	</div>
	<hr>
	<div class="main">
		<a href="./budget?action=mainroughdata&id=${sessionScope.id}"><input type="button" value="新增修改支出明細"></a>
		<a href="updateroughexpendituretype.jsp"><input type="button" value="修改粗略支出類別"></a>
		<a href="insertroughexpendituredata.jsp"><input type="button" value="新增粗略支出資料"></a>
		<c:if test="${sessionScope.id=='娃娃機'}">
			<a href="./doll?action=dailycostandinoutgoods"><input type="button" value="當日各商品成本分析"></a>
			<a href="./doll?action=getalldolldata"><input type="button" value="新增新娃娃"></a>
			<a href="uploadpicture.jsp"><input type="button" value="上傳圖片"></a>
		</c:if>
		<br>
		<a href="./logout">登出</a>
	</div>
</body>
</html>