<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	
    <c:choose>
		<c:when test="${not empty sessionScope.USER}">
			<jsp:include page="Header.jsp"/>
			<p> Hello ${ sessionScope.USER } </p>
			<c:choose>
		<c:when test="${posts.size() == 0}">
			<h3>No posts :(</h3>
		</c:when>
		<c:otherwise>
			<h3>Followed user's posts::</h3>
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
		</c:when>
		<c:otherwise>
			<jsp:include page="LoginForm.jsp" /> 
		</c:otherwise>
	</c:choose>
	<c:if test="${not empty error}">
			<h4 style=color:red;>${error}</h4>
	</c:if>

</body>
</html>