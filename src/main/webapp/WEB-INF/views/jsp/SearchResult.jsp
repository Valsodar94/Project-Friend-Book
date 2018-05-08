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
		<div class="login">
		<h3 class="title-text">Users:</h3>
			<c:forEach items="${users}" var="user">			
				<ul>
					<li style = "display: flex;  align-items: center;">
					<a class="link-text" href="./${user.getId()}">${user.getUsername()}</a></li>
				</ul>
			</c:forEach>
			</div>
		</c:when>
		<c:when test="${not empty posts}">
		<div class="profile">
				<h3 class="title-text announce-smaller">Posts:</h3>
			<c:forEach items="${posts}" var="post">
					<c:if test="${not empty sessionScope.PostMessage}">
						<c:if test="${sessionScope.postId == post.getId()}">
							<p class="login-field">${sessionScope.PostMessage}</p>
						</c:if>
					</c:if>
					<h3 class="login-field">Published by: <a class="link-text" href = "/Project-Friend-Book/${post.getUserId()}">${post.getUserUserName()}</a></h3>
					<c:if test="${not empty post.getTags()}">
						<p class="login-field">Post tags: ${post.getTags()} </p>
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
					<a href="./comment/${post.id}" class="link-text">Comments</a>
					<c:if test="${sessionScope.USERID == post.getUserId()}">
						<form action="/Project-Friend-Book/deletePost" method = "POST">
							<input type = "hidden" name = "postAuthorId" value = ${post.getUserId()}>
							<input type = "hidden" name = "postId" value = ${post.getId()}>
							<input type="submit" value="Delete post" class="login-submit delete-comment">
						</form>
					</c:if>
					<hr>
				</c:forEach>
				</div>
		</c:when>		
		<c:otherwise>
			<p class="announce-text announce-smaller">There are no results matching your search</p>
		</c:otherwise>
	</c:choose>
</body>
</html>