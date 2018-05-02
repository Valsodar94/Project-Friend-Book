<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<style>
@import url(https://fonts.googleapis.com/css?family=Open+Sans);

body {
	background: #f2f2f2;
	font-family: 'Open Sans', sans-serif;
}

.search {
	width: 100%;
	position: relative
}

.searchTerm {
	float: left;
	width: 100%;
	border: 3px solid #00B4CC;
	padding: 5px;
	height: 36px;
	border-radius: 5px;
	outline: none;
	color: #9DBFAF;
}

.searchTerm:focus {
	color: #00B4CC;
}

.searchButton {
	position: absolute;
	right: -50px;
	width: 40px;
	height: 36px;
	border: 1px solid #00B4CC;
	background: #00B4CC;
	text-align: center;
	color: #fff;
	border-radius: 5px;
	cursor: pointer;
	font-size: 20px;
}

.wrap {
	width: 30%;
	position: absolute;
	top: 5%;
	left: 50%;
	transform: translate(-50%, -50%);
}
</style>
<head>
<title>Friend-Book</title>
<link href="webjars/bootstrap/3.3.7-1/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container">
		<div style="float: left;">
			<c:if test="${not empty sessionScope.USER}">
				<a href="/Project-Friend-Book/${sessionScope.USERID}">Profile</a>
			</c:if>
			<a href="/Project-Friend-Book/">Home</a>
		</div>
		<div class="wrap">
			<div class="search" style="float: left;">
				<form method="GET" action="/Project-Friend-Book/SearchResult">
					<input type="text" name="search" class="searchTerm"
						placeholder="Search for a user profile?">
					<button type="submit" class="searchButton">
						<i class="fa fa-search"></i>
					</button>
				</form>
			</div>
		</div>
		<div style="float: right;">
			<c:choose>
				<c:when test="${not empty fn:trim(sessionScope.USER)}">
					<p>
						<a href="/Project-Friend-Book/logOut">logOut</a>
					</p>
				</c:when>
				<c:otherwise>
					<p>
						<a href="/Project-Friend-Book/login">login</a>
					</p>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<script src="/webjars/jquery/3.1.1/jquery.min.js"></script>
	<script src="/webjars/bootstrap/3.3.7-1/js/bootstrap.min.js"></script>
</body>
</html>
