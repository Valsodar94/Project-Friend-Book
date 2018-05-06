<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
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