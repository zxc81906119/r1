<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>detailexpenditure</title>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<c:if test="${requestScope.savesuccess}">
	<script type="text/javascript">
		$(function() {
			$('body').find('*').remove();
			$('body').append(
					'<h1 style="text-align:center;color:red">儲存成功!</h1>');
			//幾秒後
			setTimeout(
					function() {
						var imtr = $(window.parent.document).find(
								'table tr:not(:first-child)');
						
						for (var i = 0; i < imtr.length; i++) {
							if (imtr.eq(i).find('td').eq(2).find('span').text() == '${requestScope.allweekfirstday}'
									&& imtr.eq(i).find('td').eq(1).find('span')
											.text() == '${requestScope.type}') {
								
								var monthincomespan = $(window.parent.document)
										.find('.monthincome').find('span');
								var monthexpenditurespan = $(window.parent.document)
								.find('.monthexpenditure').find('span');
								var monthtotal=$(window.parent.document).find('.weektotal')
								//先減掉原來的當週資料
								monthincomespan.text(parseInt(monthincomespan
										.text())
										- parseInt(imtr.eq(i)
												.find('.weekincome')
												.find('span').text()));
								monthexpenditurespan.text(parseInt(monthexpenditurespan.text())-parseInt(imtr.eq(i)
										.find('.weekexpenditure')
										.find('span').text()));
								//處理當週資料
								imtr.eq(i).find('.closeiframe').val('點看詳細');
								imtr.eq(i).find('.mondayincome').find('span').text(
										parseInt('${requestScope.mondayincometotal}'));
								imtr.eq(i).find('.mondayexpenditure').find('span').text(
										parseInt('${requestScope.mondayexpendituretotal}'));
								imtr.eq(i).find('.tuesdayincome').find('span').text(
										parseInt('${requestScope.tuesdayincometotal}'));
								imtr.eq(i).find('.tuesdayexpenditure').find('span').text(
										parseInt('${requestScope.tuesdayexpendituretotal}'));
								imtr.eq(i).find('.wednesdayincome').find('span').text(
										parseInt('${requestScope.wednesdayincometotal}'));
								imtr.eq(i).find('.wednesdayexpenditure').find('span').text(
										parseInt('${requestScope.wednesdayexpendituretotal}'));
								imtr.eq(i).find('.thursdayincome').find('span').text(
										parseInt('${requestScope.thursdayincometotal}'));
								imtr.eq(i).find('.thursdayexpenditure').find('span').text(
										parseInt('${requestScope.thursdayexpendituretotal}'));
								imtr.eq(i).find('.fridayincome').find('span').text(
										parseInt('${requestScope.fridayincometotal}'));
								imtr.eq(i).find('.fridayexpenditure').find('span').text(
										parseInt('${requestScope.fridayexpendituretotal}'));
								imtr.eq(i).find('.saturdayincome').find('span').text(
										parseInt('${requestScope.saturdayincometotal}'));
								imtr.eq(i).find('.saturdayexpenditure').find('span').text(
										parseInt('${requestScope.saturdayexpendituretotal}'));
								imtr.eq(i).find('.sundayincome').find('span').text(
										parseInt('${requestScope.sundayincometotal}'));
								imtr.eq(i).find('.sundayexpenditure').find('span').text(
										parseInt('${requestScope.sundayexpendituretotal}'));
								var weekincome=0;
								var weekexpenditure=0;
								var weekincometd=imtr.eq(i).find('td[class]:even').not('.weektotal').not('.weekincome');
								var weekexpendituretd=imtr.eq(i).find('td[class]:odd').not('.weekexpenditure');
								for (var k = 0; k < weekincometd.length; k++) {
									weekincome+=parseInt(weekincometd.eq(k).find('span').text());
									weekexpenditure+=parseInt(weekexpendituretd.eq(k).find('span').text());
								}
								imtr.eq(i).find('.weekincome').find('span').text(weekincome);
								imtr.eq(i).find('.weekexpenditure').find('span').text(weekexpenditure);
								var netincome=weekincome-weekexpenditure;
								if(netincome>=0){
									imtr.eq(i).find('.weektotal')
									.html('收入<span>'+netincome+'</span>元');
								}else{
									imtr.eq(i).find('.weektotal')
									.html('支出<span>'+ (-netincome)+'</span>元');
								}
								
								monthexpenditurespan.text(parseInt(monthexpenditurespan.text())+weekexpenditure);
								monthincomespan.text(parseInt(monthincomespan.text())+weekincome);
								var deference=0;
								deference=parseInt(monthincomespan.text())-parseInt(monthexpenditurespan.text());
								if(deference>=0){
									$(window.parent.document).find('.monthtotal').html($(window.parent.document).find('.monthtotal').html().substring(0,2)+'淨收入<span>'+deference+'</span>元');
								}else{
									$(window.parent.document).find('.monthtotal').html($(window.parent.document).find('.monthtotal').html().substring(0,2)+'淨支出<span>'+(-deference)+'</span>元');
								}
								imtr.eq(i).next('tr').has('iframe').remove();
								break;
							}
						}
					}, 3000);
		});
	</script>
</c:if>
<script type="text/javascript">
	$(function() {
		$('.opendelete').click(function() {
			$('.deletebtn').toggle();
		});

		$('.additem')
				.click(
						function() {
							//原本無資料時
							if ($('table tr').length === 2) {
								$('table tr')
										.last()
										.before(
												'<tr><td><input class="deletebtn" type="button" value="刪除"><input type="text" name="content" value="" style="width:96px"></td><td><input type="text" name="mondayincome" value="0" style="width:70px"></td><td><input type="text" name="mondayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="tuesdayincome" value="0" style="width:70px"></td><td><input type="text" name="tuesdayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="wednesdayincome" value="0" style="width:70px"></td><td><input type="text" name="wednesdayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="thursdayincome" value="0" style="width:70px"></td><td><input type="text" name="thursdayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="fridayincome" value="0" style="width:70px"></td><td><input type="text" name="fridayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="saturdayincome" value="0" style="width:70px"></td><td><input type="text" name="saturdayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="sundayincome" value="0" style="width:70px"></td><td><input type="text" name="sundayexpenditure" value="0" style="width:70px"></td></tr>');
							} else {
								//原本有資料時
								if ($('table tr').last().prev('tr').find('td').eq(0).find('.deletebtn').css('display') !== "none") {
									$('table tr').last().before('<tr><td><input style="display:inline-block" class="deletebtn" type="button" value="刪除"><input type="text" name="content" value="" style="width:96px"></td><td><input type="text" name="mondayincome" value="0" style="width:70px"></td><td><input type="text" name="mondayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="tuesdayincome" value="0" style="width:70px"></td><td><input type="text" name="tuesdayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="wednesdayincome" value="0" style="width:70px"></td><td><input type="text" name="wednesdayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="thursdayincome" value="0" style="width:70px"></td><td><input type="text" name="thursdayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="fridayincome" value="0" style="width:70px"></td><td><input type="text" name="fridayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="saturdayincome" value="0" style="width:70px"></td><td><input type="text" name="saturdayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="sundayincome" value="0" style="width:70px"></td><td><input type="text" name="sundayexpenditure" value="0" style="width:70px"></td></tr>');
								} else {
									$('table tr').last().before('<tr><td><input class="deletebtn" type="button" value="刪除"><input type="text" name="content" value="" style="width:96px"></td><td><input type="text" name="mondayincome" value="0" style="width:70px"></td><td><input type="text" name="mondayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="tuesdayincome" value="0" style="width:70px"></td><td><input type="text" name="tuesdayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="wednesdayincome" value="0" style="width:70px"></td><td><input type="text" name="wednesdayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="thursdayincome" value="0" style="width:70px"></td><td><input type="text" name="thursdayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="fridayincome" value="0" style="width:70px"></td><td><input type="text" name="fridayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="saturdayincome" value="0" style="width:70px"></td><td><input type="text" name="saturdayexpenditure" value="0" style="width:70px"></td><td><input type="text" name="sundayincome" value="0" style="width:70px"></td><td><input type="text" name="sundayexpenditure" value="0" style="width:70px"></td></tr>');
								}
							}
							$('.deletebtn').click(function(e) {
								if ($('table tr').length > 3) {
									$(this).closest('tr').remove();
								}
							});
						});

		$('.deletebtn').click(function(e) {
			if ($('table tr').length > 3) {
				$(this).closest('tr').remove();
			}
		});

		$('.submit')
				.click(
						function(e) {
							if (!confirm('確定要儲存?')) {
								e.preventDefault();
							} else {
								for (var i = 0; i < $('table input[name="content"]').length; i++) {
									for (var j = i + 1; j < $('table input[name="content"]').length; j++) {
										if ($('table input[name="content"]')
												.eq(i).val() == $(
												'table input[name="content"]')
												.eq(j).val()) {
											var k = i;
											e.preventDefault();
											alert('存在重複內容!請更改一下!');
											break;
										}
									}
									if (i === k) {
										break;
									}
								}
								var allinput=$('table input[type="text"]');
								for(var o=0;o<allinput.length;o++){
									var whitespaceregex=/^\s+$/;
									if(whitespaceregex.test(allinput.val())||allinput.val().length===0){
										e.preventDefault();
										alert('有完全空白或是沒填寫的空格');
									}
								}
	var dayname = [ 'mondayincometotal','mondayexpendituretotal',
										'tuesdayincometotal',
										'tuesdayexpendituretotal',
										'wednesdayincometotal',
										'wednesdayexpendituretotal',
										'thursdayincometotal',
										'thursdayexpendituretotal',
										'fridayincometotal',
										'fridayexpendituretotal',
										'saturdayincometotal',
										'saturdayexpendituretotal',
										'sundayincometotal',
										'sundayexpendituretotal' ];
								
	var daytotal = [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
										0, 0, 0, 0 ];
								$('table tr:not(:first-child)')
										.not('table tr:last-child')
										.each(
												function() {
													for (var j = 0; j < daytotal.length; j++) {
														daytotal[j] += parseInt($(
																this)
																.find('td')
																.eq(j + 1)
																.find(
																		'input[type="text"]')
																.val());
													}
												});
								for (var k = 0; k < daytotal.length; k++) {
									$('form')
											.append(
													'<input type="hidden" name=\"'+dayname[k]+'\" value=\"'+daytotal[k]+'\">');
								}
							}
						});
	});
</script>
<style>
table tr:first-child {
	background: black;
	color: white
}

table {
	text-align: center;
}

.deletebtn {
	display: none;
}
</style>
</head>
<body>
	<form method="post" action="./budget?action=savedata">
		<input type="hidden" name="id" value="${requestScope.id}">
		<input type="hidden" name="weekfirstday" value="${requestScope.weekfirstday}">
		<input type="hidden" name="type" value="${requestScope.type}">
		
		<table style="border:8px red groove" >
			<tr>
				<td>支出內容</td>
				
				<td>一收</td>
				<td>一支</td>
				<td>二收</td>
				<td>二支</td>
				<td>三收</td>
				<td>三支</td>
				<td>四收</td>
				<td>四支</td>
				<td>五收</td>
				<td>五支</td>
				<td>六收</td>
				<td>六支</td>
				<td>日收</td>
				<td>日支</td>
				
			</tr>
			<c:if test="${requestScope.dlist!=null}">
				<c:forEach var="onebudget" items="${requestScope.dlist}">
					<tr>
						<td>
							<input class="deletebtn" type="button" value="刪除">
							<input type="text" name="content" value="${onebudget.content}" style="width:96px">
						</td>
						<td class="mondayincome">
							<input type="text" name="mondayincome" value="${onebudget.mondayincome}" style="width:70px">
						</td>
						<td class="mondayexpenditure">
							<input type="text" name="mondayexpenditure" value="${onebudget.mondayexpenditure}" style="width:70px">
						</td>
						<td class="tuesdayincome">
							<input type="text" name="tuesdayincome" value="${onebudget.tuesdayincome}" style="width:70px">
						</td>
						<td class="tuesdayexpenditure">
							<input type="text" name="tuesdayexpenditure" value="${onebudget.tuesdayexpenditure}" style="width:70px">
						</td>
						<td class="wednesdayincome">
							<input type="text" name="wednesdayincome" value="${onebudget.wednesdayincome}" style="width:70px">
						</td>
						<td class="wednesdayexpenditure">
						<input type="text" name="wednesdayexpenditure" value="${onebudget.wednesdayexpenditure}" style="width:70px">
						</td>	
						<td class="thursdayincome">
						<input type="text" name="thursdayincome" value="${onebudget.thursdayincome}" style="width:70px">
						</td>
						<td class="thursdayexpenditure">
							<input type="text" name="thursdayexpenditure" value="${onebudget.thursdayexpenditure}" style="width:70px">
						</td>	
						<td class="fridayincome">
							<input type="text" name="fridayincome" value="${onebudget.fridayincome}" style="width:70px">
						</td>
						<td class="fridayexpenditure">
							<input type="text" name="fridayexpenditure" value="${onebudget.fridayexpenditure}" style="width:70px">
						</td>
						<td class="saturdayincome">
							<input type="text" name="saturdayincome" value="${onebudget.saturdayincome}" style="width:70px">
						</td>
						<td class="saturdayexpenditure">
							<input type="text" name="saturdayexpenditure" value="${onebudget.saturdayexpenditure}" style="width:70px">
						</td>
						<td class="sundayincome">
							<input type="text" name="sundayincome" value="${onebudget.sundayincome}" style="width:70px">
						</td>
						<td class="sundayexpenditure">
							<input type="text" name="sundayexpenditure"	value="${onebudget.sundayexpenditure}" style="width:70px">
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="8"><input type="button" class="additem"
						value="增加項目"> <input class="opendelete" type="button"
						value="開啟關閉"> <input class="submit" type="submit"
						value="提交資料"></td>
				</tr>
			</c:if>
		</table>
		
	</form>
</body>
</html>