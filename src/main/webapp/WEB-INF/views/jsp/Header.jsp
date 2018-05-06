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
</head>
<body>
	<div class="container">
		<div style="float: left;">
			<c:if test="${not empty sessionScope.USER}">
				<h3><a href="/Project-Friend-Book/${sessionScope.USERID}">Profile</a></h3>
			</c:if>
			<h3><a href="/Project-Friend-Book/">Home</a></h3>
		</div>
		<div class="wrap">
			<div class="search" style="float: left;">
				<form method="GET" action="/Project-Friend-Book/SearchResult">
					<input type="text" name="search" class="searchTerm"
						placeholder="Search for a user profile?">
					<button type="submit" value="Search" class="searchButton">
						<i class="fa fa-search"></i>Search
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
					<h3>
						<a href="/Project-Friend-Book/login">login</a>
					</h3>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<script src="/webjars/jquery/3.1.1/jquery.min.js"></script>
	<script src="/webjars/bootstrap/3.3.7-1/js/bootstrap.min.js"></script>
</body>
</html>
