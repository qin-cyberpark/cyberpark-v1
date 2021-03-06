<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<jsp:include page="../header.jsp" />
<jsp:include page="../alert.jsp" />

<div class="container">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-success">
				<div class="panel-heading">
					<h4 class="panel-title">Customer View</h4>
				</div>
				<jsp:include page="customer-order-query.html" />
				<hr>
				<div id="customer-view"></div>
			</div>
		</div>
	</div>
</div>

<script type="text/html" id="customer_view_tmpl">
<jsp:include page="customer-order-view-page.html" />
</script>

<jsp:include page="../footer.jsp" />
<jsp:include page="../script.jsp" />
<script type="text/javascript" src="${ctx}/public/bootstrap3/js/jTmpl.js"></script>
<script type="text/javascript">
(function($){
	
	function doPage(pageNo, co, btn) { //console.log(btn);
		btn && btn.button('loading');
		$.get('${ctx}/broadband-user/crm/customer/order/view/' + pageNo, co, function(page){ //console.log(page);
			page.ctx = '${ctx}';
	   		var $div = $('#customer-view');
	   		$div.html(tmpl('customer_view_tmpl', page));
	   		$div.find('tfoot a').click(function(){
				doPage($(this).attr('data-pageNo'), co);
			});
	   		
	   		$('#checkbox_customer_top').click(function(){
	   			var b = $(this).prop("checked");
	   			if (b) $('input[name="checkbox_customer"]').prop("checked", true);
	   			else $('input[name="checkbox_customer"]').prop("checked", false);
	   		});

	   		$('a[data-val="all"] span').html('${allSum}');
	   		$('a[data-val="personal"] span').html('${personalSum}');
	   		$('a[data-val="business"] span').html('${businessSum}');
	   		
	   	}, 'json').always(function(){
	   		btn && btn.button('reset');
	   	});
	}
	doPage(1, null);
	
	$('input[data-role="query"]').each(function(){
		var id = $(this).attr('data-id'); //console.log(id);
		var val = $(this).prop("checked");
		if (val) $('#' + id).prop("disabled", "");
		else $('#' + id).prop("disabled", "disabled"); 
	});
	
	$('input[data-role="query"]').click(function(){
		var id = $(this).attr('data-id');
		var val = $(this).prop("checked");
		if (val) $('#' + id).prop("disabled", "");
		else $('#' + id).prop("disabled", "disabled");
	}); 
	
	$('#query').click(function(e){
		e.preventDefault();
		var co = {};
		$('input[data-role="query"]').each(function(){
			var val = $(this).prop("checked");
			if (val) {
				var field = $(this).attr('data-id');
				co[field] = $('#' + field).val();
			}
		});// console.log(customer);
		$('a[data-id="customer_type"]').each(function(){
			if($(this).attr('data-val')!='all'){
				var val = $(this).hasClass('active');
				if(val){
					var field = $(this).attr('data-id');
					co[field] = $(this).attr('data-val');
					//console.log(co);
				}
			}
		});
		console.log(co);
		doPage(1, co, $(this));
	});

	$('a[data-id="customer_type"]').click(function(){
		var co = {};
		var field = $(this).attr('data-id');
		$('input[data-role="query"]').each(function(){
			var val = $(this).prop("checked");
			if (val) {
				var field = $(this).attr('data-id');
				co[field] = $('#' + field).val();
			}
		});// console.log(co);
		$('a[data-id="customer_type"]').removeClass('active');
		$(this).addClass('active');
		if($(this).attr('data-val')!='all'){
			co[field] = $(this).attr('data-val');
		}
		doPage(1, co, null);
	});
})(jQuery);
</script>
<jsp:include page="../footer-end.jsp" />