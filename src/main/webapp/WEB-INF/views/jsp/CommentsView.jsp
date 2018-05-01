<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
.post {
    padding: 70px 0;
    text-align: center;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Post comments</title>
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
	<c:out value="${post.likes.size()} likes" />
	<br>
	<form method="POST" action="./like">
		<input type="hidden" name="postId" value="${post.getId()}"> <input
			type="submit" value="like">
	</form>
	</div>
	
	<jsp:include page="CommentList.jsp" />
	<form action="/comment/${post.id}" method="POST">
		<div class="comment">
			<textarea name="commentText" cols="50" rows="4"
				placeholder="Comment this post"></textarea>
			<input type="submit" value="Comment" />
		</div>
	</form>
</body>
</html>