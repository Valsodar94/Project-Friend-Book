<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<%@ include file="Header.jsp" %>  
 	
    <c:choose>
		<c:when test="${sessionScope.USERID == id}">
			<%@ include file="PostForm.jsp" %>
		</c:when>
		<c:otherwise>
			<form method="POST" action="./follow">
				<input type="hidden" name="profileID" value="${id}">
				<input type="submit" value="follow">
			</form>
						<form method="POST" action="./unfollow">
				<input type="hidden" name="profileID" value="${id}">
				<input type="submit" value="unfollow">
			</form>
		</c:otherwise>
	</c:choose>
	
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
						<img class="img"
							src="<c:url value="./images/${post.pictureUrl}"/>" />
						<br>
					</c:when>
				</c:choose>
				<c:out value="${post.likes.size()} likes" />
				<br>
				<hr>
			</c:forEach>
		</c:otherwise>
	</c:choose>
	
</body>
</html>