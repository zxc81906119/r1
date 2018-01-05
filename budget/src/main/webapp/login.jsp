<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
$(function(){
});
</script>
<style>
#logintitle {
	background: yellow;
	text-align: center;
	width: 100%;
	height: 150px;
}
#input {
	width: 100%;
	text-align: center;
}
</style>
<title>login</title>
</head>
<body>
	<div id="logintitle">
		<h1>收支系統</h1>
	</div>
	<hr>
	<div id="input">
		<form action="./login" method="post">
			<label for="id">帳號:</label><input id="id" type="text" name="id"
				value=""><br><label for="password">密碼:</label><input
				id="password" type="password" name="password" value="">
				<br>
			<c:if test="${requestScope.idisnotexist!=null}">
				<span>${requestScope.idisnotexist}</span>
				<br>
			</c:if>
			<c:if test="${requestScope.passworderror!=null}">
				<span>${requestScope.passworderror}</span>
				<br>
			</c:if>
			<input class="submit" type="submit" value="進入">
		</form>
	</div>
</body>
</html>