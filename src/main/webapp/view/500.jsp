<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 Internal Server Error</title>
	<link rel="icon" href="/assets/img/java_quiz_icon.jpg" type="image/jpg">    
	<style>
        body, html {
            height: 100%;
            margin: 0;
            padding: 0;
            background: linear-gradient(135deg, #667eea, #764ba2);
            font-family: Arial, sans-serif;
            color: white;
            text-align: center;
        }
        .container {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
        }
        .error-code {
            font-size: 10rem;
            font-weight: bold;
        }
        .message {
            font-size: 1.5rem;
        }
        .home-button {
            margin-top: 20px;
        }
        a {
            text-decoration: none;
            color: white;
            background-color: #50c878;
            padding: 10px 20px;
            border-radius: 5px;
            font-size: 1.2rem;
        }
        a:hover {
            background-color: #3d9d66;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="error-code">500</div>
        <div class="message">Sorry! Something went wrong on our end.</div>
        <div class="home-button">
	     <a href="/">Go to Homepage</a>
        </div>
    </div>
</body>
</html>
    