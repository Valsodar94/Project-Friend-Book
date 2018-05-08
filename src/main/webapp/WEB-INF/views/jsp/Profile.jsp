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
<style>
ul {
    list-style-type: none;
    padding: 0;
    overflow: hidden;
}

li {
    float: left;
}

li a {
    display: block;
    color: white;
    text-align: center;
    padding: 14px 16px;
    text-decoration: none;
    background-color: #111;
}

li a:hover {
    background-color: #111;
}
</style>
</head>
<body>
	<jsp:include page="Header.jsp" />
	<c:if test="${not empty error}">
		<h4 style="color: red;">${error}</h4>
	</c:if>

	<c:choose>
		<c:when test="${sessionScope.USERID == id}">
				<jsp:include page="PostForm.jsp" />
		</c:when>
		<c:otherwise>
			<form method="POST" action="/Project-Friend-Book/${id}/follow">
				<c:if test="${not empty sessionScope.followMessage}">
					<c:if test="${sessionScope.followedUser == id}">
						<p>
							<b>${sessionScope.followMessage}</b>
						</p>
					</c:if>
				</c:if>
				<div class="follow-button">
					<input type="hidden" name="profileID" value="${id}"> <input
						class="login-submit" type="submit" value="follow">
				</div>
			</form>

		</c:otherwise>
	</c:choose>
	<div class="followers-list">
		<ul >
			<li><a class="active" href="/Project-Friend-Book/${id}">Posts</a></li>
			<li><a href="/Project-Friend-Book/${id}?show=followers">Followers</a></li>
			<li><a href="/Project-Friend-Book/${id}?show=followed">Followed</a></li>
			<li><a href="/Project-Friend-Book/${id}/editProfile">Edit profile</a>
		</ul>
	</div>
	<c:choose>
		<c:when test="${not empty users}">
		<div class="followers-profiles">
			<c:forEach items="${users}" var="user">
				<ul>
					<li><a href="/Project-Friend-Book/${user.getId()}">${user.getUsername()}</a></li>
				</ul>
			</c:forEach>
			</div>
		</c:when>
	</c:choose>
	<c:choose>
		<c:when test="${posts.size() == 0}">
			<div class="message-empty">
				<h3 class="announce-text announce-smaller">No posts yet</h3>
			</div>
		</c:when>
		<c:otherwise>
			<div class="profile">
				<c:forEach items="${posts}" var="post">
					<c:if test="${not empty sessionScope.PostMessage}">
						<c:if test="${sessionScope.postId == post.getId()}">
							<p class="login-field">${sessionScope.PostMessage}</p>
						</c:if>
					</c:if>
					<p class="login-field">
						Published by: <a class="link-text"
							href="/Project-Friend-Book/${post.getUserId()}">${post.getUserUserName()}</a>
					</p>
					<c:if test="${not empty post.getTags()}">
						<p class="login-field">Tags: ${post.getTags()}</p>
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
						<c:out value="${post.getLikes()} likes" />
					</p>
					<form method="POST" action="./like">
						<input type="hidden" name="postId" value="${post.getId()}">
						<input type="hidden" name="profileId" value="${id}"> <input
							type="submit" value="like" class="like-submit">
					</form>
					<a href="./comment/${post.id}" class="link-text">Comments</a>
					<c:if
						test="${sessionScope.USERID == post.getUserId() or sessionScope.isAdmin == true}">
						<form action="/Project-Friend-Book/deletePost" method="POST">
							<div class="delete-button">
								<input type="hidden" name="postAuthorId"
									value="${post.getUserId()}"> <input type="hidden"
									name="postId" value="${post.getId()}"> <input
									class="login-submit" type="submit" value="Delete">
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