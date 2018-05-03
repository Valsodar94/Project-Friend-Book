	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html>
<html>
<head>
<title>Sign up here</title>
<link href="css/loginStyle.css" rel="stylesheet">
<link rel="icon" type="image/x-icon" href="img/fbook.ico" />
</head>
<body>
	<jsp:include page="Header.jsp"/>
	<form method="POST" action="register" class="login">
		<c:if test="${not empty error}">
			<h4 style=color:red;>${error}</h4>
		</c:if>
		<h3 class="login-field">Sign up<br></h3>

		<p class="login-field">Username: <input class="login-input" type="text" name="username" maxlength="15" pattern=".{3,15}" required title="3 to 15 characters" placeholder="3 to 15 characters" autofocus></p>
		<p class="login-field">Password: <input class="login-input" type="password" name="password" pattern=".{5,15}" required title="5 to 15 characters" placeholder="5 to 15 characters"></p>
		<p class="login-field">Confirm Password: <input class="login-input" type="password" name="password2" pattern=".{5,15}" required title="5 to 15 characters" placeholder="5 to 15 characters"></p>
		<p class="login-field">Email: <input class="login-input" type="email" name="email" placeholder="Enter email" required><br></p>		
		<input type="submit" value="register" class="login-submit">
	</form>
</body>
</html>