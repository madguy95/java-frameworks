<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link href="<c:url value="resources/bootstrap/css/bootstrap.min.css"/>"
	rel="stylesheet" />

<style>
.error {
	color: #ff0000;
	font-size: 0.9em;
	font-weight: bold;
}

.errorblock {
	color: #000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}
</style>
<title>Student Enrollment Login</title>
</head>
<body>
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
				href="/SpringWithMyBatis">Home</a></li>
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

	<div class="container">
		<div class="jumbotron">
			<div>
				<h1>Welcome to Online Student Enrollment Login</h1>
				<p>Login to explore the complete features!</p>
			</div>
		</div>

		<div></div>
	</div>

	<div class="col-lg-6 col-lg-offset-3">
		<div class="well">
			<div class="container">
				<div class="row">
					<div class="col-lg-6">
						<form:form id="myForm" method="post"
							class="bs-example form-horizontal" modelAttribute="studentLogin">
							<fieldset>
								<legend>Student Enrollment Login Form</legend>

								<div class="form-group">
									<label for="userNameInput" class="col-lg-3 control-label">User
										Name</label>
									<div class="col-lg-9">
										<form:input type="text" class="form-control" path="userName"
											id="userNameInput" placeholder="User Name" />
										<form:errors path="userName" cssClass="error" />
									</div>
								</div>

								<div class="form-group">
									<label for="passwordInput" class="col-lg-3 control-label">Password</label>
									<div class="col-lg-9">
										<form:input type="password" class="form-control"
											path="password" id="passwordInput" placeholder="Password" />
										<form:errors path="password" cssClass="error" />
									</div>
								</div>

								<div class="col-lg-9 col-lg-offset-3">
									<button class="btn btn-default">Cancel</button>

									<button class="btn btn-primary">Login</button>
								</div>


							</fieldset>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>


	<script src="resources/bootstrap/js/bootstrap.min.js">
		
	</script>
	<script src="resources/jquery-3.2.1.min.js">
		
	</script>
</body>
</html>