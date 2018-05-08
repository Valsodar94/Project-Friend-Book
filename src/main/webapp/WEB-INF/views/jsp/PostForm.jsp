<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Friend Book</title>
<link href="css/loginStyle.css" rel="stylesheet">
<link rel="icon" type="image/x-icon" href="img/fbook.ico" />
</head>
<body>	
	<div class="profile">
	<p class="title-text post-textContent">Enter your post text here</p>
	<form method="POST" action="/Project-Friend-Book/publish" enctype="multipart/form-data">	
			
		<textarea name="postText" cols="50" rows="5" class="postText-input"
		placeholder="What's on your mind <c:out value = "${USER}"/> ?" autofocus></textarea><br>
		
		<textarea name="tags" cols="50" rows="1" class="tags-input" 
		placeholder="Place for tags"></textarea>
		
		<div class="delete-button">
		<input class="login-submit" type="file" name="pictureUrl" placeholder="Upload image"/>
		<input class="login-submit" type="submit" value="Publish" />
		</div>
	</form>
	</div>
</body>
</html>