<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	
    <c:choose>
		<c:when test="${not empty sessionScope.USER}">
			<jsp:include page="Header.jsp"/>
			<p> Hello ${ sessionScope.USER } </p>
			<jsp:include page="PostForm.jsp" />
			<jsp:include page="PostList.jsp" /> 
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