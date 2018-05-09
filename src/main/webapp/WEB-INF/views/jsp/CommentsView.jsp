<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Post comments</title>
<link href="../css/loginStyle.css" rel="stylesheet">
<link href="../css/HeaderStyle.css" rel="stylesheet">
<link rel="icon" type="image/x-icon" href="img/fbook.ico" />
</head>
<body>
	<jsp:include page="Header.jsp" />
	<div class="dummy"></div>
	<div class="profile">
		<p class="login-field post-textContent">
			<c:out value="${post.text}" />
		</p>
		<br>
		<p class="login-field">
			<c:out value="Published on: ${post.time}" />
		</p>
		<br>
		<c:choose>
			<c:when test="${post.pictureUrl.length() > 0}">
				<img class="post-image" class="img"
					src="../uploaded/${post.pictureUrl}" />
				<br>
			</c:when>
		</c:choose>
		<p class="login-field">
			<c:out value="${post.getLikes()} likes" />
		</p>
		<form method="POST" action="/Project-Friend-Book/like">
			<input type="hidden" name="postId" value="${post.getId()}"> <input
				type="submit" value="like" class="like-submit">
		</form>
	</div>
	<div class="profile">
		<form action="/Project-Friend-Book/comment/${post.getId()}"
			method="POST">
			<textarea required name="commentText" cols="50" rows="4"
				placeholder="Comment this post" class="postText-input"></textarea>
			<input type="submit" value="Comment" class="delete-button login-submit"/>
		</form>
	</div>
</body>
</html>