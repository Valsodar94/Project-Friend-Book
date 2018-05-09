<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Friend Book</title>
<link href="../css/loginStyle.css" rel="stylesheet">
<link href="../css/HeaderStyle.css" rel="stylesheet">
<link rel="icon" type="image/x-icon" href="../img/fbook.ico" />
</head>
<body>
	<jsp:include page="Header.jsp" />
	<c:if
		test="${sessionScope.user.getId() == id or sessionScope.user.isAdmin() == true}">
		<c:if test="${not empty message}">
			<h4 style="color: red;">${message}</h4>
		</c:if>
			<div class="dummy"></div>
			<div class="profile">
				<form method="POST" action="/Project-Friend-Book/${id}/editProfile">
					<h3 class="title-text post-textContent">
						Edit profile
					</h3><br>

					<p class="login-field">
						New Password:<br><input type="password" name="new password"
							pattern=".{5,15}" title="5 to 15 characters"
							placeholder="5 to 15 characters" class="tags-input"  autofocus>
					</p>
					<p class="login-field">
						Confirm Password: <input type="password" name="new password2"
							pattern=".{5,15}" title="5 to 15 characters"
							placeholder="5 to 15 characters" class="tags-input" >
					</p>
					<p class="login-field">
						Email: <input type="email" name="email" required
							value="${sessionScope.user.getEmail()}" class="tags-input" ><br>
					</p>
					<p class="login-field">
						Old Password: <input type="password" name="old password"
							pattern=".{5,15}" required title="5 to 15 characters"
							placeholder="5 to 15 characters" class="tags-input" >
					</p>
					<div class="edit-button">
					<input type="submit" value="Edit profile" class="login-submit">
					</div>
				</form>
			</div>
			
			<div class="profile">
				<form method="POST"
					action="/Project-Friend-Book/${id}/deleteProfile">
					<p class="login-field">
						Password: <br><input type="password" name="password" pattern=".{5,15}"
							required title="5 to 15 characters"
							placeholder="Enter your password" class="tags-input" >
					</p>
					<div class="edit-button">
					<input type="submit" value="Delete profle" class="login-submit">
					</div>
				</form>
			</div>
		</c:if>
</body>
</html>