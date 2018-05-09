<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Friend-Book</title>
<link href="webjars/bootstrap/3.3.7-1/css/bootstrap.min.css"
	rel="stylesheet">
<link href="css/HeaderStyle.css" rel="stylesheet">
<link rel="icon" type="image/x-icon" href="img/fbook.ico" />
</head>
<body>
	<div class="container">
		<div class="homePageLink">
			<a class="hyperlink-text hyperlink-bigger"
				href="/Project-Friend-Book/">Home</a>
			<c:if test="${not empty sessionScope.user}">
				<a class="hyperlink-text hyperlink-bigger"
					href="/Project-Friend-Book/${sessionScope.user.getId()}">Profile</a>
			</c:if>
		</div>
		<div class="wrap">
			<div class="search" style="float: left;">
				<form method="GET" action="/Project-Friend-Book/SearchResult">
					<input required type="text" name="search" class="searchTerm"
						placeholder="Search for a user profile?">
					<button type="submit" value="Search" class="searchButton">
						<i class="fa fa-search"></i>Search
					</button>
				</form>
			</div>
		</div>
		<div class="loginLogoutlink">
			<c:choose>
				<c:when test="${not empty fn:trim(sessionScope.user)}">
					<a class="hyperlink-text hyperlink-bigger"
						href="/Project-Friend-Book/logOut">LogOut</a>
				</c:when>
				<c:otherwise>
					<a class="hyperlink-text hyperlink-bigger"
						href="/Project-Friend-Book/login">Login</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<script src="/webjars/jquery/3.1.1/jquery.min.js"></script>
	<script src="/webjars/bootstrap/3.3.7-1/js/bootstrap.min.js"></script>
</body>
</html>
