<%@page import="javax.sound.midi.SysexMessage"%>
<%@page import="org.omg.PortableServer.POA"%>
<%@ page contentType="text/html; charset=EUC-KR" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- <%@page import="com.model2.mvc.service.product.vo.ProductVO"%>
<%@ page import="java.util.List"  %>
<%@ page import="com.model2.mvc.service.purchase.vo.PurchaseVO" %>
<%@ page import="com.model2.mvc.common.Search" %>
<%@page import="com.model2.mvc.common.Page"%>
<%@page import="com.model2.mvc.common.util.CommonUtil"%>

<%
	List<ProductVO> list= (List<ProductVO>)session.getAttribute("so");
%>--%>
<html>
<head>

<title>열어본 상품 보기</title>

</head>
<body>
	방금 열어보신 상품!!
<br>
<br>
<%-- <%
	request.setCharacterEncoding("euc-kr");
	response.setCharacterEncoding("euc-kr");
	String history = null;
	Cookie[] cookies = request.getCookies();
	if (cookies!=null && cookies.length > 0) {
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equals("history")) {
				history = cookie.getValue();
			}
		}
		if (history != null) {
			String[] h = history.split(",");
			for (int i = 0; i < h.length; i++) {
				if (!h[i].equals("null")) {
%>
<a href="/getProduct.do?prodNo=<%=productVO.getProdNo()%>&menu=search"	target="rightFrame"><%=productVO.getProdNo()%></a>
<br>
<%
				}
			}
		}
	}
%> --%>
<table width="100%"  border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">

	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">상품이름</td>
		<td class="ct_line02"></td>
		</tr>	
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
<%--<% 	
		
		for(int i=0; i<list.size(); i++) {
			ProductVO vo = list.get(i);
%>
<tr class="ct_list_pop">
		<td align="center"><%= i + 1 %></td>
		<td></td>
		<td align="left">		
			<a href="/getProduct.do?prodNo=<%=vo.getProdNo() %>&menu=search"><%=vo.getProdName() %></a>
		</td>
		<td class="ct_write01">
			<img src = "/images/uploadFiles/<%=vo.getFileName()%>"/>
		</td>
		<td></td>
</tr>
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
	<%} %>--%>
<c:set var="i" value="0" />
	<c:forEach var="product" items="${producthistory}">
	<c:set var="i" value="${ i+1 }" />	
	<tr class="ct_list_pop">
		<td align="center">${  i  }</td>
		<td></td>
		<td align="left">
			<a href="/getProduct.do?prodNo=${ product.prodNo }&menu=search">${ product.prodName }</a>
		</td>
		<td class="ct_write01">
			<img src = "/images/uploadFiles/${ product.fileName }" width="80" height="50" />
		</td>
		<td></td>
		</tr>
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
		
	</c:forEach>	
</table>	
</body>
</html>