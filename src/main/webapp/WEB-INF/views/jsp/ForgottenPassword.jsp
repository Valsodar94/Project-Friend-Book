<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<jsp:include page="Header.jsp" />
		<form method="POST" action="resetPassword" class="login">
			<c:if test="${not empty resetPassError}">
				<h4 style=color:red;>${resetPassError}</h4>
			</c:if>
			<p class="login-field">Email: <input class="login-input" type="email" name="email" placeholder="Enter email" required autofocus><br></p>
			<input type="submit" value="reset" class="login-submit">
			
		</form>

</body>
</html>