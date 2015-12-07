<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%-- <%@ page import="java.util.List"  %>

<%@ page import="com.model2.mvc.service.purchase.vo.PurchaseVO" %>
<%@ page import="com.model2.mvc.common.Search" %>
<%@page import="com.model2.mvc.common.Page"%>
<%@page import="com.model2.mvc.common.util.CommonUtil"%>


<%
	List<PurchaseVO> list= (List<PurchaseVO>)request.getAttribute("list");
	Page resultPage=(Page)request.getAttribute("resultPage");
	
	Search search = (Search)request.getAttribute("search");
	//==> null �� ""(nullString)���� ����
	String searchCondition = CommonUtil.null2str(search.getSearchCondition());
	String searchKeyword = CommonUtil.null2str(search.getSearchKeyword());
	//if(search.getSearchKeyword()==null) search.setSearchKeyword("");
%>--%>


<html>
<head>
<title>���� �����ȸ</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">

<script type="text/javascript">
	function fncGetPurchaseList(currentPage) {
		document.getElementById("currentPage").value = currentPage;
		document.detailForm.submit();
	}
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width: 98%; margin-left: 10px;">

<form name="detailForm" action="/listPurchase.do" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"width="15" height="37"></td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left: 10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">���� �����ȸ</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"	width="12" height="37"></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top: 10px;">
	<tr>
		<td colspan="11">��ü 1 �Ǽ�, ���� 1 ������</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">��ǰ</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">��ǰ�̸�</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">��ǰ����</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">�����Ȳ</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">��������</td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>

	<%--<% 	
		
		for(int i=0; i<list.size(); i++) {
			PurchaseVO vo = list.get(i);
	%>
	
	<tr class="ct_list_pop">
		<td align="center">
			<a href="/getPurchase.do?tranNo=<%=vo.getTranNo() %>"><%= i+1 %></a>
		</td>
		<td></td>
		<td align="left">
			<a href="/getUser.do?userId=<%=vo.getBuyer().getUserId() %>"><%=vo.getBuyer().getUserId() %></a>
		</td>
		<td></td>
		<td align="left"><%=vo.getBuyer().getUserName() %></td>
		<td></td>
		<td align="left"><%= vo.getBuyer().getPhone() %></td>
		<td></td>
	
		<% if(vo.getTranCode().trim().equals("1")){ %>
			<td align="left">���� ���ſϷ� �����Դϴ�.</td>
		<%}else if(vo.getTranCode().trim().equals("2")){ %>	
			<td align="left">���� ����� �Դϴ�.</td>
		<%}else if(vo.getTranCode().trim().equals("3")){ %>
			<td align="left">���� ��ۿϷ� �����Դϴ�.	</td>
		<%} %>	
		
		<td></td>
		<% if(vo.getTranCode().trim().equals("2")){ %>
		<td align="left">
			<a href="/updateTranCode.do?prodNo=<%=vo.getPurchaseProd().getProdNo() %>&menu=search&tran_status_code=<%=vo.getTranCode()%>">���ǵ���</a>
		</td>
		<%} %>
	</tr>
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
	<%} %>
		--%>
	<c:set var="i" value="0" />
	<c:forEach var="purchase" items="${list}">
	<c:set var="i" value="${ i+1 }" />
		<tr class="ct_list_pop">
		<td align="center"><a href="/getPurchase.do?tranNo=${ purchase.tranNo }">${  i  }</a>
		</td>
		<td></td>
		<td class="ct_write01" align="center">
			<a href="/getProduct.do?prodNo=${ purchase.purchaseProd.prodNo }&menu=search">
			<img src = "/images/uploadFiles/${ purchase.purchaseProd.fileName }" width="80" height="50" /></a></td>
		<td></td>
		<td  align="center">${ purchase.purchaseProd.prodName }</td>
		<td></td>
		<td align="center">${ purchase.purchaseProd.price }</td>
		<td></td>
		
		<c:if test="${  fn:trim(purchase.tranCode) =='1' }">
			<td align="center">���� ���ſϷ� �����Դϴ�.</td>
		</c:if>
		<c:if test="${  fn:trim(purchase.tranCode)=='2'   }">
			<td align="center">���� ����� �Դϴ�.
			<a href="/updateTranCode.do?prodNo=${ purchase.purchaseProd.prodNo }&menu=search&tran_status_code=${ purchase.tranCode }">���ǵ���</a>
			</td>
		</c:if>
		<c:if test="${  fn:trim(purchase.tranCode) =='3' }">
			<td align="center">���� ��ۿϷ� �����Դϴ�.</td>
		</c:if>
		<td></td>
		
		<c:if test="${  fn:trim(purchase.tranCode) =='1' }">
		<td align="left">
		<a href="/getPurchase.do?tranNo=${ purchase.tranNo }">(���ż���)</a>
		</td>
		</c:if>
		
		</tr>
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
	</c:forEach>

</table>



<!-- PageNavigation Start... -->
<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top:10px;">
	<tr>
		<td align="center">
		   <input type="hidden" id="currentPage" name="currentPage" value=""/>
		<%--	<% if( resultPage.getCurrentPage() <= resultPage.getPageUnit() ){ %>
					�� ����
			<% }else{ %>
					<a href="javascript:fncGetProductList('<%=resultPage.getCurrentPage()-1%>')">�� ����</a>
			<% } %>

			<%	for(int i=resultPage.getBeginUnitPage();i<= resultPage.getEndUnitPage() ;i++){	%>
					<a href="javascript:fncGetProductList('<%=i %>');"><%=i %></a>
			<% 	}  %>
	
			<% if( resultPage.getEndUnitPage() >= resultPage.getMaxPage() ){ %>
					���� ��
			<% }else{ %>
					<a href="javascript:fncGetProductList('<%=resultPage.getEndUnitPage()+1%>')">���� ��</a>
			<% } %> --%>
			<%--<jsp:include page="../common/pageNavigator1.jsp"/>--%>
	<c:if test="${ resultPage.currentPage <= resultPage.pageUnit }">
			�� ����
	</c:if>
	<c:if test="${ resultPage.currentPage > resultPage.pageUnit }">
			<a href="javascript:fncGetPurchaseList('${ resultPage.currentPage-1}')">�� ����</a>
	</c:if>
	
	<c:forEach var="i"  begin="${resultPage.beginUnitPage}" end="${resultPage.endUnitPage}" step="1">
		<a href="javascript:fncGetPurchaseList('${ i }');">${ i }</a>
	</c:forEach>
	
	<c:if test="${ resultPage.endUnitPage >= resultPage.maxPage }">
			���� ��
	</c:if>
	<c:if test="${ resultPage.endUnitPage < resultPage.maxPage }">
			<a href="javascript:fncGetPurchaseList('${resultPage.endUnitPage+1}')">���� ��</a>
	</c:if>
    	</td>
	</tr>
</table>
<!-- PageNavigation End... -->

<!--  ������ Navigator �� -->
</form>

</div>

</body>
</html>