<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<link rel="icon" type="image/x-icon" href="img/fbook.ico" />
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
				<c:out value="${comment.getAnswers().size()} answers" />
				<c:choose>
					<c:when test="${comment.getAnswers().size() > 0}">
						<h4>Answers:</h4>
						<c:forEach items="${comment.getAnswers()}" var="answer">
							<c:out value="${answer.text}" />
							<br>
						</c:forEach>
					</c:when>
				</c:choose>
				<br>
				<form action="/Project-Friend-Book/comment/${post.getId()}/answer/${comment.getId()}"
					method="POST">
					<div class="answer">
						<textarea required name="answerText" cols="40" rows="1"
							placeholder="Answer to this comment"></textarea>
						<input type="submit" value="Answer" />
					</div>
				</form><br>
				<hr>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</body>
</html>