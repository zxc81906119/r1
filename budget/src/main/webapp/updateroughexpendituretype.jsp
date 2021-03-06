<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>query</title>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
	$(function() {
		var date = new Date();
		$('select option[value=\"' + (date.getMonth() + 1) + '\"]').attr({
			selected : true
		});
		$('#year').val(date.getFullYear());
		$('#date').val(date.getDate());
		$('a').click(function(e) {
			if (!confirm('確定要回主畫面?')) {
				e.preventDefault();
			}
		});
	})
</script>
<c:if test="${requestScope.updatesuccess}">
	<script type="text/javascript">
		$(function() {
			alert('更改成功');
		})
	</script>
</c:if>
<c:if test="${requestScope.hasrepeat}">
	<script type="text/javascript">
		$(function() {
			alert('新類別名稱已重複');
		})
	</script>
</c:if>
<c:if test="${requestScope.notexistroughdata}">
	<script type="text/javascript">
		$(function() {
			alert('舊類別不存在');
		})
	</script>
</c:if>
<style>
.querytitle {
	text-align: center;
	background: yellow;
	height: 150px;
}

div {
	text-align: center;
}
</style>
</head>
<body>
	<div class="querytitle">
		<h1>修改粗略收入支出類別</h1>
	</div>
	<hr>
	<div>
		<form action="./budget?action=updatetype" method="post">
			<input type="hidden" name="id" value="${sessionScope.id}"> 
			<label for="year">年:</label><input id="year" type="number" name="year" value="" required>
			<br> 
			<label>月:</label> 
			<select name="month">
				<option value="1">1月</option>
				<option value="2">2月</option>
				<option value="3">3月</option>
				<option value="4">4月</option>
				<option value="5">5月</option>
				<option value="6">6月</option>
				<option value="7">7月</option>
				<option value="8">8月</option>
				<option value="9">9月</option>
				<option value="10">10月</option>
				<option value="11">11月</option>
				<option value="12">12月</option>
			</select>
			<br> 
			<label for="date">當週隨便一日:</label><input id="date" type="number" name="date" value="" required>
			<br> 
			<label for="oldtype">舊類別:</label><input id="oldtype" type="text" name="oldtype" value="" required>
			<br> 
			<label for="bewtype">新類別:</label><input id="newtype" type="text" name="newtype" value="" required>
			<br> 
			<input class="submit" type="submit" value="更改">
		</form>
		<a href="main.jsp">回主畫面</a>
	</div>
</body>
</html>