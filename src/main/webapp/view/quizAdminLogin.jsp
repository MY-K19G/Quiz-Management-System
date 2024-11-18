<%@ include file="loader.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Management System - Login</title>
	<link rel="icon" href="/assets/img/java_quiz_icon.jpg" type="image/jpg">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(to right, #8e44ad, #3498db);
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .login-container {
            background-color: #fff;
            padding: 40px 30px;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
            width: 400px;
            text-align: center;
        }

        .login-container h1 {
            font-size: 28px;
            color: #333;
            margin-bottom: 30px;
        }

        .login-container input {
            width: 100%;
            padding: 12px 15px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }

        .login-container input[type="submit"] {
            width: 100%;
            padding: 12px;
            margin: 20px 0;
            border: none;
            background-color: #3498db;
            color: #fff;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .login-container input[type="submit"]:hover {
            background-color: #2980b9;
        }

        .login-container p {
            margin-top: 10px;
            color: #666;
        }

        .login-container a {
            color: #3498db;
            text-decoration: none;
        }

        .login-container a:hover {
            text-decoration: underline;
        }
       
		.error-message {
            color: red;
            text-align: center;
            margin-bottom: 15px;
        }
        .success-message {
            color: green;
            text-align: center;
            margin-bottom: 15px;
        }

    </style>
</head>
<body>

    <div class="login-container">
        <h1>Login to Quiz Management</h1>
        <%
            // Check for login error if the parameter error is passed
            String error = request.getParameter("error");
            if (error != null && error.equals("true")) {
        %>
            <div class="error-message">Invalid username or password. Please try again.</div>
        <%
            }
        %>

        <%
            // Check for successful logout
            String logout = request.getParameter("logout");
            if (logout != null && logout.equals("true")) {
        %>
            <div class="success-message">You have been logged out successfully.</div>
        <%
            }
        %>
        
        <form id="loginForm" action="/perform_login" method="post">
            <input type="email" id="email" name="userEmail" placeholder="Enter your email" required>
            <input type="password" id="password" name="userPassword" placeholder="Enter your password" required>
            <input type="submit" value="Login">
        </form>
        <p>Don't have an account? <a href="/register">Sign Up</a></p>
    </div>
</body>
</html>