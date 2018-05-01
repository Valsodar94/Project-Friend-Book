<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Comments list</title>
</head>
<body>

<c:choose>
		<c:when test="${comments.size() == 0}">
			<h3>No comments yet</h3>
		</c:when>
		<c:otherwise>
			<h4>Comments:</h4>
			<c:forEach items="${comments}" var="comment">
			<!-- <c:if test="${not empty sessionScope.PostMessage}">
				<c:if test="${sessionScope.postId == post.getId()}">
					<p>${sessionScope.PostMessage}</p>
				</c:if>
			</c:if> -->
				<c:out value="${comment.text}" /><br>
				<c:out value="Published on: ${comment.time}" /><br>
				<c:out value="${comments.likes.size()} likes" /><br>
				<form method="POST" action="./like">
					<input type="hidden" name="postId" value="${comment.getId()}"> 
					<input type="submit" value="like">
				</form>
				<a href="./comment/${post.id}">Comments</a><hr>
			</c:forEach>
		</c:otherwise>
	</c:choose>

</body>
</html>