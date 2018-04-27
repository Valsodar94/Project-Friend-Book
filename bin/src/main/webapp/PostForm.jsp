<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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
	<h1>Hello, <%=(String) session.getAttribute("USER")%> !</h1>
	</div>
	
	<form method="POST" action="PostServlet">
		<p>Enter your post text here</p>
		<textarea name="postText" cols="30" rows="4" placeholder="What's on your mind <%=(String) session.getAttribute("USER")%>?"></textarea></br>
		<input type="file" name="pictureUrl" placeholder="Upload image"/></br></br>
		<input type="submit" value="Publish" />
	</form>

