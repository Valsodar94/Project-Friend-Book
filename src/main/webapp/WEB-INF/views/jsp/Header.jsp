<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Friend-Book</title>
<link href="webjars/bootstrap/3.3.7-1/css/bootstrap.min.css" type="text/css"
	rel="stylesheet">
<link href="css/HeaderStyle.css" type="text/css" rel="stylesheet">
<style>
.homePage{
	position: fixed;
    top: 10 px;
    left: 30 px;
    border: 3px solid #3f65b7;
}
</style>

</head>
<body>	
		<div class="homePage">
			<h3 ><a href="/Project-Friend-Book/">Home</a></h3>
			<c:if test="${not empty sessionScope.USER}">
				<h3><a href="/Project-Friend-Book/${sessionScope.USERID}">Profile</a></h3>
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
		<div class="loginlink">
			<c:choose>
				<c:when test="${not empty fn:trim(sessionScope.USER)}">
					<p>
						<a href="/Project-Friend-Book/logOut">LogOut</a>
					</p>
				</c:when>
				<c:otherwise>
					<h3>
						<a href="/Project-Friend-Book/login">Login</a>
					</h3>
				</c:otherwise>
			</c:choose>
		</div>
	<script src="/webjars/jquery/3.1.1/jquery.min.js"></script>
	<script src="/webjars/bootstrap/3.3.7-1/js/bootstrap.min.js"></script>
</body>
</html>
