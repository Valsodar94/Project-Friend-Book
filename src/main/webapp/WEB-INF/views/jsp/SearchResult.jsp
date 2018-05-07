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
		<h3>Users:</h3>
			<c:forEach items="${users}" var="user">
				<ul>
					<li style = "display: flex;
    align-items: center;"><a href="./${user.getId()}">${user.getUsername()}</a></li>
				</ul>
			</c:forEach>
		</c:when>
		<c:when test="${not empty posts}">
				<h3>Posts:</h3>
			<c:forEach items="${posts}" var="post">
					<c:if test="${not empty sessionScope.PostMessage}">
						<c:if test="${sessionScope.postId == post.getId()}">
							<p class="login-field">${sessionScope.PostMessage}</p>
						</c:if>
					</c:if>
					<h3>Published by: <a href = "/Project-Friend-Book/${post.getUserId()}">${post.getUserUserName()}</a></h3>
					<c:if test="${not empty post.getTags()}">
						<p>Post tags: ${post.getTags()} </p>
					</c:if>
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
					<c:if test="${sessionScope.USERID == post.getUserId()}">
						<form action="/Project-Friend-Book/deletePost" method = "POST">
							<input type = "hidden" name = "postAuthorId" value = ${post.getUserId()}>
							<input type = "hidden" name = "postId" value = ${post.getId()}>
							<input type="submit" value="Delete post">
						</form>
					</c:if>
					<hr>
				</c:forEach>
		</c:when>		
		<c:otherwise>
			<p>There are no results matching your search</p>
		</c:otherwise>
	</c:choose>
</body>
</html>