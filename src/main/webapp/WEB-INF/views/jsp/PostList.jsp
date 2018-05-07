<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/loginStyle.css" media="screen" />
<link rel="icon" type="image/x-icon" href="img/fbook.ico" />
<title>Posts list</title>
</head>
<body>
	<c:choose>
		<c:when test="${posts.size() == 0}">
			<h3 class="announce-text">No posts yet</h3>
		</c:when>
		<c:otherwise>
			<div class="profile">
				<c:forEach items="${posts}" var="post">
					<c:if test="${not empty sessionScope.PostMessage}">
						<c:if test="${sessionScope.postId == post.getId()}">
							<p class="login-field">${sessionScope.PostMessage}</p>
						</c:if>
					</c:if>
					<h3 class="login-field">Published by: <a class="link-text" href = "/Project-Friend-Book/${post.getUserId()}">${post.getUserUserName()}</a></h3>
					<c:if test="${not empty post.getTags()}">
						<p>Post tags: ${post.getTags()} </p>
					</c:if>
					<p class="login-field post-textContent">
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
					<a href="./comment/${post.id}" class="link-text">Comments</a>
					<c:if test="${sessionScope.USERID == post.getUserId() or sessionScope.isAdmin == true}">
						<form action="/Project-Friend-Book/deletePost" method = "POST">
							<div class="delete-button">
								<input type = "hidden" name = "postAuthorId" value = ${post.getUserId()}>
								<input type = "hidden" name = "postId" value = ${post.getId()}>
								<input class="login-submit" type="submit" value="Delete post">
							</div>
						</form>
					</c:if>
					<hr>
				</c:forEach>
			</div>
		</c:otherwise>
	</c:choose>
</body>
</html>

