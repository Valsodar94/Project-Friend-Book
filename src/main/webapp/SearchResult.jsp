<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search result</title>
</head>
<body>
	<c:choose>
		<c:when test="${not empty users}">
			<c:forEach items = "${users}" var = "user">
				<a href="./profile/${user.getId()}">${user.getUsername()}</a>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<p>2</p>
		</c:otherwise>
		</c:choose>
</body>
</html>