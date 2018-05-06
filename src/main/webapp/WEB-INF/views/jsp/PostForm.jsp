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
<STYLE type="text/css">
.main {
    text-align: center;
}
h1 {
    display: inline-block;
}
a {
    float: right;
}
</STYLE>
</head>
<body>	
	<p>Enter your post text here</p>
	<form method="POST" action="/Project-Friend-Book/publish">		
		<textarea name="postText" cols="50" rows="5" 
		placeholder="What's on your mind <c:out value = "${USER}"/> ?"  autofocus></textarea>
		<input type="file" name="pictureUrl" placeholder="Upload image"/>
		<input type="submit" value="Publish" />
	</form>
</body>
</html>