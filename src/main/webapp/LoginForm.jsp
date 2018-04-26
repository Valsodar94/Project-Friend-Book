<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="Header.jsp" %>


	<% 
	HttpSession session2 = request.getSession();
	if (session2.getAttribute("USER") !=null) 
		response.sendRedirect("MainServlet");
	%>
	<form method="POST" action="LoginServlet">
		<h3>Please login:<br></h3> 
		<p>Username: <input type="text" name="username" placeholder="Username"><br></p>
		<p>Password: <input type="password" name="password" placeholder="Password"><br></p>
		<input type="submit" value="Login">
	</form>	
	<form action="RegistrationForm.jsp">
		<p>No registration? <input type="submit" value="Click here to sign up"></p>
	</form>	
</body>
</html>