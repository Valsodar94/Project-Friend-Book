<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Friend Book</title>
</head>
<body>
	<c:choose>
		<c:when test="${not empty sessionScope.USER}">
			<jsp:include page="Header.jsp" />
			<div>Hello ${sessionScope.USER}			
			<a href="./${sessionScope.USERID}">Home</a></div>
			<h4>Posts from the people you follow:</h4>
			<jsp:include page="PostList.jsp" />
		</c:when>
		<c:otherwise>
			<jsp:include page="LoginForm.jsp" />
		</c:otherwise>
	</c:choose>
	<c:if test="${not empty error}">
		<h4 style="color: red;">${error}</h4>
	</c:if>
	

</body>
</html>