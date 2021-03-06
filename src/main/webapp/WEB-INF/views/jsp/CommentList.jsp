<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Comments list</title>
<link href="../css/loginStyle.css" rel="stylesheet">
<link rel="icon" type="image/x-icon" href="../img/fbook.ico" />
</head>
<body>
	<jsp:include page="CommentsView.jsp" /><br>
	
	<div class="profile">
	<c:choose>
		<c:when test="${comments.size() == 0}">
			<h3 class="title-text announce-smaller">No comments yet</h3>
		</c:when>
		<c:otherwise>
			<h4 class="title-text announce-smaller">Comments:</h4>
			<c:forEach items="${comments}" var="comment">
				<c:if test="${not empty sessionScope.CommentMessage}">
					<c:if test="${sessionScope.commentId == comment.getId()}">
						<p>${sessionScope.CommentMessage}</p>
					</c:if>
				</c:if>
				<p class="login-field">Published by: <a class="link-text" href = "/Project-Friend-Book/${comment.getUserId()}">${comment.getAuthorName()}</a></p>
				<p class="login-field post-textContent"><c:out value="${comment.text}" /></p>
				<br>
				<p class="login-field">
				<c:out value="Published on: ${comment.time}" /></p>
				<br>
				<p class="login-field">
				<c:out value="${comment.getLikes()} likes" />
				</p>
				<br>
				<form method="POST" action="/Project-Friend-Book/comment/like">
					<input type="hidden" name="commentId" value="${comment.getId()}">
					<input type="hidden" name="postId" value="${comment.getPostId()}">
					<input type="submit" value="like" class="like-submit">
				</form>
				<c:if test="${sessionScope.user.getId() == comment.getUserId() or sessionScope.user.isAdmin() == true}">
					<form action="/Project-Friend-Book/comment/${post.getId()}/deleteComment" method = "POST">
						<input type = "hidden" name = "commentAuthorId" value = ${comment.getUserId()}>
						<input type = "hidden" name = "commentId" value = ${comment.getId()}>
						<input type="submit" value="Delete comment" class="login-submit delete-comment">
					</form>
				</c:if>
				<p class="login-field">
				<c:out value="${comment.getAnswers().size()} answers" /></p>
				<c:choose>
					<c:when test="${comment.getAnswers().size() > 0}">
						<h4 class="title-text announce-smaller">Answers:</h4><hr>
						<c:forEach items="${comment.getAnswers()}" var="answer">
							<c:if test="${not empty sessionScope.CommentMessage}">
								<c:if test="${sessionScope.commentId == answer.getId()}">
									<p>${sessionScope.CommentMessage}</p>
								</c:if>
							</c:if>
							<p class="login-field">Published by: <a class="link-text" href = "/Project-Friend-Book/${answer.getUserId()}">${answer.getAuthorName()}</a></p>			
							<p class="login-field post-textContent"><c:out value="${answer.text}" /></p>
							<p class="login-field">
								<c:out value="${answer.getLikes()} likes" />
							</p>
							<form method="POST" action="/Project-Friend-Book/comment/like">
								<input type="hidden" name="commentId" value="${answer.getId()}">
								<input type="hidden" name="postId" value="${answer.getPostId()}">
								<input type="submit" value="like" class="like-submit">
							</form>
							<c:if test="${sessionScope.user.getId() == answer.getUserId() or sessionScope.user.isAdmin() == true}">
								<form action="/Project-Friend-Book/comment/${post.getId()}/delete" method = "POST">
									<input type = "hidden" name = "answerAuthorId" value = ${answer.getUserId()}>
									<input type = "hidden" name = "answerId" value = ${answer.getId()}>
									<input type="submit" value="Delete answer" class="login-submit delete-comment">
								</form>
							</c:if>
							<br>
						</c:forEach>
					</c:when>
				</c:choose>
				<br>
				<form action="/Project-Friend-Book/comment/${post.getId()}/answer/${comment.getId()}"
					method="POST">
					<div class="answer">
						<textarea required name="answerText" cols="40" rows="2"
							placeholder="Answer to this comment" class="postText-input"></textarea>
						<input type="submit" value="Answer" class="delete-button login-submit"/>
					</div>
				</form><br>
				<hr>
			</c:forEach>
		</c:otherwise>
	</c:choose>
	</div>
</body>
</html>