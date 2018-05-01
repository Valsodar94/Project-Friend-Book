	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="Header.jsp" %>
<body>

	<form method="POST" action="register">
		<c:if test="${not empty error}">
			<h4 style=color:red;>${error}</h4>
		</c:if>
		<h3>Please register:<br></h3>

		<p>Username: <input type="text" name="username" maxlength="15" pattern=".{3,15}" required title="3 to 15 characters" placeholder="3 to 15 characters"><br></p>
		<p>Password: <input type="password" name="password" pattern=".{5,15}" required title="5 to 15 characters" placeholder="5 to 15 characters"><br></p>
		<p>Confirm Password: <input type="password" name="password2" pattern=".{5,15}" required title="5 to 15 characters" placeholder="5 to 15 characters"><br></p>
		<p>Email: <input type="email" name="email" placeholder="Enter email" required><br></p>		
		<input type="submit" value="register">
	</form>
</body>
</html>