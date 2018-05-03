<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Friend Book</title>
<link href="css/loginStyle.css" rel="stylesheet">
<link rel="icon" type="image/x-icon" href="img/fbook.ico" />
<style>
ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
    overflow: hidden;
    background-color: #333;
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
		<form method="POST" action="./follow">
			<input type="hidden" name="profileID" value="${id}"> <input
				type="submit" value="follow">
		</form>
		<c:if test="${not empty sessionScope.followMessage}">
			<c:if test="${sessionScope.followedUser == id}">
				<p><b>${sessionScope.followMessage}</b></p>
			</c:if>
		</c:if>
	</c:otherwise>
</c:choose>

<ul>
  <li><a class="active" href="/Project-Friend-Book/${id}">Posts</a></li>
  <li><a href="/Project-Friend-Book/${id}/followers">Followers</a></li>
  <li><a href="/Project-Friend-Book/${id}/followed">Followed</a></li>
</ul>
		<c:if test="${not empty users}">
			<c:forEach items="${users}" var="user">
				<ul>
					<li><a href="../${user.getId()}">${user.getUsername()}</a></li>
				</ul>
			</c:forEach>
		</c:if>
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
				<c:out value="${post.getLikes()} likes" /><br>
				<form method="POST" action="/Project-Friend-Book/like">
					<input type="hidden" name="profileId" value="${id}"> 
					<input type="hidden" name="postId" value="${post.getId()}"> 
					<input type="submit" value="like">
				</form>
				<a href="./comment/${post.id}">Comments</a><br><hr>
			</c:forEach>
		</c:otherwise>
	</c:choose>

</body>
</html>