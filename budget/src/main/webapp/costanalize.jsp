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
$(function(){
	$('.seedetail').click(function(){
		var inneroffset=$('.content').offset();
		$('<iframe width="100%" height="1000px" src=\"./doll?action=detailcostdata&thisdate='+$(this).closest('table').find('tr').first().find('span').text()+'\"></iframe>').appendTo('body').offset({
			top:inneroffset.top+25,
			left:inneroffset.left
		});
	});
	
	$('.updatewithdrawmoney').click(function(){
		$this=$(this);
		$.ajax({
			url:'./doll',
			type:'post',
			data:{
				action:'updatewithdrawmoney',
				withdrawmoney:$this.prev('input[type="text"]').val(),
				thisdate:$this.closest('table').find('tr').first().find('span').text(),
			},
			success:function(data){
				alert(data);
				window.location.reload();
			},
			error:function(){
				alert('再按一次啦!爸拖!剛剛系統怪異');
			}
		});	
	});
	
});
</script>
<style>
.title {
	text-align:center;
	width: 100%;
	height: 60px;
	font-weight: bold;
	background:blue;
}
.content>div{
	float:left;
	background:pink;
}
.content{
	width:100%;
}
.inner>form{
	text-align:center;
}
.seedetail{
	width:200px;
	background:black;
	color:white;
	border-radius:300px;
}
.updatewithdrawmoney{
	border-radius:300px;
}
</style>
</head>
<body>
	<div class="title">
		<h1>成本分析</h1>
	</div>
	<div class="inner">
		<form method="post" action="./doll">
			<input type="hidden" name="action" value="dailycostandinoutgoods"> <label
				for="year">年:</label><input id="year" type="text" name="year"
				pattern="^[0-9]{4}$" value="" required> <label for="month">月:</label><input
				id="month" type="text" name="month" value="">
			<label for="day">日:</label><input id="day" type="text" name="day" value="">
				<input type="submit" value="查看"><a href="main.jsp">回主畫面</a>
		</form>
		<div class="content">
			<c:if test="${pagemap!=null}">
				<c:forEach var="onelist" items="${requestScope.pagemap}">
					<c:forEach var="onedata" items="${onelist.value}">
						<div class="${'page'}${onelist.key}">
							<table>
								<tr>
									<td><b>日期:</b><span>${onedata.thisdate}</span></td>
								</tr>
								<tr>
									<td><b>本日領出的錢:</b><input style="width:50px" type="text" value="${onedata.withdrawmoney}" class="withdrawmoney"><input type="button" class="updatewithdrawmoney" value="更改提領的錢"></td>
								</tr>
								<tr>
									<td><b>本日被夾出娃娃成本:</b><span>${onedata.totalcost}</span></td>
								</tr>
								<tr>
									<td><b>本日盈餘:</b><c:if test="${onedata.withdrawmoney-onedata.totalcost>=0}">淨賺<span>${onedata.withdrawmoney-onedata.totalcost}</span>元</c:if><c:if test="${onedata.withdrawmoney-onedata.totalcost<0}">實虧<span>${onedata.totalcost-onedata.withdrawmoney}</span>元</c:if></td>						
								</tr>
								<tr>
									<td><b>本日補娃娃總數:</b><span>${onedata.alldollindollmachinecount}</span></td>
								</tr>
								<tr>
									<td><b>本日被夾出娃娃總數:</b><span>${onedata.alldolloutdollmachinecount}</span></td>
								</tr>
								<tr>
									<td>
										<button class="seedetail">
											<b>Click Me Watch Detail Data</b>
										</button>
									</td>
								</tr>
							</table>
						</div>
					</c:forEach>
				</c:forEach>
		</div>
		</c:if>
	</div>
	
</body>
</html>