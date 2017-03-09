
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" >
<title>resetPassword</title>
</head>
<body style="background-color:#b0c4de;">

<h4>Reset Password</h4>
<form action="resetPasswordHandler" method="post">
	userName:<input type="text" name="userName"> 
	<br>
	password:<input type="password" name="newPassword">
	<br>
	<input type="submit" value="submit">
</form> 

</body>
</html>