<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Friend Book post here</title>
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

	<div class="main">
	<h1>Hello, <c:out value = "${USER}"/> !</h1>
	</div>
	
	<p>Enter your post text here</p>
	<form method="POST" action="/Project-Friend-Book/publish">		
		<textarea name="postText" cols="50" rows="5" 
		placeholder="What's on your mind <c:out value = "${USER}"/>?"></textarea></br>
		<input type="file" name="pictureUrl" placeholder="Upload image"/></br></br>
		<input type="submit" value="Publish" />
	</form>

