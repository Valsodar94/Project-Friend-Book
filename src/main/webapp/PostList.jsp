<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<!-- <link rel="stylesheet" type="text/css" href="static/css/IndexStyle.css" media="screen" /> -->
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Posts list</title>
</head>
<body>
	<c:choose>
		<c:when test="${posts.size() == 0}">
			<h3>You don't have any posts yet</h3>
		</c:when>
		<c:otherwise>
			<h3>Your posts:</h3>
			<c:forEach items="${posts}" var="post">
				<c:out value="${post.text}" />
				<br>
				<c:out value="Published on: ${post.time}" />
				<br>
				<c:choose>
					<c:when test="${post.pictureUrl.length() > 0}">
						<img class="img" src="./uploaded/${post.pictureUrl}" />
						<br>
					</c:when>
				</c:choose>
				<c:out value="${post.likes.size()} likes" />
				<br>
				<br>
				<h4>Comments:</h4>
				<form action="">
					<div class="comment">
						<textarea name="postText" cols="50" rows="4"
							placeholder="Comment this post"></textarea>
						<input type="submit" value="Comment" />
					</div>
				</form>
				<hr>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</body>
</html>

