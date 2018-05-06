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
	<c:choose>
		<c:when test="${not empty users}">
			<c:forEach items="${users}" var="user">
				<ul>
					<li style = "display: flex;
    align-items: center;"><a href="./${user.getId()}">${user.getUsername()}</a></li>
				</ul>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<p>There are no other users</p>
		</c:otherwise>
	</c:choose>
</body>
</html>