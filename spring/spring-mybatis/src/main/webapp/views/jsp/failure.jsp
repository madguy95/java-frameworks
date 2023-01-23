<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Failure</title>
<link href="resources/bootstrap/css/bootstrap.min.css" rel="stylesheet" />
<c:set var="context" value="${pageContext.request.contextPath}" />
</head>
<body>

	<script src="resources/bootstrap/js/bootstrap.min.js">
		
	</script>

	<div>
		<nav class="navbar navbar-light bg-light justify-content-between">
		<a class="navbar-brand">Navbar</a>
		<form class="form-inline">
			<input class="form-control mr-sm-2" type="search"
				placeholder="Search" aria-label="Search">
			<button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
		</form>
		<ul class="nav nav-pills ">
			<li class="nav-item "><a class="nav-link"
				href="${context}">Home</a></li>
			<li class="nav-item"><a class="nav-link" href="signup">Signup</a></li>
			<li class="nav-item"><a class="nav-link active" href="login">Login</a></li>

			<li class="nav-item dropdown"><a href="#"
				class=" nav-link dropdown-toggle" data-toggle="dropdown">Explore<b
					class="caret"></b></a>
				<div class="dropdown-menu">
					<a class="dropdown-item" href="#">Contact us</a>
					<div class="divider"></div>
					<a class="dropdown-item" href="#">Further Actions</a>
				</div>
		</ul>
		</nav>
	</div>

	<!-- 
	<legend>Student Enrollment Login Success</legend>
	 -->
	<div class="panel panel-danger">
		<div class="panel-heading">
			<h3 class="panel-title">Student Enrollment Login failure</h3>
		</div>
		<div class="panel-body">
			<div class="alert alert-dismissable alert-danger">
				<button type="button" class="close" data-dismiss="alert">×</button>
				<strong>Oh snap!</strong> Something is wrong. Change a few things up
				and try submitting again.
			</div>
		</div>
	</div>
	<div></div>
	<div></div>

	<a class="btn btn-primary" href="<spring:url value="login"/>">Try
		again?</a>
</body>
</html>