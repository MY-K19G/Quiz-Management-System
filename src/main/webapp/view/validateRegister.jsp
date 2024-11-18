<%@ include file="loader.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Remove /register Access</title>
    <link rel="icon" href="/assets/img/java_quiz_icon.jpg" type="image/jpg">
    <style>
	 body {
	    font-family: Arial, sans-serif;
	    margin: 0;
	    padding: 20px;
	}
	
	.form-container {
	    max-width: 400px;
	    margin: 0 auto;
	}
	
	.input-field,
	.submit-button {
	    width: 100%;
	    padding: 10px;
	    margin: 10px 0;
	    cursor: pointer;
	}
	
	.message {
	    padding: 10px;
	    margin: 10px 0;
	    text-align: center;
	}
	
	.success {
	    color: green;
	}
	
	.error {
	    color: red;
	}

    </style>
</head>
<body>

<div class="form-container">
    <h2>Remove Access to /register</h2>
    <form id="removeRegisterForm">
        <label for="inputValidation">Enter Validation Code:</label>
        <input type="text" id="inputValidation" class="input-field" name="secret_text" placeholder="Enter code" required>
        <button type="submit" class="submit-button">Submit</button>
        <div id="message" class="message"></div>
    </form>
</div>

<script>
		document.getElementById('removeRegisterForm').addEventListener('submit', async (event) => {
			showLoader();
			event.preventDefault();
		    
		    const inputField = document.getElementById('inputValidation');
		    const messageDiv = document.getElementById('message');
		    const secretText = inputField.value.trim();
		    
		    if (!secretText) {
		        messageDiv.textContent = "Validation code is required.";
		        messageDiv.className = "message error";
		        return;
		    }
		    
		    try {
		        const response = await fetch('/api/validateRegister', {
		            method: 'POST',
		            headers: { 'Content-Type': 'application/json' },
		            body: JSON.stringify({ secret_text: secretText })  
		        });
		        
		        if (response.ok) {
		            const responseText = await response.text();
		            messageDiv.textContent = responseText;
		            messageDiv.className = "message success";
		        } else {
		            const errorText = await response.text();
		            messageDiv.textContent = "Error: " + errorText;
		            messageDiv.className = "message error";
		        }
		    } catch (error) {
		        messageDiv.textContent = "Error: " + error.message;
		        messageDiv.className = "message error";
		    }
			 finally {
		        hideLoader(); 
		    }
		});

</script>

</body>
</html>
    