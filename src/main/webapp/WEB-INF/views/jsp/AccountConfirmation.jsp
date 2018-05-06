<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Edit profile</title>
<link href="css/loginStyle.css" rel="stylesheet">
<link rel="icon" type="image/x-icon" href="img/fbook.ico" />
</head>
<body>
<jsp:include page="Header.jsp" />
		<form method="POST" action="confirmAccount">
			<c:if test="${not empty confirmationError}">
				<h4 style=color:red;>${confirmationError}</h4>
			</c:if>
			<p>Confirmation code: <input type="text" name="code"></p>
			<input type="hidden" name="username" value= "${username}" >			
		<br> <input type="submit" value="Login">
		</form>
	</body>
</html>