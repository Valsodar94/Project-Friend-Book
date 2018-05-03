<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/loginStyle.css"
	media="screen" />
<link rel="icon" type="image/x-icon" href="img/fbook.ico" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Posts list</title>
</head>
<body>
	<c:choose>
		<c:when test="${posts.size() == 0}">
			<h3 class="login-field">No posts yet</h3>
		</c:when>
		<c:otherwise>
			<div class="profile">
				<c:forEach items="${posts}" var="post">
					<c:if test="${not empty sessionScope.PostMessage}">
						<c:if test="${sessionScope.postId == post.getId()}">
							<p class="login-field">${sessionScope.PostMessage}</p>
						</c:if>
					</c:if>
					<h3>Published by: ${post.getUserUserName()}</h3>
					<p class="login-field">
						<c:out value="${post.text}" />
					</p>
					<c:choose>
						<c:when test="${post.pictureUrl.length() > 0}">							
							<img class="post-image" src="./uploaded/${post.pictureUrl}" />							
						</c:when>
					</c:choose>
					<p class="login-field">
						<c:out value="Published on: ${post.time}" />
					</p>
					<p class="login-field">
					<c:out value="${post.getLikes()} likes" /></p>
					<form method="POST" action="./like">
						<input type="hidden" name="postId" value="${post.getId()}">
						<input type="submit" value="like" class="like-submit">
					</form>					
					<a href="./comment/${post.id}" class="login-field">Comments</a>
					<hr>
				</c:forEach>
			</div>
		</c:otherwise>
	</c:choose>
</body>
</html>

