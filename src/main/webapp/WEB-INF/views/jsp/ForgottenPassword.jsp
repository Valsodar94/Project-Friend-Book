<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Friend Book</title>
<link href="css/loginStyle.css" rel="stylesheet">
<link rel="icon" type="image/x-icon" href="img/fbook.ico" />
</head>
<body>
	<jsp:include page="Header.jsp" />
	<form method="POST" action="resetPassword" class="login">
		<c:if test="${not empty resetPassError}">
			<h4 style="color: red;">${resetPassError}</h4>
		</c:if>
		<p class="login-field">
			Email: <input class="login-input" type="email" name="email"
				placeholder="Enter email" required autofocus>
		</p>
		<br> <input type="submit" value="reset" class="login-submit">
	</form>
</body>
</html>