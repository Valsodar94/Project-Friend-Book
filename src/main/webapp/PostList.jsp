<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="static/css/IndexStyle.css"
	media="screen" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Posts list</title>
</head>
<body>
	<c:choose>
		<c:when test="${posts.size() == 0}">
			<h3>No posts yet</h3>
		</c:when>
		<c:otherwise>
			<c:forEach items="${posts}" var="post">
			<c:if test="${not empty sessionScope.PostMessage}">
				<c:if test="${sessionScope.postId == post.getId()}">
					<p>${sessionScope.PostMessage}</p>
				</c:if>
			</c:if>
				<c:out value="${post.text}" /><br>
				<c:out value="Published on: ${post.time}" /><br>
				<c:choose>
					<c:when test="${post.pictureUrl.length() > 0}">
						<img class="img" src="./uploaded/${post.pictureUrl}" /><br>
					</c:when>
				</c:choose>
				<c:out value="${post.likes.size()} likes" /><br>
				<form method="POST" action="./like">
					<input type="hidden" name="postId" value="${post.getId()}"> 
					<input type="submit" value="like">
				</form>
				<a href="./comment/${post.id}">Comments</a><hr>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</body>
</html>

