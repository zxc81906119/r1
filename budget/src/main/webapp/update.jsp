<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@	taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>insertandupdatedetaildata</title>
<script type="text/javascript"
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
	$(function() {
		//無資料時畫面
		if ($('tr').length == 1) {
			$('table')
					.after('<div height="500px" style="color:red">查無資料</div>');
			$('.monthtotal').remove();
			$('.monthincome').remove();
			$('.monthexpenditure').remove();
		}

		//每週總計支出
		$('table tr:not(:first-child)').each(
				function() {
					//以索引為基數或偶數
					var imtdincome = $(this).find('td[class]:even').not(
							'.weekincome').not('.weektotal');
					var imtdexpenditure = $(this).find('td[class]:odd').not(
							'.weekexpenditure');
					var weekincome = 0;
					var weekexpenditure = 0;
					for (var k = 0; k < imtdincome.length; k++) {
						weekincome += parseInt(imtdincome.eq(k).find('span')
								.text());
						weekexpenditure += parseInt(imtdexpenditure.eq(k).find(
								'span').text());
					}
					$(this).find('.weekexpenditure').find('span').text(
							weekexpenditure);
					$(this).find('.weekincome').find('span').text(weekincome);

					var netincome = weekincome - weekexpenditure;
					if (netincome >= 0) {
						$(this).find('.weektotal').html(
								'收入<span>' + netincome + '</span>元');
					} else {
						$(this).find('.weektotal').html(
								'支出<span>' + (-netincome) + '</span>元');
					}

				});

		//當月累積支出收入
		var monthincome = 0;
		var monthexpenditure = 0;
		var deference = 0;
		$('table tr:not(:first-child)').each(
				function() {
					monthincome += parseInt($(this).find('.weekincome').find(
							'span').text());
					monthexpenditure += parseInt($(this).find(
							'.weekexpenditure').find('span').text());
				});
		$('.monthincome').find('span').text(monthincome);
		$('.monthexpenditure').find('span').text(monthexpenditure);
		deference = monthincome - monthexpenditure;
		if (deference >= 0) {
			$('.monthtotal').html(
					$('.monthtotal').html() + '淨收入<span>' + deference
							+ '</span>元');
		} else {
			$('.monthtotal').html(
					$('.monthtotal').html() + '淨支出<span>' + (-deference)
							+ '</span>元');
		}

		//預設選定當月
		$('.default').click(function() {
			var date = new Date();
			$('select option[value=\"' + (date.getMonth() + 1) + '\"]').attr({
				selected : true
			});
			$('#year').val(date.getFullYear());
		});

		//日期的顯示
		$('.seebudget table tr:not(:first-child)').each(
				function() {
					var date = new Date($(this).find('td').eq(2).find('span')
							.text());
					date.setTime(date.getTime() + 6 * 86400000);
					$(this).find('td').eq(2).find('span').text(
							$(this).find('td').eq(2).find('span').text()
									+ '~'
									+ date.getFullYear(date)
									+ '-'
									+ (date.getMonth() + 1)
									+ '-'
									+ (date.getDate() >= 10 ? date.getDate()
											: '0' + date.getDate()));
				});

		//滑鼠滑動效果
		$('tr:not(:first-child)').mouseover(function() {
			$(this).css({
				background : 'yellow'
			});
		});
		$('tr:not(:first-child)').mouseout(function() {
			if($(this).find('.weektotal').html().substring(0,2)==='支出'){
				$(this).css({
					background : 'red'
				});
			}else{
				$(this).css({
					background : 'white'
				});
			}
		})

		//換頁
		$('.first').click(function() {
			$('.pagebutton').first().click();
			$('.pagecontent').text(1);
		});
		$('.last').click(function() {
			$('.pagebutton').last().click();
			$('.pagecontent').text($('.pagebutton').last().val());
		});
		$('.prev').click(
				function() {
					if ($('.pagecontent').text() != 1) {
						$('.pagecontent').text(
								parseInt($('.pagecontent').text()) - 1);
						$(
								'.pagebutton[value=\"'
										+ parseInt($('.pagecontent').text())
										+ '\"]').click();
					}
				});
		$('.next').click(
				function() {
					if ($('.pagecontent').text() != $('.pagebutton').last()
							.val()) {
						$('.pagecontent').text(
								parseInt($('.pagecontent').text()) + 1);
						$(
								'.pagebutton[value=\"'
										+ parseInt($('.pagecontent').text())
										+ '\"]').click();
					}
				});

		//顯示iframe
		$('.seebudget table tr:not(:first-child)')
				.find('.closeiframe')
				.click(
						function() {
							var id = '${sessionScope.id}';
							var weekfirstday = $(this).closest('tr').find('td')
									.eq(2).find('span').text().split('~')[0];
							var type = $(this).closest('tr').find('td').eq(1)
									.find('span').text();
							if ($(this).closest('tr').next('tr').has('iframe').length != 0) {
								$(this).closest('tr').next('tr').has('iframe')
										.remove();
								$(this).val('點看詳細');
							} else {
								$(this)
										.closest('tr')
										.after(
												'<tr><td colspan="21"><iframe width=\"'
														+ $(this).closest('tr')
																.width()
														+ '\" src=\"./budget?action=showdetailbudget&id='
														+ id
														+ '&weekfirstday='
														+ weekfirstday
														+ '&type='
														+ type
														+ '\"></iframe></td></tr>');
								$(this).val('關掉詳細');
							}
						});

		//回主畫面
		$('a').not('.excel').click(function(e) {
			if (!confirm('確定要回主畫面?')) {
				e.preventDefault();
			}
		});
		
		//變色
		$('table tr:not(:first-child)').each(function(){
			if($(this).find('.weektotal').html().substring(0,2)==='支出'){
				$(this).css({
					background:'red',
				});
			}
		});
		
		//換頁
		$('.pagebutton')
				.click(
						function() {
							if ($('.pagecontent').text() != $(this).val()) {
								$('table tr:not(:first-child)').hide();
								$(
										'table tr[class=\"'
												+ parseInt($(this).val())
												+ '\"]').show();
								$('.pagecontent').text($(this).val());
							}
						});
		//json處理
		/*$('.excelsubmit').click(function(){
			var objarr=[];
			$('table tr:not(:first-child)').each(function(){
				var obj={
						id:$(this).find('td').first().find('span').text(),
						date:$(this).find('td').eq(2).find('span').text(),
						type:$(this).find('td').eq(1).find('span').text(),
						mondayincome:parseInt($(this).find('.mondayincome').find('span').text()),
						tuesdayincome:parseInt($(this).find('.tuesdayincome').find('span').text()),
						wednesdayincome:parseInt($(this).find('.wednesdayincome').find('span').text()),
						thursdayincome:parseInt($(this).find('.thursdayincome').find('span').text()),
						fridayincome:parseInt($(this).find('.fridayincome').find('span').text()),
						saturdayincome:parseInt($(this).find('.saturdayincome').find('span').text()),
						sundayincome:parseInt($(this).find('.sundayincome').find('span').text()),
						mondayexpenditure:parseInt($(this).find('.mondayexpenditure').find('span').text()),
						tuesdayexpenditure:parseInt($(this).find('.tuesdayexpenditure').find('span').text()),
						wednesdayexpenditure:parseInt($(this).find('.wednesdayexpenditure').find('span').text()),
						thursdayexpenditure:parseInt($(this).find('.thursdayexpenditure').find('span').text()),
						fridayexpenditure:parseInt($(this).find('.fridayexpenditure').find('span').text()),
						saturdayexpenditure:parseInt($(this).find('.saturdayexpenditure').find('span').text()),
						sundayexpenditure:parseInt($(this).find('.sundayexpenditure').find('span').text())
				};
				objarr.push(obj);
			});			
			var objarrjson=JSON.stringify(objarr);
			alert(objarrjson);
			$('.toexcelserver').append('<input type="hidden" name="json" value=\"'+objarrjson+'\">');
		})*/
		$('.excelsubmit').click(function(){
			if($('.toexcelserver').find('input[type="hidden"]').length<=1){
			$('.toexcelserver').append('<input type="hidden" name="id" value=\"${sessionScope.id}\">');
			$('table tr:not(:first-child)').each(function(){
				$('.toexcelserver').append('<input type="hidden" name="date" value=\"'+$(this).find('td').eq(2).find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="type" value=\"'+$(this).find('td').eq(1).find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="mondayincome" value=\"'+$(this).find('.mondayincome').find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="tuesdayincome" value=\"'+$(this).find('.tuesdayincome').find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="wednesdayincome" value=\"'+$(this).find('.wednesdayincome').find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="thursdayincome" value=\"'+$(this).find('.thursdayincome').find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="fridayincome" value=\"'+$(this).find('.fridayincome').find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="saturdayincome" value=\"'+$(this).find('.saturdayincome').find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="sundayincome" value=\"'+$(this).find('.sundayincome').find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="mondayexpenditure" value=\"'+$(this).find('.mondayexpenditure').find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="tuesdayexpenditure" value=\"'+$(this).find('.tuesdayexpenditure').find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="wednesdayexpenditure" value=\"'+$(this).find('.wednesdayexpenditure').find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="thursdayexpenditure" value=\"'+$(this).find('.thursdayexpenditure').find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="fridayexpenditure" value=\"'+$(this).find('.fridayexpenditure').find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="saturdayexpenditure" value=\"'+$(this).find('.saturdayexpenditure').find('span').text()+'\">');
				$('.toexcelserver').append('<input type="hidden" name="sundayexpenditure" value=\"'+$(this).find('.sundayexpenditure').find('span').text()+'\">');
			});
			}
		});
	});
</script>
<style>
table tr:first-child {
	background: black;
	color: white
}

.querytitle {
	text-align: center;
	background: yellow;
	height: 150px;
	font-size: xx-large;
	font-style: oblique;
	font-weight: bold;
}

.seebudget {
	text-align: center;
}

.monthincome, .monthexpenditure, .monthtotal,table tr:not(:first-child ){
	font-weight: bold;
}
</style>
</head>
<body>
	<div class="querytitle">
		<h1>新增修改支出明細</h1>
	</div>
	<hr>
	<div class="seebudget">
		<form class="toexcelserver" method="post" action="./budget">
			<input type="hidden" name="action" value="toexcel">
			<input type="submit" class="excelsubmit" value="下載及寄發excel檔">
		</form>
		<form action="./budget" method="post">
			<input type="hidden" name="action" value="seeroughbudget"> 
			<input type="hidden" name="id" value="${sessionScope.id}">
				<input class="default" type="button" value="預設年月"> 
				<label for="year">年份:</label> 
				<input type="text" pattern="[0-9]{4}" maxlength="4" id="year" name="year" value="" required> 
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
				<label for="type">類別:</label> 
				<input id="type" type="text" name="type" value=""> <input type="submit" value="搜尋">
		</form>
		<table align="center">
			<tr>
				<td>編號</td>
				<td>類別</td>
				<td>日期</td>
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
				<td>當週總收入</td>
				<td>當週總支出</td>
				<td>當週淨額</td>
				<td>詳細資訊</td>
			</tr>
			<c:if test="${requestScope.pagemap!=null}">
				<c:forEach var="onelist" items="${requestScope.pagemap}">
					<c:if test="${onelist.key==1}">
						<c:forEach var="onerowdata" items="${onelist.value}">
							<tr class="${onelist.key}">
								<td><span>${sessionScope.id}</span></td>
								<td><span>${onerowdata.type}</span></td>
								<td><span>${onerowdata.weekfirstday}</span></td>
								<td class="mondayincome"><span>${onerowdata.mondayincome}</span></td>
								<td class="mondayexpenditure"><span>${onerowdata.mondayexpenditure}</span></td>
								<td class="tuesdayincome"><span>${onerowdata.tuesdayincome}</span></td>
								<td class="tuesdayexpenditure"><span>${onerowdata.tuesdayexpenditure}</span></td>
								<td class="wednesdayincome"><span>${onerowdata.wednesdayincome}</span></td>
								<td class="wednesdayexpenditure"><span>${onerowdata.wednesdayexpenditure}</span></td>
								<td class="thursdayincome"><span>${onerowdata.thursdayincome}</span></td>
								<td class="thursdayexpenditure"><span>${onerowdata.thursdayexpenditure}</span></td>
								<td class="fridayincome"><span>${onerowdata.fridayincome}</span></td>
								<td class="fridayexpenditure"><span>${onerowdata.fridayexpenditure}</span></td>
								<td class="saturdayincome"><span>${onerowdata.saturdayincome}</span></td>
								<td class="saturdayexpenditure"><span>${onerowdata.saturdayexpenditure}</span></td>
								<td class="sundayincome"><span>${onerowdata.sundayincome}</span></td>
								<td class="sundayexpenditure"><span>${onerowdata.sundayexpenditure}</span></td>
								<td class="weekincome"><span></span></td>
								<td class="weekexpenditure"><span></span></td>
								<td class="weektotal"></td>
								<td><input class="closeiframe" type="button" value="點看詳細"></td>
							</tr>
						</c:forEach>
					</c:if>
					<c:if test="${onelist.key!=1}">
						<c:forEach var="onerowdata" items="${onelist.value}">
							<tr class="${onelist.key}" style="display: none">
								<td><span>${sessionScope.id}</span></td>
								<td><span>${onerowdata.type}</span></td>
								<td><span>${onerowdata.weekfirstday}</span></td>
								<td class="mondayincome"><span>${onerowdata.mondayincome}</span></td>
								<td class="mondayexpenditure"><span>${onerowdata.mondayexpenditure}</span></td>
								<td class="tuesdayincome"><span>${onerowdata.tuesdayincome}</span></td>
								<td class="tuesdayexpenditure"><span>${onerowdata.tuesdayexpenditure}</span></td>
								<td class="wednesdayincome"><span>${onerowdata.wednesdayincome}</span></td>
								<td class="wednesdayexpenditure"><span>${onerowdata.wednesdayexpenditure}</span></td>
								<td class="thursdayincome"><span>${onerowdata.thursdayincome}</span></td>
								<td class="thursdayexpenditure"><span>${onerowdata.thursdayexpenditure}</span></td>
								<td class="fridayincome"><span>${onerowdata.fridayincome}</span></td>
								<td class="fridayexpenditure"><span>${onerowdata.fridayexpenditure}</span></td>
								<td class="saturdayincome"><span>${onerowdata.saturdayincome}</span></td>
								<td class="saturdayexpenditure"><span>${onerowdata.saturdayexpenditure}</span></td>
								<td class="sundayincome"><span>${onerowdata.sundayincome}</span></td>
								<td class="sundayexpenditure"><span>${onerowdata.sundayexpenditure}</span></td>
								<td class="weekincome"><span></span>
								</td>
								<td class="weekexpenditure"><span></span>
								</td>
								<td class="weektotal"><span></span></td>
								<td><input class="closeiframe" type="button" value="點看詳細"></td>
							</tr>
						</c:forEach>
					</c:if>
				</c:forEach>
			</c:if>
		</table>

		<div>
			<c:if test="${requestScope.pagemap!=null}">
				<c:if test="${requestScope.mapsize>1}">
					<input class="first" type="button" value="最前頁">
					<c:forEach var="i" begin="1" end="${requestScope.mapsize}">
						<input class="pagebutton" type="button" value="${i}">
					</c:forEach>
					<input class="last" type="button" value="最終頁">
					<br> -第<span class="pagecontent">1</span>頁-
				<br>
				</c:if>
				<c:choose>
					<c:when test="${requestScope.alldata}">
						<div class="monthincome">
							至今總收入<span></span>元
						</div>
						<div class="monthexpenditure">
							至今總支出<span></span>元
						</div>
						<div class="monthtotal">至今</div>
					</c:when>
					<c:otherwise>
						<div class="monthincome">
							本月總收入<span></span>元
						</div>
						<div class="monthexpenditure">
							本月總支出<span></span>元
						</div>
						<div class="monthtotal">本月</div>
					</c:otherwise>
				</c:choose>
			</c:if>
		</div>
		<a href="main.jsp">回主畫面</a>
	</div>
</body>
</html>