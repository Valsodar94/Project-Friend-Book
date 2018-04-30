<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search result</title>
</head>
<body>
	<c:choose>
		<c:when test="${not empty users}">
			<c:forEach items="${users}" var="user">
				<a href="./${user.getId()}">${user.getUsername()}</a>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<p>There are no other users</p>
		</c:otherwise>
	</c:choose>
</body>
</html>