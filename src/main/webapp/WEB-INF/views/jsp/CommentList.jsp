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
	<jsp:include page="CommentsView.jsp" />

	<c:choose>
		<c:when test="${comments.size() == 0}">
			<h3>No comments yet</h3>
		</c:when>
		<c:otherwise>
			<h4>Comments:</h4>
			<c:forEach items="${comments}" var="comment">
				<c:if test="${not empty sessionScope.CommentMessage}">
					<c:if test="${sessionScope.commentId == comment.getId()}">
						<p>${sessionScope.CommentMessage}</p>
					</c:if>
				</c:if>
				<c:out value="${comment.text}" />
				<br>
				<c:out value="Published on: ${comment.time}" />
				<br>
				<c:out value="${comment.getLikes()} likes" />
				<br>
				<form method="POST" action="/Project-Friend-Book/comment/like">
					<input type="hidden" name="commentId" value="${comment.getId()}">
					<input type="hidden" name="postId" value="${comment.getPostId()}">
					<input type="submit" value="like">
				</form>
				<hr>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</body>
</html>