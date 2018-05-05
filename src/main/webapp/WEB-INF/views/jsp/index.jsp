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
	<c:choose>
		<c:when test="${not empty sessionScope.USER}">
				<h2>Hello ${sessionScope.USER}</h2>
				<h4>Posts from the people you follow:</h4>
			<jsp:include page="PostList.jsp" />
		</c:when>
		<c:otherwise>
			<form method="POST" action="login" class="login">
				<c:if test="${not empty error}">
					<h4 style=color:red;>${error}</h4>
				</c:if>
			
				<h3 class="login-field">
					FriendBook<br>
				</h3>
				<p class="login-field">
					Username: <input type="text" name="username" class="login-input"
						placeholder="Username" autofocus>
				</p>
				<br>
				<p class="login-field">
					Password: <input type="password" name="password"
						class="login-input" placeholder="Password">
				</p>
				<br> <input type="submit" value="Login" class="login-submit">
				<p class="login-help">
					<a href="register">Sign up</a>
				</p>
				<p class = "login-help"><a href = "forgottenPass">Forgotten Password</a></p>
			</form>

		</c:otherwise>
	</c:choose>

</body>
</html>