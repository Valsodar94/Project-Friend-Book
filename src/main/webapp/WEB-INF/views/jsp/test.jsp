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
	<jsp:include page="Header.jsp" />
	<c:choose>
		<c:when test="${not empty sessionScope.USER}">
			<div>Hello ${sessionScope.USER}			
			<h4>Posts from the people you follow:</h4>
			<jsp:include page="PostList.jsp" />
		</c:when>
		<c:otherwise>
			<form method="POST" action="login">
				<h3>Please login:<br></h3> 
				<p>Username: <input type="text" name="username" placeholder="Username"><br></p>
				<p>Password: <input type="password" name="password" placeholder="Password"><br></p>
				<input type="submit" value="Login">
			</form>	
			<form action="register">
				<p>No registration? <input type="submit" value="Click here to sign up"></p>
			</form>	
		</c:otherwise>
	</c:choose>
	<c:if test="${not empty error}">
		<h4 style="color: red;">${error}</h4>
	</c:if>
	

</body>
</html>