<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<link href="<c:url value="resources/bootstrap/css/bootstrap.min.css"/>"
	rel="stylesheet" />
<style>
body {
	height: 100%;
	margin: 0;
	background: url(resources/assets/img/books.jpg);
	background-size: 1440px 800px;
	background-repeat: no-repeat;
	display: compact;
}
</style>
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
				<li class="nav-item"><a class="nav-link active" href="#">Home</a></li>
				<li class="nav-item"><a class="nav-link" href="signup">Signup</a></li>
				<li class="nav-item"><a class="nav-link" href="login">Login</a></li>

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
				<h1>Welcome to Online Student Enrollment!</h1>
				<p>To get started, you need to enter your details to enroll with
					us. Or login to access your details, if you are already enrolled.</p>
			</div>

			<a class="btn btn-primary" href="signup">Signup » </a> <a
				class="btn btn-primary" href="login">Login » </a>
		</div>

		<div></div>
	</div>
	<script src="resources/jquery-3.2.1.min.js">
		
	</script>

	<script src="resources/bootstrap/js/bootstrap.min.js">
		
	</script>

</body>
</html>
