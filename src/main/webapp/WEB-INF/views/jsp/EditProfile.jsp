<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:include page="Header.jsp" />
	<c:if test="${sessionScope.USERID == id or sessionScope.isAdmin == true}">
						<c:if test="${not empty message}">
					<h4 style=color:red;>${message}</h4>
				</c:if>
		<c:if test="${not empty user}">
			<form method="POST" action="/Project-Friend-Book/${id}/editProfile">
				<h3>Edit profile<br></h3>
		
				<p>New Password: <input type="password" name="new password" pattern=".{5,15}"  title="5 to 15 characters" placeholder="5 to 15 characters"></p>
				<p>Confirm Password: <input type="password" name="new password2" pattern=".{5,15}"  title="5 to 15 characters" placeholder="5 to 15 characters"></p>
				<p>Email: <input type="email" name="email" required value = "${user.getEmail()}"><br></p>		
				<p>Old Password: <input type="password" name="old password" pattern=".{5,15}" required title="5 to 15 characters" placeholder="5 to 15 characters"></p>
				
				<input type="submit" value="Edit profile">
			</form>
			<form method="POST" action="/Project-Friend-Book/${id}/deleteProfile">
				<p>Password: <input type="password" name="password" pattern=".{5,15}" required title="5 to 15 characters" placeholder="Enter your password"></p>
				<input type="submit" value="Delete profle">
			</form>
		</c:if>
	</c:if>
	</body>
</html>