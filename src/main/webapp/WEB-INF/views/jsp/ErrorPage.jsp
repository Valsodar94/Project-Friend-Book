<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Friend Book</title>
<link href="css/loginStyle.css" rel="stylesheet">
<link rel="icon" type="image/x-icon" href="img/fbook.ico" />
</head>
<body>
	<jsp:include page="Header.jsp" />

	<h1>An Error has occurred</h1>
	
	<c:if test="${not empty errorMessage}">
		<h4 style="color: red;">${errorMessage}</h4>
	</c:if>
</body>
</html>