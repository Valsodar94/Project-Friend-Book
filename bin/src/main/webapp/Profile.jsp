<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<%@ include file="Header.jsp" %>  
		<c:if test="${sessionScope.USERID == param.id}">
			<%@ include file="PostForm.jsp" %> 
		</c:if>
	<%@ include file="PostList.jsp" %>  
 
</body>
</html>