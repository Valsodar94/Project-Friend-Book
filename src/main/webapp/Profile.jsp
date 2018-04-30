<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<jsp:include page="Header.jsp" />
<c:choose>
	<c:when test="${sessionScope.USERID == id}">
		<jsp:include page="PostForm.jsp" />
	</c:when>
	<c:otherwise>
		<form method="POST" action="./follow">
			<input type="hidden" name="profileID" value="${id}"> <input
				type="submit" value="follow">
		</form>
		<form method="POST" action="./unfollow">
			<input type="hidden" name="profileID" value="${id}"> <input
				type="submit" value="unfollow">
		</form>
	</c:otherwise>
</c:choose>

<jsp:include page="PostList.jsp" />

</body>
</html>