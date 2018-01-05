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
	$.ajax({
		url:'./doll',
		type:'post',
		data:{
			action:'getalldollnames'
		},
		dataType:'text',
		success:function(data){
			var optionarray=data.split(',');
			var optionstring='';
			for(var i=0;i<optionarray.length;i++){
				optionstring+='<option value=\"'+optionarray[i]+'\">'+optionarray[i]+'</option>';
			}
			$('body').append('<div class="savedollnames" style="display:none"><select class="dollname" name="dollname">'+optionstring+'</select></div>')
		},
		error:function(){
			alert('沒抓到娃娃名');
		}
		
	});
	
	
	$('.updatecount').click(function(){
		var $this=$(this);
		$.ajax({
			type:'post',
			url:'./doll',
			data:{
				action:'updatedetailonedata',
				thisdate:'${requestScope.thisdate}',
				dollname:$this.closest('tr').find('.dollname').val(),
				todollmachinecount:$this.closest('tr').find('.todollmachinecount').val(),
				outdollmachinecount:$this.closest('tr').find('.outdollmachinecount').val()
			},
			success:function(data){
				alert(data);
				$this.closest('tr').find('td').eq(5).text(parseInt($this.closest('tr').find('td').eq(2).text())*parseInt($this.closest('tr').find('.outdollmachinecount').val()));
			},
			error:function(){
				alert('在按一次啦!爸拖!剛剛系統怪異');
			}
		});
	});
	$('.addonedata').click(function(){
		//原本無資料時
		if($(this).closest('table').find('tr').length===2){
			$(this).closest('tr').before('<tr><td><input type="button" class="deletebtn" value="delete">'+$('.savedollnames').html()+'</td><td colspan="2"></td><td><input class="todollmachinecount" type="number" name="todollmachinecount" value=""></td><td><input class="outdollmachinecount" type="number" name="outdollmachinecount" value=""></td><td colspan="2"></td></tr>');
		}else{
			if($(this).closest('tr').prev().find('.deletebtn').css('display')==='none'){
				$(this).closest('tr').before('<tr><td><input type="button" class="deletebtn" value="delete">'+$('.savedollnames').html()+'</td><td colspan="2"></td><td><input class="todollmachinecount" type="number" name="todollmachinecount" value=""></td><td><input class="outdollmachinecount" type="number" name="outdollmachinecount" value=""></td><td colspan="2"></td></tr>');
			}else{
				$(this).closest('tr').before('<tr><td><input style="display:inline-block" type="button" class="deletebtn" value="delete">'+$('.savedollnames').html()+'</td><td colspan="2"></td><td><input class="todollmachinecount" type="number" name="todollmachinecount" value=""></td><td><input class="outdollmachinecount" type="number" name="outdollmachinecount" value=""></td><td colspan="2"></td></tr>');
			}
		}
		$('.deletebtn').click(function(){
			if($('table').find('tr').length>3){
				$(this).closest('tr').remove();
			}
		});
		
	});
	$('.openclose').click(function(){
		$('.deletebtn').toggle();
	});
	$('.deletebtn').click(function(){
		if($('table').find('tr').length>3){
			$(this).closest('tr').remove();
		}
	});
	$('.delete').click(function(){
		$(window.parent.document).find('head').append('<meta http-equiv="refresh" content="0">');
		$(window.parent.document).find('iframe').remove();
	});
	
});
</script>
<style>
body{
	background:black;
	color:white;
	text-align:center;
}
.addonedata,.openclose,.submit{
	border-radius:300px;
	width:300px;
}
.deletebtn{
	border-radius:300px;
	width:50px;
	background:red;
	display:none;
}
.delete{
	position:absolute;
	top:0px;
	right:0px;
}
</style>
</head>
<body>
	<button class="delete"><img src="images/deleteiframe.jpg" width="50px" height="50px"></button>
	<form method="post" action="./doll">
	<input type="hidden" name="action" value="multiupdate">
	<input type="hidden" name="thisdate" value="${requestScope.thisdate}">
		<table align="center">
			<tr>
				<td>娃娃名稱</td><td>娃娃圖片</td><td>娃娃購入成本</td><td>放入娃娃機數量</td><td>被夾出數量</td><td>娃娃被夾出成本</td><td>更改數量</td>
			</tr>
			<c:if test="${requestScope.dlist!=null}">
				<c:forEach var="onerow" items="${requestScope.dlist}">
				<tr><td><input class="deletebtn" type="button" value="delete"><input class="dollname" type="text" name="dollname" value="${onerow.dollname}" readonly></td><td><img width="80px" src="${onerow.thisdoll.dollpictureurl}"></td><td>${onerow.thisdoll.buyonedollprice}</td><td><input type=number class="todollmachinecount" name="todollmachinecount" value="${onerow.todollmachinecount}"></td><td><input class="outdollmachinecount" type="number" name="outdollmachinecount" value="${onerow.outdollmachinecount}"></td><td>${onerow.thisdoll.buyonedollprice*onerow.outdollmachinecount}</td><td><input class="updatecount" type="button" value="更改數量"></td></tr>
				</c:forEach>
			</c:if>
			<tr><td colspan="7"><input class="addonedata" type="button" value="press me add one data"><input class="openclose" type="button" value="press me open deletebutton"><input class="submit" type="submit" value="multiupdate"></td></tr>
		</table>
	</form>

</body>
</html>