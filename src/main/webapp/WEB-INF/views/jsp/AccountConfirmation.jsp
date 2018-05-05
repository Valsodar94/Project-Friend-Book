<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:include page="Header.jsp" />
		<form method="POST" action="confirmAccount">
			<c:if test="${not empty confirmationError}">
				<h4 style=color:red;>${confirmationError}</h4>
			</c:if>
			<p>Confirmation code: <input type="text" name="code"></p>
			<input type="hidden" name="username" value= "${username}" >			
		<br> <input type="submit" value="Login">
		</form>
	</body>
</html>