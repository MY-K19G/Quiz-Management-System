<%@ page isELIgnored="true"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quiz Management System - Register</title>
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

        .register-container {
            background-color: #fff;
            padding: 40px 30px;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
            width: 400px;
            text-align: center;
        }

        .register-container h1 {
            font-size: 28px;
            color: #333;
            margin-bottom: 30px;
        }

        .register-container input {
            width: 100%;
            padding: 12px 15px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }

        .register-container input[type="submit"] {
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

        .register-container input[type="submit"]:hover {
            background-color: #2980b9;
        }

        .register-container p {
            margin-top: 10px;
            color: #666;
        }

        .register-container a {
            color: #3498db;
            text-decoration: none;
        }

        .register-container a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <div class="register-container">
        <h1>Register for Quiz Management</h1>
        <form id="registerForm" action="/perform_register" method="post">
            <input type="text" id="name" placeholder="Enter your full name" required>
            <input type="email" id="email" name="userEmail" placeholder="Enter your email" required>
            <input type="password" id="password" name="userPassword" placeholder="Enter your password" required>
            <input type="password" id="confirmPassword"  placeholder="Confirm your password" required>
            <input type="submit" value="Register">
        </form>
        <p>Already have an account? <a href="/login">Login here</a></p>
    </div>
<!--  
    <script>
        document.getElementById('registerForm').addEventListener('submit', function(event) {
            event.preventDefault(); // Prevent form submission

            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            if (password !== confirmPassword) {
                alert("Passwords do not match!");
            } else {
               // alert("Registration successful!");
                // Here you can handle the registration process (e.g., save data to the database)
            }
        });
    </script>
-->
</body>
</html>
