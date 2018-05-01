<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Post comments</title>
</head>
<body>
	<h4>Comments:</h4>
	
	
	
	<form action="/comment" method="POST">
		<div class="comment">
			<textarea name="commentText" cols="50" rows="4"
				placeholder="Comment this post"></textarea>
			<input type="submit" value="Comment" />
		</div>
	</form>
</body>
</html>