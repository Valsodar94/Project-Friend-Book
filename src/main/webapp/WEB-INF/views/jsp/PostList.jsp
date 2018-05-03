<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/loginStyle.css"
	media="screen" />
<link rel="icon" type="image/x-icon" href="img/fbook.ico" />
<link href="css/PostForm.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Posts list</title>
</head>




<body>
	<c:choose>
		<c:when test="${posts.size() == 0}">
			<h3 class="login-field">No posts yet</h3>
		</c:when>
		<c:otherwise>
			<c:forEach items="${posts}" var="post">
				<c:if test="${not empty sessionScope.PostMessage}">
					<c:if test="${sessionScope.postId == post.getId()}">
						<p class="login-field">${sessionScope.PostMessage}</p>
					</c:if>
				</c:if>
				<div class="container">
					<div class="col-md-5">
				        <div class="panel panel-default">
				            <div class="panel-body">
				               <section class="post-heading">
				                    <div class="row">
				                        <div class="col-md-11">
				                            <div class="media">
				                              <div class="media-body">
				                                <p class="anchor-username"><h4 class="media-heading"> ${post.getUserUserName()} </h4> 
				                                <p class="anchor-time">${post.time}</p>
				                              </div>
				                            </div>
				                        </div>
				                    </div>             
				               </section>
				               <section class="post-body">
				                   <p>"${post.text}"</p>
				               </section>
				               <c:if test="${post.pictureUrl.length() > 0}">
									<div class="post-image">
										<img class="img" src="./uploaded/${post.pictureUrl}" />
									</div><br>
								</c:if>
				               <section class="post-footer">
				                   <hr>
				                   <div class="post-footer-option container">
				                        <ul class="list-unstyled">
				                            <li><form method="POST" action="./like">
										<input type="hidden" name="postId" value="${post.getId()}">
										<input type="submit" value="like" class="like-submit">
									</form>	<c:out value="${post.getLikes()} likes" /></li>
				                            <li><a href="./comment/${post.id}"><i class="glyphicon glyphicon-comment"></i> Comments</a></li>
				                        </ul>
				                   </div>
				               </section>
				            </div>
				        </div>   
					</div>
				</div>
			</c:forEach>
		</c:otherwise>
	</c:choose>
</body>
</html>