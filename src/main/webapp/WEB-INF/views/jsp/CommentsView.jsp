<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Post comments</title>
<style type="text/css">
.post {
    padding: 70px 0;
    text-align: center;
}
</style>
<link rel="icon" type="image/x-icon" href="img/fbook.ico" />
</head>
<body>
	<jsp:include page="Header.jsp" />
	
	<div class="post">
	<c:out value="${post.text}" />
	<br>
	<c:out value="Published on: ${post.time}" />
	<br>
	<c:choose>
		<c:when test="${post.pictureUrl.length() > 0}">
			<img class="img" src="../uploaded/${post.pictureUrl}" />
			<br>
		</c:when>
	</c:choose>
	<c:out value="${post.getLikes()} likes" />
	<br>
	<form method="POST" action="../like">
		<input type="hidden" name="postId" value="${post.getId()}"> <input
			type="submit" value="like">
	</form>
	</div>
	<form action="/Project-Friend-Book/comment/${post.getId()}" method="POST">
		<div class="comment">
			<textarea name="commentText" cols="50" rows="4"
				placeholder="Comment this post"></textarea>
			<input type="submit" value="Comment" />
		</div>
	</form>
</body>
</html>