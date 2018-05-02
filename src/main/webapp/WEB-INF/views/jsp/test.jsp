<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Friend Book</title>
<link rel="stylesheet" href="static/css/loginStyle.css">
</head>
<body>
	<jsp:include page="Header.jsp" />
	<c:choose>
		<c:when test="${not empty sessionScope.USER}">
				Hello ${sessionScope.USER}
				<h4>Posts from the people you follow:</h4>
			<jsp:include page="PostList.jsp" />
		</c:when>
		<c:otherwise>
			<div class="container">
				<form method="POST" action="login" class="login">
					<h3>Please login:<br></h3>
					<p>Username: <input type="text" name="username" class="login-input"	placeholder="Username" autofocus></p><br>
					<p>Password: <input type="password" name="password" class="login-input"	placeholder="Password"></p><br>
					<input type="submit" value="Login" class="login-submit">
				</form>
				
				<form action="register" class="login-help">
					<p>No registration? <input type="submit" value="Click here to sign up"></p>
				</form>
			</div>
		</c:otherwise>
	</c:choose>
	<c:if test="${not empty error}">
		<h4 style="color: red;">${error}</h4>
	</c:if>
</body>
</html>